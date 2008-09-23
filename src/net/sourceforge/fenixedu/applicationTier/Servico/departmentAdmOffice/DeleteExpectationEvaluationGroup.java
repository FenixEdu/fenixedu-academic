package net.sourceforge.fenixedu.applicationTier.Servico.departmentAdmOffice;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.ExpectationEvaluationGroup;

public class DeleteExpectationEvaluationGroup extends FenixService {

    public void run(ExpectationEvaluationGroup group) {
	if (group != null) {
	    group.delete();
	}
    }

}
