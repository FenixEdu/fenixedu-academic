package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.credits;

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.presentationTier.Action.credits.ShowTeacherCreditsDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class ScientificCouncilShowTeacherCreditsDispatchAction extends ShowTeacherCreditsDispatchAction {

    public ActionForward showTeacherCredits(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NumberFormatException, FenixFilterException, FenixServiceException,
	    ParseException {

	DynaActionForm teacherCreditsForm = (DynaActionForm) form;
	ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID((Integer) teacherCreditsForm
		.get("executionPeriodId"));

	Teacher teacher = rootDomainObject.readTeacherByOID((Integer) teacherCreditsForm.get("teacherId"));

	if (teacher == null) {
	    request.setAttribute("teacherNotFound", "teacherNotFound");
	    return mapping.findForward("teacher-not-found");
	}

	getAllTeacherCredits(request, executionSemester, teacher);
	return mapping.findForward("show-teacher-credits");
    }
}
