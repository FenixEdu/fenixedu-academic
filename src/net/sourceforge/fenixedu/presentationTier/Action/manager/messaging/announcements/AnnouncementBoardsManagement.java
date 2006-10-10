/**
 * Author : Goncalo Luiz
 * Creation Date: Jun 20, 2006,5:02:14 PM
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager.messaging.announcements;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import net.sourceforge.fenixedu.domain.messaging.Announcement;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a><br>
 *         <br>
 *         Created on Jun 20, 2006,5:02:14 PM
 * 
 */
public class AnnouncementBoardsManagement extends FenixDispatchAction {

    public ActionForward stats(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        if (!getLoggedPerson(request).hasRole(RoleType.MANAGER))
        {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                    "error.not.allowed.to.view.board.statistics"));
            saveErrors(request, actionMessages);
            return this.start(mapping, actionForm, request, response);
        }
        int boardsCount = rootDomainObject.getAnnouncementBoardsCount();
        int announcementsCount = rootDomainObject.getAnnouncementsCount();

        Collection<Announcement> announcements = rootDomainObject.getAnnouncements();
        int visibleAnnouncementsCount = 0;
        int visibleNotExpiredAnnouncementsCount = 0;
        int visibleExpiredAnnouncementsCount = 0;
        int invisibleAnnouncementsCount = 0;
        int invisibleNotExpiredAnnouncementsCount = 0;
        int invisibleExpiredAnnouncementsCount = 0;

        for (Announcement announcement : announcements) {
            if (!announcement.getVisible()) {
                invisibleAnnouncementsCount++;
                if (announcement.getPublicationEnd() == null
                        || announcement.getPublicationEnd().isAfterNow()) {
                    invisibleNotExpiredAnnouncementsCount++;
                } else {
                    invisibleExpiredAnnouncementsCount++;
                }
            } else {
                visibleAnnouncementsCount++;
                if (announcement.getPublicationEnd() == null
                        || announcement.getPublicationEnd().isAfterNow()) {
                    visibleNotExpiredAnnouncementsCount++;
                } else {
                    visibleExpiredAnnouncementsCount++;
                }
            }
        }

        request.setAttribute("boardsCount", boardsCount);
        request.setAttribute("announcementsCount", announcementsCount);
        request.setAttribute("visibleAnnouncementsCount", visibleAnnouncementsCount);
        request.setAttribute("visibleNotExpiredAnnouncementsCount", visibleNotExpiredAnnouncementsCount);
        request.setAttribute("visibleExpiredAnnouncementsCount", visibleExpiredAnnouncementsCount);
        request.setAttribute("invisibleAnnouncementsCount", invisibleAnnouncementsCount);
        request.setAttribute("invisibleNotExpiredAnnouncementsCount",
                invisibleNotExpiredAnnouncementsCount);
        request.setAttribute("invisibleExpiredAnnouncementsCount", invisibleExpiredAnnouncementsCount);

        return mapping.findForward("statistics");
    }

    public ActionForward start(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        return mapping.findForward("firstPage");
    }

    public ActionForward chooseBoardType(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("chooseBoardType");
    }
    
    public ActionForward createExecutionCourseAnnouncementBoard(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("chooseBoardType");
    }
}
