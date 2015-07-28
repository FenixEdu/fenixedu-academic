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
package org.fenixedu.academic.domain.curricularRules;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.curricularRules.executors.verifyExecutors.VerifyRuleExecutor;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.GenericPair;

public class EnrolmentPeriodRestrictions extends EnrolmentPeriodRestrictions_Base {

    public EnrolmentPeriodRestrictions(final DegreeModule degreeModuleToApplyRule, final ExecutionSemester begin,
            final ExecutionSemester end) {

        super();
        checkDegreeModule(degreeModuleToApplyRule);
        init(degreeModuleToApplyRule, null, begin, end, CurricularRuleType.ENROLMENT_PERIOD_RESTRICTIONS);
    }

    public EnrolmentPeriodRestrictions(final DegreeModule degreeModuleToApplyRule, final ExecutionSemester begin) {
        this(degreeModuleToApplyRule, begin, null);
    }

    private void checkDegreeModule(final DegreeModule degreeModule) {
        if (!degreeModule.isRoot()) {
            throw new DomainException("error.curricularRules.should.be.applied.to.root.degreeModule");
        }
    }

    @Override
    protected void checkParameters(final DegreeModule degreeModuleToApplyRule, final ExecutionSemester begin) {
        if (degreeModuleToApplyRule == null || begin == null) {
            throw new DomainException("curricular.rule.invalid.parameters");
        }
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    protected void removeOwnParameters() {
    }

    @Override
    public List<GenericPair<Object, Boolean>> getLabel() {
        final List<GenericPair<Object, Boolean>> result = new ArrayList<GenericPair<Object, Boolean>>(3);

        result.add(new GenericPair<Object, Boolean>("label.enrolmentPeriodRestrictions", true));

        return result;
    }

    @Override
    public VerifyRuleExecutor createVerifyRuleExecutor() {
        return VerifyRuleExecutor.NULL_VERIFY_EXECUTOR;
    }

}
