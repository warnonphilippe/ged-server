package be.phw.gedserver.dto;

import java.util.Objects;

public class CivadisDocument {

    private String id;
    private String repo;
    private String path;
    private String name;
    private String mimeType;
    private String description;
    private boolean directory;

    public CivadisDocument() {
    }

    public CivadisDocument(CivadisDocument base) {
        this.id = base.id;
        this.repo = base.repo;
        this.path = base.path;
        this.name = base.name;
        this.mimeType = base.mimeType;
        this.description = base.description;
        this.directory = base.directory;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRepo() {
        return repo;
    }

    public void setRepo(String repo) {
        this.repo = repo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDirectory() {
        return directory;
    }

    public void setDirectory(boolean directory) {
        this.directory = directory;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.id);
        hash = 83 * hash + Objects.hashCode(this.repo);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CivadisDocument other = (CivadisDocument) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.repo, other.repo)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CivadisDocument{" + "id='" + id + '\'' + ", path='" + path + '\'' + ", name='" + name + '\'' + '}';
    }
}
