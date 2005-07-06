/*
 * Site.java
 * Mar 10, 2003
 */
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
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

    public boolean equals(Object obj) {
        if (obj instanceof ISite) {
            final ISite site = (ISite) obj;
            return this.getIdInternal().equals(site.getIdInternal());
        }
        return false;
    }

    public IAnnouncement createAnnouncement(final String announcementTitle,
            final String announcementInformation) {

        if (announcementTitle == null || announcementInformation == null) {
            throw new NullPointerException();
        }

        final Date currentDate = Calendar.getInstance().getTime();
        final IAnnouncement announcement = new Announcement(announcementTitle, currentDate, currentDate,
                announcementInformation, this);

        if (this.getAssociatedAnnouncements() == null)
            this.setAssociatedAnnouncements(new ArrayList());

        this.getAssociatedAnnouncements().add(announcement);

        return announcement;
    }

    public void editSite(final String initialStatement, final String introduction, final String mail,
            final String alternativeSite) {
        if (initialStatement == null || introduction == null || mail == null || alternativeSite == null) {
            throw new NullPointerException();
        }
        setInitialStatement(initialStatement);
        setIntroduction(introduction);
        setMail(mail);
        setAlternativeSite(alternativeSite);
    }

}
