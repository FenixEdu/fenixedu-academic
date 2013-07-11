/*
 * Created on Feb 6, 2006
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRulesManager;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class EditCurricularRule {

    @Atomic
    public static void run(String curricularRuleID, String beginExecutionPeriodID, String endExecutionPeriodID)
            throws FenixServiceException {

        final CurricularRule curricularRule = FenixFramework.getDomainObject(curricularRuleID);
        if (curricularRule == null) {
            throw new FenixServiceException("error.noCurricularRule");
        }

        final ExecutionSemester beginExecutionPeriod;
        if (beginExecutionPeriodID == null) {
            beginExecutionPeriod = ExecutionSemester.readActualExecutionSemester();
        } else {
            beginExecutionPeriod = FenixFramework.getDomainObject(beginExecutionPeriodID);
        }

        final ExecutionSemester endExecutionPeriod =
                (endExecutionPeriodID == null) ? null : FenixFramework.<ExecutionSemester> getDomainObject(endExecutionPeriodID);

        CurricularRulesManager.editCurricularRule(curricularRule, beginExecutionPeriod, endExecutionPeriod);
    }
}
