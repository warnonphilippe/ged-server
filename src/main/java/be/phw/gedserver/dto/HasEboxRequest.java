package be.phw.gedserver.dto;

import java.util.List;

public class HasEboxRequest {

    private String qualityCode;
    private List<EntityIdentDto> entityIdentList;

    public HasEboxRequest() {
    }

    public String getQualityCode() {
        return this.qualityCode;
    }

    public void setQualityCode(String qualityCode) {
        this.qualityCode = qualityCode;
    }

    public List<EntityIdentDto> getEntityIdent() {
        return this.entityIdentList;
    }

    public void setEntityIdent(List<EntityIdentDto> entityIdentList) {
        this.entityIdentList = entityIdentList;
    }

}