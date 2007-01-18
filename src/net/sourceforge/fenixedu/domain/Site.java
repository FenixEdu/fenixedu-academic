/*
 * Site.java
 * Mar 10, 2003
 */
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.accessControl.EveryoneGroup;
import net.sourceforge.fenixedu.domain.accessControl.InternalPersonGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import net.sourceforge.fenixedu.util.MultiLanguageString;
import net.sourceforge.fenixedu.util.domain.OrderedRelationAdapter;

/**
 * @author Ivo Brandão
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
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public Section createSection(MultiLanguageString sectionName, Section parentSection,
            Integer sectionOrder) {
        return new Section(this, sectionName, sectionOrder, parentSection);
    }

    public abstract IGroup getOwner();

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
    
    private Site getSiteTemplate() {
        Site template = getTemplate();
        if (template != null) {
            return template;
        }

        template = SiteTemplate.getTemplateForType(getClass());
        if (template != null) {
            //setTemplate(template); // can only happen in a write tx 
            return template;
        }
        
        return null;
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

    public String getAuthorName() {
        return null;
    }

    public ExecutionPeriod getExecutionPeriod() {
        return null;
    }

    public List<Section> getOrderedTemplateSections() {
        List<Section> sections = new ArrayList<Section>();
        
        Site template = getSiteTemplate();
        if (template != null) {
            sections.addAll(template.getOrderedTemplateSections());
            sections.addAll(template.getOrderedTopLevelSections());
        }
        
        return sections;
    }

    public List<Section> getTemplateSections() {
        List<Section> sections = new ArrayList<Section>();
        
        Site template = getSiteTemplate();
        if (template != null) {
            sections.addAll(template.getTemplateSections());
            sections.addAll(template.getTopLevelSections());
        }
        
        return sections;
    }

    public List<Section> getAllOrderedTopLevelSections() {
        List<Section> sections = getOrderedTemplateSections();
        sections.addAll(getOrderedTopLevelSections());
        
        return sections;
    }

    public List<Section> getAllTopLevelSections() {
        List<Section> sections = getTemplateSections();
        sections.addAll(getTopLevelSections());
        
        return sections;
    }
    
    public static List<Section> getOrderedSections(Collection<Section> sections) {
        List<Section> orderedSections = new ArrayList<Section>(sections);
        Collections.sort(orderedSections, Section.COMPARATOR_BY_ORDER);
        
        return orderedSections;
    }
}
