/*
 * Author : Goncalo Luiz
 * Creation Date: Jul 4, 2006,3:41:09 PM
 */
package net.sourceforge.fenixedu.presentationTier.Action.messaging;

import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoardAccessLevel;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoardAccessType;

import org.apache.struts.validator.ValidatorForm;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a><br>
 *         <br>
 *         Created on Jul 4, 2006,3:41:09 PM
 * 
 */
public class AnnouncementsStartPageForm extends ValidatorForm {

    private static final long serialVersionUID = -1164357680230910679L;

    private String recentBoardsTimeSpanSelection = RecentBoardsTimeSpanSelection.TS_LAST_WEEK.name();
    private String announcementBoardAccessType = AnnouncementBoardAccessType.ABAT_ALL.name();
    private String announcementBoardAccessLevel = AnnouncementBoardAccessLevel.ABAL_ALL.name();
    private Integer howManyAnnouncementsToShow = Integer.valueOf(6);

    public Integer getHowManyAnnouncementsToShow() {
        return howManyAnnouncementsToShow;
    }

    public void setHowManyAnnouncementsToShow(Integer howManyAnnouncementsToShow) {
        this.howManyAnnouncementsToShow = howManyAnnouncementsToShow;
    }

    public String getAnnouncementBoardAccessLevel() {
        return announcementBoardAccessLevel;
    }

    public void setAnnouncementBoardAccessLevel(String announcementBoardAccessLevel) {
        this.announcementBoardAccessLevel = announcementBoardAccessLevel;
    }

    public String getAnnouncementBoardAccessType() {
        return announcementBoardAccessType;
    }

    public void setAnnouncementBoardAccessType(String announcementBoardAccessType) {
        this.announcementBoardAccessType = announcementBoardAccessType;
    }

    public String getRecentBoardsTimeSpanSelection() {
        return recentBoardsTimeSpanSelection;
    }

    public void setRecentBoardsTimeSpanSelection(String recentBoardsTimeSpanSelection) {
        this.recentBoardsTimeSpanSelection = recentBoardsTimeSpanSelection;
    }
}
