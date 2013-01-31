/*
 * Created on Feb 6, 2006
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;

public class DeleteCurricularRule extends FenixService {

	public void run(Integer curricularRuleID) throws FenixServiceException {
		final CurricularRule curricularRule = rootDomainObject.readCurricularRuleByOID(curricularRuleID);
		if (curricularRule == null) {
			throw new FenixServiceException("error.noCurricularRule");
		}
		curricularRule.delete();
	}
}
