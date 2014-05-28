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
 * Author : Goncalo Luiz
 * Creation Date: Jun 19, 2006,3:51:45 PM
 */
package net.sourceforge.fenixedu.presentationTier.Action.publico.messaging;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourseSite;
import net.sourceforge.fenixedu.domain.Site.SiteMapper;
import net.sourceforge.fenixedu.domain.cms.OldCmsSemanticURLHandler;
import net.sourceforge.fenixedu.domain.messaging.Announcement;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NotAuthorizedActionException;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.announcements.dto.AnnouncementArchive;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a><br>
 * <br>
 *         Created on Jun 19, 2006,3:51:45 PM
 * 
 */
@Mapping(module = "publico", path = "/announcementManagement", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "viewAnnouncement", path = "executionCoruse-view-announcement"),
        @Forward(name = "listAnnouncements", path = "public-list-announcements") })
public class ExecutionCoursePublicAnnouncementManagement extends PublicAnnouncementDispatchAction {

    protected String getRequestedExecutionCourseId(HttpServletRequest request) {

        final String executionCourseIDString = request.getParameter("executionCourseID");

        if (executionCourseIDString == null) {
            ExecutionCourseSite site = SiteMapper.getSite(request);
            return site.getSiteExecutionCourse().getExternalId();
        }

        return executionCourseIDString;
    }

    protected ExecutionCourse getRequestedExecutionCourse(HttpServletRequest request) {
        DomainObject obj = FenixFramework.getDomainObject(this.getRequestedExecutionCourseId(request));
        return obj instanceof ExecutionCourse ? (ExecutionCourse) obj : null;
    }

    @Override
    protected AnnouncementBoard getRequestedAnnouncementBoard(HttpServletRequest request) {
        return this.getRequestedExecutionCourse(request).getBoard();
    }

    @Override
    public ActionForward start(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return super.viewAnnouncements(mapping, actionForm, request, response);
    }

    @Override
    protected Collection<Announcement> getThisMonthAnnouncements(AnnouncementBoard board, HttpServletRequest request) {
        boolean useArchive = request.getParameter("ommitArchive") == null;
        if (useArchive) {
            return super.getThisMonthAnnouncements(board, request);
        } else {
            List<Announcement> announcements = new ArrayList<Announcement>(board.getAnnouncementSet());
            Collections.sort(announcements, Announcement.NEWEST_FIRST);

            return announcements;
        }
    }

    @Override
    protected AnnouncementArchive buildArchive(AnnouncementBoard board, HttpServletRequest request) {
        boolean useArchive = request.getParameter("ommitArchive") == null;
        if (useArchive) {
            return super.buildArchive(board, request);
        } else {
            return null;
        }
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ExecutionCourse course = getRequestedExecutionCourse(request);
        if (course == null) {
            response.setStatus(404);
            response.getWriter().print("Bad request");
            return null;
        } else {
            OldCmsSemanticURLHandler.selectSite(request, course.getSite());
        }
        request.setAttribute("executionCourse", course);
        return super.execute(mapping, actionForm, request, response);
    }

    @Override
    protected String getExtraRequestParameters(HttpServletRequest request) {
        return "executionCourseID=" + this.getRequestedExecutionCourseId(request);
    }

    @Override
    protected String getContextInformation(ActionMapping mapping, HttpServletRequest request) {
        return "/announcementManagement.do";

    }

    @Override
    protected Collection<AnnouncementBoard> boardsToView(HttpServletRequest request) throws Exception {
        Collection<AnnouncementBoard> boards = new ArrayList<AnnouncementBoard>(1);
        AnnouncementBoard board = this.getRequestedExecutionCourse(request).getBoard();
        if (board.getReaders() == null || (getUserView(request) != null && board.getReaders().isMember(getUserView(request)))) {
            boards.add(this.getRequestedExecutionCourse(request).getBoard());
        }
        return boards;
    }

    @Override
    public ActionForward editAnnouncement(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        throw new NotAuthorizedActionException("cannot edit announcement");
    }

    @Override
    public ActionForward deleteAnnouncement(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        throw new NotAuthorizedActionException("cannot delete announcement");
    }
}
