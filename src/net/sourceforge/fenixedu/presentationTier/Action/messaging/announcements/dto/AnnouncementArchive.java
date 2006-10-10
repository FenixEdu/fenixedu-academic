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
 *         <br>
 *         Created on Jul 27, 2006,3:09:47 PM
 * 
 */
public class AnnouncementArchive {

    private AnnouncementBoard board;

    private AnnouncementArchiveAnnouncementsVisibility visibility;

    private Map<Integer, YearAnnouncementArchiveEntry> entries = null;

    public Map<Integer, YearAnnouncementArchiveEntry> getEntries() {
        if (this.entries == null) {
            this.init();
        }
        return entries;
    }

    public AnnouncementArchive(AnnouncementBoard board,
            AnnouncementArchiveAnnouncementsVisibility visibility) {
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
            announcements.addAll(this.board.getAnnouncements());
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
        YearAnnouncementArchiveEntry year = entries.get(announcement.getCreationDate().getYear());
        if (year == null) {
            year = new YearAnnouncementArchiveEntry(announcement.getCreationDate().getYear());
            entries.put(announcement.getCreationDate().getYear(), year);
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
