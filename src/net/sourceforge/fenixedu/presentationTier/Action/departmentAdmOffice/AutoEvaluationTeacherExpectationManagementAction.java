package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.TeacherAutoEvaluationDefinitionPeriod;
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

@Mapping(module = "departmentAdmOffice", path = "/autoEvaluationTeacherExpectationManagementAction", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "showDefinitionPeriod", path = "/departmentAdmOffice/teacher/teacherPersonalExpectationsManagement/showAutoEvaluationPeriodDefinition.jsp"),
		@Forward(name = "editDefinitionPeriod", path = "/departmentAdmOffice/teacher/teacherPersonalExpectationsManagement/editAutoEvaluationPeriodDefinition.jsp") })
public class AutoEvaluationTeacherExpectationManagementAction extends TeacherPersonalExpectationsDefinitionPeriodDA {

    @Override
    protected void readAndSetPeriod(HttpServletRequest request, ExecutionYear year) {
	if (year != null) {
	    TeacherAutoEvaluationDefinitionPeriod teacherExpectationDefinitionPeriod = getDepartment(request)
		    .getTeacherAutoEvaluationDefinitionPeriodForExecutionYear(year);
	    request.setAttribute("period", teacherExpectationDefinitionPeriod);
	}
    }
}
