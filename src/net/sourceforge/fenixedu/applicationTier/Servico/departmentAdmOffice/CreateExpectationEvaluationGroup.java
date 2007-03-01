package net.sourceforge.fenixedu.applicationTier.Servico.departmentAdmOffice;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.ExpectationEvaluationGroup;
import net.sourceforge.fenixedu.domain.Teacher;

public class CreateExpectationEvaluationGroup extends Service {

    public void run(Teacher appraiser, Teacher evaluated, ExecutionYear executionYear) {
	new ExpectationEvaluationGroup(appraiser, evaluated, executionYear);	
    }
}
