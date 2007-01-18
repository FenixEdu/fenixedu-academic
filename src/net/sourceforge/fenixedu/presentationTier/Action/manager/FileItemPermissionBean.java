package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.FileItem;
import net.sourceforge.fenixedu.domain.accessControl.EveryoneGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;

public class FileItemPermissionBean implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private DomainReference<FileItem> fileItem;
    private Group permittedGroup;

    public FileItemPermissionBean(FileItem fileItem) {
        this.fileItem = new DomainReference<FileItem>(fileItem);
        this.permittedGroup = fileItem.getPermittedGroup();
    }

    public FileItem getFileItem() {
        return this.fileItem.getObject();
    }

    public Group getPermittedGroup() {
        if (this.permittedGroup != null) {
            return this.permittedGroup;
        }
        else {
            return new EveryoneGroup();
        }
    }

    public void setPermittedGroup(Group permittedGroup) {
        this.permittedGroup = permittedGroup;
    }
    
}
