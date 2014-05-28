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
package net.sourceforge.fenixedu.presentationTier.Action.publicRelationsOffice;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import net.sourceforge.fenixedu.domain.messaging.UnitAnnouncementBoard;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.AnnouncementManagement;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = PublicRelationsApplication.class, path = "announcements", titleKey = "label.announcements")
@Mapping(path = "/announcementsManagement", module = "publicRelations")
@Forwards({ @Forward(name = "add", path = "/publicRelations/announcements/addAnnouncement.jsp"),
        @Forward(name = "edit", path = "/publicRelations/announcements/editAnnouncement.jsp"),
        @Forward(name = "listAnnouncementBoards", path = "/publicRelations/announcements/listAnnouncementBoards.jsp"),
        @Forward(name = "listAnnouncements", path = "/messaging/announcements/listBoardAnnouncements.jsp"),
        @Forward(name = "viewAnnouncementBoard", path = "publicRelationsOffice-view-announcementBoard"),
        @Forward(name = "viewAnnouncement", path = "/messaging/announcements/viewAnnouncement.jsp"),
        @Forward(name = "uploadFile", path = "/messaging/announcements/uploadFileToBoard.jsp"),
        @Forward(name = "editFile", path = "/messaging/announcements/editFileInBoard.jsp") })
public class PublicRelationAnnouncementManagement extends AnnouncementManagement {

    @Override
    protected Collection<AnnouncementBoard> boardsToView(HttpServletRequest request) throws Exception {
        final Collection<AnnouncementBoard> boards = new ArrayList<AnnouncementBoard>();
        final Person person = this.getLoggedPerson(request);
        for (final AnnouncementBoard currentBoard : rootDomainObject.getInstitutionUnit().getBoardsSet()) {
            final UnitAnnouncementBoard board = (UnitAnnouncementBoard) currentBoard;
            if (board.getWriters().isMember(person.getUser())) {
                boards.add(board);
            }
        }
        return boards;
    }

    @Override
    protected String getContextInformation(ActionMapping mapping, HttpServletRequest request) {
        return "/announcementsManagement.do";
    }

    @Override
    protected String getExtraRequestParameters(HttpServletRequest request) {
        return "tabularVersion=true";
    }

    @Override
    @EntryPoint
    public ActionForward viewAllBoards(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return super.viewAllBoards(mapping, form, request, response);
    }

}
