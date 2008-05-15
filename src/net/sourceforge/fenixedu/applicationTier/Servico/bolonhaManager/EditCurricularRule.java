/*
 * Created on Feb 6, 2006
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRulesManager;

public class EditCurricularRule extends Service {

    public void run(Integer curricularRuleID, Integer beginExecutionPeriodID, Integer endExecutionPeriodID)
	    throws FenixServiceException {

	final CurricularRule curricularRule = rootDomainObject.readCurricularRuleByOID(curricularRuleID);
	if (curricularRule == null) {
	    throw new FenixServiceException("error.noCurricularRule");
	}

	final ExecutionSemester beginExecutionPeriod;
	if (beginExecutionPeriodID == null) {
	    beginExecutionPeriod = ExecutionSemester.readActualExecutionSemester();
	} else {
	    beginExecutionPeriod = rootDomainObject.readExecutionSemesterByOID(beginExecutionPeriodID);
	}

	final ExecutionSemester endExecutionPeriod = (endExecutionPeriodID == null) ? null : rootDomainObject
		.readExecutionSemesterByOID(endExecutionPeriodID);

	CurricularRulesManager.editCurricularRule(curricularRule, beginExecutionPeriod, endExecutionPeriod);
    }
}
