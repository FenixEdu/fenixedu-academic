package net.sourceforge.fenixedu.presentationTier.Action.manager.enrolments;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.curriculumLineLog.SearchCurriculumLineLog;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/curriculumLineLogs", module = "manager")
@Forwards( { @Forward(name = "searchCurriculumLineLogs", path = "/manager/viewCurriculumLineLogs.jsp") })
public class CurriculumLineLogsDA extends FenixDispatchAction {

    public ActionForward prepareViewCurriculumLineLogs(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("bean", new SearchCurriculumLineLog());
	return mapping.findForward("searchCurriculumLineLogs");
    }

    public ActionForward viewCurriculumLineLogs(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final SearchCurriculumLineLog searchCurriculumLineLog = (SearchCurriculumLineLog) getRenderedObject();
	request.setAttribute("bean", searchCurriculumLineLog);
	Student student = Student.readStudentByNumber(searchCurriculumLineLog.getStudentNumber());

	if (student == null) {
	    addActionMessage(request, "exception.student.does.not.exist");
	    return mapping.findForward("searchCurriculumLineLogs");
	}

	request.setAttribute("curriculumLineLogs", student.getCurriculumLineLogs(searchCurriculumLineLog.getExecutionPeriod()));
	return mapping.findForward("searchCurriculumLineLogs");
    }
}
