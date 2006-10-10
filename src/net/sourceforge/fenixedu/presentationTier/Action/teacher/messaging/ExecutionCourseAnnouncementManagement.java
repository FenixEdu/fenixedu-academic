package net.sourceforge.fenixedu.presentationTier.Action.teacher.messaging;

import java.util.Collection;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.AnnouncementManagement;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ExecutionCourseAnnouncementManagement extends AnnouncementManagement {

    protected Integer getRequestedExecutionCourseId(HttpServletRequest request) {
        return Integer.valueOf(request.getParameter("objectCode"));
    }

    protected ExecutionCourse getRequestedExecutionCourse(HttpServletRequest request) {
        return RootDomainObject.getInstance().readExecutionCourseByOID(
                this.getRequestedExecutionCourseId(request));
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final SiteView siteView = (SiteView) ServiceUtils.executeService(getUserView(request), "ReadCourseInformation",
                new Object[] { this.getRequestedExecutionCourseId(request) });
        request.setAttribute("siteView", siteView);
        return super.execute(mapping, actionForm, request, response);
    }

    @Override
    protected AnnouncementBoard getRequestedAnnouncementBoard(HttpServletRequest request) {
        return rootDomainObject.readExecutionCourseByOID(this.getRequestedExecutionCourseId(request)).getBoard();
    }

    @Override
    public ActionForward start(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {        
        return super.viewAnnouncements(mapping, actionForm, request, response);
    }

    @Override
    protected String getExtraRequestParameters(HttpServletRequest request) {
        return "objectCode=" + this.getRequestedExecutionCourseId(request);
    }

    @Override
    protected String getContextInformation(HttpServletRequest request) {
        return "/announcementManagement.do";

    }

    @Override
    protected Collection<AnnouncementBoard> boardsToView(HttpServletRequest request) throws Exception {
	final AnnouncementBoard board = this.getRequestedAnnouncementBoard(request);
	return board.hasWriter(getUserView(request).getPerson()) ? Collections.singletonList(board) : Collections.EMPTY_LIST;
    }

}
