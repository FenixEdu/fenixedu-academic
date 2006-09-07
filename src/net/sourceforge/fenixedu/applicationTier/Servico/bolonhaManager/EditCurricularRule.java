/*
 * Created on Feb 6, 2006
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRulesManager;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EditCurricularRule extends Service {

    public void run(Integer curricularRuleID, Integer beginExecutionPeriodID, Integer endExecutionPeriodID)
            throws FenixServiceException, ExcepcaoPersistencia {

        final CurricularRule curricularRule = rootDomainObject.readCurricularRuleByOID(curricularRuleID);
        if (curricularRule == null) {
            throw new FenixServiceException("error.noCurricularRule");
        }
        
        final ExecutionPeriod beginExecutionPeriod;
        if (beginExecutionPeriodID == null) {
            final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
            final ExecutionYear nextExecutionYear = currentExecutionYear.getNextExecutionYear();
            if (nextExecutionYear == null) {
        	beginExecutionPeriod = currentExecutionYear.readExecutionPeriodForSemester(Integer.valueOf(1));
            } else {
        	beginExecutionPeriod = nextExecutionYear.readExecutionPeriodForSemester(Integer.valueOf(1));
            }
        } else {
            beginExecutionPeriod = rootDomainObject.readExecutionPeriodByOID(beginExecutionPeriodID);
        }

        final ExecutionPeriod endExecutionPeriod = (endExecutionPeriodID == null) ? null
                : rootDomainObject.readExecutionPeriodByOID(endExecutionPeriodID);
        
        CurricularRulesManager.editCurricularRule(curricularRule, beginExecutionPeriod, endExecutionPeriod);
    }
}
