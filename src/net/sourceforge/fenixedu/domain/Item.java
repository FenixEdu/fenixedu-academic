package net.sourceforge.fenixedu.domain;

import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DuplicatedNameException;
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
                chain.addComparator(new BeanComparator("idInternal"));
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
        setItemOrder(getNewOrder(null));
    }

    public Item(Section section, MultiLanguageString name, MultiLanguageString information, Integer itemOrder) {
        this(section, name);
        
        setInformation(information);
        setNewItemOrder(itemOrder);
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
    
    private int getNewOrder(Integer order) {
        return order == null ? getSection().getAssociatedItemsSet().size() - 1 : order.intValue();
    }

    public Integer getNewItemOrder() {
        return getItemOrder();
    }
    
    /**
     * Changes the order of this item updating all the siblings items to reflect
     * this change. All items that have an order greater or equal to the given
     * order will have they value incremented by one.
     * 
     * @param itemOrder
     *            the new item order inside the the containing section
     */
    public void setNewItemOrder(Integer itemOrder) {
        int newOrder = getNewOrder(itemOrder);
        
        if (getItemOrder() == null) {
            setItemOrder(Integer.valueOf(Integer.MAX_VALUE));
        }
        
        if (getItemOrder() != null) {
            final int oldOrder = getItemOrder().intValue();
            final boolean moveUp = newOrder > oldOrder;
            if (moveUp && itemOrder != null) {
                newOrder--;
            }
            if (newOrder != oldOrder) {
                for (final Item otherItem : getSection().getAssociatedItemsSet()) {
                    if (this != otherItem) {
                        final int otherOrder = otherItem.getItemOrder().intValue();
                        if (moveUp) {
                            if (otherOrder > oldOrder && otherOrder <= newOrder) {
                                otherItem.setItemOrder(Integer.valueOf(otherOrder - 1));
                            }
                        } else {
                            if (otherOrder >= newOrder && otherOrder < oldOrder) {
                                otherItem.setItemOrder(Integer.valueOf(otherOrder + 1));
                            }
                        }
                    }
                }
            }
        }

        setItemOrder(Integer.valueOf(newOrder));
    }
    
    /**
     * @return the item immediately after this item in the section or <code>null</code> if the item is the last 
     */
    public Item getNextItem() {
        Integer thisOrder = getItemOrder();
        if (thisOrder == null) {
            thisOrder = new Integer(Integer.MAX_VALUE);
        }
        
        for (Item item : getSection().getOrderedItems()) {
            Integer itemOrder = item.getItemOrder();
            if (itemOrder == null) {
                itemOrder = new Integer(Integer.MAX_VALUE);
            }
            
            if (itemOrder > thisOrder) {
                return item;
            }
        }
        
        return null;
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
        setNewItemOrder(item != null ? item.getItemOrder(): null);
    }

    @Override
    public void disconnect() {
        Section section = this.getSection();
        if (section != null && section.getAssociatedItems() != null) {
            List<Item> items = section.getAssociatedItems();
            int associatedItemOrder;

            for (Item item : items) {
                associatedItemOrder = item.getItemOrder().intValue();
                if (associatedItemOrder > this.getItemOrder().intValue()) {
                    item.setItemOrder(new Integer(associatedItemOrder - 1));
                }
            }
        }

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

}
