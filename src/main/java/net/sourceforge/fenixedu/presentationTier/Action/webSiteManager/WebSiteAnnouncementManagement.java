/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.webSiteManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.UnitBoardPermittedGroupType;
import net.sourceforge.fenixedu.domain.messaging.Announcement;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import net.sourceforge.fenixedu.domain.messaging.UnitAnnouncementBoard;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.AnnouncementManagement;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt"> Goncalo Luiz</a><br/>
 *         Created on Jun 8, 2006, 2:28:29 PM
 * 
 */
@Mapping(module = "webSiteManager", path = "/announcementsManagement", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "viewAnnouncement", path = "websiteManager-view-announcement"),
        @Forward(name = "uploadFile", path = "websiteManager-uploadFile"),
        @Forward(name = "viewAnnouncementBoard", path = "websiteManager-view-announcementBoard"),
        @Forward(name = "unitStructuredBoards", path = "unit-structured-boards"),
        @Forward(name = "edit", path = "websiteManager-edit-announcement"),
        @Forward(name = "listAnnouncements", path = "websiteManager-list-announcements"),
        @Forward(name = "add", path = "websiteManager-add-announcement"),
        @Forward(name = "editStickies", path = "websiteManager-editStickies"),
        @Forward(name = "editFile", path = "websiteManager-editFile"),
        @Forward(name = "listAnnouncementBoards", path = "websiteManager-list-announcement-boards") })
public class WebSiteAnnouncementManagement extends AnnouncementManagement {

    private static final int UP = -1;

    private static final int DOWN = 1;

    @Override
    public ActionForward start(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        if (getAnnouncementBoardId(request) == null) {
            return super.start(mapping, actionForm, request, response);
        } else {
            return this.viewAnnouncementBoard(mapping, actionForm, request, response);
        }
    }

    public ActionForward viewAnnouncementBoard(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        super.viewAllBoards(mapping, actionForm, request, response);
        request.setAttribute("announcementBoard", this.getRequestedAnnouncementBoard(request));
        return mapping.findForward("viewAnnouncementBoard");
    }

    public ActionForward listAnnouncements(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        super.viewAnnouncements(mapping, form, request, response);
        super.viewAllBoards(mapping, form, request, response);
        getSortByParameter(request);
        return mapping.findForward("listAnnouncements");
    }

    private void getSortByParameter(HttpServletRequest request) {
        if (request.getParameter("sortBy") == null) {
            request.setAttribute("sortBy", "creationDate=descending");
        } else {
            request.setAttribute("sortBy", request.getParameter("sortBy"));
        }
    }

    @Override
    public ActionForward viewAnnouncement(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        this.viewAllBoards(mapping, actionForm, request, response);
        return super.viewAnnouncement(mapping, actionForm, request, response);
    }

    @Override
    public ActionForward viewAnnouncements(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        super.viewAnnouncements(mapping, form, request, response);
        super.viewAllBoards(mapping, form, request, response);
        return mapping.findForward("viewAnnouncementBoard");
    }

    @Override
    public ActionForward addAnnouncement(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        super.viewAllBoards(mapping, form, request, response);
        // try{
        return super.addAnnouncement(mapping, form, request, response);
        // } catch (ConsistencyException consistentcyException) {
        // ActionMessages actionMessages = new ActionMessages();
        // actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
        // "error.cannot.create.sticky.announcement.noPublicationBegin"));
        // saveErrors(request, actionMessages);
        // return this.start(mapping, form, request, response);
        // }
    }

    @Override
    protected String getContextInformation(ActionMapping mapping, HttpServletRequest request) {
        return "/announcementsManagement.do";
    }

    @Override
    protected Collection<AnnouncementBoard> boardsToView(HttpServletRequest request) throws Exception {
        final Collection<AnnouncementBoard> boards = new ArrayList<AnnouncementBoard>();
        for (final AnnouncementBoard currentBoard : rootDomainObject.getInstitutionUnit().getBoards()) {
            final UnitAnnouncementBoard board = (UnitAnnouncementBoard) currentBoard;
            if (board.getUnitPermittedWriteGroupType() == UnitBoardPermittedGroupType.UB_WEBSITE_MANAGER
                    && board.getWriters().isMember(this.getLoggedPerson(request))) {
                boards.add(board);
            }
        }
        return boards;
    }

    @Override
    protected String getExtraRequestParameters(HttpServletRequest request) {
        return "tabularVersion=true";
    }

    @Override
    public ActionForward viewArchive(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        getSortByParameter(request);
        return super.viewArchive(mapping, form, request, response);
    }

    @Override
    public ActionForward deleteAnnouncement(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        deleteAnnouncement(request);
        return this.listAnnouncements(mapping, form, request, response);
    }

    /**
     * Entry point for Sticky Announcement Management
     * 
     * Priority Re-ordering using Ajax drag-and-drop
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward showStickyAnnoucements(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        AnnouncementBoard board = this.getRequestedAnnouncementBoard(request);
        if (checkConditions(mapping, form, request, response) != null) {
            return super.start(mapping, form, request, response);
        }

        RenderUtils.invalidateViewState();
        super.viewAllBoards(mapping, form, request, response);
        request.setAttribute("alterOrder", "alterOrder");
        request.setAttribute("announcementBoard", board);
        request.setAttribute("announcements", getOrderedStickies(board, request));
        return mapping.findForward("editStickies");
    }

    public ActionForward up(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        AnnouncementBoard board = this.getRequestedAnnouncementBoard(request);
        if (checkConditions(mapping, form, request, response) != null) {
            return super.start(mapping, form, request, response);
        }

        changeOrder(board, request, UP);
        return showStickyAnnoucements(mapping, form, request, response);
    }

    public ActionForward down(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        AnnouncementBoard board = this.getRequestedAnnouncementBoard(request);
        if (checkConditions(mapping, form, request, response) != null) {
            return super.start(mapping, form, request, response);
        }

        changeOrder(board, request, DOWN);
        return showStickyAnnoucements(mapping, form, request, response);
    }

    public ActionForward alterOrder(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        request.setAttribute("alterOrder", "alterOrder");
        return showStickyAnnoucements(mapping, form, request, response);
    }

    private void changeOrder(AnnouncementBoard board, HttpServletRequest request, int direction) throws FenixServiceException {
        String oid = request.getParameter("oid");

        IUserView userView = getUserView(request);

        Announcement announcement = FenixFramework.getDomainObject(oid);
        List<Announcement> orderedAnnouncements = getOrderedStickies(board, request);

        int index = orderedAnnouncements.indexOf(announcement);
        if (index + direction >= 0) {
            orderedAnnouncements.remove(announcement);
            if (index + direction > orderedAnnouncements.size()) {
                orderedAnnouncements.add(index, announcement);
            } else {
                orderedAnnouncements.add(index + direction, announcement);
            }
            updateAnnouncementsAccordingToList(orderedAnnouncements);
        }
    }

    private void updateAnnouncementsAccordingToList(List<Announcement> orderedAnnouncements) {
        for (int i = 0; i < orderedAnnouncements.size(); i++) {
            orderedAnnouncements.get(i).updatePriority(i + 1);
        }
    }

    public ActionForward changeOrderUsingAjaxTree(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String treeStructure = (String) getFromRequest(request, "tree");
        AnnouncementBoard board = this.getRequestedAnnouncementBoard(request);
        request.setAttribute("announcementBoard", board);
        // if (checkConditions(mapping, form, request, response) != null) {
        // return super.start(mapping, form, request, response);
        // }
        List<Announcement> newAnnouncementsOrder = reOrderAnnouncements(treeStructure, getOrderedStickies(board, request));

        updateAnnouncementsAccordingToList(newAnnouncementsOrder);

        return showStickyAnnoucements(mapping, form, request, response);
    }

    private List<Announcement> reOrderAnnouncements(String treeStructure, List<Announcement> oldOrder) {
        List<Announcement> newAnnouncementsOrder = new ArrayList<Announcement>();
        List<Announcement> oldAnnouncementsOrder = oldOrder;

        String[] nodes = treeStructure.split(",");

        for (String node : nodes) {
            String[] parts = node.split("-");

            Integer index = getId(parts[0]) - 1;
            Announcement announcement = oldAnnouncementsOrder.get(index);
            newAnnouncementsOrder.add(announcement);
        }
        return newAnnouncementsOrder;
    }

    private Integer getId(String id) {
        if (id == null) {
            return null;
        }
        try {
            return new Integer(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<Announcement> getOrderedStickies(AnnouncementBoard board, HttpServletRequest request)
            throws FenixServiceException {
        return super.getStickyAnnouncements(board, request);

    }

}
