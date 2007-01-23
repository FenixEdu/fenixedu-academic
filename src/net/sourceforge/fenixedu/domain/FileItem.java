package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.domain.InverseOrderedRelationAdapter;

public class FileItem extends FileItem_Base {

    public static Comparator<FileItem> COMPARATOR_BY_ORDER = new Comparator<FileItem>() {
        
        private ComparatorChain chain = null;
        
        public int compare(FileItem one, FileItem other) {
            if (this.chain == null) {
                chain = new ComparatorChain();
                
                chain.addComparator(new BeanComparator("orderInItem"));
                chain.addComparator(new BeanComparator("displayName"));
                chain.addComparator(new BeanComparator("idInternal"));
            }
            
            return chain.compare(one, other);
        }
        
    };

    public static InverseOrderedRelationAdapter<FileItem, Item> ORDERED_ADAPTER;
    static {
        ORDERED_ADAPTER = new InverseOrderedRelationAdapter<FileItem, Item>("orderInItem", "fileItems");
        ItemFileItem.addListener(ORDERED_ADAPTER);
    }
    
    protected FileItem(Item item) {
        super();

        setVisible(true);
        setItem(item);
    }

    public FileItem(Item item, String filename, String displayName, String mimeType, String checksum,
            String checksumAlgorithm, Integer size, String externalStorageIdentification,
            Group permittedGroup) {
        this(item);
        init(filename, displayName, mimeType, checksum, checksumAlgorithm, size,
                externalStorageIdentification, permittedGroup);
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
