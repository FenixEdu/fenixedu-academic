package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.io.InputStream;
import java.io.Serializable;

import net.sourceforge.fenixedu.domain.FileContent.EducationalResourceType;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.accessControl.EveryoneGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.contents.Container;

public class FileContentCreationBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Container fileHolder;
    private Site site;

    private String fileName;
    private Long fileSize;

    private String displayName;
    private Group permittedGroup;

    transient private InputStream file;
    private EducationalResourceType educationalLearningResourceType;
    private String authorsName;

    public String getAuthorsName() {
        return authorsName;
    }

    public Container getFileHolder() {
        return fileHolder;
    }

    public void setFileHolder(Container fileHolder) {
        this.fileHolder = fileHolder;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public void setAuthorsName(String authorsName) {
        this.authorsName = authorsName;
    }

    public FileContentCreationBean(Container container, Site site) {
        super();
        setSite(site);
        setFileHolder(container);

        this.permittedGroup = new EveryoneGroup();
    }

    public Group getPermittedGroup() {
        return this.permittedGroup;
    }

    public void setPermittedGroup(Group permittedGroup) {
        this.permittedGroup = permittedGroup;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public InputStream getFile() {
        return this.file;
    }

    public void setFile(InputStream file) {
        this.file = file;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public EducationalResourceType getEducationalLearningResourceType() {
        return educationalLearningResourceType;
    }

    public void setEducationalLearningResourceType(EducationalResourceType educationalLearningResourceType) {
        this.educationalLearningResourceType = educationalLearningResourceType;
    }

}