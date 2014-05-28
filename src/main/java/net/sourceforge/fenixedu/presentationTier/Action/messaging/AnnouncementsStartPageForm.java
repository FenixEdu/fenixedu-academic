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
 * Creation Date: Jul 4, 2006,3:41:09 PM
 */
package net.sourceforge.fenixedu.presentationTier.Action.messaging;

import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoardAccessLevel;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoardAccessType;

import org.apache.struts.action.ActionForm;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a><br>
 * <br>
 *         Created on Jul 4, 2006,3:41:09 PM
 * 
 */
public class AnnouncementsStartPageForm extends ActionForm {

    private static final long serialVersionUID = -1164357680230910679L;

    private String recentBoardsTimeSpanSelection = RecentBoardsTimeSpanSelection.TS_LAST_WEEK.name();
    private String announcementBoardAccessType = AnnouncementBoardAccessType.ABAT_ALL.name();
    private String announcementBoardAccessLevel = AnnouncementBoardAccessLevel.ABAL_ALL.name();
    private Integer howManyAnnouncementsToShow = Integer.valueOf(6);
    private String boardType = "BOOKMARKED";

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

    public String getBoardType() {
        return boardType;
    }

    public void setBoardType(String boardType) {
        this.boardType = boardType;
    }
}
