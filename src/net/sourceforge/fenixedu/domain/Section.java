/*
 * Section.java
 * Mar 10, 2003
 */
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.YearMonthDay;

/**
 * @author Ivo Brand�o
 */
public class Section extends Section_Base {

	public static final Comparator<Section> COMPARATOR_BY_ORDER = new BeanComparator("sectionOrder");

    protected Section() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
    }

    public Section(final Site site, final MultiLanguageString name, final Integer order, final Section superiorSection) {
        this();
        if (site == null) {
            throw new NullPointerException();
        }

        setSite(site);
        setSuperiorSection(superiorSection);
        edit(name, order);
    }

    public void edit(final MultiLanguageString name, final Integer order) {
        if (name == null || order == null) {
            throw new NullPointerException();
        }

        setLastModifiedDateYearMonthDay(new YearMonthDay());
        setName(name);
        final int newOrder = order.intValue();
        final int oldOrder = getSectionOrder() == null ? Integer.MAX_VALUE : getSectionOrder().intValue();
        if (newOrder != oldOrder) {
            final boolean moveUp = newOrder > oldOrder;
            for (final Section otherSection : getSite().getAssociatedSectionsSet()) {
                if (otherSection.getSuperiorSection() == getSuperiorSection()) {
                    if (otherSection != this) {
                        final int otherOrder = otherSection.getSectionOrder().intValue();
                        if (moveUp) {
                            if (otherOrder > oldOrder && otherOrder <= newOrder) {
                                otherSection.setSectionOrder(Integer.valueOf(otherOrder - 1));
                            }
                        } else {
                            if (otherOrder >= newOrder && otherOrder < oldOrder) {
                                otherSection.setSectionOrder(Integer.valueOf(otherOrder + 1));
                            }
                        }
                    }
                }
            }
            setSectionOrder(order);
        }
    }

    public void insertItem(String itemName, String itemInformation, Boolean itemUrgent,
            Integer insertItemOrder) throws DomainException {

        if (itemName == null || insertItemOrder == null || itemUrgent == null || insertItemOrder == null) {
            throw new NullPointerException();
        }

        for (Item item : this.getAssociatedItems()) {
            if (item.getName().equals(itemName))
                throw new DomainException("error.duplicate.item");
        }

        Item item = new Item();
//        item.setInformation(itemInformation);
//        item.setName(itemName);
        item.setUrgent(itemUrgent);
        Integer itemOrder = new Integer(organizeExistingItemsOrder(insertItemOrder.intValue()));
        item.setItemOrder(itemOrder);

        item.setSection(this);
    }

    private int organizeExistingItemsOrder(int insertItemOrder) {

        Iterator items = this.getAssociatedItemsIterator();

        if (this.getAssociatedItems() != null) {
            int itemOrder;

            for (; items.hasNext();) {
                Item item = (Item) items.next();
                itemOrder = item.getItemOrder().intValue();
                if (itemOrder >= insertItemOrder)
                    item.setItemOrder(new Integer(itemOrder + 1));
            }
        }
        return insertItemOrder;
    }

    public void edit(String newSectionName, Integer newOrder) {

        if (newSectionName == null || newOrder == null) {
            throw new NullPointerException();
        }

        newOrder = organizeSectionsOrder(newOrder, this.getSectionOrder(), this.getSuperiorSection(),
                this.getSite());

//        this.setName(newSectionName);
        this.setSectionOrder(newOrder);
    }

    private Integer organizeSectionsOrder(Integer newOrder, Integer oldOrder, Section superiorSection,
            Site site) {

        List<Section> sectionsList = site.getAssociatedSections(superiorSection);

        int diffOrder = newOrder.intValue() - oldOrder.intValue();

        if (diffOrder != 0) {
            if (diffOrder > 0) {
                for (Section section : sectionsList) {
                    int sectionOrder = section.getSectionOrder().intValue();
                    if (sectionOrder > oldOrder.intValue() && sectionOrder <= newOrder.intValue()) {
                        section.setSectionOrder(new Integer(sectionOrder - 1));
                    }
                }
            } else {
                for (Section section : sectionsList) {
                    int sectionOrder = section.getSectionOrder().intValue();
                    if (sectionOrder >= newOrder.intValue() && sectionOrder < oldOrder.intValue()) {
                        section.setSectionOrder(new Integer(sectionOrder + 1));
                    }
                }
            }
        }
        return newOrder;
    }

    public void delete() {

        checkIfSectionCanBeDeleted(this);

        for (Section subSection : this.getAssociatedSections()) {
            checkIfSectionCanBeDeleted(subSection);
        }

        Section superiorSection = this.getSuperiorSection();
        Site sectionSite = this.getSite();
        Integer sectionToDeleteOrder = this.getSectionOrder();

        // Delete Associated Items
        if (this.getAssociatedItemsCount() != 0) {
            List<Item> items = new ArrayList();
            items.addAll(this.getAssociatedItems());
            for (Item item : items) {
                item.delete();
            }
        }

        // Delete Associated Sections
        if (this.getAssociatedSectionsCount() != 0) {
            List<Section> sections = new ArrayList();
            sections.addAll(this.getAssociatedSections());
            for (Section section : sections) {
                section.delete();
            }
        }

        // Delete Associations with Superior Section if exists
        if (superiorSection != null) {
            this.setSuperiorSection(null);
        }

        // Delete Associations with Site
        this.setSite(null);

        // ReOrder Sections
        List<Section> sectionsReordered = sectionSite.getAssociatedSections(superiorSection);
        for (Section section : sectionsReordered) {
            Integer sectionOrder = section.getSectionOrder();
            if (sectionOrder.intValue() > sectionToDeleteOrder.intValue()) {
                section.setSectionOrder(new Integer(sectionOrder.intValue() - 1));
            }
        }

        removeRootDomainObject();
        super.deleteDomainObject();
    }

    private void checkIfSectionCanBeDeleted(Section section) {
        for (Item item : section.getAssociatedItems()) {
            if (item.getFileItems().size() != 0) {
                throw new DomainException("section.cannotDeleteWhileHasItemsWithFiles");
            }
        }
    }

    public SortedSet<Section> getOrderedSubSections() {
    	final SortedSet<Section> sections = new TreeSet<Section>(Section.COMPARATOR_BY_ORDER);
    	sections.addAll(getAssociatedSectionsSet());
    	return sections;
    }

    public SortedSet<Item> getOrderedItems() {
        final SortedSet<Item> items = new TreeSet<Item>(Item.COMPARATOR_BY_ORDER);
        items.addAll(getAssociatedItemsSet());
        return items;
    }

}
