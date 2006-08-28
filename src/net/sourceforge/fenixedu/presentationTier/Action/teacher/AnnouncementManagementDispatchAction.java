package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.InfoAnnouncement;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Ivo Brandão
 */
public class AnnouncementManagementDispatchAction extends FenixDispatchAction {

    public ActionForward prepareCreateAnnouncement(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);
        session.removeAttribute(SessionConstants.INFO_SECTION);
        session.removeAttribute("insertAnnouncementForm");

        return mapping.findForward("insertAnnouncement");
    }

    public ActionForward createAnnouncement(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);
        session.removeAttribute(SessionConstants.INFO_SECTION);
        DynaActionForm insertAnnouncementForm = (DynaActionForm) form;
        String title = (String) insertAnnouncementForm.get("title");
        String information = (String) insertAnnouncementForm.get("information");

        InfoSite infoSite = (InfoSite) session.getAttribute(SessionConstants.INFO_SITE);
        IUserView userView = getUserView(request);

        Object args[] = { infoSite, title, information };
        ServiceManagerServiceFactory.executeService(userView, "InsertAnnouncement", args);

        //return to announcementManagement
        return mapping.findForward("accessAnnouncementManagement");
    }

    public ActionForward prepareEditAnnouncement(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);
        session.removeAttribute(SessionConstants.INFO_SECTION);
        //retrieve announcement
        List announcements = (List) session.getAttribute(SessionConstants.INFO_SITE_ANNOUNCEMENT_LIST);
        String announcementIndex = request.getParameter("index");
        Integer index = new Integer(announcementIndex);
        InfoAnnouncement infoAnnouncement = (InfoAnnouncement) announcements.get(index.intValue());

        session.setAttribute("Announcement", infoAnnouncement);

        return mapping.findForward("editAnnouncement");
    }

    public ActionForward editAnnouncement(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);
        session.removeAttribute(SessionConstants.INFO_SECTION);
        DynaActionForm insertAnnouncementForm = (DynaActionForm) form;
        String newTitle = (String) insertAnnouncementForm.get("title");
        String newInformation = (String) insertAnnouncementForm.get("information");

        InfoSite infoSite = (InfoSite) session.getAttribute(SessionConstants.INFO_SITE);
        InfoAnnouncement infoAnnouncement = (InfoAnnouncement) session.getAttribute("Announcement");
        IUserView userView = getUserView(request);

        Object args[] = { infoSite, infoAnnouncement, newTitle, newInformation };
        ServiceManagerServiceFactory.executeService(userView, "EditAnnouncement", args);

        //remove index from session
        session.removeAttribute("index");

        //return to announcementManagement
        return mapping.findForward("accessAnnouncementManagement");
    }

    public ActionForward deleteAnnouncement(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        /*
         * session: Site, Announcement; action: delete Announcement.
         */

        HttpSession session = request.getSession(false);
        session.removeAttribute(SessionConstants.INFO_SECTION);
        InfoSite infoSite = (InfoSite) session.getAttribute(SessionConstants.INFO_SITE);

        List announcements = (List) session.getAttribute(SessionConstants.INFO_SITE_ANNOUNCEMENT_LIST);
        String announcementIndex = request.getParameter("index");
        Integer index = new Integer(announcementIndex);
        InfoAnnouncement infoAnnouncement = (InfoAnnouncement) announcements.get(index.intValue());

        IUserView userView = getUserView(request);

        Object args[] = { infoSite.getInfoExecutionCourse(), infoSite, infoAnnouncement };
        ServiceManagerServiceFactory.executeService(userView, "DeleteAnnouncement", args);

        //remove index from session
        session.removeAttribute("index");

        //return to announcementManagement
        return mapping.findForward("accessAnnouncementManagement");
    }

    public ActionForward showAnnouncements(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        //return to announcementManagement
        return mapping.findForward("showAnnouncements");
    }

}