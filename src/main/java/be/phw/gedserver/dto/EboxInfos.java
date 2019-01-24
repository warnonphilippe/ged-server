package be.phw.gedserver.dto;

import java.util.Date;


public class EboxInfos {

    private boolean exists;
    private EntityIdentDto entityIdent;
    private Date lastConnecion;

    public boolean isExists() {
        return this.exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public EntityIdentDto getEntityIdent() {
        return this.entityIdent;
    }

    public void setEntityIdent(EntityIdentDto entityIdent) {
        this.entityIdent = entityIdent;
    }

    public Date getLastConnecion() {
        return this.lastConnecion;
    }

    public void setLastConnecion(Date lastConnecion) {
        this.lastConnecion = lastConnecion;
    }

}