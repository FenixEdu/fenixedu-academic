package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.TeacherPersonalExpectationsVisualizationPeriod;

public class TeacherPersonalExpectationsVisualizationPeriodDA extends TeacherPersonalExpectationsDefinitionPeriodDA {

    @Override
    protected void readAndSetPeriod(HttpServletRequest request, ExecutionYear year) {
	if(year != null) {
            TeacherPersonalExpectationsVisualizationPeriod teacherExpectationDefinitionPeriod = getDepartment(request).getTeacherPersonalExpectationsVisualizationPeriodByExecutionYear(year);	
            request.setAttribute("period", teacherExpectationDefinitionPeriod);
	}
    }       
}
