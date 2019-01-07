package be.phw.gedserver.client.conversion;

import java.util.Objects;

/**

 @author david
 */
public class SupportedFormat {

    private String mimeType;
    private String extension;

    public SupportedFormat() {
    }

    public SupportedFormat(String mimeType, String extension) {
        this.mimeType=mimeType;
        this.extension=extension;
    }

    public String getMimeType() {
        return mimeType;
    }
    public void setMimeType(String mimeType) {
        this.mimeType=mimeType;
    }

    public String getExtension() {
        return extension;
    }
    public void setExtension(String extension) {
        this.extension=extension;
    }

    @Override
    public int hashCode() {
        int hash=5;
        hash=31 * hash + Objects.hashCode(this.mimeType);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(obj == null) {
            return false;
        }
        if(getClass() != obj.getClass()) {
            return false;
        }
        final SupportedFormat other=(SupportedFormat) obj;
        if(!Objects.equals(this.mimeType, other.mimeType)) {
            return false;
        }
        return true;
    }


}
