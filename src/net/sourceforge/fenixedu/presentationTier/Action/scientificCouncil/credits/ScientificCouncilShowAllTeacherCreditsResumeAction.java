package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.credits;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.presentationTier.Action.credits.ShowAllTeacherCreditsResumeAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class ScientificCouncilShowAllTeacherCreditsResumeAction extends ShowAllTeacherCreditsResumeAction {

    public ActionForward prepareTeacherSearch(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NumberFormatException, FenixFilterException, FenixServiceException {

	DynaActionForm dynaForm = (DynaActionForm) form;
	dynaForm.set("method", "showTeacherCreditsResume");
	return mapping.findForward("search-teacher-form");
    }

    public ActionForward showTeacherCreditsResume(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	DynaActionForm dynaActionForm = (DynaActionForm) form;
	Integer teacherNumber = Integer.valueOf(dynaActionForm.getString("teacherNumber"));
	Teacher teacher = Teacher.readByNumber(teacherNumber);
	if (teacher == null) {
	    request.setAttribute("teacherNotFound", "teacherNotFound");
	    dynaActionForm.set("method", "showTeacherCreditsResume");
	    return mapping.findForward("teacher-not-found");
	}
	readAllTeacherCredits(request, teacher);
	return mapping.findForward("show-all-credits-resume");
    }
}
