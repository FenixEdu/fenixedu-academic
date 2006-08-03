package net.sourceforge.fenixedu.presentationTier.Action.degreeAdministrativeOffice.withoutRules;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class ChangeStudentAreasDispatchAction extends FenixDispatchAction {

	public ActionForward chooseStudent(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("degreeType", DegreeType.DEGREE.name());
		return mapping.findForward("chooseStudent");
	}

	public ActionForward showAndChooseStudentAreas(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) {

		final DynaActionForm form = (DynaActionForm) actionForm;
		final Integer studentNumber = Integer.valueOf(form.getString("studentNumber"));

		try {
			final Student student = Student.readStudentByNumberAndDegreeType(studentNumber,
					DegreeType.DEGREE);
			final StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) ServiceManagerServiceFactory
					.executeService(getUserView(request), "ReadStudentCurricularPlanForEnrollments",
							new Object[] { null, student });

			prepareStudentAreasInformation(request, form, studentCurricularPlan);

		} catch (Exception e) {
			setAcurateErrorMessage(request, e, studentNumber, "chooseStudent");
			return mapping.getInputForward();
		}

		return mapping.findForward("showAndChooseStudentAreas");
	}

	private void prepareStudentAreasInformation(HttpServletRequest request, final DynaActionForm form,
			final StudentCurricularPlan studentCurricularPlan) {

		request.setAttribute("studentCurricularPlan", studentCurricularPlan);

		if (studentCurricularPlan.hasBranch()) {
			form.set("specializationAreaID", studentCurricularPlan.getBranch().getIdInternal());
		}
		if (studentCurricularPlan.hasSecundaryBranch()) {
			form.set("secondaryAreaID", studentCurricularPlan.getSecundaryBranch().getIdInternal());
		}
	}

	public ActionForward showChangeOfStudentAreasConfirmation(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {

		final DynaActionForm form = (DynaActionForm) actionForm;

		final Integer studentNumber = Integer.valueOf(form.getString("studentNumber"));
		final DegreeType degreeType = DegreeType.valueOf(form.getString("degreeType"));
		final Integer specializationAreaID = (Integer) form.get("specializationAreaID");
		final Integer secondaryAreaID = (Integer) form.get("secondaryAreaID");

		final Student student = Student.readStudentByNumberAndDegreeType(studentNumber, degreeType);
		try {
			ServiceManagerServiceFactory.executeService(getUserView(request),
					"WriteStudentAreasWithoutRestrictions", new Object[] { student, degreeType,
							specializationAreaID, secondaryAreaID });
		} catch (Exception e) {
			setAcurateErrorMessage(request, e, studentNumber, "showAndChooseStudentAreas");
			prepareStudentAreasInformation(request, form, student
					.getActiveOrConcludedStudentCurricularPlan());
			return mapping.findForward("showAndChooseStudentAreas");
		}
		return chooseStudent(mapping, form, request, response);
	}

	public ActionForward exit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		return mapping.findForward("exit");
	}

	public ActionForward error(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		final String methodName = (String) request.getAttribute("methodName");
		if (methodName == null) {
			request.setAttribute("degreeType", Integer.valueOf(request.getParameter("degreeType")));
			return mapping.findForward("chooseStudent");
		}
		return mapping.findForward(methodName);
	}

	private void setAcurateErrorMessage(HttpServletRequest request, Exception e, Integer studentNumber,
			String methodName) {
		e.printStackTrace();
		if (e instanceof NotAuthorizedException) {
			addActionMessage(request, "error.exception.notAuthorized2");
		} else if (e instanceof ExistingServiceException) {
			if (e.getMessage().equals("student")) {
				addActionMessage(request, "error.no.student.in.database", studentNumber.toString());
			} else if (e.getMessage().equals("studentCurricularPlan")) {
				addActionMessage(request, "error.student.curricularPlan.nonExistent");
			}
		} else if (e instanceof NonExistingServiceException) {
			addActionMessage(request, "error.student.curricularPlan.nonExistent");

		} else if (e instanceof DomainException) {
			addActionMessage(request, e.getMessage());
		} else if (e instanceof FenixServiceException) {
			addActionMessage(request, "error.impossible.operations");
		} else {
			addActionMessage(request, "error.impossible.operations");
		}
		request.setAttribute("methodName", methodName);
	}
}