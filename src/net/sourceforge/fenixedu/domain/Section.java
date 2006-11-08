/*
 * Section.java
 * Mar 10, 2003
 */
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DuplicatedNameException;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.YearMonthDay;

/**
 * @author Ivo Brandão
 */
public class Section extends Section_Base {

    public static final Comparator<Section> COMPARATOR_BY_ORDER = new Comparator<Section>() {
        
        private ComparatorChain chain = null;
        
        public int compare(Section one, Section other) {
            if (this.chain == null) {
                chain = new ComparatorChain();
                
                chain.addComparator(new BeanComparator("sectionOrder"));
                chain.addComparator(new BeanComparator("name"));
                chain.addComparator(new BeanComparator("idInternal"));
            }
            
            return chain.compare(one, other);
        }
        
    };

    protected Section() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public Section(Site site, MultiLanguageString name) {
        this();
        
        if (site == null) {
            throw new NullPointerException();
        }

        setSite(site);
        setName(name);
        setSectionOrder(getNewOrder(null));
    }

    public Section(Site site, MultiLanguageString name, Integer sectionOrder, Section section) {
        this(site, name);
        
        setNewSectionOrder(sectionOrder);
        setSuperiorSection(section);
    }

    // TODO: check were lastModifiedDate is used and if this edit is really required
    public void edit(MultiLanguageString name, Section nextSection, Group permittedGroup) {
        setLastModifiedDateYearMonthDay(new YearMonthDay());
        
        setName(name);
        setNextSection(nextSection);
        setPermittedGroup(permittedGroup);
    }

    public void setName(MultiLanguageString name) {
        if (name == null || name.isEmpty()) {
            throw new NullPointerException();
        }

        if (! isNameUnique(getSiblings(), name)) {
            throw new DuplicatedNameException("site.section.name.duplicated");
        }
        
        super.setName(name);
    }

    private List<Section> getSiblings() {
        return getSite().getAssociatedSections(getSuperiorSection());
    }

    public Integer getNewSectionOrder() {
        return getSectionOrder();
    }
    
    /**
     * Changes the order of this section updating all the siblings sections to
     * reflect this change. All section that have an order greater or equal to
     * the given order will have they value incremented by one.
     * 
     * @param sectionOrder
     *            the new section order inside the the superior section
     */
    public void setNewSectionOrder(Integer sectionOrder) {
        int newOrder = getNewOrder(sectionOrder);
        if (getSectionOrder() == null) {
            setSectionOrder(Integer.valueOf(Integer.MAX_VALUE));
        }
        if (getSectionOrder() != null) {
            final int oldOrder = getSectionOrder().intValue();
            final boolean moveUp = newOrder > oldOrder;
            if (moveUp && sectionOrder != null) {
                newOrder--;
            }
            if (newOrder != oldOrder) {
                final Collection<Section> sections = getSuperiorSection() == null ? getSite()
                        .getAssociatedSectionsSet() : getSuperiorSection().getAssociatedSectionsSet();
                for (final Section otherSection : sections) {
                    if (otherSection.getSuperiorSection() == getSuperiorSection()) {
                        if (this != otherSection) {
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
            }
        }

        setSectionOrder(Integer.valueOf(newOrder));
    }
    
    /**
     * @return the section immediately after this section in the superior
     *         section or <code>null</code> if the section is the last
     */
    public Section getNextSection() {
        Integer thisOrder = getSectionOrder();
        if (thisOrder == null) {
            thisOrder = new Integer(Integer.MAX_VALUE);
        }
        
        Collection<Section> siblingsAndSelf;
        if (getSuperiorSection() != null) {
            siblingsAndSelf = getSuperiorSection().getOrderedSubSections();
        }
        else {
            siblingsAndSelf = getSite().getOrderedTopLevelSections();
        }
        
        for (Section section : siblingsAndSelf) {
            Integer sectionOrder = section.getSectionOrder();
            if (sectionOrder == null) {
                sectionOrder = new Integer(Integer.MAX_VALUE);
            }
            
            if (sectionOrder > thisOrder) {
                return section;
            }
        }
        
        return null;
    }
    
    /**
     * Changes the order of this section so that the given section is immediately
     * after this section. If the given section is <code>null</code> then this section
     * will be the last
     * 
     * @param section
     *            the section that should appear after this section or
     *            <code>null</code> if this section should be last
     */
    public void setNextSection(Section section) {
        setNewSectionOrder(section != null ? section.getSectionOrder() : null);
    }
    
    private int getNewOrder(Integer order) {
        if (order == null) {
            if (getSuperiorSection() == null) {
                return getSite().getNumberOfTopLevelSections();
            } else {
                return getSuperiorSection().getAssociatedSectionsSet().size() - 1;
            }
        }
        return order.intValue();
    }

    public void insertItem(MultiLanguageString itemName, MultiLanguageString itemInformation,
            Integer insertItemOrder) {
        new Item(this, itemName, itemInformation, insertItemOrder);
    }

    public void copyItemsFrom(Section sectionFrom) {
        for (final Item item : sectionFrom.getAssociatedItems()) {
            this.insertItem(item.getName(), item.getInformation(), item.getItemOrder());
        }
    }

    public void copySubSectionsAndItemsFrom(Section sectionFrom) {
        for (final Section subSectionFrom : sectionFrom.getAssociatedSections()) {
            if (subSectionFrom.getSuperiorSection() != null) {
                Section subSectionTo = this.getSite().createSection(subSectionFrom.getName(), this,
                        subSectionFrom.getSectionOrder());
                subSectionTo.copyItemsFrom(subSectionFrom);
                subSectionTo.copySubSectionsAndItemsFrom(subSectionFrom);
            }
        }
    }

    public void delete() {
        if (! isDeletable()) {
            throw new DomainException("section.cannotDeleteWhileHasItemsWithFiles");
        }

        Section superiorSection = this.getSuperiorSection();
        Site sectionSite = this.getSite();
        Integer sectionToDeleteOrder = this.getSectionOrder();

        // Delete Associated Items
        if (this.getAssociatedItemsCount() != 0) {
            List<Item> items = new ArrayList<Item>();
            items.addAll(this.getAssociatedItems());
            for (Item item : items) {
                item.delete();
            }
        }

        // Delete Associated Sections
        if (this.getAssociatedSectionsCount() != 0) {
            List<Section> sections = new ArrayList<Section>();
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

    @Override
    public boolean isDeletable() {
        for (Section subSection : this.getAssociatedSections()) {
            if (! subSection.isDeletable()) {
                return false;
            }
        }

        for (Item item : getAssociatedItems()) {
            if (! item.isDeletable()) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    protected void checkDeletion() {
        if (! isDeletable()) {
            throw new DomainException("site.section.delete.notAllowed");
        }
    }

    @Override
    protected void disconnect() {
        super.disconnect();
        
        // Delete Associated Items
        if (this.getAssociatedItemsCount() != 0) {
            for (Item item : getAssociatedItems()) {
                item.delete();
            }
        }

        // Delete Associated Sections
        if (this.getAssociatedSectionsCount() != 0) {
            for (Section section : getAssociatedSections()) {
                section.delete();
            }
        }

        // ReOrder Sections
        Site sectionSite = getSite();
        Integer sectionToDeleteOrder = getSectionOrder();

        List<Section> sectionsReordered = sectionSite.getAssociatedSections(getSuperiorSection());
        for (Section section : sectionsReordered) {
            Integer sectionOrder = section.getSectionOrder();
            
            if (sectionOrder > sectionToDeleteOrder) {
                section.setSectionOrder(new Integer(sectionOrder - 1));
            }
        }
        
        removeRootDomainObject();
        removeSuperiorSection();
        removeSite();
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

    /**
     * A section is always visible to the user as an entry in the menu. The
     * content of the section may not be available and that will be checked when
     * the user tries to access the section.
     */
    @Override
    public boolean isVisible(FunctionalityContext context) {
        return isVisible();
    }

    @Override
    public boolean isAvailable(FunctionalityContext context) {
        if (getSuperiorSection() != null && !getSuperiorSection().isAvailable(context)) {
            return false;
        }
        
        return super.isAvailable(context);
    }

}