/*
 * Created on Feb 6, 2006
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.bolonhaManager.CurricularRuleParametersDTO;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRuleType;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRulesManager;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class CreateRule {

    @Atomic
    public static void run(String degreeModuleToApplyRuleID, CurricularRuleType selectedCurricularRuleType,
            CurricularRuleParametersDTO parametersDTO, String beginExecutionPeriodID, String endExecutionPeriodID)
            throws FenixServiceException {

        final DegreeModule degreeModuleToApplyRule = FenixFramework.getDomainObject(degreeModuleToApplyRuleID);
        if (degreeModuleToApplyRule == null) {
            throw new FenixServiceException("error.noDegreeModule");
        }

        final ExecutionSemester beginExecutionPeriod;
        if (beginExecutionPeriodID == null) {
            beginExecutionPeriod = ExecutionSemester.readActualExecutionSemester();
        } else {
            beginExecutionPeriod = FenixFramework.getDomainObject(beginExecutionPeriodID);
        }

        final ExecutionSemester endExecutionPeriod =
                (endExecutionPeriodID == null) ? null : FenixFramework.<ExecutionSemester> getDomainObject(endExecutionPeriodID);

        CurricularRulesManager.createCurricularRule(degreeModuleToApplyRule, beginExecutionPeriod, endExecutionPeriod,
                selectedCurricularRuleType, parametersDTO);
    }
}
