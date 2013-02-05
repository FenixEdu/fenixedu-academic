package net.sourceforge.fenixedu.presentationTier.Action.publico.pedagogicalCouncil;

import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.MetaDomainObject;
import net.sourceforge.fenixedu.domain.PedagogicalCouncilSite;
import net.sourceforge.fenixedu.domain.contents.MetaDomainObjectPortal;
import net.sourceforge.fenixedu.presentationTier.Action.publico.UnitSiteVisualizationDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "publico", path = "/pedagogicalCouncil/viewSite", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "announcementsAction", path = "/pedagogicalCouncil/announcements.do"),
        @Forward(name = "frontPage-INTRO_BANNER", path = "pedagogicalCouncil-site-front-page-intro-banner"),
        @Forward(name = "eventsAction", path = "/pedagogicalCouncil/events.do"),
        @Forward(name = "frontPage-BANNER_INTRO", path = "pedagogicalCouncil-site-front-page-banner-intro"),
        @Forward(name = "site-section-adviseLogin", path = "pedagogicalCouncil-section-adviseLogin"),
        @Forward(name = "frontPage-BANNER_INTRO_COLLAPSED", path = "pedagogicalCouncil-site-front-page-intro-float"),
        @Forward(name = "site-section", path = "pedagogicalCouncil-section"),
        @Forward(name = "site-item", path = "pedagogicalCouncil-item"),
        @Forward(name = "eventsRSSAction", path = "/pedagogicalCouncil/eventsRSS.do"),
        @Forward(name = "site-item-deny", path = "pedagogicalCouncil-item-deny"),
        @Forward(name = "site-item-adviseLogin", path = "pedagogicalCouncil-item-adviseLogin"),
        @Forward(name = "announcementsRSSAction", path = "/pedagogicalCouncil/announcementsRSS.do"),
        @Forward(name = "site-section-deny", path = "pedagogicalCouncil-section-deny"),
        @Forward(name = "unit-organization", path = "pedagogicalCouncil-organization") })
public class ViewPedagogicalSiteDA extends UnitSiteVisualizationDA {

    @Override
    protected String getDirectLinkContext(HttpServletRequest request) {
        MetaDomainObjectPortal portal =
                (MetaDomainObjectPortal) MetaDomainObject.getMeta(PedagogicalCouncilSite.class).getAssociatedPortal();
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
