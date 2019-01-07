package be.phw.gedserver.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A TicketMerge.
 */
public class TicketMerge implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String mergeType;

    private String templateRepo;

    private String templatePath;

    private String templateId;

    private String destRepo;

    private String destPath;

    private String destId;

    private String destMimeType;

    private String destDescription;

    private ZonedDateTime startDate;

    private ZonedDateTime endDate;

    private Boolean ok;

    private String errorMsg;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMergeType() {
        return mergeType;
    }

    public TicketMerge mergeType(String mergeType) {
        this.mergeType = mergeType;
        return this;
    }

    public void setMergeType(String mergeType) {
        this.mergeType = mergeType;
    }

    public String getTemplateRepo() {
        return templateRepo;
    }

    public TicketMerge templateRepo(String templateRepo) {
        this.templateRepo = templateRepo;
        return this;
    }

    public void setTemplateRepo(String templateRepo) {
        this.templateRepo = templateRepo;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public TicketMerge templatePath(String templatePath) {
        this.templatePath = templatePath;
        return this;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public String getTemplateId() {
        return templateId;
    }

    public TicketMerge templateId(String templateId) {
        this.templateId = templateId;
        return this;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getDestRepo() {
        return destRepo;
    }

    public TicketMerge destRepo(String destRepo) {
        this.destRepo = destRepo;
        return this;
    }

    public void setDestRepo(String destRepo) {
        this.destRepo = destRepo;
    }

    public String getDestPath() {
        return destPath;
    }

    public TicketMerge destPath(String destPath) {
        this.destPath = destPath;
        return this;
    }

    public void setDestPath(String destPath) {
        this.destPath = destPath;
    }

    public String getDestId() {
        return destId;
    }

    public TicketMerge destId(String destId) {
        this.destId = destId;
        return this;
    }

    public void setDestId(String destId) {
        this.destId = destId;
    }

    public String getDestMimeType() {
        return destMimeType;
    }

    public TicketMerge destMimeType(String destMimeType) {
        this.destMimeType = destMimeType;
        return this;
    }

    public void setDestMimeType(String destMimeType) {
        this.destMimeType = destMimeType;
    }

    public String getDestDescription() {
        return destDescription;
    }

    public TicketMerge destDescription(String destDescription) {
        this.destDescription = destDescription;
        return this;
    }

    public void setDestDescription(String destDescription) {
        this.destDescription = destDescription;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public TicketMerge startDate(ZonedDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public TicketMerge endDate(ZonedDateTime endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public Boolean isOk() {
        return ok;
    }

    public TicketMerge ok(Boolean ok) {
        this.ok = ok;
        return this;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public TicketMerge errorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        return this;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TicketMerge ticketMerge = (TicketMerge) o;
        if (ticketMerge.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ticketMerge.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TicketMerge{" +
            "id=" + getId() +
            ", mergeType='" + getMergeType() + "'" +
            ", templateRepo='" + getTemplateRepo() + "'" +
            ", templatePath='" + getTemplatePath() + "'" +
            ", templateId='" + getTemplateId() + "'" +
            ", destRepo='" + getDestRepo() + "'" +
            ", destPath='" + getDestPath() + "'" +
            ", destId='" + getDestId() + "'" +
            ", destMimeType='" + getDestMimeType() + "'" +
            ", destDescription='" + getDestDescription() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", ok='" + isOk() + "'" +
            ", errorMsg='" + getErrorMsg() + "'" +
            "}";
    }
}
