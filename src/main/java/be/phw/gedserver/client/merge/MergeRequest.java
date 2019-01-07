package be.phw.gedserver.client.merge;

import java.util.Map;

/**

 @author david
 */
public class MergeRequest {

    //type de merge
    private String mergeType;
    //repo contenant le modèle
    private String templateRepo;
    //path du modèle
    private String templatePath;
    //id du modèle
    private String templateId;
    //repo dans lequel placer fichier fusionné
    private String destRepo;
    //path où déposer le fichier fusionné
    private String destPath;
    //id du fichier fusionné
    private String destId;
    //mimetype du fichier fusionné, le modèle sera donc fusionné avec les données et converti dans ce mimetype
    private String destMimeType;
    //description du fichier de destination
    private String destDescription;
    //données de fusion à inclure dans le modèle
    private Map<String, Object> data;

    public String getMergeType() {
        return mergeType;
    }
    public void setMergeType(String mergeType) {
        this.mergeType=mergeType;
    }

    public String getTemplateRepo() {
        return templateRepo;
    }
    public void setTemplateRepo(String templateRepo) {
        this.templateRepo=templateRepo;
    }

    public String getTemplatePath() {
        return templatePath;
    }
    public void setTemplatePath(String templatePath) {
        this.templatePath=templatePath;
    }

    public String getTemplateId() {
        return templateId;
    }
    public void setTemplateId(String templateId) {
        this.templateId=templateId;
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

    public Map<String, Object> getData() {
        return data;
    }
    public void setData(Map<String, Object> data) {
        this.data=data;
    }
}
