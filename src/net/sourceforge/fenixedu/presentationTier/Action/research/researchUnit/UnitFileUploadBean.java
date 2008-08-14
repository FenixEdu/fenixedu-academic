package net.sourceforge.fenixedu.presentationTier.Action.research.researchUnit;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.injectionCode.IGroup;

public class UnitFileUploadBean extends UnitFileBean implements Serializable {

    private DomainReference<Unit> unit;

    private String fileName;
    private Long fileSize;

    private List<IGroup> permittedGroups;
    private Group permittedGroup;

    transient private InputStream uploadFile;
    private String authorsName;

    public UnitFileUploadBean(Unit unit) {
	this.unit = new DomainReference<Unit>(unit);
	permittedGroups = new ArrayList<IGroup>();
    }

    public Unit getUnit() {
	return this.unit.getObject();
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
	return new GroupUnion((Collection<IGroup>) getPermittedGroups());
    }

    public List<IGroup> getPermittedGroups() {
	return permittedGroups;
    }

    public void setPermittedGroups(List<IGroup> permittedGroups) {
	this.permittedGroups = permittedGroups;
    }
}
