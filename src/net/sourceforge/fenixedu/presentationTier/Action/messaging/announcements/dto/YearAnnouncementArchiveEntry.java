/*
 * Author : Goncalo Luiz
 * Creation Date: Jul 27, 2006,3:06:08 PM
 */
package net.sourceforge.fenixedu.presentationTier.Action.messaging.announcements.dto;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import net.sourceforge.fenixedu.domain.messaging.Announcement;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a><br>
 *         <br>
 *         Created on Jul 27, 2006,3:06:08 PM
 * 
 */
public class YearAnnouncementArchiveEntry {

    private Integer year;

    final private Map<Integer, MonthAnnouncementArchiveEntry> entries = new TreeMap<Integer, MonthAnnouncementArchiveEntry>();

    public Map<Integer, MonthAnnouncementArchiveEntry> getEntries() {
        return entries;
    }

    public YearAnnouncementArchiveEntry(Integer year) {
        this.year = year;
    }

    public void addAnnouncement(Announcement announcement) {
        MonthAnnouncementArchiveEntry month = entries.get(announcement.getCreationDate()
                .getMonthOfYear());
        if (month == null) {
            month = new MonthAnnouncementArchiveEntry(announcement.getCreationDate().getMonthOfYear());
            entries.put(announcement.getCreationDate().getMonthOfYear(), month);
        }
        month.addAnnouncement(announcement);

    }

    public Integer getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonthsCount() {
        return this.entries.size();
    }

    public Integer getFirstPostMonth() {
        Integer firstMonth = null;
        if (this.entries.size() != 0) {
            firstMonth = this.entries.keySet().iterator().next();
        }

        return firstMonth;
    }
}
