package net.sourceforge.fenixedu.presentationTier.Action.publico.rss;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.messaging.ExecutionCourseAnnouncementBoard;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class GenerateAnnoucementsRSS extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	final String executionCourseIdString = request.getParameter("id");
	final Integer executionCourseId = Integer.valueOf(executionCourseIdString);
	final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseId);
	if (executionCourse == null) {
	    return forward("/publico/notFound.do");
	}
	final ExecutionCourseAnnouncementBoard announcementBoard = executionCourse.getBoard();
	if (announcementBoard == null) {
	    return forward("/publico/executionCourse.do?method=notFound&executionCourseID=" + executionCourse.getIdInternal());
	}
	return forward("/external/announcementsRSS.do?announcementBoardId=" + announcementBoard.getIdInternal());
    }

    private ActionForward forward(String purl) {
	return new ActionForward(purl);
    }

}