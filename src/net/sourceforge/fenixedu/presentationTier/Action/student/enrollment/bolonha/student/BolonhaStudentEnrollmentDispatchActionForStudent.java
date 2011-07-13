package net.sourceforge.fenixedu.presentationTier.Action.student.enrollment.bolonha.student;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "student", path = "/bolonhaStudentEnrollment", attribute = "bolonhaEnrollmentForm", formBean = "bolonhaEnrollmentForm", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "notAuthorized", path = "/student/notAuthorized_bd.jsp"),
		@Forward(name = "chooseOptionalCurricularCourseToEnrol", path = "/student/enrollment/bolonha/chooseOptionalCurricularCourseToEnrol.jsp"),
		@Forward(name = "showDegreeModulesToEnrol", path = "/student/enrollment/bolonha/showDegreeModulesToEnrol.jsp"),
		@Forward(name = "showEnrollmentInstructions", path = "/student/enrollment/bolonha/showEnrollmentInstructions.jsp"),
		@Forward(name = "chooseCycleCourseGroupToEnrol", path = "/student/enrollment/bolonha/chooseCycleCourseGroupToEnrol.jsp"),
		@Forward(name = "welcome", path = "/student/enrollment/welcome.jsp"),
		@Forward(name = "enrollmentCannotProceed", path = "/student/enrollment/bolonha/enrollmentCannotProceed.jsp"),
		@Forward(name = "welcome-dea-degree", path = "/phdStudentEnrolment.do?method=showWelcome") })
public class BolonhaStudentEnrollmentDispatchActionForStudent extends net.sourceforge.fenixedu.presentationTier.Action.student.enrollment.bolonha.BolonhaStudentEnrollmentDispatchAction {
}