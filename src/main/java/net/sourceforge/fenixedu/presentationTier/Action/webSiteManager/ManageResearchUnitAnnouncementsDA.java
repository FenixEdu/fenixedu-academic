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
