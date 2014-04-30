package net.sourceforge.fenixedu.presentationTier.Action.webSiteManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

public class WebsiteForwards extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        UnitSite site = getDomainObject(request, "oid");
        request.setAttribute("siteActionName", "/manageUnitSite.do");
        request.setAttribute("siteContextParam", "oid");
        request.setAttribute("siteContextParamValue", site.getExternalId());

        request.setAttribute("unitId", site.getUnit().getExternalId());
        request.setAttribute("announcementsActionName", "/manageUnitAnnouncements.do");
        return new ActionForward("redirect", mapping.getParameter(), false, "/webSiteManager");
    }

    @Mapping(path = "/manageInstitutionSite", module = "webSiteManager", parameter = "/announcementsManagement.do?method=start",
            functionality = ListSitesAction.class)
    public static class ManageInstitutionSite extends WebsiteForwards {
    }

    @Mapping(path = "/manageTutorSite", module = "webSiteManager", parameter = "/manageUnitSite.do?method=prepare",
            functionality = ListSitesAction.class)
    public static class ManageTutorSite extends WebsiteForwards {
    }

    @Mapping(path = "/manageAssemblySite", module = "webSiteManager", parameter = "/manageUnitSite.do?method=prepare",
            functionality = ListSitesAction.class)
    public static class ManageAssemblySite extends WebsiteForwards {
    }

    @Mapping(path = "/manageStudentsSite", module = "webSiteManager", parameter = "/manageUnitSite.do?method=prepare",
            functionality = ListSitesAction.class)
    public static class ManageStudentsSite extends WebsiteForwards {
    }

    @Mapping(path = "/manageManagementCouncilSite", module = "webSiteManager", parameter = "/manageUnitSite.do?method=prepare",
            functionality = ListSitesAction.class)
    public static class ManageManagementCouncilSite extends WebsiteForwards {
    }

    @Mapping(path = "/manageEdamSite", module = "webSiteManager", parameter = "/manageUnitSite.do?method=prepare",
            functionality = ListSitesAction.class)
    public static class ManageEdamSite extends WebsiteForwards {
    }

    @Mapping(path = "/manageScientificAreaSite", module = "webSiteManager", parameter = "/manageUnitSite.do?method=prepare",
            functionality = ListSitesAction.class)
    public static class ManageScientificAreaSite extends WebsiteForwards {
    }

}
