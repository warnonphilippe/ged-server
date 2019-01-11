package be.phw.gedserver.web.rest;

import be.phw.gedserver.client.conversion.ConversionRequest;
import be.phw.gedserver.client.merge.MergeRequest;
import be.phw.gedserver.config.ApplicationProperties;
import be.phw.gedserver.domain.CivadisDocument;
import be.phw.gedserver.domain.TicketInfo;
import be.phw.gedserver.security.SecurityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.text.StrBuilder;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**

 @author phw
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
@Api(value = "facadeRest", description = "Endpoint pour opérations sur les documents (Conversion, Fusion, Edition et CMIS")
public class FacadeRest {

    private static final String TMP_FILE_PREFIX = "cvd_facade_rest_app";

    @Resource
    private ApplicationProperties applicationProperties;

    public FacadeRest() {
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(path="/token", produces="text/plain")
    public String hello() {
        StrBuilder str = new StrBuilder("Token : ");
        str.append(SecurityUtils.getCurrentUserLogin().get());
        return str.toString();
    }

    private CivadisDocument getCivadisDocument(String id, String path, String name, boolean directory, String description){
        CivadisDocument doc = new CivadisDocument();
        doc.setId(id);
        doc.setPath(path);
        doc.setName(name);
        doc.setDirectory(directory);
        doc.setDescription(description);
        return doc;
    }

    /**
     * Création d'un répertoire
     * @param parent répertoire parent sous lequel créer le nouveau répertoire (path commencant par / ou id du répertoire)
     * @param name nom du nouveau répertoire
     * @return
     */
    @GetMapping(path="/createFolder")
    @ApiOperation(value = "Crée un répertoire sous un répertoire parent")
    public ResponseEntity<CivadisDocument> createFolder(@ApiParam(name="parent", value = "répertoire parent (path commencant par / ou id", required = true) @RequestParam(value="parent") String parent,
                                                        @ApiParam(name="name", value = "nom du nouveau répertoire", required = true) @RequestParam(value="name") String name) {
        return ResponseEntity.ok(getCivadisDocument("1", parent, name, true, "test"));
    }

    //l'url ne peut pas contenir un argument contenant des caractères spéciaux, on passe l'id par des requestParam
    /**
     * Obtenir les informations à propos d'un document
     * @param id id du document
     * @param path path du document
     * @return
     */
    @GetMapping(path="/document-detail")
    @ApiOperation(value = "get les infos d'un document (la version dépend de l'id)", response = CivadisDocument.class)
    public ResponseEntity<CivadisDocument> getDocument(
        @ApiParam(name="id", value = "id du document") @RequestParam(name = "id",required = false) String id,
        @ApiParam(name="path", value = "path du document") @RequestParam(name = "path", required = false) String path,
        @ApiParam(name="latest", value = "indique si on souhaite la dernière version du document, par défaut true") @RequestParam(name = "latest", required = false) Boolean latest
    ) {
        return ResponseEntity.ok(getCivadisDocument(id, path, "name", false, "latest " + latest));
    }

    //l'url ne peut pas contenir un argument contenant des caractères spéciaux, on passe l'id par des requestParam
    /**
     * Obtenir les informations à propos de toutes les versions d'un document
     * @param id id du document
     * @return
     */
    @GetMapping(path="/document-all-versions")
    @ApiOperation(value = "get les infos de toutes les versions d'un document (chaque version du document est retournée en tant que document)", response = CivadisDocument.class)
    public ResponseEntity<List<CivadisDocument>> getDocumentAllVersions(@ApiParam(name="id", value = "id du document", required = true) @RequestParam("id") String id) {
        List<CivadisDocument> list = Arrays.asList(
            getCivadisDocument("1", "path", "name", false, "version 1"),
            getCivadisDocument("1", "path", "name", false, "version 2"));
        return ResponseEntity.ok(list);
    }

    /**
     * Obtenir les infos des documents sous un répertoire parent
     * @param parent id du répertoire parent
     * @return
     */
    @GetMapping(path="/documents")
    @ApiOperation(value = "get les infos des documents sous un répertoire parent (dernière version des documents)", response = CivadisDocument.class)
    public ResponseEntity<List<CivadisDocument>> getDocuments(@ApiParam(name="parent", value = "path du répertoire parent", required = false) @RequestParam(value="parent", required=false) String parent) {
        //System.out.println(SecurityUtils.getCurrentUserJWT());
        List<CivadisDocument> list = Arrays.asList(
            getCivadisDocument("1", parent + "/name", "name", false, "test by parent"));
        return ResponseEntity.ok(list);
    }

    /**
     * Rechercher les documents par une recherche dans leur contenu, 3 types de recherche :
     *  - OR : les documents doivent contenir au moins 1 des mots (par défaut)
     *  - AND : les documents doivent contenir tous lers mots
     *  - PHRASE : les documents doivent contenir la phrase exacte
     * @param parent path ou id réperoire parent sous lequel rechercher (facultatif)
     * @param valueContent mot(s) à rechercher, séparés par des espaces
     * @param searchTypeContent type de recherche (OR, AND, PHRASE)
     * @return
     */
    @GetMapping(path="/documents-by-content")
    @ApiOperation(value = "get les infos des documents contenant le ou les mots recherchés", response = CivadisDocument.class)
    public ResponseEntity<List<CivadisDocument>> getDocumentsByContent(
            @ApiParam(name="parent", value = "path ou id du répertoire parent (un path doit commencé par /)") @RequestParam(name = "parent", required = false) String parent,
            @ApiParam(name="valueContent", value = "mots recherchés", required = true) @RequestParam("valueContent") String valueContent,
            @ApiParam(name="searchTypeContent", value = "type de recherche") @RequestParam(name = "searchTypeContent", required = false) String searchTypeContent) {

            List<CivadisDocument> list = Arrays.asList(
                    getCivadisDocument("1", parent + "/name", "name", false, "test by content : " + valueContent + ", " + searchTypeContent));
            return ResponseEntity.ok(list);
    }

    /**
     * Rechercher les documents par une recherche dans leurs tags, 2 types de recherche :
     *  - OR : les documents doivent être associés à au moins 1 des tags
     *  - AND : les documents doivent être associés à tous les tags
     * @param parent path id réperoire parent sous lequel rechercher (facultatif)
     * @param valueTags mot(s) à rechercher, séparés par des espaces
     * @param searchTypeTags type de recherche (OR, AND)
     * @return
     */
    @GetMapping(path="/documents-by-tags")
    @ApiOperation(value = "get les infos des documents contenant le ou les tags recherchés", response = CivadisDocument.class)
    public ResponseEntity<List<CivadisDocument>> getDocumentsByTags(
        @ApiParam(name="parent", value = "path ou id du répertoire parent (un path doit commencé par /)") @RequestParam(name = "parent", required = false) String parent,
        @ApiParam(name="valueTags", value = "tags recherchés", required = true) @RequestParam("valueTags") String valueTags,
        @ApiParam(name="searchTypeTags", value = "type de recherche") @RequestParam(name = "searchTypeTags", required = false) String searchTypeTags) {

        List<CivadisDocument> list = Arrays.asList(
                getCivadisDocument("1", parent + "/name", "name", false, "test by tags : " + valueTags + ", " + searchTypeTags));
        return ResponseEntity.ok(list);
    }

    /**
     * Recherche des documents par une recherche sur leur contenu et selon leurs tags
     * 3 types de recherche du contenu :
     *      - OR : les documents doivent contenir au moins 1 des mots (par défaut)
     *      - AND : les documents doivent contenir tous lers mots
     *      - PHRASE : les documents doivent contenir la phrase exacte
     * 2 types de recherche selon les tags:
     *      - OR : les documents doivent être associés à au moins 1 des tags
     *      - AND : les documents doivent être associés à tous les tags
     * Si des conditions de recherche sont précisées pour le contenu et pour les tags, les documements retenus devront satisfaire au 2 recherches
     * @param parent path ou id réperoire parent sous lequel rechercher (facultatif)
     * @param valueContent mot(s) à rechercher, séparés par des espaces
     * @param searchTypeContent type de recherche (OR, AND, PHRASE)
     * @param valueTags tag(s) à rechercher, séparés par des espaces
     * @param searchTypeTags type de recherche (OR, AND)
     * @return
     */
    @GetMapping(path="/documents-by-content-tags")
    @ApiOperation(value = "get les infos des documents correspondant aux critères de recherche (mots et/ou tags)", response = CivadisDocument.class)
    public ResponseEntity<List<CivadisDocument>> searchDocuments(
            @ApiParam(name="parent", value = "path ou id du répertoire parent (un path doit commencé par /)") @RequestParam(name = "parent", required = false) String parent,
            @ApiParam(name="valueContent", value = "mots recherchés", required = true) @RequestParam("valueContent") String valueContent,
            @ApiParam(name="searchTypeContent", value = "type de recherche") @RequestParam(name = "searchTypeContent", required = false) String searchTypeContent,
            @ApiParam(name="valueTags", value = "tags recherchés") @RequestParam(name = "valueTags", required = false) String valueTags,
            @ApiParam(name="searchTypeTags", value = "type de recherche") @RequestParam(name = "searchTypeTags", required = false) String searchTypeTags) {

            List<CivadisDocument> list = Arrays.asList(
                    getCivadisDocument("1", parent + "/name", "name", false, "test by content : " + valueContent + ", " + searchTypeContent),
                    getCivadisDocument("2", parent + "/name", "name", false, "test by tags : " + valueTags + ", " + searchTypeTags));
            return ResponseEntity.ok(list);
    }

    private List<String> listTags(String valueTags){
        if (valueTags != null && !"null".equalsIgnoreCase(valueTags.trim())){
            List<String> tags = new ArrayList<>();
            if (valueTags != null){
                StringTokenizer st = new StringTokenizer(valueTags, " ");
                while (st.hasMoreTokens()) {
                    tags.add(st.nextToken());
                }
            }
            return tags;
        } else {
            return null;
        }
    }

    //l'url ne peut pas contenir un argument contenant des caractères spéciaux, on passe l'id par des requestParam
    /**
     * Download un document selon son id
     * 
     * @param id     id du document
     * @param latest indique si on dowload la dernière version du document, sinon
     *               celle associée au no de version présent dans l'id
     * @return
     * @throws IOException
     */
    @GetMapping(path="/download-document")
    @ApiOperation(value = "download le document", response = org.springframework.core.io.Resource.class)
    public ResponseEntity<org.springframework.core.io.Resource> downloadDocument(
            @ApiParam(name="id", value = "id du document", required = true) @RequestParam("id") String id,
            @ApiParam(name="latest", value = "indique si on souhaite la dernière version du document") @RequestParam(name = "latest", required = false) Boolean latest) throws IOException {
            return getResource();
    }

    /**
     * Upload le fichier référencer par fileRef sous le répertoire parentPath
     * Si le fichier est déjà présent, upload en incrémentant la version
     * Si un targetName est fourni, le fichier sera stocké sous ce nom
     * @param fileRef référence au fichier à uploader
     * @param parentPath path du répertoire sous lequel placer le fichier
     * @return
     * @throws IOException
     * @throws OfficeException
     */
    @PostMapping(path="/upload-document")
    @ApiOperation(value = "Upload du document", response = CivadisDocument.class)
    public ResponseEntity<CivadisDocument> uploadDocument(
        @ApiParam(name="file", value = "fichier à uploader", required = true) @RequestParam("file") MultipartFile fileRef,
        @ApiParam(name="parent", value = "path du répertoire parent", required = true) @RequestParam("parent") String parentPath,
        @ApiParam(name="targetName", value = "nom de la cible") @RequestParam(value="targetName", required=false) String targetName,
        @ApiParam(name="description", value = "description du fichier") @RequestParam(value="description", required = false) String description
    ) throws IOException {
        return ResponseEntity.ok(getCivadisDocument("1", parentPath + "/" + targetName, targetName, false, "upload"));
    }

    //l'url ne peut pas contenir un argument contenant des caractères spéciaux, on passe l'id par des requestParam
    /**
     * Suppression d'un document
     * @param id id du document à supprimé
     */
    @DeleteMapping(path="/delete-document")
    @ApiOperation(value = "Supprime un document")
    public ResponseEntity deleteDocument(@ApiParam(name="id", value = "id du document", required = true) @RequestParam("id") String id) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Check out la dernière version du document
     * @param id
     * @param path
     * @return
     * @throws DocumentConcurrentException
     */
    @GetMapping(path="/checkout-document")
    @ApiOperation(value = "Check out la dernière version du document et en retourne une copie privée)", response = CivadisDocument.class)
    public ResponseEntity<CivadisDocument> checkOutLatestDocument(
        @ApiParam(name="id", value = "id du document") @RequestParam(value = "id", required = false) String id,
        @ApiParam(name="path", value = "path du document") @RequestParam(value = "path", required = false) String path) {
            return ResponseEntity.ok(getCivadisDocument(id, path, "name", false, "checkout"));
    }

    /**
     * Upload et document et fait le checkin
     * @param fileRef fichier uploadé
     * @param id id de la copie privée du document
     * @param description description du document
     * @return
     * @throws IOException
     */
    @PostMapping(path="/upload-checkin-document")
    @ApiOperation(value = "Checkin du document", response = CivadisDocument.class)
    public ResponseEntity<CivadisDocument> uploadCheckInDocument(
        @ApiParam(name="file", value = "fichier à uploader", required = true) @RequestParam("file") MultipartFile fileRef,
        @ApiParam(name="id", value = "id du document", required = true) @RequestParam("id") String id,
        @ApiParam(name="description", value = "description du fichier") @RequestParam(value="description", required = false) String description
    ) throws IOException {
        return ResponseEntity.ok(getCivadisDocument(id, "path_to_file", fileRef.getName(), false, description));
    }

    /**
     * Indique si la dernière version d'un document est lockée
     * @param id id du document
     * @return
     */
    @GetMapping(path="/is-document-checked-out")
    @ApiOperation(value = "get les infos d'un document (la version dépend de l'id)", response = Boolean.class)
    public ResponseEntity<Boolean> isDocumentCheckedOut(
            @ApiParam(name="id", value = "id du document") @RequestParam(value = "id", required = false) String id,
            @ApiParam(name="path", value = "path du document") @RequestParam(value = "path", required = false) String path) {
        return ResponseEntity.ok(true);
    }

    /**
     * Annule le lock de la dernière version d'un document
     * @param id id du document
     * @return
     */
    @GetMapping(path="/cancel-document-checkout")
    @ApiOperation(value = "Annule le lock de la dernière version d'un document", response = Boolean.class)
    public ResponseEntity<Boolean> cancelDocumentCheckOut(
            @ApiParam(name="id", value = "id du document") @RequestParam(value = "id", required = false) String id) {
            return ResponseEntity.ok(true);
    }

    /***********************************************************/
    /** Edit-service *******************************************/
    /***********************************************************/

    /**
     * Retourne l'url à utiliser pour une édition en ligne du document (protocol wopi)
     * @param id id du document
     * @param mergeFieldsId champs de fusion
     * @param readOnly indique si édition en readonly
     * @return
     */
    @RequestMapping(path="/editUrl", method=RequestMethod.GET, produces="text/plain")
    @ApiOperation(value = "get url pour édition en ligne du document avec protocol wopi", response = String.class)
    public String getEditUrl(
            @ApiParam(name="id", value = "id du document", required = true) @RequestParam("id") String id,
            @ApiParam(name="mergeFieldsId", value = "champs de fusion") @RequestParam(value="mergeFieldsId", required=false) String mergeFieldsId,
            @ApiParam(name="readOnly", value = "edition en readonly") @RequestParam(value="readOnly", required=false) Boolean readOnly) {

        return "url from args : " + id + "/" + mergeFieldsId + "/" + readOnly.toString();
    }

    /**
     * Edition du document dans un libre office (protocol webdav)
     * @param id id du documemt
     * @param mergeFieldsId champs de fusion
     * @param readOnly indique si edition en readonly
     * @return
     */
    @GetMapping(path="/webdavOdt")
    @ApiOperation(value = "Edition du document dans libre office par protocol webdav", response = org.springframework.core.io.Resource.class)
    public ResponseEntity<org.springframework.core.io.Resource> getWebdavOdt(
            @ApiParam(name="id", value = "id du document", required = true) @RequestParam("id") String id,
            @ApiParam(name="mergeFieldsId", value = "champs de fusion") @RequestParam(value="mergeFieldsId", required=false) String mergeFieldsId,
            @ApiParam(name="readOnly", value = "edition en readonly") @RequestParam(value="readOnly", required=false) Boolean readOnly) {
            return null;
    }

    /**
     * Télécharge l'extension à installer dans libre office
     * @return
     */
    @GetMapping(path="/webdavOdtExt")
    @ApiOperation(value = "Download extension pour libre office", response = org.springframework.core.io.Resource.class)
    public ResponseEntity<org.springframework.core.io.Resource> getWebdavOdt() {
        return null;
    }

    /**
     * Edition du document dans MS Word (protocol webdav)
     * @param id id du documemt
     * @param mergeFieldsId champs de fusion
     * @param readOnly indique si edition en readonly
     * @return
     */
    @GetMapping(path="/webdavDocm")
    @ApiOperation(value = "Edition du document dans ms office par protocol webdav", response = org.springframework.core.io.Resource.class)
    public ResponseEntity<org.springframework.core.io.Resource> getWebdavDocm(
            @ApiParam(name="id", value = "id du document", required = true) @RequestParam("id") String id,
            @ApiParam(name="mergeFieldsId", value = "champs de fusion") @RequestParam(value="mergeFieldsId", required=false) String mergeFieldsId,
            @ApiParam(name="readOnly", value = "edition en readonly") @RequestParam(value="readOnly", required=false) Boolean readOnly) {
        return null;
    }


    /***********************************************************/
    /** Conversion-service *************************************/
    /***********************************************************/
    /**
     * Conversion d'un document
     * @param id id du document à convertir
     * @param targetName nom et extension cible
     * @return
     * @throws OfficeException
     */
    @GetMapping(path="/convert")
    @ApiOperation(value = "Conversion du document", response = TicketInfo.class)
    public ResponseEntity<TicketInfo> convert(
            @ApiParam(name="id", value = "id du document à convertir", required = true) @RequestParam("id") String id,
            @ApiParam(name="targetName", value = "nom et extension cible", required = true) @RequestParam(value="targetName") String targetName,
            @ApiParam(name="engineType", value = "type de moteur de conversion (office ou libreoffice)") @RequestParam(value="engineType", required = false) String engineType) {
        return ResponseEntity.ok(new TicketInfo(1L));
    }

    @PostMapping(path="/convert")
    @ApiOperation(value = "Conversion du document", response = TicketInfo.class)
    public ResponseEntity<TicketInfo> convert(
        @ApiParam(name="engineType", value = "type de moteur de conversion (office ou libreoffice)") @RequestParam(value="engineType", required = false) String engineType,
        @ApiParam(name="request", value = "params de conversion", required = true) @RequestBody ConversionRequest request) {
            return ResponseEntity.ok(new TicketInfo(1L));
    }

    @PostMapping(path="/convertStream")
    @ApiOperation(value = "Conversion du document non stocké dans alfresco et retour du résultat (traitement synchrone)", response = org.springframework.core.io.Resource.class)
    public ResponseEntity<org.springframework.core.io.Resource> convert(
        @ApiParam(name="file", value="fichier à convertir", required = true) @RequestParam("file") MultipartFile fileRef,
        @ApiParam(name="sourceMimeType", value="mimetype du fichier à convertir", required = true) @RequestParam("sourceMimeType") String sourceMimeType,
        @ApiParam(name="destMimeType", value="mimetype dans lequel convertir", required = true) @RequestParam("destMimeType") String destMimeType,
        @ApiParam(name="engineType", value = "type de moteur de conversion (office ou libreoffice)") @RequestParam(value="engineType", required = false) String engineType) throws IOException {
        return getResource();
    }

    /**
     * Upload le fichier référencer par fileRef sous le répertoire parentPath
     * Si le fichier est déjà présent, upload en incrémentant la version
     * Si un targetName est fourni, le fichier sera converti et stocké sous ce nom
     * @param fileRef référence au fichier à uploader
     * @param parentPath path du répertoire sous lequel placer le fichier
     * @param targetName nom et extension du fichier destination, facultatif
     * @return
     * @throws IOException
     * @throws OfficeException
     */
    @PostMapping(path="/upload-convert-document")
    @ApiOperation(value = "Upload et Conversion du document", response = TicketInfo.class)
    public ResponseEntity<TicketInfo> uploadDocument(
            @ApiParam(name="file", value = "fichier à uploader", required = true) @RequestParam("file") MultipartFile fileRef,
            @ApiParam(name="parent", value = "path du répertoire parent", required = true) @RequestParam("parent") String parentPath,
            @ApiParam(name="description", value = "description du fichier") @RequestParam(value="description", required = false) String description,
            @ApiParam(name="targetName", value = "nom et extension cible", required=true) @RequestParam(value="targetName") String targetName,
            @ApiParam(name="engineType", value = "type de moteur de conversion (office ou libreoffice)") @RequestParam(value="engineType", required = false) String engineType
    ) throws IOException {
        return ResponseEntity.ok(new TicketInfo(1L));
    }

    /***********************************************************/
    /** Merge-service ******************************************/
    /***********************************************************/
    /**
1     * Fusion d'un modèle de document avec des données
     * @return
     * @throws Exception
     */
    @PostMapping(path="/merge")
    @ApiOperation(value = "Fusion d'unb modèle de document et de données", response = TicketInfo.class)
    public ResponseEntity<TicketInfo> merge(
        @ApiParam(name="templateId", value = "id du template dans la ged", required = true) @RequestParam("templateId") String templateId,
        @ApiParam(name="mergeType", value = "type de merge (ex: odt-velocity, odt-saphir, pdf-form, jasper-json", required = true) @RequestParam("mergeType") String mergeType,
        @ApiParam(name="destPath", value = "path de destination du fichier mergé dans la ged", required = true) @RequestParam("destPath") String destPath,
        @ApiParam(name="description", value = "description du fichier") @RequestParam(value = "description", required = false) String description,
        @ApiParam(name="data", value = "données à fusionner, aux format JSON", required = true) @RequestBody Map<String,Object> data
    ) throws Exception {

        return ResponseEntity.ok(new TicketInfo(1L));
    }


    @RequestMapping(path="/mergeRequest", method=RequestMethod.POST)
    public ResponseEntity<TicketInfo> mergeRequest(@RequestBody MergeRequest request) throws Exception {

        return ResponseEntity.ok(new TicketInfo(1L));
    }

    @PostMapping(path="/mergeStream")
    @ApiOperation(value = "Fusion d'un modèle de document et de données, sans utilisation alfresco (traitement synchrone)", response = org.springframework.core.io.Resource.class)
    public ResponseEntity<org.springframework.core.io.Resource> merge(
        @ApiParam(name="template", value="modèle de document", required = true) @RequestParam("template") MultipartFile templateRef,
        @ApiParam(name="data", value="fichier json de données", required = true) @RequestParam("data") MultipartFile dataRef,
        @ApiParam(name="mergeType", value="type de fusion", required = true) @RequestParam("mergeType") String mergeType,
        @ApiParam(name="destMimeType", value="mime type du fichier de fusion") @RequestParam(value="destMimeType", required=false) String destMimeType) throws Exception {

        return getResource();
    }

    //test sans alfresco////////////////////////////////////////////////////////////////

   
    @GetMapping(path="/test-upload-document", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CivadisDocument> testUploadDocument(
        @RequestParam("file") MultipartFile fileRef, @RequestParam("parent") String parentPath, @RequestParam(value="description", required = false) String description
    ) throws IOException {

        File tmp = new File("/Users/philippe/tmp/testUploaded");
        try(InputStream in = fileRef.getInputStream()) {
            Files.copy(in, tmp.toPath(), StandardCopyOption.REPLACE_EXISTING);
            CivadisDocument doc = new CivadisDocument();
            return ResponseEntity.ok(doc);
        } 
    }

    @GetMapping(path="/test-download-document", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<org.springframework.core.io.Resource> testDownloadDocument(@RequestParam("id") String id) throws FileNotFoundException, IOException {
        return getResource();
    }

    @GetMapping(path="/test-documents")
    public ResponseEntity<List<CivadisDocument>> testDocuments(@RequestParam(name = "parent", required = false) String parent) {
        CivadisDocument doc1 = new CivadisDocument();
        doc1.setName("testdoc1");
        CivadisDocument doc2 = new CivadisDocument();
        doc2.setName("testdoc2");
        return ResponseEntity.ok(Arrays.asList(doc1, doc2));
    }
    
    private ResponseEntity<org.springframework.core.io.Resource> getResource() throws FileNotFoundException, IOException {
        File tmp = new File("/Users/philippe/tmp/test.docx");
        InputStream in = new FileInputStream(tmp);
        // Attention, si on close l'inputstream en l'incluant dans un try(...),
        // spring ne peut pas le lire et génère une erreur lors de l'envoi
        MediaType type = MediaType.APPLICATION_OCTET_STREAM;
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(new InputStreamResource(in));
    }


    



}
