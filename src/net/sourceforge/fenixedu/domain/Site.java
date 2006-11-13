/*
 * Site.java
 * Mar 10, 2003
 */
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.accessControl.IGroup;
import net.sourceforge.fenixedu.domain.accessControl.EveryoneGroup;
import net.sourceforge.fenixedu.domain.accessControl.InternalPersonGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.MultiLanguageString;
import net.sourceforge.fenixedu.util.domain.OrderedRelationAdapter;

/**
 * @author Ivo Brand�o
 */
public abstract class Site extends Site_Base {

    public static OrderedRelationAdapter<Site, Section> SECTION_ORDER_ADAPTER;
    static {
        SECTION_ORDER_ADAPTER = new OrderedRelationAdapter<Site, Section>("topLevelSections", "sectionOrder");
        SiteSection.addListener(SECTION_ORDER_ADAPTER);
    }
    
    public Site() {
        super();
        setOjbConcreteClass(this.getClass().getName());
    }

    public Section createSection(MultiLanguageString sectionName, Section parentSection,
            Integer sectionOrder) {
        return new Section(this, sectionName, sectionOrder, parentSection);
    }

    public boolean canBeDeleted() {
        return !hasAnyAssociatedSections();
    }

    public void delete() {
        if (canBeDeleted()) {
            deleteRelations();

            super.deleteDomainObject();
        } else {
            throw new DomainException("site.cannot.be.deleted");
        }
    }

    protected void deleteRelations() {
    }
    
    public List<Section> getTopLevelSections() {
        return getAssociatedSections(null);
    }
    
    public List<Section> getAssociatedSections(final Section parentSection) {
        final List<Section> result;
        if (parentSection != null) {
            result = parentSection.getAssociatedSections();
        } else {
            result = new ArrayList<Section>();
            for (final Section section : this.getAssociatedSections()) {
                if (!section.hasSuperiorSection()) {
                    result.add(section);
                }
            }
        }
        return result;
    }

    public SortedSet<Section> getOrderedTopLevelSections() {
        final SortedSet<Section> sections = new TreeSet<Section>(Section.COMPARATOR_BY_ORDER);
        for (final Section section : getAssociatedSectionsSet()) {
            if (section.getSuperiorSection() == null) {
                sections.add(section);
            }
        }
        return sections;
    }

    public int getNumberOfTopLevelSections() {
        int count = 0;
        for (final Section section : getAssociatedSectionsSet()) {
            if (section.getSuperiorSection() == null) {
                count++;

            }
        }
        return count;
    }

    public void copySectionsAndItemsFrom(Site siteFrom) {
        for (Section sectionFrom : siteFrom.getAssociatedSections()) {
            if (sectionFrom.getSuperiorSection() == null) {
                Section sectionTo = this.createSection(sectionFrom.getName(), null, sectionFrom
                        .getSectionOrder());
                sectionTo.copyItemsFrom(sectionFrom);
                sectionTo.copySubSectionsAndItemsFrom(sectionFrom);
            }
        }
    }

    /**
     * Obtains a list of all the groups available in the context of this site.
     * 
     * @return
     */
    public List<IGroup> getContextualPermissionGroups() {
        List<IGroup> groups = new ArrayList<IGroup>();
        
        groups.add(new EveryoneGroup());
        groups.add(new InternalPersonGroup());
        
        return groups;
    }

    public void setTopLevelSectionsOrder(List<Section> sections) {
        SECTION_ORDER_ADAPTER.updateOrder(this, sections);
    }
}
