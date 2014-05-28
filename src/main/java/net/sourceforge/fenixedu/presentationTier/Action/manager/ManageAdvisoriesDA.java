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
 * Created on 2003/09/06
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import net.sourceforge.fenixedu.presentationTier.Action.manager.ManagerApplications.ManagerMessagesAndNoticesApp;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.AnnouncementManagement;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author Luis Cruz
 * @author Gonçalo Luiz
 */

@StrutsFunctionality(app = ManagerMessagesAndNoticesApp.class, path = "manage-notices", titleKey = "title.notices")
@Mapping(module = "manager", path = "/manageAdvisories", input = "/manageAdvisories.do?method=prepare&page=0",
        formBean = "advisoryForm")
@Forwards({ @Forward(name = "uploadFile", path = "/messaging/announcements/uploadFileToBoard.jsp"),
        @Forward(name = "edit", path = "/messaging/announcements/editAnnouncement.jsp"),
        @Forward(name = "listAnnouncements", path = "/messaging/announcements/listBoardAnnouncements.jsp"),
        @Forward(name = "add", path = "/messaging/announcements/addAnnouncement.jsp"),
        @Forward(name = "success", path = "/advisoriesManagement/listCurrentAdvisories.faces"),
        @Forward(name = "editFile", path = "/messaging/announcements/editFileInBoard.jsp"),
        @Forward(name = "listAnnouncementBoards", path = "/messaging/announcements/listAnnouncementBoards.jsp") })
public class ManageAdvisoriesDA extends AnnouncementManagement {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("returnAction", this.getContextInformation(mapping, request));
        request.setAttribute("returnMethod", "start");
        return super.execute(mapping, actionForm, request, response);
    }

    @Override
    protected Collection<AnnouncementBoard> boardsToView(HttpServletRequest request) throws Exception {
        final User userView = getUserView(request);
        final Collection<AnnouncementBoard> boardsToShow = new ArrayList<AnnouncementBoard>();
        for (AnnouncementBoard board : rootDomainObject.getInstitutionUnit().getBoards()) {
            if (board.getWriters() == null || board.getWriters().isMember(userView)) {
                boardsToShow.add(board);
            }
        }
        return boardsToShow;
    }

    @Override
    protected String getContextInformation(ActionMapping mapping, HttpServletRequest request) {
        return "/manageAdvisories.do";
    }

    @Override
    protected String getExtraRequestParameters(HttpServletRequest request) {
        return "";
    }
}