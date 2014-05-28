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
 * Creation Date: Jul 27, 2006,2:35:49 PM
 */
package net.sourceforge.fenixedu.presentationTier.Action.messaging.announcements.dto;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.domain.messaging.Announcement;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a><br>
 * <br>
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
