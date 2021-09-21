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

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.curricularRules.executors.verifyExecutors.VerifyRuleExecutor;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.studentCurriculum.CycleCurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.ExternalCurriculumGroup;
import org.fenixedu.academic.dto.GenericPair;

public class CreditsLimitInExternalCycle extends CurricularRuleNotPersistent {

    private static final double MIN_CREDITS_IN_PREVIOUS_CYCLE = 120;

    private ExternalCurriculumGroup externalCurriculumGroup;
    private CycleCurriculumGroup previousCycleCurriculumGroup;

    public CreditsLimitInExternalCycle(final CycleCurriculumGroup previousCycleCurriculumGroup,
            final ExternalCurriculumGroup toApply) {
        if (toApply == null || previousCycleCurriculumGroup == null) {
            throw new DomainException("curricular.rule.invalid.parameters");
        }

        this.previousCycleCurriculumGroup = previousCycleCurriculumGroup;
        this.externalCurriculumGroup = toApply;

    }

    @Override
    public List<GenericPair<Object, Boolean>> getLabel() {
        return Collections.singletonList(new GenericPair<Object, Boolean>("label.creditsLimitInExternalCycle", true));
    }

    @Override
    public DegreeModule getDegreeModuleToApplyRule() {
        return externalCurriculumGroup.getDegreeModule();
    }

    @Override
    public CourseGroup getContextCourseGroup() {
        return null;
    }

    @Override
    public CompositeRule getParentCompositeRule() {
        return null;
    }

    public boolean creditsExceedMaximumInExternalCycle(final Double numberOfCreditsInExternalCycle,
            final Double numberOfCreditsInPreviousCycle) {
        return numberOfCreditsInExternalCycle.compareTo(getMaxCreditsInExternalCycle(numberOfCreditsInPreviousCycle)) > 0;
    }

    public boolean creditsInPreviousCycleSufficient(final Double previousCycleCredits) {
        return previousCycleCredits.compareTo(MIN_CREDITS_IN_PREVIOUS_CYCLE) >= 0;
    }

    @Override
    public CurricularRuleType getCurricularRuleType() {
        return CurricularRuleType.CREDITS_LIMIT_IN_EXTERNAL_CYCLE;
    }

    @Override
    public ExecutionSemester getBegin() {
        return ExecutionSemester.readActualExecutionSemester();
    }

    @Override
    public ExecutionSemester getEnd() {
        return null;
    }

    public ExternalCurriculumGroup getExternalCurriculumGroup() {
        return externalCurriculumGroup;
    }

    public CycleCurriculumGroup getPreviousCycleCurriculumGroup() {
        return previousCycleCurriculumGroup;
    }

    public Double getMinCreditsInPreviousCycle() {
        return MIN_CREDITS_IN_PREVIOUS_CYCLE;
    }

    @Override
    public VerifyRuleExecutor createVerifyRuleExecutor() {
        return VerifyRuleExecutor.NULL_VERIFY_EXECUTOR;
    }

    public Double getMaxCreditsInExternalCycle(Double numberOfCreditsInPreviousCycle) {
        final BigDecimal previous = new BigDecimal(numberOfCreditsInPreviousCycle);
        return previous.multiply(new BigDecimal("1.4")).subtract(new BigDecimal("168")).doubleValue();
    }
}
