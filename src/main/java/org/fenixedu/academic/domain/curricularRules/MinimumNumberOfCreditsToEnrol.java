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
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.GenericPair;

public class MinimumNumberOfCreditsToEnrol extends MinimumNumberOfCreditsToEnrol_Base {

    private MinimumNumberOfCreditsToEnrol(final Double minimumNumberOfCredits) {
        super();
        checkCredits(minimumNumberOfCredits);
        setMinimumCredits(minimumNumberOfCredits);
        setCurricularRuleType(CurricularRuleType.MINIMUM_NUMBER_OF_CREDITS_TO_ENROL);
    }

    private void checkCredits(final Double minimumNumberOfCredits) throws DomainException {
        if (minimumNumberOfCredits == null) {
            throw new DomainException("curricular.rule.invalid.parameters");
        }
    }

    protected MinimumNumberOfCreditsToEnrol(final DegreeModule degreeModuleToApplyRule, final CourseGroup contextCourseGroup,
            final ExecutionSemester begin, final ExecutionSemester end, final Double minimumNumberOfCredits) {

        this(minimumNumberOfCredits);
        init(degreeModuleToApplyRule, contextCourseGroup, begin, end);
    }

    protected void edit(final CourseGroup contextCourseGroup, final Double minimumNumberOfCredits) {
        checkCredits(minimumNumberOfCredits);
        setContextCourseGroup(contextCourseGroup);
        setMinimumCredits(minimumNumberOfCredits);
    }

    @Override
    protected void removeOwnParameters() {
        // no domain parameters
    }

    @Override
    public List<GenericPair<Object, Boolean>> getLabel() {
        return getLabel(null);
    }

    @Override
    public List<GenericPair<Object, Boolean>> getLabel(final ExecutionSemester executionSemester) {
        List<GenericPair<Object, Boolean>> labelList = new ArrayList<GenericPair<Object, Boolean>>();

        labelList.add(new GenericPair<Object, Boolean>("label.minimumNumberOfCreditsToEnrol", true));
        labelList.add(new GenericPair<Object, Boolean>(": ", false));
        labelList.add(new GenericPair<Object, Boolean>(getMinimumCredits(), false));

        if (getContextCourseGroup() != null) {
            labelList.add(new GenericPair<Object, Boolean>(", ", false));
            labelList.add(new GenericPair<Object, Boolean>("label.inGroup", true));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>(getContextCourseGroup().getOneFullName(executionSemester), false));
        }

        return labelList;
    }

    public boolean allowCredits(final Double credits) {
        return credits.doubleValue() >= getMinimumCredits().doubleValue();
    }

    @Override
    public VerifyRuleExecutor createVerifyRuleExecutor() {
        return VerifyRuleExecutor.NULL_VERIFY_EXECUTOR;
    }

}
