package be.phw.gedserver.client.conversion;

/**

 @author david
 */
public class ConversionResponse {
    private String documentRepo;
    private String documentId;

    public ConversionResponse() {
    }

    public ConversionResponse(String documentRepo, String documentId) {
        this.documentRepo=documentRepo;
        this.documentId=documentId;
    }

    public String getDocumentRepo() {
        return documentRepo;
    }
    public void setDocumentRepo(String documentRepo) {
        this.documentRepo=documentRepo;
    }

    public String getDocumentId() {
        return documentId;
    }
    public void setDocumentId(String documentId) {
        this.documentId=documentId;
    }
}
