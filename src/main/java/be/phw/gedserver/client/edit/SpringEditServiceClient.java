package be.phw.gedserver.client.edit;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**

 @author david
 */
public class SpringEditServiceClient {

    private String baseUrl;
    private RestTemplate restTemplate;

    public String getBaseUrl() {
        return baseUrl;
    }
    public void setBaseUrl(String baseUrl) {
        this.baseUrl=baseUrl;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate=restTemplate;
    }

    public String getWebvavUrl(EditRequest request) {
        return restTemplate.postForObject(baseUrl + "/webdavUrl", request, String.class);
    }

    public String getWopiUrl(EditRequest request) {
        return restTemplate.postForObject(baseUrl + "/wopiUrl", request, String.class);
    }

    public byte[] getWebdavOdtExt() {
        return restTemplate.getForObject(baseUrl + "/resources/Civadis.oxt", byte[].class);
    }

    public byte[] getWebdavOdt(EditRequest request) {
        return restTemplate.postForObject(baseUrl + "/webdavOdt", request, byte[].class);
    }

    public byte[] getWebdavDocm(EditRequest request) {
        return restTemplate.postForObject(baseUrl + "/webdavDocm", request, byte[].class);
    }

    public EditRequest upload(File file, String mimeType, Boolean readOnly) throws IOException {
        return doUpload(file, null, mimeType, readOnly);
    }
    public EditRequest upload(File file, File mergeFile, String mimeType, Boolean readOnly) throws IOException {
        return doUpload(file, mergeFile, mimeType, readOnly);
    }
    public EditRequest upload(InputStream in, String mimeType, Boolean readOnly) throws IOException {
        return doUpload(in, null, mimeType, readOnly);
    }
    public EditRequest upload(InputStream in, InputStream mergeIn, String mimeType, Boolean readOnly) throws IOException {
        return doUpload(in, mergeIn, mimeType, readOnly);
    }

    private EditRequest doUpload(Object in, Object mergeIn, String mimeType, Boolean readOnly) throws IOException {
        File tmp = null;
        File tmpMerge = null;
        try {
            if(in instanceof InputStream) {
                tmp = File.createTempFile(SpringEditServiceClient.class.getSimpleName(), null);
                Files.copy((InputStream)in, tmp.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } else {
                tmp = (File)in;
            }
            if(mergeIn != null) {
                if(mergeIn instanceof InputStream) {
                    tmpMerge = File.createTempFile(SpringEditServiceClient.class.getSimpleName(), null);
                    Files.copy((InputStream)mergeIn, tmpMerge.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } else {
                    tmpMerge = (File)mergeIn;
                }
            }

            MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
            parameters.add("file", new FileSystemResource(tmp));
            if(mergeIn != null) {
                parameters.add("mergeFields", new FileSystemResource(tmpMerge));
            }
            parameters.add("mimeType", mimeType);
            parameters.add("readOnly", readOnly != null && readOnly);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "multipart/form-data");
            headers.set("Accept", "application/json");

            return restTemplate.postForObject(baseUrl + "/upload",
                    new HttpEntity<MultiValueMap<String, Object>>(parameters, headers),
                    EditRequest.class);
        } finally {
            if(in instanceof InputStream && tmp != null) {
                try { tmp.delete(); } catch(Exception e) {}
            }
            if(mergeIn != null && mergeIn instanceof InputStream && tmpMerge != null) {
                try { tmpMerge.delete(); } catch(Exception e) {}
            }
        }
    }

    public void download(EditRequest request, OutputStream out) throws IOException {
        org.springframework.core.io.Resource resp = restTemplate.postForObject(baseUrl + "/download",
                request,
                org.springframework.core.io.Resource.class);
        try(InputStream tmpIn = resp.getInputStream()) {
            byte[] buffer = new byte[10240];
            for(int r;(r=tmpIn.read(buffer))>=0;) {
                out.write(buffer, 0, r);
            }
        }
    }

    public void deleteUploadedDocument(EditRequest request) {
        restTemplate.postForObject(baseUrl + "/deleteUploadedDocument", request, Void.class);
    }
}
