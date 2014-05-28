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
package net.sourceforge.fenixedu.presentationTier.Action.webSiteManager;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.messaging.UnitAnnouncementBoard;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "webSiteManager", path = "/manageResearchUnitAnnouncements", functionality = ListSitesAction.class)
public class ManageResearchUnitAnnouncementsDA extends UnitSiteAnnouncementManagement {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("siteActionName", "/manageResearchUnitSite.do");
        request.setAttribute("siteContextParam", "oid");
        request.setAttribute("siteContextParamValue", getSite(request).getExternalId());
        request.setAttribute("siteId", getSite(request).getExternalId());
        request.setAttribute("announcementsActionName", "/manageResearchUnitAnnouncements.do");
        request.setAttribute("researchUnit", true);
        return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward editAnnouncementBoards(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Collection<UnitAnnouncementBoard> boards = getUnit(request).getBoardsSet();
        request.setAttribute("announcementBoards", boards);
        return mapping.findForward(boards.isEmpty() ? "noBoards" : "listAnnouncementBoards");
    }

    @Override
    protected String getContextInformation(ActionMapping mapping, HttpServletRequest request) {
        return "/manageResearchUnitAnnouncements.do";
    }

}
