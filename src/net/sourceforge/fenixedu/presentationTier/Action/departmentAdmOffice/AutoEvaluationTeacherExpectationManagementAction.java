package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.TeacherAutoEvaluationDefinitionPeriod;

public class AutoEvaluationTeacherExpectationManagementAction extends TeacherPersonalExpectationsDefinitionPeriodDA {
    
    @Override
    protected void readAndSetPeriod(HttpServletRequest request, ExecutionYear year) {
	if(year != null) {
            TeacherAutoEvaluationDefinitionPeriod teacherExpectationDefinitionPeriod = getDepartment(request).getTeacherAutoEvaluationDefinitionPeriodForExecutionYear(year);
            request.setAttribute("period", teacherExpectationDefinitionPeriod);
	}
    }      
}
