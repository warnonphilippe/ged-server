package be.phw.gedserver.dto;

public class EntityIdentDto {

    private String companyId;
    private String niss;
    private String inami;

    public EntityIdentDto() {
    }

    public String getCompanyId() {
        return this.companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public EntityIdentDto companyId(String id) {
        this.companyId = id;
        return this;
    }

    public String getNiss() {
        return this.niss;
    }

    public void setNiss(String niss) {
        this.niss = niss;
    }

    public EntityIdentDto niss(String id) {
        this.niss = id;
        return this;
    }

    public String getInami() {
        return this.inami;
    }

    public void setInami(String inami) {
        this.inami = inami;
    }

    public EntityIdentDto inami(String id) {
        this.inami = id;
        return this;
    }

}
