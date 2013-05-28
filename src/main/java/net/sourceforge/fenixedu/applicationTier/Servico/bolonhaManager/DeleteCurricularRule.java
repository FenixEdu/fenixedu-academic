/*
 * Created on Feb 6, 2006
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class DeleteCurricularRule {

    @Service
    public static void run(Integer curricularRuleID) throws FenixServiceException {
        final CurricularRule curricularRule = AbstractDomainObject.fromExternalId(curricularRuleID);
        if (curricularRule == null) {
            throw new FenixServiceException("error.noCurricularRule");
        }
        curricularRule.delete();
    }
}
