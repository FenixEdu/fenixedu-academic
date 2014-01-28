/*
 * Section.java
 * Mar 10, 2003
 */
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * @author Ivo Brandão
 */
public class Section extends Section_Base {

    public static final Comparator<Section> COMPARATOR_BY_ORDER = new Comparator<Section>() {

        @Override
        public int compare(Section o1, Section o2) {
            final int co = o1.getOrder().compareTo(o2.getOrder());
            if (co != 0) {
                return co;
            }
            final int cn = o1.getName().compareTo(o2.getName());
            return cn == 0 ? DomainObjectUtil.COMPARATOR_BY_ID.compare(o1, o2) : cn;
        }

    };

    protected Section() {
        super();

        setCreationDate(new DateTime());
        setShowSubSections(true);
    }

    public Section(Section parent, MultiLanguageString name) {
        this();

        if (parent == null) {
            throw new NullPointerException();
        }

        setName(name);
        setParent(parent);
        setVisible(true);
    }

    public Section(Section parent, MultiLanguageString name, Integer sectionOrder) {
        this();
        setName(name);
        setParent(parent);
        setVisible(true);
    }

    private void setParent(Section parent) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public Set<Section> getChildrenSections() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public Integer getOrder() {
        throw new UnsupportedOperationException("Not implemented");
    }

    // TODO: check were lastModifiedDate is used and if this edit is really
    // required
    public void edit(MultiLanguageString name, Section nextSection) {
        setModificationDate(new YearMonthDay());
        setName(name);
        setNextSection(nextSection);
        Site st = getSite();
    }

    @Override
    public void setName(MultiLanguageString name) {
        if (name == null || name.isEmpty()) {
            throw new NullPointerException();
        }

        // NOTE: removed restriction because it introduces techinal problems
        // that are not really necessary or good for any of the parts: developer
        // and user

        // if (! isNameUnique(getSiblings(), name)) {
        // throw new DuplicatedNameException("site.section.name.duplicated");
        // }

        super.setName(name);
    }

    public void setNextSection(Section section) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public void insertItem(MultiLanguageString itemName, MultiLanguageString itemInformation, Integer insertItemOrder,
            Boolean showName) {
        new Item(this, itemName, itemInformation, insertItemOrder, showName);
    }

    public void copyItemsFrom(Section sectionFrom) {
        for (final Item item : sectionFrom.getAssociatedItems()) {
            this.insertItem(item.getName(), item.getBody(), item.getItemOrder(), item.getShowName());
        }
    }

    public void copySubSectionsAndItemsFrom(Section sectionFrom) {
        for (final Section subSectionFrom : sectionFrom.getChildrenSections()) {
            if (subSectionFrom.getSuperiorSection() != null) {
                Section subSectionTo = this.getSite().createSection(subSectionFrom.getName(), this, subSectionFrom.getOrder());
                subSectionTo.copyItemsFrom(subSectionFrom);
                subSectionTo.copySubSectionsAndItemsFrom(subSectionFrom);
            }
        }
    }

    public SortedSet<Section> getOrderedSubSections() {
        final SortedSet<Section> sections = new TreeSet<Section>(Section.COMPARATOR_BY_ORDER);
        sections.addAll(getChildrenSections());
        return sections;
    }

    public SortedSet<Section> getOrderedVisibleSubSections() {
        final SortedSet<Section> sections = new TreeSet<Section>(Section.COMPARATOR_BY_ORDER);
        for (Section section : getChildrenSections()) {
            if (section.getVisible()) {
                sections.add(section);
            }
        }
        return sections;
    }

    public boolean getShowSubSectionTree() {
        return getShowSubSections() != null && getShowSubSections() && !getOrderedVisibleSubSections().isEmpty();
    }

    public SortedSet<Item> getOrderedItems() {
        final SortedSet<Item> items = new TreeSet<Item>(Item.COMPARATOR_BY_ORDER);
        items.addAll(getAssociatedItems());
        return items;
    }

    /**
     * A section is always visible to the user as an entry in the menu. The
     * content of the section may not be available and that will be checked when
     * the user tries to access the section.
     */

    public boolean isVisible() {
        return getVisible();
    }

    public boolean isSubSectionAllowed() {
        return true;
    }

    public boolean isItemAllowed() {
        return true;
    }

    public List<Item> getAssociatedItems() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public int getAssociatedItemsCount() {
        return getAssociatedItems().size();
    }

    public Section getSuperiorSection() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public List<FileContent> getChildrenFiles() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void addFile(FileContent content) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public List<Section> getSubSections() {
        List<Section> sections = new ArrayList<Section>();
        for (Section section : getChildrenSections()) {
            sections.addAll(section.getSubSections());
        }
        return sections;
    }

    public boolean hasSuperiorSection() {
        return getSuperiorSection() != null;
    }

    public Site getSite() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public boolean isDeletable() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public void setVisible(Boolean visible) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public Boolean getVisible() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public void delete() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public boolean isAvailable() {
        throw new UnsupportedOperationException("Not implemented");
    }

}
