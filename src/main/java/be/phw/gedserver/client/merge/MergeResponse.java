package be.phw.gedserver.client.merge;

/**

 @author david
 */
public class MergeResponse {
    private String documentRepo;
    private String documentId;
    private String mimeType;

    public MergeResponse() {
    }

    public MergeResponse(String documentRepo, String documentId, String mimeType) {
        this.documentRepo=documentRepo;
        this.documentId=documentId;
        this.mimeType = mimeType;
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

    public String getMimeType() {
        return mimeType;
    }
    public void setMimeType(String mimeType) {
        this.mimeType=mimeType;
    }
}
