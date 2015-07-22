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

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.curricularRules.executors.verifyExecutors.VerifyRuleExecutor;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.GenericPair;

public class MaximumNumberOfCreditsForEnrolmentPeriod extends MaximumNumberOfCreditsForEnrolmentPeriod_Base {

    /**
     * This is a default value, and should not be used externally.
     * 
     * Use instead {@link #getMaximumCredits()} on a specific instance.
     */
    @Deprecated
    static final public double MAXIMUM_NUMBER_OF_CREDITS = 40.5;

    /**
     * This is a default value, and should not be used externally.
     * 
     * Use instead {@link #getMaximumCreditsPartialTime()} on a specific instance.
     */
    @Deprecated
    static final public double MAXIMUM_NUMBER_OF_CREDITS_PARTIAL_TIME = MAXIMUM_NUMBER_OF_CREDITS / 2;

    /*
     * Previous value was 0.75d until 2008/2009. These constants should be rule
     * attributes, and to change this we should have a new rule. When
     * refactoring rule pay attention to these values. Be aware of
     * getAccumulatedEcts and getMaximumNumberOfCredits static methods, it can
     * not be used in this way
     */
    static final private double ACCUMULATED_FACTOR = 1.0d;

    public MaximumNumberOfCreditsForEnrolmentPeriod(final DegreeModule degreeModuleToApplyRule, final ExecutionSemester begin,
            final ExecutionSemester end) {

        super();
        checkDegreeModule(degreeModuleToApplyRule);
        init(degreeModuleToApplyRule, null, begin, end, CurricularRuleType.MAXIMUM_NUMBER_OF_CREDITS_FOR_ENROLMENT_PERIOD);
    }

    public MaximumNumberOfCreditsForEnrolmentPeriod(final DegreeModule degreeModuleToApplyRule, final ExecutionSemester begin) {
        this(degreeModuleToApplyRule, begin, null);
    }

    private void checkDegreeModule(final DegreeModule degreeModule) {
        if (!degreeModule.isRoot()) {
            throw new DomainException(
                    "error.curricularRules.MaximumNumberOfCreditsForEnrolmentPeriod.should.be.applied.to.root.degreeModule");
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

        result.add(new GenericPair<Object, Boolean>("label.maximumNumberOfCreditsForEnrolmentPeriod", true));
        result.add(new GenericPair<Object, Boolean>(": ", false));
        result.add(new GenericPair<Object, Boolean>(getMaxCredits(), false));
        result.add(new GenericPair<Object, Boolean>(" (", false));
        result.add(new GenericPair<Object, Boolean>(getMaxCreditsPartialTime(), false));
        result.add(new GenericPair<Object, Boolean>(")", false));

        return result;
    }

    @Override
    public VerifyRuleExecutor createVerifyRuleExecutor() {
        return VerifyRuleExecutor.NULL_VERIFY_EXECUTOR;
    }

    @Override
    public Double getMaxCredits() {
        // TODO Make this property required in the next major
        if (super.getMaxCredits() == null) {
            return MAXIMUM_NUMBER_OF_CREDITS;
        } else {
            return super.getMaxCredits();
        }
    }

    @Override
    public Double getMaxCreditsPartialTime() {
        // TODO Make this property required in the next major
        if (super.getMaxCreditsPartialTime() == null) {
            return MAXIMUM_NUMBER_OF_CREDITS_PARTIAL_TIME;
        } else {
            return super.getMaxCreditsPartialTime();
        }
    }

    static public Double getAccumulatedEcts(final CurricularCourse curricularCourse, final ExecutionSemester executionSemester) {
        return curricularCourse.getEctsCredits(executionSemester.getSemester(), executionSemester) * ACCUMULATED_FACTOR;
    }

    static public double getMaximumNumberOfCredits(final StudentCurricularPlan studentCurricularPlan,
            final ExecutionYear executionYear) {
        boolean partialRegime = studentCurricularPlan.getRegistration().isPartialRegime(executionYear);
        return studentCurricularPlan
                .getDegreeCurricularPlan()
                .getRoot()
                .getCurricularRules(CurricularRuleType.MAXIMUM_NUMBER_OF_CREDITS_FOR_ENROLMENT_PERIOD, executionYear)
                .stream()
                .map(MaximumNumberOfCreditsForEnrolmentPeriod.class::cast)
                .mapToDouble(
                        partialRegime ? MaximumNumberOfCreditsForEnrolmentPeriod::getMaxCreditsPartialTime : MaximumNumberOfCreditsForEnrolmentPeriod::getMaxCredits)
                .min().orElse(partialRegime ? MAXIMUM_NUMBER_OF_CREDITS_PARTIAL_TIME : MAXIMUM_NUMBER_OF_CREDITS);
    }
}
