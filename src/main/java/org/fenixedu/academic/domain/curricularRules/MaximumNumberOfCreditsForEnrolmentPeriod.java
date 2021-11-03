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

import org.fenixedu.academic.FenixEduAcademicConfiguration;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.curricularRules.executors.verifyExecutors.VerifyRuleExecutor;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.GenericPair;

public class MaximumNumberOfCreditsForEnrolmentPeriod extends MaximumNumberOfCreditsForEnrolmentPeriod_Base {

    static final public double MAXIMUM_NUMBER_OF_CREDITS = FenixEduAcademicConfiguration.getConfiguration().getMaximumNumberOfCreditsForEnrolment();

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
        result.add(new GenericPair<Object, Boolean>(MAXIMUM_NUMBER_OF_CREDITS, false));

        return result;
    }

    @Override
    public VerifyRuleExecutor createVerifyRuleExecutor() {
        return VerifyRuleExecutor.NULL_VERIFY_EXECUTOR;
    }

    static public Double getAccumulatedEcts(final CurricularCourse curricularCourse, final ExecutionSemester executionSemester) {
        return curricularCourse.getEctsCredits(executionSemester.getSemester(), executionSemester) * ACCUMULATED_FACTOR;
    }

    /**
     * @deprecated Use static MAXIMUM_NUMBER_OF_CREDITS field instead
     */
    @Deprecated
    static public double getMaximumNumberOfCredits(final StudentCurricularPlan studentCurricularPlan,
            final ExecutionYear executionYear) {
    	return MAXIMUM_NUMBER_OF_CREDITS;
    }
}
