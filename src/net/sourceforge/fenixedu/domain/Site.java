/*
 * Site.java
 * Mar 10, 2003
 */
package net.sourceforge.fenixedu.domain;

import java.util.Calendar;
import java.util.Date;

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

    public boolean equals(Object arg0) {
        boolean result = false;
        if (arg0 instanceof ISite) {
            ISite site = (ISite) arg0;
            result = (getExecutionCourse().equals(site.getExecutionCourse()));
        }
        return result;
    }

    public void createAnnouncement(final String announcementTitle, final String announcementInformation) {
        
        if (announcementTitle == null || announcementInformation == null) {
            throw new NullPointerException();
        }
        
        final Date currentDate = Calendar.getInstance().getTime();
        final IAnnouncement announcement = new Announcement(announcementTitle, currentDate, currentDate,
                announcementInformation, this);
        this.getAssociatedAnnouncements().add(announcement);
    }  
}
