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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ResearchUnitSite;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.Site.SiteMapper;
import net.sourceforge.fenixedu.domain.cms.OldCmsSemanticURLHandler;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "publico", path = "/researchSite/viewResearchUnitSite", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "announcementsAction", path = "/researchSite/manageResearchUnitAnnouncements.do"),
        @Forward(name = "frontPage-INTRO_BANNER", path = "research-site-front-page-intro-banner"),
        @Forward(name = "eventsAction", path = "/researchSite/manageResearchUnitAnnouncements.do"),
        @Forward(name = "showBoardAnnouncements", path = "show-research-unit-board-announcements"),
        @Forward(name = "unit-subunits", path = "show-research-unit-subunits"),
        @Forward(name = "frontPage-BANNER_INTRO", path = "research-site-front-page-banner-intro"),
        @Forward(name = "site-section-adviseLogin", path = "view-researchUnit-section-adviseLogin"),
        @Forward(name = "frontPage-BANNER_INTRO_COLLAPSED", path = "research-site-front-page-intro-float"),
        @Forward(name = "site-section", path = "view-researchUnit-section"),
        @Forward(name = "site-item", path = "view-researchUnit-item"),
        @Forward(name = "showResearchers", path = "show-research-unit-researchers"),
        @Forward(name = "eventsRSSAction", path = "/researchSite/eventsRSS.do"),
        @Forward(name = "site-item-deny", path = "view-researchUnit-item-deny"),
        @Forward(name = "site-item-adviseLogin", path = "view-researchUnit-item-adviseLogin"),
        @Forward(name = "announcementsRSSAction", path = "/researchSite/announcementsRSS.do"),
        @Forward(name = "unit-organization", path = "show-research-unit-organization"),
        @Forward(name = "site-section-deny", path = "view-researchUnit-section-deny"),
        @Forward(name = "showBoardEvents", path = "show-research-unit-board-events") })
public class ViewResearchUnitSiteDA extends UnitSiteVisualizationDA {

    @Override
    protected ActionForward getSiteDefaultView(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return frontPage(mapping, form, request, response);
    }

    public ActionForward frontPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return presentation(mapping, form, request, response);
    }

    public ActionForward showResearchers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        ResearchUnitSite site = getSite(request);
        request.setAttribute("researchUnit", site.getUnit());
        return mapping.findForward("showResearchers");
    }

    @Override
    protected String getContextParamName(HttpServletRequest request) {
        return "siteID";
    }

    @Override
    protected Unit getUnit(HttpServletRequest request) {
        return getSite(request).getUnit();
    }

    private ResearchUnitSite getSite(HttpServletRequest request) {
        Site container = SiteMapper.getSite(request);
        if (container == null) {
            String siteID = request.getParameter(getContextParamName(request));
            ResearchUnitSite site = FenixFramework.getDomainObject(siteID);
            OldCmsSemanticURLHandler.selectSite(request, site);
            return site;
        } else {
            return (ResearchUnitSite) container;
        }
    }

}
