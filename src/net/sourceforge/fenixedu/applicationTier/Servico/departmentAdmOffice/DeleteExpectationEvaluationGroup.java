package net.sourceforge.fenixedu.applicationTier.Servico.departmentAdmOffice;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExpectationEvaluationGroup;

public class DeleteExpectationEvaluationGroup extends Service {

    public void run(ExpectationEvaluationGroup group) {
	if(group != null) {
	    group.delete();
	}
    }
    
}
