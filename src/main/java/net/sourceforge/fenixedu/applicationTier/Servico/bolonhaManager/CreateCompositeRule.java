/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on Feb 6, 2006
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRulesManager;
import net.sourceforge.fenixedu.domain.util.LogicOperator;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class CreateCompositeRule {

    @Atomic
    public static void run(LogicOperator logicOperator, String[] selectedCurricularRuleIDs) throws FenixServiceException {
        if (selectedCurricularRuleIDs != null) {
            final CurricularRule[] curricularRules = new CurricularRule[selectedCurricularRuleIDs.length];

            for (int i = 0; i < selectedCurricularRuleIDs.length; i++) {
                final CurricularRule curricularRule = FenixFramework.getDomainObject(selectedCurricularRuleIDs[i]);
                if (curricularRule == null) {
                    throw new FenixServiceException("error.invalidCurricularRule");
                }
                curricularRules[i] = curricularRule;
            }

            CurricularRulesManager.createCompositeRule(logicOperator, curricularRules);
        }
    }

}
