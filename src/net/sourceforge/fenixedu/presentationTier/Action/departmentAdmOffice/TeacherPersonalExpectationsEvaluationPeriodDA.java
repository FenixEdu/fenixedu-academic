package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.TeacherPersonalExpectationsEvaluationPeriod;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "departmentAdmOffice", path = "/teacherPersonalExpectationsEvaluationPeriod", scope = "request",
        parameter = "method")
@Forwards(
        value = {
                @Forward(
                        name = "showDefinitionPeriod",
                        path = "/departmentAdmOffice/teacher/teacherPersonalExpectationsManagement/showTeacherPersonalExpectationsEvaluationPeriod.jsp"),
                @Forward(
                        name = "editDefinitionPeriod",
                        path = "/departmentAdmOffice/teacher/teacherPersonalExpectationsManagement/editTeacherPersonalExpectationsEvaluationPeriod.jsp") })
public class TeacherPersonalExpectationsEvaluationPeriodDA extends TeacherPersonalExpectationsDefinitionPeriodDA {

    @Override
    protected void readAndSetPeriod(HttpServletRequest request, ExecutionYear year) {
        if (year != null) {
            TeacherPersonalExpectationsEvaluationPeriod teacherExpectationDefinitionPeriod =
                    getDepartment(request).getTeacherPersonalExpectationsEvaluationPeriodByExecutionYear(year);
            request.setAttribute("period", teacherExpectationDefinitionPeriod);
        }
    }
}
