/*
 * Created on Feb 6, 2006
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.bolonhaManager.CurricularRuleParametersDTO;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRuleFactory;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRuleType;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateCurricularRule extends Service {
    
    public void run(Integer degreeModuleToApplyRuleID, CurricularRuleType selectedCurricularRuleType, CurricularRuleParametersDTO parametersDTO) throws FenixServiceException, ExcepcaoPersistencia {
        
        final DegreeModule degreeModuleToApplyRule = (DegreeModule) persistentObject.readByOID(DegreeModule.class, degreeModuleToApplyRuleID);
        if (degreeModuleToApplyRule == null) {
            throw new FenixServiceException("error.noDegreeModule");
        }

        // TODO: this should be modified to receive ExecutionYear, but for now we just read the '2006/2007'
        ExecutionYear executionYear = persistentSupport.getIPersistentExecutionYear().readExecutionYearByName("2006/2007");
        ExecutionPeriod begin = executionYear.getExecutionPeriodForSemester(Integer.valueOf(1));
        
        CurricularRuleFactory.createCurricularRule(degreeModuleToApplyRule, begin, null, selectedCurricularRuleType, parametersDTO, persistentObject);
    }
}
