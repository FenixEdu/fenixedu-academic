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
	final ExecutionCourseAnnouncementBoard announcementBoard = executionCourse.getBoard();
	final ActionForward actionForward;
	if (announcementBoard == null) {
	    actionForward = new ActionForward("/publico/executionCourse.do?method=notFound&executionCourseID=" + executionCourse.getIdInternal());
	} else {
	    actionForward = new ActionForward("/external/announcementsRSS.do?announcementBoardId=" + announcementBoard.getIdInternal());
	}
	return actionForward;
    }

}