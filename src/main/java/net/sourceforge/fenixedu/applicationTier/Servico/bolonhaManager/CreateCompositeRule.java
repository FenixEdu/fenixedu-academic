/*
 * Created on Feb 6, 2006
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRulesManager;
import net.sourceforge.fenixedu.domain.util.LogicOperator;
import pt.ist.fenixWebFramework.services.Service;

public class CreateCompositeRule {

    @Service
    public static void run(LogicOperator logicOperator, Integer[] selectedCurricularRuleIDs) throws FenixServiceException {
        if (selectedCurricularRuleIDs != null) {
            final CurricularRule[] curricularRules = new CurricularRule[selectedCurricularRuleIDs.length];

            for (int i = 0; i < selectedCurricularRuleIDs.length; i++) {
                final CurricularRule curricularRule =
                        RootDomainObject.getInstance().readCurricularRuleByOID(selectedCurricularRuleIDs[i]);
                if (curricularRule == null) {
                    throw new FenixServiceException("error.invalidCurricularRule");
                }
                curricularRules[i] = curricularRule;
            }

            CurricularRulesManager.createCompositeRule(logicOperator, curricularRules);
        }
    }

}
