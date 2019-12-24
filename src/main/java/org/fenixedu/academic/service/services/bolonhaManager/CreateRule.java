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
import org.fenixedu.academic.domain.curricularRules.CurricularRuleType;
import org.fenixedu.academic.domain.curricularRules.CurricularRulesManager;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.dto.bolonhaManager.CurricularRuleParametersDTO;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class CreateRule {

    @Atomic
    public static void run(String degreeModuleToApplyRuleID, CurricularRuleType selectedCurricularRuleType,
            CurricularRuleParametersDTO parametersDTO, String beginExecutionPeriodID, String endExecutionPeriodID)
            throws FenixServiceException {

        final DegreeModule degreeModuleToApplyRule = FenixFramework.getDomainObject(degreeModuleToApplyRuleID);
        if (degreeModuleToApplyRule == null) {
            throw new FenixServiceException("error.noDegreeModule");
        }

        if (beginExecutionPeriodID == null) {
            throw new FenixServiceException("error.CreateRule.beginExecutionPeriod.required");
        }

        final ExecutionInterval beginExecutionPeriod = FenixFramework.getDomainObject(beginExecutionPeriodID);

        final ExecutionInterval endExecutionPeriod =
                (endExecutionPeriodID == null) ? null : FenixFramework.<ExecutionInterval> getDomainObject(endExecutionPeriodID);

        CurricularRulesManager.createCurricularRule(degreeModuleToApplyRule, beginExecutionPeriod, endExecutionPeriod,
                selectedCurricularRuleType, parametersDTO);
    }
}
