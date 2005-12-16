/*
 * Site.java
 * Mar 10, 2003
 */
package net.sourceforge.fenixedu.domain;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Ivo Brandão
 */
public class Site extends Site_Base {

    public String toString() {
        String result = "[SITE";
        result += ", codInt=" + getIdInternal();
        result += ", executionCourse=" + getExecutionCourse();
        result += ", initialStatement=" + getInitialStatement();
        result += ", introduction=" + getIntroduction();
        result += ", mail =" + getMail();
        result += ", alternativeSite=" + getAlternativeSite();
        result += "]";
        return result;
    }

    public void edit(final String initialStatement, final String introduction, final String mail,
            final String alternativeSite) {
        
        setInitialStatement(initialStatement);
        setIntroduction(introduction);
        setMail(mail);
        setAlternativeSite(alternativeSite);
    }

    public void createAnnouncement(final String announcementTitle, final String announcementInformation) {

        if (announcementTitle == null || announcementInformation == null) {
            throw new NullPointerException();
        }
        final Date currentDate = Calendar.getInstance().getTime();
        final IAnnouncement announcement = new Announcement();
        announcement.setTitle(announcementTitle);
        announcement.setInformation(announcementInformation);
        announcement.setCreationDate(currentDate);
        announcement.setLastModifiedDate(currentDate);
        announcement.setSite(this);
    }

    public ISection createSection(String sectionName, ISection parentSection, Integer sectionOrder) {
        if (sectionName == null || sectionOrder == null)
            throw new NullPointerException();

        final ISection section = new Section();
        section.setName(sectionName);
        section.setLastModifiedDate(Calendar.getInstance().getTime());

        Integer newSectionOrder = organizeExistingSectionsOrder(parentSection, sectionOrder);
        section.setSectionOrder(newSectionOrder);
        
        section.setSite(this);
        section.setSuperiorSection(parentSection);
        
        return section;
    }

    private Integer organizeExistingSectionsOrder(final ISection parentSection, int sectionOrder) {

        List<ISection> associatedSections = Section.getSections(parentSection, this);
        
        if (associatedSections != null) {
            if (sectionOrder == -1) {
                sectionOrder = associatedSections.size();
            }
            for (final ISection section : associatedSections) {
                int oldSectionOrder = section.getSectionOrder();
                if (oldSectionOrder >= sectionOrder) {
                    section.setSectionOrder(oldSectionOrder + 1);
                }
            }
        }
        return sectionOrder;
    }


    private static final Comparator<IAnnouncement> ANNOUNCEMENT_ORDER = new Comparator<IAnnouncement>() {
	public int compare(IAnnouncement an1, IAnnouncement an2) {
	    return an2.getLastModifiedDate().compareTo(an1.getLastModifiedDate());
	}
    };

    // NOTE: The two following methods could be made much simpler if we supported sorted sets 
    // in the description of DML relations
    // This approach is not good because we are copying the set everytime we need it...

    public Set<IAnnouncement> getSortedAnnouncements() {
	TreeSet<IAnnouncement> sortedAnnouncements = new TreeSet<IAnnouncement>(ANNOUNCEMENT_ORDER);
	sortedAnnouncements.addAll(getAssociatedAnnouncements());
	return Collections.unmodifiableSet(sortedAnnouncements);
    }

    public IAnnouncement getLastAnnouncement() {
	List<IAnnouncement> allAnnouncements = getAssociatedAnnouncements();
	if (allAnnouncements.isEmpty()) {
	    return null;
	} else {
	    IAnnouncement last = allAnnouncements.get(0);
	    for (IAnnouncement ann : allAnnouncements) {
		if (last.getLastModifiedDate().before(ann.getLastModifiedDate())) {
		    last = ann;
		}
	    }
	    return last;
	}
    }
    
    private boolean canBeDeleted() {
    	if (hasAnyAssociatedAnnouncements()) {
            return false;
        }
        if (hasAnyAssociatedSections()) {
            return false;
        }
        
        return true;
    }
    
    public void delete() {
    	if(canBeDeleted()) {
    		setExecutionCourse(null);
    		super.deleteDomainObject();
    	}
    }
}
