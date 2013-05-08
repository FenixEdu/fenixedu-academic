/*
 * Created on Feb 6, 2006
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.bolonhaManager.CurricularRuleParametersDTO;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRuleType;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRulesManager;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import pt.ist.fenixWebFramework.services.Service;

public class CreateRule {

    @Service
    public static void run(Integer degreeModuleToApplyRuleID, CurricularRuleType selectedCurricularRuleType,
            CurricularRuleParametersDTO parametersDTO, Integer beginExecutionPeriodID, Integer endExecutionPeriodID)
            throws FenixServiceException {

        final DegreeModule degreeModuleToApplyRule =
                RootDomainObject.getInstance().readDegreeModuleByOID(degreeModuleToApplyRuleID);
        if (degreeModuleToApplyRule == null) {
            throw new FenixServiceException("error.noDegreeModule");
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

        CurricularRulesManager.createCurricularRule(degreeModuleToApplyRule, beginExecutionPeriod, endExecutionPeriod,
                selectedCurricularRuleType, parametersDTO);
    }
}
