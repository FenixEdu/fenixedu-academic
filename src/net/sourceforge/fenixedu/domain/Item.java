package net.sourceforge.fenixedu.domain;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

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

    public Item(final Section section, final MultiLanguageString name, final MultiLanguageString information,
            final Boolean urgent, final Integer order) {
        this();
        if (section == null) {
            throw new NullPointerException();
        }

        setSection(section);
        edit(name, information, urgent, order);
    }

    public void edit(final MultiLanguageString name, final MultiLanguageString information, final Boolean urgent,
            final Integer order) {
        if (name == null) {
            throw new NullPointerException();
        }

        setName(name);
        setInformation(information);
        setUrgent(urgent == null ? Boolean.FALSE : Boolean.TRUE);

        final int newOrder = getNewOrder(order);
        final int oldOrder = getItemOrder() == null ? Integer.MAX_VALUE : getItemOrder().intValue();
        if (newOrder != oldOrder) {
            final boolean moveUp = newOrder > oldOrder;
            for (final Item otherItem : getSection().getAssociatedItemsSet()) {
                if (otherItem != this) {
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
            setItemOrder(Integer.valueOf(newOrder));
        }
    }

    private int getNewOrder(Integer order) {
	if (order == null) {
	    return getSection().getAssociatedItemsSet().size();
	}
	return order.intValue() - 1;
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

    public void edit(String newItemName, String newItemInformation, Boolean newItemUrgent,
            Integer newOrder) {

        if (newItemName == null || newItemInformation == null || newItemUrgent == null
                || newOrder == null)
            throw new NullPointerException();

        newOrder = organizeItemsOrder(newOrder, this.getItemOrder(), this.getSection());

//        this.setInformation(newItemInformation);
        this.setItemOrder(newOrder);
//        this.setName(newItemName);
        this.setUrgent(newItemUrgent);
    }

    private Integer organizeItemsOrder(Integer newOrder, Integer oldOrder, Section section) {

        List<Item> items = section.getAssociatedItems();

        int diffOrder = newOrder.intValue() - oldOrder.intValue();

        if (diffOrder != 0) {
            if (diffOrder > 0)
                for (Item item : items) {
                    int iterItemOrder = item.getItemOrder().intValue();
                    if (iterItemOrder > oldOrder.intValue() && iterItemOrder <= newOrder.intValue()) {
                        item.setItemOrder(new Integer(iterItemOrder - 1));
                    }
                }
            else {
                for (Item item : items) {
                    int iterItemOrder = item.getItemOrder().intValue();
                    if (iterItemOrder >= newOrder.intValue() && iterItemOrder < oldOrder.intValue()) {
                        item.setItemOrder(new Integer(iterItemOrder + 1));
                    }
                }
            }
        }
        return newOrder;
    }

    public static abstract class ItemFactory implements Serializable, FactoryExecutor {
	private Boolean urgent;
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
	public Boolean getUrgent() {
	    return urgent;
	}
	public void setUrgent(Boolean urgent) {
	    this.urgent = urgent;
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
	    return new Item(section, getName(), getInformation(), getUrgent(), getItemOrder());
	}
    }

}
