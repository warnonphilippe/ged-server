package be.phw.gedserver.dto;

import java.util.List;

public class HasEboxResponse {

    private String qualityCode;
    private List<EboxInfos> eboxes;

    public String getQualityCode() {
        return this.qualityCode;
    }

    public void setQualityCode(String qualityCode) {
        this.qualityCode = qualityCode;
    }

    public List<EboxInfos> getEboxes() {
        return this.eboxes;
    }

    public void setEboxes(List<EboxInfos> eboxes) {
        this.eboxes = eboxes;
    }

}