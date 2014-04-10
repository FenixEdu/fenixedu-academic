package net.sourceforge.fenixedu.presentationTier.Action.research.researchUnit;

import java.io.InputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.UnionGroup;

public class UnitFileUploadBean extends UnitFileBean implements Serializable {

    private Unit unit;

    private String fileName;
    private Long fileSize;

    private Set<Group> permittedGroups;
    private Group permittedGroup;

    transient private InputStream uploadFile;
    private String authorsName;

    public UnitFileUploadBean(Unit unit) {
        this.unit = unit;
        permittedGroups = new HashSet<Group>();
    }

    @Override
    public Unit getUnit() {
        return this.unit;
    }

    public String getAuthorsName() {
        return authorsName;
    }

    public void setAuthorsName(String authorsName) {
        this.authorsName = authorsName;
    }

    public InputStream getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(InputStream file) {
        this.uploadFile = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Group getPermittedGroup() {
        return (permittedGroup != null) ? permittedGroup : getUnion();
    }

    public void setPermittedGroup(Group permittedGroup) {
        this.permittedGroup = permittedGroup;
    }

    public Group getUnion() {
        return UnionGroup.of(getPermittedGroups());
    }

    public Set<Group> getPermittedGroups() {
        return permittedGroups;
    }

    public void setPermittedGroups(Set<Group> permittedGroups) {
        this.permittedGroups = permittedGroups;
    }
}
