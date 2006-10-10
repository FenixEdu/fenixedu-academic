/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.messaging;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.messaging.Announcement;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.announcements.dto.AnnouncementArchive;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.announcements.dto.AnnouncementArchiveAnnouncementsVisibility;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.joda.time.DateTime;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt"> Goncalo Luiz</a><br/> Created
 *         on May 30, 2006, 12:21:56 PM
 * 
 */
public abstract class AnnouncementManagement extends FenixDispatchAction {

    public ActionForward start(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("announcementBoard", this.getRequestedAnnouncementBoard(request));
        return this.viewAllBoards(mapping, actionForm, request, response);
    }
    
    public ActionForward viewAnnouncement(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        Announcement announcement = this.getRequestedAnnouncement(request);
        request.setAttribute("announcement", announcement.getVisible() ? announcement : null);

        return mapping.findForward("viewAnnouncement");
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("contextPrefix", getContextPrefix(request));
        request.setAttribute("extraParameters", getExtraRequestParameters(request));
        request.setAttribute("person", this.getLoggedPerson(request));
        return super.execute(mapping, actionForm, request, response);
    }

    protected Integer getAnnouncementBoardId(HttpServletRequest request) {
        return request.getParameter("announcementBoardId") == null ? null : Integer.valueOf(request
                .getParameter("announcementBoardId"));
    }

    protected Integer getAnnouncementId(HttpServletRequest request) {
        return Integer.valueOf(request.getParameter("announcementId"));
    }

    protected AnnouncementBoard getRequestedAnnouncementBoard(HttpServletRequest request) {
        return rootDomainObject.readAnnouncementBoardByOID(this.getAnnouncementBoardId(request));
    }

    protected Announcement getRequestedAnnouncement(HttpServletRequest request) {
        return rootDomainObject.readAnnouncementByOID(this.getAnnouncementId(request));
    }

    public ActionForward addBookmark(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        createBookmark(request);
        return this.start(mapping, form, request, response);
    }

    protected void createBookmark(HttpServletRequest request) throws FenixServiceException, FenixFilterException {
	final IUserView userView = getUserView(request);
        final AnnouncementBoard board = this.getRequestedAnnouncementBoard(request);
        ServiceUtils.executeService(userView, "AddAnnouncementBoardBookmark", new Object[] { board,
                userView.getPerson() });
    }

    public ActionForward removeBookmark(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        removeBookmark(request);
        return this.start(mapping, form, request, response);
    }

    protected void removeBookmark(HttpServletRequest request) throws FenixServiceException, FenixFilterException {
	IUserView userView = getUserView(request);
        AnnouncementBoard board = this.getRequestedAnnouncementBoard(request);
        ServiceUtils.executeService(userView, "RemoveAnnouncementBoardBookmark", new Object[] { board,
                userView.getPerson() });
    }

    public ActionForward addAnnouncement(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        AnnouncementBoard board = this.getRequestedAnnouncementBoard(request);
        if (board.getWriters() != null && !board.getWriters().isMember(getLoggedPerson(request))) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                    "error.not.allowed.to.write.board"));
            saveErrors(request, actionMessages);
            return this.start(mapping, form, request, response);
        }
        request.setAttribute("announcementBoard", board);
        return mapping.findForward("add");
    }

    public ActionForward viewAnnouncements(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        AnnouncementBoard board = this.getRequestedAnnouncementBoard(request);
        if (board == null)
            return this.start(mapping, form, request, response);
        if (board.getReaders() != null && !board.getReaders().isMember(getLoggedPerson(request))) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                    "error.not.allowed.to.read.board"));
            saveErrors(request, actionMessages);
            return this.start(mapping, form, request, response);
        }
        request.setAttribute("announcementBoard", board);
        request.setAttribute("announcements", this.getThisMonthAnnouncements(board, request));
        request.setAttribute("archive", this.buildArchive(board, request));
        return mapping.findForward("listAnnouncements");
    }

    private AnnouncementArchive buildArchive(AnnouncementBoard board, HttpServletRequest request) {
	AnnouncementArchive archive = new AnnouncementArchive(
		board,
		board.hasWriter(getUserView(request).getPerson()) ? AnnouncementArchiveAnnouncementsVisibility.ALL
			: AnnouncementArchiveAnnouncementsVisibility.ACTIVE);
	return archive;
    }

    private Collection<Announcement> getThisMonthAnnouncements(AnnouncementBoard board,
            HttpServletRequest request) {
        Collection<Announcement> announcements = (board.getWriters() == null || board.getWriters()
                .allows(getUserView(request))) ? board.getAnnouncements() : board
                .getActiveAnnouncements();
        Collection<Announcement> thisMonthAnnouncements = new ArrayList<Announcement>();

        DateTime now = new DateTime();
        for (Announcement announcement : announcements) {
            if (announcement.getCreationDate().getMonthOfYear() == now.getMonthOfYear()
                    && announcement.getCreationDate().getYear() == now.getYear())
                thisMonthAnnouncements.add(announcement);
        }

        return thisMonthAnnouncements;
    }

    public ActionForward editAnnouncement(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        Announcement announcement = this.getRequestedAnnouncement(request);
        if (announcement.getAnnouncementBoard().getWriters() != null
                && !announcement.getAnnouncementBoard().getWriters().isMember(getLoggedPerson(request))) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                    "error.not.allowed.to.write.board"));
            saveErrors(request, actionMessages);
            return this.start(mapping, form, request, response);
        }
        request.setAttribute("announcementBoard", announcement.getAnnouncementBoard());
        request.setAttribute("announcement", announcement);
        request.setAttribute("extraParameters", getExtraRequestParameters(request));
        return mapping.findForward("edit");
    }

    public ActionForward deleteAnnouncement(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        IUserView userView = getUserView(request);
        Announcement announcement = this.getRequestedAnnouncement(request);
        if (announcement.getAnnouncementBoard().getWriters() != null
                && !announcement.getAnnouncementBoard().getWriters().isMember(getLoggedPerson(request))) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                    "error.not.allowed.to.write.board"));
            saveErrors(request, actionMessages);
            return this.start(mapping, form, request, response);
        }
        ServiceUtils.executeService(userView, "DeleteAnnouncement", new Object[] { announcement });

        return this.viewAnnouncements(mapping, form, request, response);
    }

    private String getContextPrefix(HttpServletRequest request) {
        String contextPrefix = getContextInformation(request);

        if (contextPrefix.contains("?")) {
            contextPrefix += "&";
        } else {
            contextPrefix += "?";
        }

        return contextPrefix;
    }

    public ActionForward viewAllBoards(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("announcementBoards", this.boardsToView(request));
        return mapping.findForward("listAnnouncementBoards");
    }

    private Integer getSelectedArchiveMonth(HttpServletRequest request) {
        return Integer.valueOf(request.getParameter("selectedMonth"));
    }

    private Integer getSelectedArchiveYear(HttpServletRequest request) {
        return Integer.valueOf(request.getParameter("selectedYear"));
    }

    public ActionForward viewArchive(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        AnnouncementArchive archive = this.buildArchive(this.getRequestedAnnouncementBoard(request),
                request);
        this.viewAnnouncements(mapping, form, request, response);
        request.setAttribute("announcements", archive.getEntries().get(
                this.getSelectedArchiveYear(request)).getEntries().get(
                (this.getSelectedArchiveMonth(request))).getAnnouncements());

        return mapping.findForward("listAnnouncements");
    }
    
    /**
     * Method to override in action specific context, to allow specification of
     * additional parameters regarding to action context. Example:
     * /actionXpto.do?someObjectID=1234
     * 
     * 
     * 
     * @return
     */
    protected abstract String getExtraRequestParameters(HttpServletRequest request);

    /**
     * This method must return any additional request parameters that may be
     * necessary
     * 
     * 
     * 
     * @return
     */
    protected abstract String getContextInformation(HttpServletRequest request);

    /**
     * This method should return all the boards to show. Example: <code><br>
     * Party istUnit = UnitUtils.readUnitWithoutParentstByName(UnitUtils.IST_UNIT_NAME);<br>
     * return istUnit.getBoards());<br>
     * </code>
     * 
     * @param request
     * @return
     */
    protected abstract Collection<AnnouncementBoard> boardsToView(HttpServletRequest request)
            throws Exception;

}
