package be.phw.gedserver.client.conversion;

/**

 @author david
 */
public class ConversionRequest {
    private String sourceRepo;
    private String sourcePath;
    private String sourceId;
    private String sourceMimeType;
    private String sourceExt;

    private String destRepo;
    private String destPath;
    private String destId;
    private String destMimeType;
    private String destDescription;

    public String getSourceRepo() {
        return sourceRepo;
    }
    public void setSourceRepo(String sourceRepo) {
        this.sourceRepo=sourceRepo;
    }

    public String getSourcePath() {
        return sourcePath;
    }
    public void setSourcePath(String sourcePath) {
        this.sourcePath=sourcePath;
    }

    public String getSourceId() {
        return sourceId;
    }
    public void setSourceId(String sourceId) {
        this.sourceId=sourceId;
    }

    public String getSourceMimeType() {
        return sourceMimeType;
    }
    public void setSourceMimeType(String sourceMimeType) {
        this.sourceMimeType=sourceMimeType;
    }

    public String getSourceExt(){
		return this.sourceExt;
	}

	public void setSourceExt(String sourceExt){
		this.sourceExt = sourceExt;
	}

    public String getDestRepo() {
        return destRepo;
    }
    public void setDestRepo(String destRepo) {
        this.destRepo=destRepo;
    }

    public String getDestPath() {
        return destPath;
    }
    public void setDestPath(String destPath) {
        this.destPath=destPath;
    }

    public String getDestId() {
        return destId;
    }
    public void setDestId(String destId) {
        this.destId=destId;
    }

    public String getDestMimeType() {
        return destMimeType;
    }
    public void setDestMimeType(String destMimeType) {
        this.destMimeType=destMimeType;
    }

    public String getDestDescription() {
        return destDescription;
    }

    public void setDestDescription(String destDescription) {
        this.destDescription = destDescription;
    }
}
