/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.messaging;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.DeleteFileContent;
import net.sourceforge.fenixedu.applicationTier.Servico.messaging.AddAnnouncementBoardBookmark;
import net.sourceforge.fenixedu.applicationTier.Servico.messaging.AproveActionAnnouncement;
import net.sourceforge.fenixedu.applicationTier.Servico.messaging.CreateFileContentForBoard;
import net.sourceforge.fenixedu.applicationTier.Servico.messaging.DeleteAnnouncement;
import net.sourceforge.fenixedu.applicationTier.Servico.messaging.RemoveAnnouncementBoardBookmark;
import net.sourceforge.fenixedu.domain.FileContent;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.messaging.Announcement;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import net.sourceforge.fenixedu.domain.messaging.UnitAnnouncementBoard;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.manager.FileContentCreationBean;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.announcements.dto.AnnouncementArchive;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.announcements.dto.AnnouncementArchiveAnnouncementsVisibility;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.portal.EntryPoint;
import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.FileUtils;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt"> Goncalo Luiz</a><br/>
 *         Created on May 30, 2006, 12:21:56 PM
 * 
 */
public abstract class AnnouncementManagement extends FenixDispatchAction {

    @EntryPoint
    public ActionForward start(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("announcementBoard", this.getRequestedAnnouncementBoard(request));
        return this.viewAllBoards(mapping, actionForm, request, response);
    }

    public ActionForward viewAnnouncement(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Announcement announcement = this.getRequestedAnnouncement(request);
        request.setAttribute("announcement", (announcement != null && announcement.getVisible()) ? announcement : null);

        return mapping.findForward("viewAnnouncement");
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        request.setAttribute("contextPrefix", getContextPrefix(mapping, request));
        request.setAttribute("extraParameters", getExtraRequestParameters(request));
        AnnouncementBoard requestedAnnouncementBoard = getRequestedAnnouncementBoard(request);

        if (requestedAnnouncementBoard != null && requestedAnnouncementBoard instanceof UnitAnnouncementBoard) {
            request.setAttribute("site", ((UnitAnnouncementBoard) requestedAnnouncementBoard).getUnit().getSite());
        }
        final Person person = getLoggedPerson(request);
        if (person != null) {
            request.setAttribute("person", person);
        }
        return super.execute(mapping, actionForm, request, response);
    }

    protected String getAnnouncementBoardId(HttpServletRequest request) {
        return request.getParameter("announcementBoardId");
    }

    protected String getAnnouncementId(HttpServletRequest request) {
        return request.getParameter("announcementId");
    }

    protected AnnouncementBoard getRequestedAnnouncementBoard(HttpServletRequest request) {
        String id = this.getAnnouncementBoardId(request);
        return id != null ? (AnnouncementBoard) FenixFramework.getDomainObject(id) : null;
    }

    protected Announcement getRequestedAnnouncement(HttpServletRequest request) {
        String id = this.getAnnouncementId(request);
        DomainObject obj = FenixFramework.getDomainObject(id);
        return obj instanceof Announcement ? (Announcement) obj : null;
    }

    public ActionForward addBookmark(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        createBookmark(request);
        return this.start(mapping, form, request, response);
    }

    protected void createBookmark(HttpServletRequest request) throws FenixServiceException {
        final User userView = getUserView(request);
        final AnnouncementBoard board = this.getRequestedAnnouncementBoard(request);
        AddAnnouncementBoardBookmark.run(board, userView.getPerson());
    }

    public ActionForward removeBookmark(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        removeBookmark(request);
        return this.start(mapping, form, request, response);
    }

    protected void removeBookmark(HttpServletRequest request) throws FenixServiceException {
        User userView = getUserView(request);
        AnnouncementBoard board = this.getRequestedAnnouncementBoard(request);
        RemoveAnnouncementBoardBookmark.run(board, userView.getPerson());
    }

    public ActionForward addAnnouncement(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        AnnouncementBoard board = this.getRequestedAnnouncementBoard(request);
        if (board.getWriters() != null && !board.getWriters().isMember(Authenticate.getUser())) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.not.allowed.to.write.board"));
            saveErrors(request, actionMessages);
            return this.start(mapping, form, request, response);
        }
        request.setAttribute("announcementBoard", board);
        return mapping.findForward("add");
    }

    public ActionForward viewAnnouncements(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        AnnouncementBoard board = this.getRequestedAnnouncementBoard(request);
        if (board == null) {
            return this.start(mapping, form, request, response);
        }
        if (!board.hasReader(getLoggedPerson(request))) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.not.allowed.to.read.board"));
            saveErrors(request, actionMessages);
            return this.start(mapping, form, request, response);
        }
        request.setAttribute("announcementBoard", board);
        request.setAttribute("announcements", this.getThisMonthAnnouncements(board, request));
        request.setAttribute("archive", this.buildArchive(board, request));
        return mapping.findForward("listAnnouncements");
    }

    protected ActionForward checkConditions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        AnnouncementBoard board = this.getRequestedAnnouncementBoard(request);
        if (board == null) {
            return this.start(mapping, form, request, response);
        }
        if (!board.hasReader(getLoggedPerson(request))) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.not.allowed.to.read.board"));
            saveErrors(request, actionMessages);
            return this.start(mapping, form, request, response);
        }
        return null;
    }

    protected AnnouncementArchive buildArchive(AnnouncementBoard board, HttpServletRequest request) {
        AnnouncementArchive archive =
                new AnnouncementArchive(
                        board,
                        board.hasWriter(getLoggedPerson(request)) ? AnnouncementArchiveAnnouncementsVisibility.ALL : AnnouncementArchiveAnnouncementsVisibility.ACTIVE);
        return archive;
    }

    protected Collection<Announcement> getThisMonthAnnouncements(AnnouncementBoard board, HttpServletRequest request) {

        final List<Announcement> announcements =
                board.hasWriter(getLoggedPerson(request)) ? new ArrayList<Announcement>(board.getAnnouncementSet()) : board
                        .getApprovedAnnouncements();

        List<Announcement> thisMonthAnnouncements = getThisMonthAnnouncements(announcements);
        if (thisMonthAnnouncements.isEmpty()) {
            thisMonthAnnouncements = getLastSixAnnouncements(announcements);
        }
        Collections.sort(thisMonthAnnouncements, Announcement.NEWEST_FIRST);
        return thisMonthAnnouncements;
    }

    private List<Announcement> getLastSixAnnouncements(List<Announcement> announcements) {
        return announcements.subList(0, Math.min(6, announcements.size()));
    }

    private List<Announcement> getThisMonthAnnouncements(Collection<Announcement> announcements) {
        final DateTime now = new DateTime();
        final List<Announcement> result = new ArrayList<Announcement>();
        for (final Announcement announcement : announcements) {
            if (announcement.hasCreationDateFor(now.getYear(), now.getMonthOfYear())) {
                result.add(announcement);
            }
        }
        return result;
    }

    protected List<Announcement> getStickyAnnouncements(AnnouncementBoard board, HttpServletRequest request) {

        final List<Announcement> announcements =
                board.hasWriter(getLoggedPerson(request)) ? new ArrayList<Announcement>(board.getAnnouncementSet()) : board
                        .getApprovedAnnouncements();

        List<Announcement> stickies = filterStickies(announcements);

        Collections.sort(stickies, Announcement.PRIORITY_FIRST);
        return stickies;
    }

    private List<Announcement> filterStickies(Collection<Announcement> announcements) {
        List<Announcement> stickies = new ArrayList<Announcement>();

        for (Announcement annoucement : announcements) {
            if (annoucement.getSticky() != null && annoucement.getSticky()) {
                stickies.add(annoucement);
            }
        }
        return stickies;
    }

    public ActionForward editAnnouncement(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        Announcement announcement = this.getRequestedAnnouncement(request);
        if (announcement.getAnnouncementBoard().getWriters() != null
                && !announcement.getAnnouncementBoard().getWriters().isMember(Authenticate.getUser())) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.not.allowed.to.write.board"));
            saveErrors(request, actionMessages);
            return this.start(mapping, form, request, response);
        }
        request.setAttribute("announcementBoard", announcement.getAnnouncementBoard());
        request.setAttribute("announcement", announcement);
        request.setAttribute("extraParameters", getExtraRequestParameters(request));
        return mapping.findForward("edit");
    }

    public ActionForward deleteAnnouncement(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        if (!deleteAnnouncement(request)) {
            return this.start(mapping, form, request, response);
        }
        return this.viewAnnouncements(mapping, form, request, response);
    }

    protected boolean deleteAnnouncement(HttpServletRequest request) throws FenixServiceException {
        User userView = getUserView(request);
        final Announcement announcement = getRequestedAnnouncement(request);
        if (!announcement.getAnnouncementBoard().hasWriter(getLoggedPerson(request))) {
            addActionMessage(request, "error.not.allowed.to.write.board");
            return false;
        }
        DeleteAnnouncement.run(announcement);
        return true;
    }

    public ActionForward aproveAction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        if (!aproveAction(request)) {
            return this.start(mapping, form, request, response);
        }
        return this.viewAnnouncements(mapping, form, request, response);
    }

    protected boolean aproveAction(HttpServletRequest request) throws FenixServiceException {
        User userView = getUserView(request);
        final Announcement announcement = getRequestedAnnouncement(request);
        final Boolean action = Boolean.valueOf(request.getParameter("action"));
        if (!announcement.getAnnouncementBoard().hasApprover(getLoggedPerson(request))) {
            addActionMessage(request, "error.not.allowed.to.approve.board");
            return false;
        }
        AproveActionAnnouncement.run(announcement, action);
        return true;
    }

    private String getContextPrefix(ActionMapping mapping, HttpServletRequest request) {
        String contextPrefix = getContextInformation(mapping, request);

        if (contextPrefix.contains("?")) {
            contextPrefix += "&amp;";
        } else {
            contextPrefix += "?";
        }

        return contextPrefix;
    }

    public ActionForward viewAllBoards(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("announcementBoards", this.boardsToView(request));
        return mapping.findForward("listAnnouncementBoards");
    }

    protected Integer getSelectedArchiveMonth(HttpServletRequest request) {
        return Integer.valueOf(request.getParameter("selectedMonth"));
    }

    protected Integer getSelectedArchiveYear(HttpServletRequest request) {
        return Integer.valueOf(request.getParameter("selectedYear"));
    }

    public ActionForward viewArchive(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Integer selectedArchiveYear = this.getSelectedArchiveYear(request);
        Integer selectedArchiveMonth = this.getSelectedArchiveMonth(request);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, selectedArchiveMonth - 1);
        calendar.set(Calendar.YEAR, selectedArchiveYear);
        request.setAttribute("archiveDate", calendar.getTime());

        AnnouncementArchive archive = this.buildArchive(this.getRequestedAnnouncementBoard(request), request);
        this.viewAnnouncements(mapping, form, request, response);
        request.setAttribute("announcements",
                archive.getEntries().get(selectedArchiveYear).getEntries().get((selectedArchiveMonth)).getAnnouncements());

        return mapping.findForward("listAnnouncements");
    }

    public ActionForward prepareAddFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        AnnouncementBoard board = getRequestedAnnouncementBoard(request);
        FileContentCreationBean bean = new FileContentCreationBean(board, null);

        bean.setAuthorsName(getLoggedPerson(request).getName());
        request.setAttribute("bean", bean);

        return mapping.findForward("uploadFile");
    }

    public ActionForward fileUpload(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        FileContentCreationBean bean = (FileContentCreationBean) RenderUtils.getViewState("creator").getMetaObject().getObject();
        RenderUtils.invalidateViewState();

        InputStream formFileInputStream = null;
        File file = null;
        try {
            formFileInputStream = bean.getFile();
            file = FileUtils.copyToTemporaryFile(formFileInputStream);

            CreateFileContentForBoard.runCreateFileContentForBoard(bean.getFileHolder(), file, bean.getFileName(),
                    bean.getDisplayName(), bean.getPermittedGroup(), getLoggedPerson(request));
        } catch (DomainException e) {
            addErrorMessage(request, "board", e.getKey(), (Object[]) e.getArgs());
        } finally {
            if (formFileInputStream != null) {
                formFileInputStream.close();
            }
            if (file != null) {
                file.delete();
            }
        }

        return prepareAddFile(mapping, form, request, response);
    }

    public ActionForward deleteFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        FileContent fileContent = getFileContent(request);

        DeleteFileContent.runDeleteFileContent(fileContent);
        return prepareAddFile(mapping, form, request, response);
    }

    public ActionForward editFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        AnnouncementBoard board = getRequestedAnnouncementBoard(request);
        FileContent fileContent = getFileContent(request);

        request.setAttribute("board", board);
        request.setAttribute("fileContent", fileContent);

        return mapping.findForward("editFile");
    }

    private FileContent getFileContent(HttpServletRequest request) {
        FileContent fileContent = (FileContent) FenixFramework.getDomainObject(request.getParameter("fileId"));
        return fileContent;
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
     * @param mapping
     *            TODO
     * 
     * 
     * 
     * @return
     */
    protected abstract String getContextInformation(ActionMapping mapping, HttpServletRequest request);

    /**
     * This method should return all the boards to show. Example: <code><br>
     * Party istUnit = UnitUtils.readUnitWithoutParentstByName(UnitUtils.IST_UNIT_NAME);<br>
     * return istUnit.getBoards());<br>
     * </code>
     * 
     * @param request
     * @return
     */
    protected abstract Collection<AnnouncementBoard> boardsToView(HttpServletRequest request) throws Exception;

}