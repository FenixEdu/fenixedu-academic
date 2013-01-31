package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.TeacherPersonalExpectationsVisualizationPeriod;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(
		module = "departmentAdmOffice",
		path = "/teacherPersonalExpectationsVisualizationPeriod",
		scope = "request",
		parameter = "method")
@Forwards(
		value = {
				@Forward(
						name = "showDefinitionPeriod",
						path = "/departmentAdmOffice/teacher/teacherPersonalExpectationsManagement/showTeacherPersonalExpectationsVisualizationPeriod.jsp"),
				@Forward(
						name = "editDefinitionPeriod",
						path = "/departmentAdmOffice/teacher/teacherPersonalExpectationsManagement/editTeacherPersonalExpectationsVisualizationPeriod.jsp") })
public class TeacherPersonalExpectationsVisualizationPeriodDA extends TeacherPersonalExpectationsDefinitionPeriodDA {

	@Override
	protected void readAndSetPeriod(HttpServletRequest request, ExecutionYear year) {
		if (year != null) {
			TeacherPersonalExpectationsVisualizationPeriod teacherExpectationDefinitionPeriod =
					getDepartment(request).getTeacherPersonalExpectationsVisualizationPeriodByExecutionYear(year);
			request.setAttribute("period", teacherExpectationDefinitionPeriod);
		}
	}
}
