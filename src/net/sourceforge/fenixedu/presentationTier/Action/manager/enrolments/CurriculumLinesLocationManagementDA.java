package net.sourceforge.fenixedu.presentationTier.Action.manager.enrolments;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.commons.student.AbstractCurriculumLinesLocationManagementDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/curriculumLinesLocationManagement", module = "manager")
@Forwards({

@Forward(name = "showCurriculum", path = "/manager/curriculum/curriculumLines/location/showCurriculum.jsp"),

@Forward(name = "chooseNewLocation", path = "/manager/curriculum/curriculumLines/location/chooseNewLocation.jsp"),

@Forward(name = "backToChooseStudentCurricularPlan", path = "/bolonhaStudentEnrolment.do?method=showAllStudentCurricularPlans")

})
public class CurriculumLinesLocationManagementDA extends AbstractCurriculumLinesLocationManagementDA {

	@Override
	protected boolean isWithRules(final HttpServletRequest request) {
		return false;
	}

	public ActionForward backToStudentEnrolments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		request.setAttribute("studentId", getStudentCurricularPlan(request).getRegistration().getStudent().getIdInternal());
		return mapping.findForward("backToChooseStudentCurricularPlan");
	}

}
