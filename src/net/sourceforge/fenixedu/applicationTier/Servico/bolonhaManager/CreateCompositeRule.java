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
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.util.LogicOperators;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateCompositeRule extends Service {

    public void run(Integer degreeModuleToApplyRuleID, LogicOperators logicOperator,
            Integer[] selectedCurricularRuleIDs, Integer beginExecutionPeriodID,
            Integer endExecutionPeriodID) throws FenixServiceException, ExcepcaoPersistencia {

        final DegreeModule degreeModuleToApplyRule = rootDomainObject.readDegreeModuleByOID(degreeModuleToApplyRuleID);
        if (degreeModuleToApplyRule == null) {
            throw new FenixServiceException("error.noDegreeModule");
        }

        if (selectedCurricularRuleIDs != null) {
            final CurricularRule[] curricularRules = new CurricularRule[selectedCurricularRuleIDs.length];
            for (int i = 0; i < selectedCurricularRuleIDs.length; i++) {
                final CurricularRule curricularRule = rootDomainObject
                        .readCurricularRuleByOID(selectedCurricularRuleIDs[i]);
                if (curricularRule == null) {
                    throw new FenixServiceException("error.invalidCurricularRule");
                }
                curricularRules[i] = curricularRule;
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

            CurricularRulesManager.createCompositeRule(degreeModuleToApplyRule, logicOperator,
                    beginExecutionPeriod, endExecutionPeriod, curricularRules);
        }
    }
}
