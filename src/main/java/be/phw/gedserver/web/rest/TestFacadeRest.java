package be.phw.gedserver.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.text.StrBuilder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.HttpEntityMethodProcessor;

import be.phw.gedserver.domain.CivadisDocument;
import be.phw.gedserver.security.SecurityUtils;

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
 * 
 * @author phw
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/test")
@Api(value = "testFacadeRest", description = "Endpoint pour opérations de tests")
public class TestFacadeRest {

    public TestFacadeRest() {
    }

    // test sans
    // alfresco////////////////////////////////////////////////////////////////

    @GetMapping(path = "/test-documents")
    public ResponseEntity<List<CivadisDocument>> testDocuments(
            @RequestParam(name = "parent", required = false) String parent) {
        if (SecurityUtils.getCurrentUserLogin().isPresent()) {
            System.out.println("Current User : " + SecurityUtils.getCurrentUserLogin().get());
        }
        CivadisDocument doc1 = new CivadisDocument();
        doc1.setName("testdoc1");
        CivadisDocument doc2 = new CivadisDocument();
        doc2.setName("testdoc2");
        return ResponseEntity.ok(Arrays.asList(doc1, doc2));
    }

    @PostMapping(path = "/test-post-document")
    public ResponseEntity<CivadisDocument> testPostDocument(@RequestBody CivadisDocument doc) throws IOException {
        return ResponseEntity.ok(doc);
    }

    @PostMapping(path = "/test-upload-document") // consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    public ResponseEntity<CivadisDocument> testUploadDocument(@RequestParam("file") MultipartFile fileRef,
            @RequestParam("parent") String parentPath,
            @RequestParam(value = "description", required = false) String description) throws IOException {

        File tmp = new File("/Users/philippe/testuploadeddoc");
        try (InputStream in = fileRef.getInputStream()) {
            Files.copy(in, tmp.toPath(), StandardCopyOption.REPLACE_EXISTING);
            CivadisDocument doc = new CivadisDocument();
            return ResponseEntity.ok(doc);
        }
    }

    @GetMapping(path = "/test-download-document") // produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    public ResponseEntity<org.springframework.core.io.Resource> testDownloadDocument(@RequestParam("id") String id)
            throws FileNotFoundException, IOException {
        File tmp = new File("/Users/philippe/testuploadeddoc");
        InputStream in = new FileInputStream(tmp);
        // Attention, si on close l'inputstream en l'incluant dans un try(...),
        // spring ne peut pas le lire et génère une erreur lors de l'envoi
        MediaType type = MediaType.APPLICATION_OCTET_STREAM;
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(new InputStreamResource(in));
    }

}
