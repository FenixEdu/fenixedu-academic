/*
 * Section.java
 * Mar 10, 2003
 */
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext;
import net.sourceforge.fenixedu.util.MultiLanguageString;
import net.sourceforge.fenixedu.util.domain.OrderedRelationAdapter;

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
                chain.addComparator(DomainObject.COMPARATOR_BY_ID);
            }
            
            return chain.compare(one, other);
        }
        
    };

    public static OrderedRelationAdapter<Section, Item> ITEM_ORDER_ADAPTER;
    static {
        ITEM_ORDER_ADAPTER = new OrderedRelationAdapter<Section, Item>("associatedItems", "itemOrder");
        SectionItem.addListener(ITEM_ORDER_ADAPTER);
    }

    public static OrderedRelationAdapter<Section, Section> SECTION_ORDER_ADAPTER;
    static {
        SECTION_ORDER_ADAPTER = new OrderedRelationAdapter<Section, Section>("associatedSections", "sectionOrder");
        SectionSuperiorSection.addListener(SECTION_ORDER_ADAPTER);
    }
    
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
    }

    public Section(Site site, MultiLanguageString name, Integer sectionOrder, Section section) {
        this(site, name);
        
        setSectionOrder(sectionOrder);
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
        
        // NOTE: removed restriction because it introduces techinal problems
        // that are not really necessary or good for any of the parts: developer
        // and user
        
//        if (! isNameUnique(getSiblings(), name)) {
//            throw new DuplicatedNameException("site.section.name.duplicated");
//        }
        
        super.setName(name);
    }

    public List<Section> getSiblings() {
        return getSite().getAssociatedSections(getSuperiorSection());
    }

    /**
     * @return the section immediately after this section in the superior
     *         section or <code>null</code> if the section is the last
     */
    public Section getNextSection() {
        if (getSuperiorSection() == null) {
            return Site.SECTION_ORDER_ADAPTER.getNext(getSite(), this);
        }
        else {
            return SECTION_ORDER_ADAPTER.getNext(getSuperiorSection(), this);
        }
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
        setSectionOrder(section != null ? section.getSectionOrder() : null);
        
        if (getSuperiorSection() == null) {
            Site.SECTION_ORDER_ADAPTER.orderChanged(getSite(), this);
        } else {
            SECTION_ORDER_ADAPTER.orderChanged(getSuperiorSection(), this);
        }
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

    public void setItemsOrder(List<Item> items) {
        ITEM_ORDER_ADAPTER.updateOrder(this, items);
    }

    public void setSectionsOrder(List<Section> sections) {
        SECTION_ORDER_ADAPTER.updateOrder(this, sections);
    }

    public boolean isSubSectionAllowed() {
        return true;
    }
    
    public boolean isItemAllowed() {
        return true;
    }
}