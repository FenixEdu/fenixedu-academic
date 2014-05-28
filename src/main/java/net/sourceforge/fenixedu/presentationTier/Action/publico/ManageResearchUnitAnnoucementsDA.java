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
package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ResearchUnitSite;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.Site.SiteMapper;
import net.sourceforge.fenixedu.domain.messaging.UnitAnnouncementBoard;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

@Mapping(module = "publico", path = "/researchSite/manageResearchUnitAnnouncements", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "viewAnnouncement", path = "research-view-announcement"),
        @Forward(name = "viewEvent", path = "research-view-event"),
        @Forward(name = "listAnnouncements", path = "show-research-unit-board-announcements"),
        @Forward(name = "listEvents", path = "show-research-unit-board-events") })
public class ManageResearchUnitAnnoucementsDA extends UnitSiteBoardsDA {

    protected ResearchUnitSite getSite(HttpServletRequest request) {
        Site content = SiteMapper.getSite(request);
        if (content != null) {
            return (ResearchUnitSite) content;
        }
        String siteID = request.getParameter("siteID");
        if (siteID != null) {
            ResearchUnitSite site = (ResearchUnitSite) FenixFramework.getDomainObject(siteID);
            return site;
        } else {
            return null;
        }
    }

    @Override
    public Unit getUnit(HttpServletRequest request) {
        return getSite(request).getUnit();
    }

    public ActionForward editAnnouncementBoards(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ResearchUnitSite site = getSite(request);
        Collection<UnitAnnouncementBoard> boards = site.getUnit().getBoards();
        request.setAttribute("announcementBoards", boards);
        return mapping.findForward(boards.isEmpty() ? "noBoards" : "listAnnouncementBoards");
    }

    @Override
    public String getContextParamName() {
        return "siteID";
    }

    @Override
    protected MultiLanguageString getBoardName(HttpServletRequest request) {
        return request.getMethod().equals("viewAnnouncements") ? UnitSiteBoardsDA.ANNOUNCEMENTS : UnitSiteBoardsDA.EVENTS;
    }

    @Override
    public ActionForward viewAnnouncements(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        setReturnMethodToView(request);
        return super.viewAnnouncements(mapping, form, request, response);
    }

    public ActionForward viewEvents(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        this.viewAnnouncements(mapping, form, request, response);
        return mapping.findForward("listEvents");
    }

    @Override
    public ActionForward viewEvent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        super.viewAnnouncement(mapping, form, request, response);
        return mapping.findForward("viewEvent");
    }

    private void setReturnMethodToView(HttpServletRequest request) {
        request.setAttribute("returnMethod", "viewAnnouncements");
    }

    @Override
    public ActionForward viewArchive(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        super.viewArchive(mapping, form, request, response);
        /*
         * Major refactor needed :-(
         */
        return mapping
                .findForward(getBoardName(request).equals(UnitSiteBoardsDA.ANNOUNCEMENTS) ? "listAnnouncements" : "listEvents");
    }
}
