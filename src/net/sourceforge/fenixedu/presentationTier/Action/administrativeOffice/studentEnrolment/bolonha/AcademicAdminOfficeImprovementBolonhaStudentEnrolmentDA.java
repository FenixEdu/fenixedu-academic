package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.studentEnrolment.bolonha;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha.ImprovementBolonhaStudentEnrolmentBean;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(
		path = "/improvementBolonhaStudentEnrollment",
		module = "academicAdministration",
		formBean = "bolonhaStudentEnrollmentForm")
@Forwards({ @Forward(
		name = "showDegreeModulesToEnrol",
		path = "/academicAdminOffice/student/enrollment/bolonha/showDegreeModulesToEnrol.jsp") })
public class AcademicAdminOfficeImprovementBolonhaStudentEnrolmentDA extends AcademicAdminOfficeBolonhaStudentEnrollmentDA {

	@Override
	protected CurricularRuleLevel getCurricularRuleLevel(ActionForm form) {
		return CurricularRuleLevel.IMPROVEMENT_ENROLMENT;
	}

	@Override
	protected ActionForward prepareShowDegreeModulesToEnrol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response, StudentCurricularPlan studentCurricularPlan, ExecutionSemester executionSemester) {
		request.setAttribute("action", getAction());
		request.setAttribute("bolonhaStudentEnrollmentBean", new ImprovementBolonhaStudentEnrolmentBean(studentCurricularPlan,
				executionSemester));

		addDebtsWarningMessages(studentCurricularPlan.getRegistration().getStudent(), executionSemester, request);

		return mapping.findForward("showDegreeModulesToEnrol");
	}

	@Override
	protected String getAction() {
		return "/improvementBolonhaStudentEnrollment.do";
	}

}
