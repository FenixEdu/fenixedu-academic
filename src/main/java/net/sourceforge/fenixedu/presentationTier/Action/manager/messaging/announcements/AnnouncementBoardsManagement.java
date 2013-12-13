/**
 * Author : Goncalo Luiz
 * Creation Date: Jun 20, 2006,5:02:14 PM
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager.messaging.announcements;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.messaging.Announcement;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a><br>
 * <br>
 *         Created on Jun 20, 2006,5:02:14 PM
 * 
 */
@Mapping(module = "manager", path = "/announcements/announcementBoardsManagement", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "chooseBoardType", path = "/manager/announcements/chooseBoardType.jsp", tileProperties = @Tile(
                navLocal = "/manager/announcements/menu.jsp")),
        @Forward(name = "createUnitAnnouncementBoard", path = "/messaging/announcements/createUnitAnnouncementBoard.jsp",
                tileProperties = @Tile(navLocal = "/manager/announcements/menu.jsp")),
        @Forward(name = "firstPage", path = "/manager/announcements/firstPage.jsp", tileProperties = @Tile(
                navLocal = "/manager/announcements/menu.jsp")),
        @Forward(name = "statistics", path = "/manager/announcements/statistics.jsp", tileProperties = @Tile(
                navLocal = "/manager/announcements/menu.jsp")) })
public class AnnouncementBoardsManagement extends FenixDispatchAction {

    public ActionForward stats(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        if (!getLoggedPerson(request).hasRole(RoleType.MANAGER)) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.not.allowed.to.view.board.statistics"));
            saveErrors(request, actionMessages);
            return this.start(mapping, actionForm, request, response);
        }
        int boardsCount = 0;
        int announcementsCount = 0;

        int visibleAnnouncementsCount = 0;
        int visibleNotExpiredAnnouncementsCount = 0;
        int visibleExpiredAnnouncementsCount = 0;
        int invisibleAnnouncementsCount = 0;
        int invisibleNotExpiredAnnouncementsCount = 0;
        int invisibleExpiredAnnouncementsCount = 0;

        for (final Content content : rootDomainObject.getContentsSet()) {
            if (content.isAnAnnouncementBoard()) {
                boardsCount++;
            }

            if (content.isAnAnnouncement()) {
                final Announcement announcement = (Announcement) content;
                announcementsCount++;

                if (!announcement.getVisible()) {
                    invisibleAnnouncementsCount++;
                    if (announcement.getPublicationEnd() == null || announcement.getPublicationEnd().isAfterNow()) {
                        invisibleNotExpiredAnnouncementsCount++;
                    } else {
                        invisibleExpiredAnnouncementsCount++;
                    }
                } else {
                    visibleAnnouncementsCount++;
                    if (announcement.getPublicationEnd() == null || announcement.getPublicationEnd().isAfterNow()) {
                        visibleNotExpiredAnnouncementsCount++;
                    } else {
                        visibleExpiredAnnouncementsCount++;
                    }
                }
            }
        }

        request.setAttribute("boardsCount", boardsCount);
        request.setAttribute("announcementsCount", announcementsCount);
        request.setAttribute("visibleAnnouncementsCount", visibleAnnouncementsCount);
        request.setAttribute("visibleNotExpiredAnnouncementsCount", visibleNotExpiredAnnouncementsCount);
        request.setAttribute("visibleExpiredAnnouncementsCount", visibleExpiredAnnouncementsCount);
        request.setAttribute("invisibleAnnouncementsCount", invisibleAnnouncementsCount);
        request.setAttribute("invisibleNotExpiredAnnouncementsCount", invisibleNotExpiredAnnouncementsCount);
        request.setAttribute("invisibleExpiredAnnouncementsCount", invisibleExpiredAnnouncementsCount);

        return mapping.findForward("statistics");
    }

    public ActionForward start(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        return mapping.findForward("firstPage");
    }

    public ActionForward chooseBoardType(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("chooseBoardType");
    }

    public ActionForward createExecutionCourseAnnouncementBoard(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("chooseBoardType");
    }
}
