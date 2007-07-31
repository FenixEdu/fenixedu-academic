package net.sourceforge.fenixedu.presentationTier.Action.manager.enrolments;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.commons.student.AbstractCurriculumLinesLocationManagementDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class CurriculumLinesLocationManagementDA extends AbstractCurriculumLinesLocationManagementDA {

    public ActionForward backToStudentEnrolments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("studentId", getStudentCurricularPlan(request).getRegistration().getStudent().getIdInternal());

	return mapping.findForward("backToChooseStudentCurricularPlan");

    }

}
