package net.sourceforge.fenixedu.presentationTier.Action.publico.scientificCouncil;

import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.MetaDomainObject;
import net.sourceforge.fenixedu.domain.ScientificCouncilSite;
import net.sourceforge.fenixedu.domain.contents.MetaDomainObjectPortal;
import net.sourceforge.fenixedu.presentationTier.Action.publico.UnitSiteVisualizationDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "publico", path = "/scientificCouncil/viewSite", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "announcementsAction", path = "/scientificCouncil/announcements.do"),
        @Forward(name = "frontPage-INTRO_BANNER", path = "scientificCouncil-site-front-page-intro-banner"),
        @Forward(name = "eventsAction", path = "/scientificCouncil/events.do"),
        @Forward(name = "frontPage-BANNER_INTRO", path = "scientificCouncil-site-front-page-banner-intro"),
        @Forward(name = "site-section-adviseLogin", path = "scientificCouncil-section-adviseLogin"),
        @Forward(name = "frontPage-BANNER_INTRO_COLLAPSED", path = "scientificCouncil-site-front-page-intro-float"),
        @Forward(name = "site-section", path = "scientificCouncil-section"),
        @Forward(name = "site-item", path = "scientificCouncil-item"),
        @Forward(name = "eventsRSSAction", path = "/scientificCouncil/eventsRSS.do"),
        @Forward(name = "site-item-deny", path = "scientificCouncil-item-deny"),
        @Forward(name = "site-item-adviseLogin", path = "scientificCouncil-item-adviseLogin"),
        @Forward(name = "announcementsRSSAction", path = "/scientificCouncil/announcementsRSS.do"),
        @Forward(name = "site-section-deny", path = "scientificCouncil-section-deny"),
        @Forward(name = "unit-organization", path = "scientificCouncil-organization") })
public class ViewScientificCouncilSiteDA extends UnitSiteVisualizationDA {

    @Override
    protected String getDirectLinkContext(HttpServletRequest request) {
        MetaDomainObjectPortal portal =
                (MetaDomainObjectPortal) MetaDomainObject.getMeta(ScientificCouncilSite.class).getAssociatedPortal();
        try {
            return RequestUtils.absoluteURL(request, portal.getNormalizedName().getContent()).toString();
        } catch (MalformedURLException e) {
            return "";
        }
    }

    @Override
    protected ActionForward getSiteDefaultView(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return presentation(mapping, form, request, response);
    }

}
