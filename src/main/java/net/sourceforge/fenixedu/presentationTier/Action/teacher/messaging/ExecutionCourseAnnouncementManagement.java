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
package net.sourceforge.fenixedu.presentationTier.Action.teacher.messaging;

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
import net.sourceforge.fenixedu.applicationTier.Servico.messaging.CreateFileContentForBoard;
import net.sourceforge.fenixedu.applicationTier.Servico.messaging.DeleteAnnouncement;
import net.sourceforge.fenixedu.domain.FileContent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.messaging.Announcement;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import net.sourceforge.fenixedu.presentationTier.Action.manager.FileContentCreationBean;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.announcements.dto.AnnouncementArchive;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.announcements.dto.AnnouncementArchiveAnnouncementsVisibility;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.ManageExecutionCourseDA;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.executionCourse.ExecutionCourseBaseAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.fenixedu.bennu.core.security.Authenticate;
import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.FileUtils;

@Mapping(module = "teacher", path = "/announcementManagement", functionality = ManageExecutionCourseDA.class)
@Forwards({ @Forward(name = "viewAnnouncement", path = "teacher-view-announcement"),
        @Forward(name = "edit", path = "teacher-edit-announcement"), @Forward(name = "add", path = "teacher-add-announcement") })
public class ExecutionCourseAnnouncementManagement extends ExecutionCourseBaseAction {

    public ActionForward viewAnnouncements(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        AnnouncementBoard board = this.getRequestedAnnouncementBoard(request);
        if (!board.hasReader(getLoggedPerson(request))) {
            throw new RuntimeException("User is now allowed to read this board!");
        }
        request.setAttribute("announcementBoard", board);
        request.setAttribute("announcements", this.getThisMonthAnnouncements(board, request));
        request.setAttribute("archive", this.buildArchive(board, request));
        return forward(request, "/teacher/executionCourse/announcements/listBoardAnnouncements.jsp");
    }

    public ActionForward viewAnnouncement(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Announcement announcement = getDomainObject(request, "announcementId");
        request.setAttribute("announcement", (announcement != null && announcement.getVisible()) ? announcement : null);

        return forward(request, "/teacher/executionCourse/announcements/viewAnnouncement.jsp");
    }

    public ActionForward addAnnouncement(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        AnnouncementBoard board = this.getRequestedAnnouncementBoard(request);
        if (board.getWriters() != null && !board.getWriters().isMember(Authenticate.getUser())) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.not.allowed.to.write.board"));
            saveErrors(request, actionMessages);
            return this.viewAnnouncements(mapping, form, request, response);
        }
        request.setAttribute("announcementBoard", board);
        return forward(request, "/teacher/executionCourse/announcements/addAnnouncement.jsp");
    }

    public ActionForward deleteAnnouncement(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        if (!deleteAnnouncement(request)) {
            return this.viewAnnouncements(mapping, form, request, response);
        }
        return this.viewAnnouncements(mapping, form, request, response);
    }

    protected boolean deleteAnnouncement(HttpServletRequest request) throws FenixServiceException {
        final Announcement announcement = getDomainObject(request, "announcementId");
        if (!announcement.getAnnouncementBoard().hasWriter(getLoggedPerson(request))) {
            addActionMessage(request, "error.not.allowed.to.write.board");
            return false;
        }
        DeleteAnnouncement.run(announcement);
        return true;
    }

    public ActionForward editAnnouncement(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        Announcement announcement = getDomainObject(request, "announcementId");
        if (announcement.getAnnouncementBoard().getWriters() != null
                && !announcement.getAnnouncementBoard().getWriters().isMember(Authenticate.getUser())) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.not.allowed.to.write.board"));
            saveErrors(request, actionMessages);
            return this.viewAnnouncements(mapping, form, request, response);
        }
        request.setAttribute("announcementBoard", announcement.getAnnouncementBoard());
        request.setAttribute("announcement", announcement);
        return forward(request, "/teacher/executionCourse/announcements/editAnnouncement.jsp");
    }

    public ActionForward prepareAddFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        request.setAttribute("contextPrefix", "/announcementManagement.do?");
        request.setAttribute("extraParameters", "executionCourseID=" + getExecutionCourse(request).getExternalId());

        AnnouncementBoard board = getRequestedAnnouncementBoard(request);
        FileContentCreationBean bean = new FileContentCreationBean(board, null);

        bean.setAuthorsName(getLoggedPerson(request).getName());
        request.setAttribute("bean", bean);

        return forward(request, "/messaging/announcements/uploadFileToBoard.jsp");
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

        deleteIt(fileContent);

        return prepareAddFile(mapping, form, request, response);
    }

    @Atomic
    private void deleteIt(FileContent fileContent) {
        fileContent.delete();
    }

    public ActionForward editFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        AnnouncementBoard board = getRequestedAnnouncementBoard(request);
        FileContent fileContent = getFileContent(request);

        request.setAttribute("board", board);
        request.setAttribute("fileContent", fileContent);

        request.setAttribute("contextPrefix", "/announcementManagement.do?");
        request.setAttribute("extraParameters", "executionCourseID=" + getExecutionCourse(request).getExternalId());

        return forward(request, "/messaging/announcements/editFileInBoard.jsp");
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

        return forward(request, "/teacher/executionCourse/announcements/listBoardAnnouncements.jsp");
    }

    protected Integer getSelectedArchiveMonth(HttpServletRequest request) {
        return Integer.valueOf(request.getParameter("selectedMonth"));
    }

    protected Integer getSelectedArchiveYear(HttpServletRequest request) {
        return Integer.valueOf(request.getParameter("selectedYear"));
    }

    private FileContent getFileContent(HttpServletRequest request) {
        FileContent fileContent = (FileContent) FenixFramework.getDomainObject(request.getParameter("fileId"));
        return fileContent;
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

    protected AnnouncementBoard getRequestedAnnouncementBoard(HttpServletRequest request) {
        return getExecutionCourse(request).getBoard();
    }

    protected Collection<AnnouncementBoard> boardsToView(HttpServletRequest request) throws Exception {
        final AnnouncementBoard board = this.getRequestedAnnouncementBoard(request);
        return board.hasWriter(getUserView(request).getPerson()) ? Collections.singletonList(board) : Collections
                .<AnnouncementBoard> emptyList();
    }

    protected AnnouncementArchive buildArchive(AnnouncementBoard board, HttpServletRequest request) {
        AnnouncementArchive archive =
                new AnnouncementArchive(
                        board,
                        board.hasWriter(getLoggedPerson(request)) ? AnnouncementArchiveAnnouncementsVisibility.ALL : AnnouncementArchiveAnnouncementsVisibility.ACTIVE);
        return archive;
    }

}
