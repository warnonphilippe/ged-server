package be.phw.gedserver.client.edit;

/**

 @author david
 */
public class EditRequest {
    private String documentRepo;
    private String documentPath;
    private String documentId;
    private String documentMimeType;
    private String mergeFieldsRepo;
    private String mergeFieldsPath;
    private String mergeFieldsId;
    private String title;
    private boolean readOnly;

    public String getDocumentRepo() {
        return documentRepo;
    }
    public void setDocumentRepo(String documentRepo) {
        this.documentRepo=documentRepo;
    }

    public String getDocumentPath() {
        return documentPath;
    }
    public void setDocumentPath(String documentPath) {
        this.documentPath=documentPath;
    }

    public String getDocumentId() {
        return documentId;
    }
    public void setDocumentId(String documentId) {
        this.documentId=documentId;
    }

    public String getDocumentMimeType() {
        return documentMimeType;
    }
    public void setDocumentMimeType(String documentMimeType) {
        this.documentMimeType=documentMimeType;
    }

    public String getMergeFieldsRepo() {
        return mergeFieldsRepo;
    }
    public void setMergeFieldsRepo(String mergeFieldsRepo) {
        this.mergeFieldsRepo=mergeFieldsRepo;
    }

    public String getMergeFieldsPath() {
        return mergeFieldsPath;
    }
    public void setMergeFieldsPath(String mergeFieldsPath) {
        this.mergeFieldsPath=mergeFieldsPath;
    }

    public String getMergeFieldsId() {
        return mergeFieldsId;
    }
    public void setMergeFieldsId(String mergeFieldsId) {
        this.mergeFieldsId=mergeFieldsId;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title=title;
    }

    public boolean isReadOnly() {
        return readOnly;
    }
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }
}
