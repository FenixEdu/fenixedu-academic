/*
 * Created on Feb 6, 2006
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRulesManager;
import pt.ist.fenixWebFramework.services.Service;

public class EditCurricularRule {

    @Service
    public static void run(Integer curricularRuleID, Integer beginExecutionPeriodID, Integer endExecutionPeriodID)
            throws FenixServiceException {

        final CurricularRule curricularRule = RootDomainObject.getInstance().readCurricularRuleByOID(curricularRuleID);
        if (curricularRule == null) {
            throw new FenixServiceException("error.noCurricularRule");
        }

        final ExecutionSemester beginExecutionPeriod;
        if (beginExecutionPeriodID == null) {
            beginExecutionPeriod = ExecutionSemester.readActualExecutionSemester();
        } else {
            beginExecutionPeriod = RootDomainObject.getInstance().readExecutionSemesterByOID(beginExecutionPeriodID);
        }

        final ExecutionSemester endExecutionPeriod =
                (endExecutionPeriodID == null) ? null : RootDomainObject.getInstance().readExecutionSemesterByOID(
                        endExecutionPeriodID);

        CurricularRulesManager.editCurricularRule(curricularRule, beginExecutionPeriod, endExecutionPeriod);
    }
}
