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
 * Creation Date: Jul 27, 2006,3:09:47 PM
 */
package net.sourceforge.fenixedu.presentationTier.Action.messaging.announcements.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import net.sourceforge.fenixedu.domain.messaging.Announcement;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a><br>
 * <br>
 *         Created on Jul 27, 2006,3:09:47 PM
 * 
 */
public class AnnouncementArchive {

    private final AnnouncementBoard board;

    private final AnnouncementArchiveAnnouncementsVisibility visibility;

    private Map<Integer, YearAnnouncementArchiveEntry> entries = null;

    public Map<Integer, YearAnnouncementArchiveEntry> getEntries() {
        if (this.entries == null) {
            this.init();
        }
        return entries;
    }

    public AnnouncementArchive(AnnouncementBoard board, AnnouncementArchiveAnnouncementsVisibility visibility) {
        this.board = board;
        this.visibility = visibility;
    }

    private void init() {
        Collection<Announcement> announcements = new ArrayList<Announcement>();
        this.entries = new TreeMap<Integer, YearAnnouncementArchiveEntry>();
        switch (this.visibility) {
        case ACTIVE:
            announcements.addAll(this.board.getActiveAnnouncements());
            break;
        case ALL:
            announcements.addAll(this.board.getAnnouncementSet());
            break;
        case VISIBLE:
            announcements.addAll(this.board.getVisibleAnnouncements());
            break;
        }
        for (Announcement announcement : announcements) {
            this.addAnnouncement(announcement);
        }
    }

    public void addAnnouncement(Announcement announcement) {

        Integer announcementYear =
                announcement.getReferedSubjectBegin() != null ? announcement.getReferedSubjectBegin().getYear() : announcement
                        .getCreationDate().getYear();

        YearAnnouncementArchiveEntry year = entries.get(announcementYear);
        if (year == null) {
            year = new YearAnnouncementArchiveEntry(announcementYear);
            entries.put(announcementYear, year);
        }
        year.addAnnouncement(announcement);
    }

    public int getYearsCount() {
        return this.entries.keySet().size();
    }

    public AnnouncementBoard getBoard() {
        return board;
    }
}
