/*
 * Site.java
 * Mar 10, 2003
 */
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

/**
 * @author Ivo Brand�o
 */
public class Site extends Site_Base {

    public Site() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setDynamicMailDistribution(false);
        setLessonPlanningAvailable(false);
    }

    public void edit(final String initialStatement, final String introduction, final String mail,
            final String alternativeSite) {

        setInitialStatement(initialStatement);
        setIntroduction(introduction);
        setMail(mail);
        setAlternativeSite(alternativeSite);
    }

    public void createAnnouncement(final String title, final String information) {
        new Announcement(title, new DateTime(), information, this);
    }

    public Section createSection(String sectionName, Section parentSection, Integer sectionOrder) {
        if (sectionName == null || sectionOrder == null)
            throw new NullPointerException();

        final Section section = new Section();
        // section.setName(sectionName);
        section.setLastModifiedDate(Calendar.getInstance().getTime());

        Integer newSectionOrder = organizeExistingSectionsOrder(parentSection, sectionOrder);
        section.setSectionOrder(newSectionOrder);

        section.setSite(this);
        section.setSuperiorSection(parentSection);

        return section;
    }

    private Integer organizeExistingSectionsOrder(final Section parentSection, int sectionOrder) {

        List<Section> associatedSections = getAssociatedSections(parentSection);

        if (associatedSections != null) {
            if (sectionOrder == -1) {
                sectionOrder = associatedSections.size();
            }
            for (final Section section : associatedSections) {
                int oldSectionOrder = section.getSectionOrder();
                if (oldSectionOrder >= sectionOrder) {
                    section.setSectionOrder(oldSectionOrder + 1);
                }
            }
        }
        return sectionOrder;
    }

    private static final Comparator<Announcement> ANNOUNCEMENT_ORDER = new Comparator<Announcement>() {
        public int compare(Announcement an1, Announcement an2) {
            return an2.getLastModifiedDate().compareTo(an1.getLastModifiedDate());
        }
    };

    // NOTE: The two following methods could be made much simpler if we
    // supported sorted sets
    // in the description of DML relations
    // This approach is not good because we are copying the set everytime we
    // need it...

    public Set<Announcement> getSortedAnnouncements() {
        TreeSet<Announcement> sortedAnnouncements = new TreeSet<Announcement>(ANNOUNCEMENT_ORDER);
        sortedAnnouncements.addAll(getAssociatedAnnouncements());
        return Collections.unmodifiableSet(sortedAnnouncements);
    }

    public Announcement getLastAnnouncement() {
        List<Announcement> allAnnouncements = getAssociatedAnnouncements();
        if (allAnnouncements.isEmpty()) {
            return null;
        } else {
            Announcement last = allAnnouncements.get(0);
            for (Announcement ann : allAnnouncements) {
                if (last.getLastModifiedDate().before(ann.getLastModifiedDate())) {
                    last = ann;
                }
            }
            return last;
        }
    }

    public boolean canBeDeleted() {
        return !hasAnyAssociatedAnnouncements() && !hasAnyAssociatedSections();
    }

    public void delete() {
        if (canBeDeleted()) {
            removeExecutionCourse();
            removeRootDomainObject();
            super.deleteDomainObject();
        } else {
            throw new DomainException("site.cannot.be.deleted");
        }
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
                Section sectionTo = this.createSection(sectionFrom.getName().getContent(Language.pt), null, sectionFrom.getSectionOrder());
                sectionTo.copyItemsFrom(sectionFrom);
                sectionTo.copySubSectionsAndItemsFrom(sectionFrom);
            }
        }
    }
    
}
