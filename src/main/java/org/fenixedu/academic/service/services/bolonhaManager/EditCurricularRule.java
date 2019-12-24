/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on Feb 6, 2006
 */
package org.fenixedu.academic.service.services.bolonhaManager;

import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.curricularRules.CurricularRule;
import org.fenixedu.academic.domain.curricularRules.CurricularRulesManager;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class EditCurricularRule {

    @Atomic
    public static void run(String curricularRuleID, String beginExecutionPeriodID, String endExecutionPeriodID)
            throws FenixServiceException {

        final CurricularRule curricularRule = FenixFramework.getDomainObject(curricularRuleID);
        if (curricularRule == null) {
            throw new FenixServiceException("error.noCurricularRule");
        }

        if (beginExecutionPeriodID == null) {
            throw new DomainException("error.EditCurricularRule.beginExecutionPeriod.required");
        }

        final ExecutionInterval beginExecutionPeriod = FenixFramework.getDomainObject(beginExecutionPeriodID);

        final ExecutionInterval endExecutionPeriod =
                (endExecutionPeriodID == null) ? null : FenixFramework.<ExecutionInterval> getDomainObject(endExecutionPeriodID);

        CurricularRulesManager.editCurricularRule(curricularRule, beginExecutionPeriod, endExecutionPeriod);
    }
}
