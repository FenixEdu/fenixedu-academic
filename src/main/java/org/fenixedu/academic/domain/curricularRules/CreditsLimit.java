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
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.curricularRules.executors.verifyExecutors.VerifyRuleExecutor;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.GenericPair;

public class CreditsLimit extends CreditsLimit_Base {
    

    protected CreditsLimit() {
        
    }

    private CreditsLimit(final Double minimum, final Double maximum) {
        super();
        checkCredits(minimum, maximum);
        setMinimumCredits(minimum);
        setMaximumCredits(maximum);
        setCurricularRuleType(CurricularRuleType.CREDITS_LIMIT);
    }

    public CreditsLimit(final DegreeModule degreeModuleToApplyRule, final CourseGroup contextCourseGroup,
            final ExecutionInterval begin, final ExecutionInterval end, final Double minimum, final Double maximum) {

        this(minimum, maximum);
        checkParameters(degreeModuleToApplyRule);
        init(degreeModuleToApplyRule, contextCourseGroup, begin, end);
    }

    private void checkParameters(final DegreeModule degreeModuleToApplyRule) {
        if (degreeModuleToApplyRule.isLeaf() && !((CurricularCourse) degreeModuleToApplyRule).isOptionalCurricularCourse()) {
            throw new DomainException(
                    "error.curricularRules.CreditsLimit.invalid.degreeModule.must.be.group.or.optional.curricularCourse");
        }
    }

    protected void edit(CourseGroup contextCourseGroup, Double minimumCredits, Double maximumCredits) {
        checkCredits(minimumCredits, maximumCredits);
        setContextCourseGroup(contextCourseGroup);
        setMinimumCredits(minimumCredits);
        setMaximumCredits(maximumCredits);
    }

    private void checkCredits(Double minimum, Double maximum) throws DomainException {
        if (minimum == null || maximum == null) {
            throw new DomainException("curricular.rule.invalid.parameters");
        }
        if (minimum.doubleValue() > maximum.doubleValue()) {
            throw new DomainException("error.minimum.greater.than.maximum");
        }
    }

    @Override
    public List<GenericPair<Object, Boolean>> getLabel() {
        List<GenericPair<Object, Boolean>> labelList = new ArrayList<GenericPair<Object, Boolean>>();

        labelList.add(new GenericPair<Object, Boolean>("label.creditsForApproval", true));
        labelList.add(new GenericPair<Object, Boolean>(": ", false));
        if (getMinimumCredits().doubleValue() == getMaximumCredits().doubleValue()) {
            labelList.add(new GenericPair<Object, Boolean>(getMinimumCredits(), false));
        } else {
            labelList.add(new GenericPair<Object, Boolean>(getMinimumCredits(), false));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>("label.to", true));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>(getMaximumCredits(), false));
        }
        if (getContextCourseGroup() != null) {
            labelList.add(new GenericPair<Object, Boolean>(", ", false));
            labelList.add(new GenericPair<Object, Boolean>("label.inGroup", true));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>(getContextCourseGroup().getOneFullName(), false));
        }

        return labelList;
    }

    @Override
    protected void removeOwnParameters() {
        // no domain parameters
    }

    public boolean allowCredits(final Double numberOfCredits) {
        return !(numberOfCredits.compareTo(getMinimumCredits()) < 0 || numberOfCredits.compareTo(getMaximumCredits()) > 0);
    }

    public boolean creditsExceedMaximum(final Double numberOfCredits) {
        return numberOfCredits.compareTo(getMaximumCredits()) > 0;
    }

    @Override
    public VerifyRuleExecutor createVerifyRuleExecutor() {
        return VerifyRuleExecutor.NULL_VERIFY_EXECUTOR;
    }

}
