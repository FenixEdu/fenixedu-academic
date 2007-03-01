package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.TeacherPersonalExpectationsEvaluationPeriod;

public class TeacherPersonalExpectationsEvaluationPeriodDA extends TeacherPersonalExpectationsDefinitionPeriodDA {

    @Override
    protected void readAndSetPeriod(HttpServletRequest request, ExecutionYear year) {
	TeacherPersonalExpectationsEvaluationPeriod teacherExpectationDefinitionPeriod = 
	    getDepartment(request).getTeacherPersonalExpectationsEvaluationPeriodByExecutionYear(year);	
	request.setAttribute("period", teacherExpectationDefinitionPeriod);
    }       
}
