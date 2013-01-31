package net.sourceforge.fenixedu.presentationTier.Action.student.transition;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.commons.transition.AbstractBolonhaTransitionManagementDA;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(
		module = "student",
		path = "/bolonhaTransitionManagement",
		attribute = "bolonhaTransitionManagementForm",
		formBean = "bolonhaTransitionManagementForm",
		scope = "request",
		parameter = "method")
@Forwards(value = {
		@Forward(name = "showStudentCurricularPlan", path = "/student/transition/bolonha/showStudentCurricularPlan.jsp"),
		@Forward(name = "chooseRegistration", path = "/student/transition/bolonha/chooseRegistration.jsp") })
public class BolonhaTransitionManagementDA extends AbstractBolonhaTransitionManagementDA {

	@Override
	protected List<Registration> getRegistrations(final HttpServletRequest request) {
		return getStudent(request).getTransitionRegistrations();

	}

	private Student getStudent(final HttpServletRequest request) {
		return getLoggedPerson(request).getStudent();
	}

}
