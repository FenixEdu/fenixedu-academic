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

    public void run(Integer degreeModuleToApplyRuleID, LogicOperators logicOperator, Integer[] selectedCurricularRuleIDs) throws FenixServiceException,
            ExcepcaoPersistencia {

        final DegreeModule degreeModuleToApplyRule = (DegreeModule) persistentObject.readByOID(DegreeModule.class, degreeModuleToApplyRuleID);
        if (degreeModuleToApplyRule == null) {
            throw new FenixServiceException("error.noDegreeModule");
        }
        
        final CurricularRule[] curricularRules = new CurricularRule[selectedCurricularRuleIDs.length];
        for (int i=0; i < selectedCurricularRuleIDs.length; i++) {
            final CurricularRule curricularRule = (CurricularRule) persistentObject.readByOID(CurricularRule.class, selectedCurricularRuleIDs[i]);
            if (curricularRule == null) {
                throw new FenixServiceException("error.invalidCurricularRule");
            }
            curricularRules[i] = curricularRule;
        }

        // TODO: this should be modified to receive ExecutionYear, but for now we just read the '2006/2007'
        ExecutionYear executionYear = persistentSupport.getIPersistentExecutionYear().readExecutionYearByName("2006/2007");
        ExecutionPeriod begin = executionYear.getExecutionPeriodForSemester(Integer.valueOf(1));

        CurricularRulesManager.createCompositeRule(degreeModuleToApplyRule, logicOperator, begin, null, curricularRules);        
    }
}
