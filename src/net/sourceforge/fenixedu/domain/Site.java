/*
 * Site.java
 * Mar 10, 2003
 */
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

    public boolean equals(Object obj) {
        if (obj instanceof ISite) {
            final ISite site = (ISite) obj;
            return this.getIdInternal().equals(site.getIdInternal());
        }
        return false;
    }

    public void edit(final String initialStatement, final String introduction, final String mail,
            final String alternativeSite) {
        if (initialStatement == null || introduction == null || mail == null || alternativeSite == null) {
            throw new NullPointerException();
        }
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

    public void createSection(String sectionName, ISection parentSection, Integer sectionOrder) {
        if (sectionName == null || sectionOrder == null)
            throw new NullPointerException();

        final ISection section = new Section();
        section.setName(sectionName);
        section.setLastModifiedDate(Calendar.getInstance().getTime());

        Integer newSectionOrder = organizeExistingSectionsOrder(parentSection, sectionOrder);
        section.setSectionOrder(newSectionOrder);
        
        section.setSite(this);
        section.setSuperiorSection(parentSection);
    }

    private Integer organizeExistingSectionsOrder(final ISection parentSection, int sectionOrder) {

        List<ISection> associatedSections = null;
        if (parentSection == null) { // Then parent is Site
            associatedSections = getAssociatedSectionsWhithSiteAsParent();
        } else {
            associatedSections = parentSection.getAssociatedSections();
        }

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

    private List<ISection> getAssociatedSectionsWhithSiteAsParent() {
        final List<ISection> result = new ArrayList();
        for (final ISection section : this.getAssociatedSections()) {
            if (section.getSuperiorSection() == null) {
                result.add(section);
            }
        }
        return result;
    }

}
