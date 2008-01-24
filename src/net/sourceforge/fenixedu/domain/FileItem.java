package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.contents.Attachment;

public class FileItem extends FileItem_Base {

    protected FileItem(Attachment attachment) {
        super();

        setAttachment(attachment);
    }

    public FileItem(Attachment attachment, String filename, String displayName, String mimeType, String checksum,
            String checksumAlgorithm, Integer size, String externalStorageIdentification,
            Group permittedGroup) {
        this(attachment);
        init(filename, displayName, mimeType, checksum, checksumAlgorithm, size,
                externalStorageIdentification, permittedGroup);
    }
    
    public FileItem(String filename, String displayName, String mimeType, String checksum,
            String checksumAlgorithm, Integer size, String externalStorageIdentification,
            Group permittedGroup) {
        init(filename, displayName, mimeType, checksum, checksumAlgorithm, size,
                externalStorageIdentification, permittedGroup);
    }


    public void delete() {
	Attachment attachment = getAttachment();
	if(attachment != null) {
	    setAttachment(null);
	    attachment.delete();
	}
	removeRootDomainObject();
        super.deleteDomainObject();
    }

    public static FileItem readByOID(Integer idInternal) {
        return (FileItem) RootDomainObject.getInstance().readFileByOID(idInternal);
    }

    public static List<FileItem> readAllFileItems() {
        List<FileItem> fileItems = new ArrayList<FileItem>();
        
        for (File file : RootDomainObject.getInstance().getFiles()) {
            if (file instanceof FileItem) {
                fileItems.add((FileItem) file);
            }
        }
        
        return fileItems;
    }
    
    public Item getItem() {
	return (Item) getAttachment().getParents().get(0).getParent();
    }
}
