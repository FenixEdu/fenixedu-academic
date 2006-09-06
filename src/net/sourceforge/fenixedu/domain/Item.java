package net.sourceforge.fenixedu.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.apache.commons.beanutils.BeanComparator;

/**
 * 
 * @author ars
 */

public class Item extends Item_Base {

    public static final Comparator<Item> COMPARATOR_BY_ORDER = new BeanComparator("itemOrder");

    protected Item() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public Item(final Section section, final MultiLanguageString name,
	    final MultiLanguageString information, final Integer order) {
	this();
	if (section == null) {
	    throw new NullPointerException();
	}

	setSection(section);
	edit(name, information, order);
    }

    public void edit(final MultiLanguageString name, final MultiLanguageString information,
	    final Integer order) {
	if (name == null) {
	    throw new NullPointerException();
	}

	setName(name);
	setInformation(information);

	int newOrder = getNewOrder(order);
	if (getItemOrder() == null) {
	    setItemOrder(Integer.valueOf(Integer.MAX_VALUE));
	}
	if (getItemOrder() != null) {
	    final int oldOrder = getItemOrder().intValue();
	    final boolean moveUp = newOrder > oldOrder;
	    if (moveUp && order != null) {
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

    private int getNewOrder(Integer order) {
	return order == null ? getSection().getAssociatedItemsSet().size() - 1 : order.intValue();
    }

    public void delete() {

	if (this.getFileItems().size() != 0) {
	    throw new DomainException("item.cannotDeleteWhileHasFiles");
	}

	Section section = this.getSection();

	if (this.getSection() != null && this.getSection().getAssociatedItems() != null) {

	    this.setSection(null);
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
	super.deleteDomainObject();
    }

    public static abstract class ItemFactory implements Serializable, FactoryExecutor {
	private Integer itemOrder;

	private MultiLanguageString information;

	private MultiLanguageString name;

	public MultiLanguageString getInformation() {
	    return information;
	}

	public void setInformation(MultiLanguageString information) {
	    this.information = information;
	}

	public Integer getItemOrder() {
	    return itemOrder;
	}

	public void setItemOrder(Integer itemOrder) {
	    this.itemOrder = itemOrder;
	}

	public MultiLanguageString getName() {
	    return name;
	}

	public void setName(MultiLanguageString name) {
	    this.name = name;
	}
    }

    public static class ItemFactoryCreator extends ItemFactory {
	private DomainReference<Section> sectionDomainReference;

	public ItemFactoryCreator(final Section section) {
	    super();
	    setSection(section);
	}

	public Section getSection() {
	    return sectionDomainReference == null ? null : sectionDomainReference.getObject();
	}

	public void setSection(Section section) {
	    this.sectionDomainReference = section == null ? null : new DomainReference<Section>(section);
	}

	public Item execute() {
	    return execute(getSection());
	}

	public Item execute(final Section section) {
	    return new Item(section, getName(), getInformation(), getItemOrder());
	}
    }
    
    public List<FileItem> getSortedFileItems() {
	final Set<FileItem> sortedFileItems = new TreeSet<FileItem>(FileItem.COMPARATOR_BY_DISPLAY_NAME);
	
	return new ArrayList<FileItem>(sortedFileItems);
    }

}
