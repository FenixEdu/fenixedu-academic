/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Author : Goncalo Luiz
 * Creation Date: Jul 27, 2006,3:06:08 PM
 */
package net.sourceforge.fenixedu.presentationTier.Action.messaging.announcements.dto;

import java.util.Map;
import java.util.TreeMap;

import net.sourceforge.fenixedu.domain.messaging.Announcement;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a><br>
 * <br>
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
        int monthOfYear =
                announcement.getReferedSubjectBegin() != null ? announcement.getReferedSubjectBegin().getMonthOfYear() : announcement
                        .getCreationDate().getMonthOfYear();
        MonthAnnouncementArchiveEntry month = entries.get(monthOfYear);
        if (month == null) {
            month = new MonthAnnouncementArchiveEntry(monthOfYear);
            entries.put(monthOfYear, month);
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
