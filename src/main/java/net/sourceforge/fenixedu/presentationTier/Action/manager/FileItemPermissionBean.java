package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.FileContent;
import net.sourceforge.fenixedu.domain.accessControl.EveryoneGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;

public class FileItemPermissionBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private FileContent fileItem;
    private Group permittedGroup;

    public FileItemPermissionBean(FileContent fileItem) {
        this.fileItem = fileItem;
        this.permittedGroup = fileItem.getPermittedGroup();
    }

    public FileContent getFileItem() {
        return this.fileItem;
    }

    public Group getPermittedGroup() {
        if (this.permittedGroup != null) {
            return this.permittedGroup;
        } else {
            return new EveryoneGroup();
        }
    }

    public void setPermittedGroup(Group permittedGroup) {
        this.permittedGroup = permittedGroup;
    }

    public void logItemFilePermittedGroup() {
        fileItem.logItemFilePermittedGroup();
    }
}
