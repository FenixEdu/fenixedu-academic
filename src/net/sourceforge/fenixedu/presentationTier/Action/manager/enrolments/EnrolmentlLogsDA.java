package net.sourceforge.fenixedu.presentationTier.Action.manager.enrolments;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.enrolmentLog.SearchEnrolmentLog;
import net.sourceforge.fenixedu.domain.log.EnrolmentLog;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class EnrolmentlLogsDA extends FenixDispatchAction {

    public ActionForward prepareViewEnrolmentLogs(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	SearchEnrolmentLog searchEnrolmentLog = new SearchEnrolmentLog();
	request.setAttribute("bean", searchEnrolmentLog);
	return mapping.findForward("searchEnrolmentLogs");
    }

    public ActionForward viewEnrolmentLogs(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	SearchEnrolmentLog searchEnrolmentLog = (SearchEnrolmentLog) getRenderedObject();
	request.setAttribute("bean", searchEnrolmentLog);
	Student student = Student.readStudentByNumber(searchEnrolmentLog.getStudentNumber());

	if (student == null) {
	    addActionMessage(request, "exception.student.does.not.exist");
	    return mapping.findForward("searchEnrolmentLogs");
	}

	Collection<EnrolmentLog> enrolmentLogsByPeriod = student
		.getEnrolmentLogsByPeriod(searchEnrolmentLog.getExecutionPeriod());
	request.setAttribute("enrolmentLogs", enrolmentLogsByPeriod);

	return mapping.findForward("searchEnrolmentLogs");
    }
}
