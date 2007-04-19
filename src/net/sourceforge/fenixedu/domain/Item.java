package net.sourceforge.fenixedu.domain;

import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DuplicatedNameException;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

/**
 * An Item represents a piece of text a user can add to a section of a site. It contains a title 
 * and a body text: the item's information.
 * 
 * @author ars
 */
public class Item extends Item_Base {

    public static final Comparator<Item> COMPARATOR_BY_ORDER = new Comparator<Item>() {

        private ComparatorChain chain = null;
        
        public int compare(Item one, Item other) {
            if (this.chain == null) {
                chain = new ComparatorChain();
                
                chain.addComparator(new BeanComparator("itemOrder"));
                chain.addComparator(new BeanComparator("name"));
                chain.addComparator(DomainObject.COMPARATOR_BY_ID);
            }
            
            return chain.compare(one, other);
        }
    };

    protected Item() {
        super();
        
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public Item(Section section, MultiLanguageString name) {
        this();
        
        if (section == null) {
            throw new NullPointerException();
        }

        setSection(section);
        setName(name);
    }

    public Item(Section section, MultiLanguageString name, MultiLanguageString information, Integer itemOrder) {
        this(section, name);
        
        setInformation(information);
    }

    public void setName(MultiLanguageString name) {
        if (name == null) {
            throw new NullPointerException();
        }

        if (! isNameUnique(getSection().getAssociatedItems(), name)) {
            throw new DuplicatedNameException("site.section.item.name.duplicated");
        }
        
        super.setName(name);
    }
    
    /**
     * @return the item immediately after this item in the section or <code>null</code> if the item is the last 
     */
    public Item getNextItem() {
        return Section.ITEM_ORDER_ADAPTER.getNext(getSection(), this);
    }
    
    /**
     * Changes the order of this item so that the given item is immediately
     * after this item. If the given item is <code>null</code> then this item
     * will be the last in the section
     * 
     * @param item
     *            the item that should appear after this item or
     *            <code>null</code> if this item should be last
     */
    public void setNextItem(Item item) {
        setItemOrder(item != null ? item.getItemOrder() : null);
        Section.ITEM_ORDER_ADAPTER.orderChanged(getSection(), this);
    }

    @Override
    public void disconnect() {
        removeRootDomainObject();
        removeSection();
    }

    @Override
    protected void checkDeletion() {
        if (! isDeletable()) {
            throw new DomainException("site.section.item.delete.notAllowed", getName().getContent());
        }
    }
    
    @Override
    public boolean isDeletable() {
        return this.getFileItemsCount() == 0;
    }

    public SortedSet<FileItem> getSortedVisibleFileItems() {
        final SortedSet<FileItem> sortedFileItems = new TreeSet<FileItem>(FileItem.COMPARATOR_BY_ORDER);
        
        for (FileItem fileItem : getFileItems()) {
            if (fileItem.isVisible()) {
                sortedFileItems.add(fileItem);
            }
        }
        
        return sortedFileItems;
    }
    
    public SortedSet<FileItem> getSortedFileItems() {
        final SortedSet<FileItem> sortedFileItems = new TreeSet<FileItem>(FileItem.COMPARATOR_BY_ORDER);
        sortedFileItems.addAll(getFileItems());

        return sortedFileItems;
    }
    
    @Override
    public boolean isAvailable(FunctionalityContext context) {
        if (getSection() != null && !getSection().isAvailable(context)) {
            return false;
        }
        
        return super.isAvailable(context);
    }

    public void setFileItemsOrder(List<FileItem> files) {
        FileItem.ORDERED_ADAPTER.updateOrder(this, files);
    }

}
