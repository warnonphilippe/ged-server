package be.phw.gedserver.client.merge;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Map;

/**

 @author david
 */
public class SpringMergeServiceClient {

    private final static ObjectMapper defaultObjectMapper;
    private final static RestTemplate defaultRestTemplate;

    static {
        defaultObjectMapper = new ObjectMapper();
        defaultObjectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        HttpMessageConverter<Object> jackson = new MappingJackson2HttpMessageConverter(defaultObjectMapper);
        HttpMessageConverter<Resource> resource = new ResourceHttpMessageConverter();
        FormHttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();
        formHttpMessageConverter.addPartConverter(jackson);
        formHttpMessageConverter.addPartConverter(resource);
        defaultRestTemplate = new RestTemplate(Arrays.asList(jackson, resource, formHttpMessageConverter));
    }

    private String baseUrl;
    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;

    public String getBaseUrl() {
        return baseUrl;
    }
    public void setBaseUrl(String baseUrl) {
        this.baseUrl=baseUrl;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate=restTemplate;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private RestTemplate getRestTemplate() {
        return restTemplate != null ? restTemplate : defaultRestTemplate;
    }

    private ObjectMapper getObjectMapper() {
        return objectMapper != null ? objectMapper : defaultObjectMapper;
    }

    public MergeResponse merge(MergeRequest request) {
        return getRestTemplate().postForObject(baseUrl + "/merge", request, MergeResponse.class);
    }

    public void merge(File inFile, Map<String, Object> data, File outFile, String mergeType, String mimeType) throws IOException {
        try(
            OutputStream out = new FileOutputStream(outFile);
        ) {
            doMerge(inFile, data, out, mergeType, mimeType);
        }
    }

    public void merge(File inFile, Map<String, Object> data, OutputStream out, String mergeType, String mimeType) throws IOException {
        doMerge(inFile, data, out, mergeType, mimeType);
    }

    public void merge(InputStream in, Map<String, Object> data, OutputStream out, String mergeType, String mimeType) throws IOException {
        doMerge(in, data, out, mergeType, mimeType);
    }

    private void doMerge(Object in, Map<String, Object> data, OutputStream out, String mergeType, String mimeType) throws IOException {
        File tmp = null;
        File tmpData = null;
        try {
            if(in instanceof InputStream) {
                tmp = File.createTempFile(SpringMergeServiceClient.class.getSimpleName(), null);
                Files.copy((InputStream)in, tmp.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } else {
                tmp = (File)in;
            }

            tmpData = File.createTempFile(SpringMergeServiceClient.class.getSimpleName(), null);
            getObjectMapper().writeValue(tmpData, data);

            MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
            parameters.add("template", new FileSystemResource(tmp));
            parameters.add("data", new FileSystemResource(tmpData));
            parameters.add("mergeType", mergeType);
            parameters.add("destMimeType", mimeType);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "multipart/form-data");
            headers.set("Accept", "text/plain");

            Resource resp = getRestTemplate().postForObject(baseUrl + "/mergeStream",
                    new HttpEntity<MultiValueMap<String, Object>>(parameters, headers),
                    Resource.class);
            try(InputStream tmpIn = resp.getInputStream()) {
                byte[] buffer = new byte[10240];
                for(int r;(r=tmpIn.read(buffer))>=0;) {
                    out.write(buffer, 0, r);
                }
            }
        } finally {
            if(tmpData != null) try { tmpData.delete(); } catch(Exception e) {}
            if(in instanceof InputStream && tmp != null) {
                try { tmp.delete(); } catch(Exception e) {}
            }
        }
    }

}
