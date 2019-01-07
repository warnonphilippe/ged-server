package be.phw.gedserver.client.conversion;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**

 @author david
 */
public class SpringConversionServiceClient {

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

    public ConversionResponse convert(ConversionRequest request) {
        return restTemplate.postForObject(baseUrl + "/convert", request, ConversionResponse.class);
    }

    public List<SupportedFormat> getSupportedFormats(String intutType) {
        SupportedFormat[] sf = restTemplate.getForObject(baseUrl + "/supportedFormats?input={input}", SupportedFormat[].class
                , Collections.singletonMap("input", intutType));
        return Arrays.asList(sf);
    }

    public void convert(File inFile, File outFile, String sourceMimeType, String destMimeType) throws IOException {
        try(
            OutputStream out = new FileOutputStream(outFile);
        ) {
            doConvert(inFile, out, sourceMimeType, destMimeType);
        }
    }

    public void convert(File inFile, OutputStream out, String sourceMimeType, String destMimeType) throws IOException {
        doConvert(inFile, out, sourceMimeType, destMimeType);
    }

    public void convert(InputStream in, OutputStream out, String sourceMimeType, String destMimeType) throws IOException {
        doConvert(in, out, sourceMimeType, destMimeType);
    }

    private void doConvert(Object in, OutputStream out, String sourceMimeType, String destMimeType) throws IOException {
        File tmp = null;
        try {
            if(in instanceof InputStream) {
                tmp = File.createTempFile(SpringConversionServiceClient.class.getSimpleName(), null);
                Files.copy((InputStream)in, tmp.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } else {
                tmp = (File)in;
            }

            MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
            parameters.add("file", new FileSystemResource(tmp));
            parameters.add("sourceMimeType", sourceMimeType);
            parameters.add("destMimeType", destMimeType);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "multipart/form-data");
            headers.set("Accept", "text/plain");

            org.springframework.core.io.Resource resp = restTemplate.postForObject(baseUrl + "/convertStream",
                    new HttpEntity<MultiValueMap<String, Object>>(parameters, headers),
                    org.springframework.core.io.Resource.class);
            try(InputStream tmpIn = resp.getInputStream()) {
                byte[] buffer = new byte[10240];
                for(int r;(r=tmpIn.read(buffer))>=0;) {
                    out.write(buffer, 0, r);
                }
            }
        } finally {
            if(in instanceof InputStream && tmp != null) {
                try { tmp.delete(); } catch(Exception e) {}
            }
        }
    }

}
