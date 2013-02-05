package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.MetaDomainObject;
import net.sourceforge.fenixedu.domain.ResearchUnitSite;
import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.functionalities.AbstractFunctionalityContext;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "publico", path = "/researchSite/viewResearchUnitSite", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "announcementsAction", path = "/researchSite/manageResearchUnitAnnouncements.do"),
        @Forward(name = "frontPage-INTRO_BANNER", path = "research-site-front-page-intro-banner"),
        @Forward(name = "eventsAction", path = "/researchSite/manageResearchUnitAnnouncements.do"),
        @Forward(name = "showPublications", path = "show-research-unit-publications"),
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
        Container container = AbstractFunctionalityContext.getCurrentContext(request).getSelectedContainer();
        if (container == null) {
            String siteID = request.getParameter(getContextParamName(request));
            return (ResearchUnitSite) rootDomainObject.readContentByOID(Integer.valueOf(siteID));
        } else {
            return (ResearchUnitSite) container;
        }
    }

    @Override
    protected String getDirectLinkContext(HttpServletRequest request) {
        ResearchUnitSite site = getSite(request);

        try {
            MetaDomainObject metaDomainObject = MetaDomainObject.getMeta(ResearchUnitSite.class);
            String path =
                    metaDomainObject.getAssociatedPortal().getNormalizedName().getContent() + "/"
                            + site.getUnit().getUnitPath("/");
            return RequestUtils.absoluteURL(request, path).toString();
        } catch (MalformedURLException e) {
            return null;
        }
    }

}
