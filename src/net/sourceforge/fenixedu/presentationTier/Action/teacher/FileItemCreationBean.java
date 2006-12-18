package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.io.InputStream;
import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.accessControl.EveryoneGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;

public class FileItemCreationBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private DomainReference<Item> item;
    
    private String fileName;
    private Long fileSize;
    
    private String displayName;
    private Group permittedGroup;

    transient private InputStream file;
    
	public FileItemCreationBean(Item item) {
        super();
        
        this.item = new DomainReference<Item>(item);
        this.permittedGroup = new EveryoneGroup();
    }

    public Item getItem() {
        return this.item.getObject();
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
    
}
