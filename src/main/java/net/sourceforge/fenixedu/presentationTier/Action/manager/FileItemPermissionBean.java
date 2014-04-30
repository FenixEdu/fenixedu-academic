package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.FileContent;

import org.fenixedu.bennu.core.groups.Group;

public class FileItemPermissionBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private final FileContent fileItem;
    private Group permittedGroup;

    public FileItemPermissionBean(FileContent fileItem) {
        this.fileItem = fileItem;
        this.permittedGroup = fileItem.getPermittedGroup();
    }

    public FileContent getFileItem() {
        return this.fileItem;
    }

    public Group getPermittedGroup() {
        return permittedGroup;
    }

    public void setPermittedGroup(Group permittedGroup) {
        this.permittedGroup = permittedGroup;
    }

    public void logItemFilePermittedGroup() {
        fileItem.logItemFilePermittedGroup();
    }

}
