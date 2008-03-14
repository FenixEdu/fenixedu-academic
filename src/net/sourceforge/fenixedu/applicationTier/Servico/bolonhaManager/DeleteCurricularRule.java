/*
 * Created on Feb 6, 2006
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteCurricularRule extends Service {

    public void run(Integer curricularRuleID) throws FenixServiceException, ExcepcaoPersistencia {
	final CurricularRule curricularRule = rootDomainObject.readCurricularRuleByOID(curricularRuleID);
	if (curricularRule == null) {
	    throw new FenixServiceException("error.noCurricularRule");
	}
	curricularRule.delete();
    }
}
