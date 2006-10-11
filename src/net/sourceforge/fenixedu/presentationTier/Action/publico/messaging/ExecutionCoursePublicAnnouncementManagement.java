/**
 * Author : Goncalo Luiz
 * Creation Date: Jun 19, 2006,3:51:45 PM
 */
package net.sourceforge.fenixedu.presentationTier.Action.publico.messaging;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NotAuthorizedActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a><br>
 *         <br>
 *         Created on Jun 19, 2006,3:51:45 PM
 * 
 */
public class ExecutionCoursePublicAnnouncementManagement extends PublicAnnouncementDispatchAction {

    protected Integer getRequestedExecutionCourseId(HttpServletRequest request) {
	return Integer.valueOf(request.getParameter("executionCourseID"));
    }

    protected ExecutionCourse getRequestedExecutionCourse(HttpServletRequest request) {
	return RootDomainObject.getInstance().readExecutionCourseByOID(
		this.getRequestedExecutionCourseId(request));
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
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	ExecutionCourse course = getRequestedExecutionCourse(request);
	request.setAttribute("executionCourse", course);
	return super.execute(mapping, actionForm, request, response);
    }

    @Override
    protected String getExtraRequestParameters(HttpServletRequest request) {
	return "executionCourseID=" + this.getRequestedExecutionCourseId(request);
    }

    @Override
    protected String getContextInformation(HttpServletRequest request) {
	return "/announcementManagement.do";

    }

    @Override
    protected Collection<AnnouncementBoard> boardsToView(HttpServletRequest request) throws Exception {
	Collection<AnnouncementBoard> boards = new ArrayList<AnnouncementBoard>(1);
	AnnouncementBoard board = this.getRequestedExecutionCourse(request).getBoard();
	if (board.getReaders() == null
		|| (getUserView(request) != null && board.getReaders().allows(getUserView(request)))) {
	    boards.add(this.getRequestedExecutionCourse(request).getBoard());
	}
	return boards;
    }

    public ActionForward editAnnouncement(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	throw new NotAuthorizedActionException("cannot edit announcement");
    }

    public ActionForward deleteAnnouncement(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	throw new NotAuthorizedActionException("cannot delete announcement");
    }
}
