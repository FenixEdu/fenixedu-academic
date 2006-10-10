/*
 * Author : Goncalo Luiz
 * Creation Date: Jul 27, 2006,2:35:49 PM
 */
package net.sourceforge.fenixedu.presentationTier.Action.messaging.announcements.dto;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.domain.messaging.Announcement;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a><br>
 *         <br>
 *         Created on Jul 27, 2006,2:35:49 PM
 * 
 */
public class MonthAnnouncementArchiveEntry {

    private Integer month;

    final private Collection<Announcement> announcements = new ArrayList<Announcement>();

    public int getAnnouncementCount() {
        return announcements.size();
    }

    public MonthAnnouncementArchiveEntry(Integer month) {
        this.month = month;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Collection<Announcement> getAnnouncements() {
        return announcements;
    }

    public void addAnnouncement(Announcement announcement) {
        this.announcements.add(announcement);
    }

}
