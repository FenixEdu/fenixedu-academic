/*
 * Created on Feb 6, 2006
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.bolonhaManager.CurricularRuleParametersDTO;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRulesManager;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EditCurricularRule extends Service {
    
    public void run(Integer curricularRuleID, CurricularRuleParametersDTO parametersDTO) throws FenixServiceException, ExcepcaoPersistencia {
        
        final CurricularRule curricularRule = (CurricularRule) persistentObject.readByOID(CurricularRule.class, curricularRuleID);
        if (curricularRule == null) {
            throw new FenixServiceException("error.noCurricularRule");
        }
        CurricularRulesManager.editCurricularRule(curricularRule, parametersDTO, persistentObject);
    }
}
