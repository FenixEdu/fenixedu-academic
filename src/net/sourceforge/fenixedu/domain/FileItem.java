package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class FileItem extends FileItem_Base {

    public static Comparator<FileItem> COMPARATOR_BY_ORDER = new Comparator<FileItem>() {
        public int compare(FileItem one, FileItem other) {
            int comparison = String.valueOf(one.getOrderInItem()).compareTo(
                    String.valueOf(other.getOrderInItem()));

            // keep old behaviour
            if (comparison != 0) {
                return comparison;
            } else {
                return String.valueOf(one.getDisplayName()).compareTo(
                        String.valueOf(other.getDisplayName()));
            }
        }
    };

    public FileItem(Item item) {
        super();

        setVisible(true);
        setItem(item);
        setOrderInItem(getNextOrderInItem());
    }

    public FileItem(Item item, String filename, String displayName, String mimeType, String checksum,
            String checksumAlgorithm, Integer size, String externalStorageIdentification,
            Group permittedGroup) {
        this(item);
        init(filename, displayName, mimeType, checksum, checksumAlgorithm, size,
                externalStorageIdentification, permittedGroup);
    }

    private Integer getNextOrderInItem() {
        int order = 0;
        
        for (FileItem fileItem : getItem().getFileItems()) {
            if (fileItem == this) {
                continue;
            }
            
            order = Math.max(order, fileItem.getOrderInItem());
        }
        
        return order++;
    }

    @Deprecated
    public Boolean getVisible() {
        return super.getVisible();
    }
    
    public Boolean isVisible() {
        return super.getVisible();
    }
    
    public void delete() {
        if (this.hasItem()) {
            throw new DomainException("fileItem.cannotBeDeleted");
        }

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
}
