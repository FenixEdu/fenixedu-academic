package net.sourceforge.fenixedu.presentationTier.Action.student.enrollment.bolonha.student;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "student", path = "/bolonhaStudentEnrollment", attribute = "bolonhaEnrollmentForm",
        formBean = "bolonhaEnrollmentForm", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "notAuthorized", path = "/student/notAuthorized_bd.jsp", tileProperties = @Tile(
                title = "private.academicadministrativeoffice.studentoperations.viewstudents")),
        @Forward(name = "chooseOptionalCurricularCourseToEnrol",
                path = "/student/enrollment/bolonha/chooseOptionalCurricularCourseToEnrol.jsp", tileProperties = @Tile(
                        title = "private.academicadministrativeoffice.studentoperations.viewstudents")),
        @Forward(name = "showDegreeModulesToEnrol", path = "/student/enrollment/bolonha/showDegreeModulesToEnrol.jsp",
                tileProperties = @Tile(title = "private.student.subscribe.courses")),
        @Forward(name = "showEnrollmentInstructions", path = "/student/enrollment/bolonha/showEnrollmentInstructions.jsp",
                tileProperties = @Tile(title = "private.academicadministrativeoffice.studentoperations.viewstudents")),
        @Forward(name = "chooseCycleCourseGroupToEnrol", path = "/student/enrollment/bolonha/chooseCycleCourseGroupToEnrol.jsp",
                tileProperties = @Tile(title = "private.academicadministrativeoffice.studentoperations.viewstudents")),
        @Forward(name = "welcome", path = "/student/enrollment/welcome.jsp", tileProperties = @Tile(
                title = "private.student.subscribe.courses")),
        @Forward(name = "enrollmentCannotProceed", path = "/student/enrollment/bolonha/enrollmentCannotProceed.jsp",
                tileProperties = @Tile(title = "private.student.subscribe.courses")),
        @Forward(name = "welcome-dea-degree", path = "/phdStudentEnrolment.do?method=showWelcome") })
public class BolonhaStudentEnrollmentDispatchActionForStudent extends
        net.sourceforge.fenixedu.presentationTier.Action.student.enrollment.bolonha.BolonhaStudentEnrollmentDispatchAction {
}