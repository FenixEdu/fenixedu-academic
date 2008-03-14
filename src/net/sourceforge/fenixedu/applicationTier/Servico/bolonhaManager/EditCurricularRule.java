/*
 * Created on Feb 6, 2006
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRulesManager;

public class EditCurricularRule extends Service {

    public void run(Integer curricularRuleID, Integer beginExecutionPeriodID, Integer endExecutionPeriodID)
            throws FenixServiceException {

        final CurricularRule curricularRule = rootDomainObject.readCurricularRuleByOID(curricularRuleID);
        if (curricularRule == null) {
            throw new FenixServiceException("error.noCurricularRule");
        }
        
        final ExecutionPeriod beginExecutionPeriod;
        if (beginExecutionPeriodID == null) {
          beginExecutionPeriod = ExecutionPeriod.readActualExecutionPeriod();  
        } else {
            beginExecutionPeriod = rootDomainObject.readExecutionPeriodByOID(beginExecutionPeriodID);
        }

        final ExecutionPeriod endExecutionPeriod = (endExecutionPeriodID == null) ? null
                : rootDomainObject.readExecutionPeriodByOID(endExecutionPeriodID);
        
        CurricularRulesManager.editCurricularRule(curricularRule, beginExecutionPeriod, endExecutionPeriod);
    }
}
