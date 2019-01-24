package be.phw.gedserver.web.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.annotation.Resource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang.text.StrBuilder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import be.phw.gedserver.dto.HasEboxRequest;
import be.phw.gedserver.dto.HasEboxResponse;
import be.phw.gedserver.dto.TicketInfo;
import feign.form.FormData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author phw
 */
//@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
@Api(value = "ebox", description = "Endpoint pour opérations sur les documents (Envoi vers ebox,...")
public class DocumentResource {

        private static String TMP_FILE_PREFIX = "smartdoc_tmp";

        public DocumentResource() {
        }

        //TODO : ajouter méthode GetRestrictedCitizenProfile 

        @PostMapping("/ebox/has-ebox")
        @ApiOperation(value = "Indique si les eboxes existent", response = HasEboxResponse.class)
        public ResponseEntity<HasEboxResponse> hasEbox(
                        @ApiParam(name = "request", value = "Critères de recherche des ebox", required = true) @RequestBody HasEboxRequest request) {
                return ResponseEntity.ok(new HasEboxResponse());
        }

        @GetMapping("/ebox/test")
        public ResponseEntity<HasEboxResponse> test() {
                return ResponseEntity.ok(new HasEboxResponse());
        }

        @PostMapping("/ebox/test")
        public ResponseEntity<HasEboxResponse> testPost(@RequestBody HasEboxRequest request) {
                return ResponseEntity.ok(new HasEboxResponse());
        }

        @PutMapping("/ebox/test")
        public ResponseEntity<HasEboxResponse> testPut(@RequestBody HasEboxRequest request) {
                return ResponseEntity.ok(new HasEboxResponse());
        }

        // TODO : a compléter, manque le recipientId [1..5],
        // mais que passer, comment le passer ???
        // -> compléter les params de l'upload
        // -> compléter le client !!!

        /**
         * Evoir le document dans la ebox
         * 
         * @param fileRef     fichier à uploader
         * @param title       titre du document
         * @param ticket      code d'identification du document
         * @param sequence    version du document
         * @param messageType type de message
         * @param boxType     type de box
         * @return
         * @throws Exception
         * @throws OfficeException
         */
        @PostMapping("/ebox/upload-document")
        @ApiOperation(value = "Envoi du document vers l'ebox", response = TicketInfo.class)
        public ResponseEntity<TicketInfo> uploadDocument(
                        @ApiParam(name = "file", value = "fichier à uploader", required = true) @RequestParam("file") MultipartFile fileRef,
                        @ApiParam(name = "title", value = "titre du document", required = true) @RequestParam("title") String title,
                        @ApiParam(name = "ticket", value = "code d'identification du document", required = true) @RequestParam("ticket") String ticket,
                        @ApiParam(name = "sequence", value = "version du document") @RequestParam(value = "sequence", required = false) String sequence,
                        @ApiParam(name = "messageType", value = "type de message", required = true) @RequestParam("messageType") String messageType,
                        @ApiParam(name = "boxType", value = "type de box", required = true) @RequestParam("boxType") String boxType,
                        @ApiParam(name = "qualityCode", value = "quality code", required = true) @RequestParam("qualityCode") String qualityCode,
                        @ApiParam(name = "recipient", value = "recipient") @RequestParam(value = "recipient", required = false) String recipient,
                        @ApiParam(name = "metas", value = "Mete-données") @RequestParam(value = "metas", required = false) String metas)
                        throws Exception {

                
                        return ResponseEntity.ok(new TicketInfo(1L));

        }

}
