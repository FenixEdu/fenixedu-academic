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
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.GenericPair;

public class DegreeModulesSelectionLimit extends DegreeModulesSelectionLimit_Base {

    private DegreeModulesSelectionLimit(final Integer minimum, final Integer maximum) {
        super();
        checkLimits(minimum, maximum);
        setMinimumLimit(minimum);
        setMaximumLimit(maximum);
        setCurricularRuleType(CurricularRuleType.DEGREE_MODULES_SELECTION_LIMIT);
    }

    protected DegreeModulesSelectionLimit(final CourseGroup degreeModuleToApplyRule, final CourseGroup contextCourseGroup,
            final ExecutionSemester begin, final ExecutionSemester end, final Integer minimum, final Integer maximum) {

        this(minimum, maximum);
        init(degreeModuleToApplyRule, contextCourseGroup, begin, end);
    }

    protected void edit(CourseGroup contextCourseGroup, Integer minimumLimit, Integer maximumLimit) {
        checkLimits(minimumLimit, maximumLimit);
        setContextCourseGroup(contextCourseGroup);
        setMinimumLimit(minimumLimit);
        setMaximumLimit(maximumLimit);
    }

    private void checkLimits(Integer minimum, Integer maximum) throws DomainException {
        if (minimum == null || maximum == null) {
            throw new DomainException("curricular.rule.invalid.parameters");
        }
        if (minimum.intValue() > maximum.intValue()) {
            throw new DomainException("error.minimum.greater.than.maximum");
        }
    }

    @Override
    public CourseGroup getDegreeModuleToApplyRule() {
        return (CourseGroup) super.getDegreeModuleToApplyRule();
    }

    @Override
    public List<GenericPair<Object, Boolean>> getLabel() {
        return getLabel(null);
    }

    @Override
    public List<GenericPair<Object, Boolean>> getLabel(final ExecutionSemester executionSemester) {
        List<GenericPair<Object, Boolean>> labelList = new ArrayList<GenericPair<Object, Boolean>>();

        labelList.add(new GenericPair<Object, Boolean>("label.modulesSelection", true));
        labelList.add(new GenericPair<Object, Boolean>(": ", false));
        if (getMinimumLimit().intValue() == getMaximumLimit().intValue()) {
            labelList.add(new GenericPair<Object, Boolean>("label.choose", true));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>(getMinimumLimit(), false));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            if (getMinimumLimit().intValue() == 1) {
                labelList.add(new GenericPair<Object, Boolean>("label.module", true));
            } else {
                labelList.add(new GenericPair<Object, Boolean>("label.modules", true));
            }
        } else {
            labelList.add(new GenericPair<Object, Boolean>("label.chooseFrom", true));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>(getMinimumLimit(), false));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>("label.to", true));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>(getMaximumLimit(), false));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>("label.modules", true));
        }
        if (getContextCourseGroup() != null) {
            labelList.add(new GenericPair<Object, Boolean>(", ", false));
            labelList.add(new GenericPair<Object, Boolean>("label.inGroup", true));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>(getContextCourseGroup().getOneFullName(executionSemester), false));
        }
        return labelList;
    }

    @Override
    protected void removeOwnParameters() {
        // no domain parameters
    }

    public boolean allowNumberOfDegreeModules(final Integer value) {
        return !(value.compareTo(getMinimumLimit()) < 0 || value.compareTo(getMaximumLimit()) > 0);
    }

    public boolean numberOfDegreeModulesExceedMaximum(final Integer numberOfDegreeModules) {
        return numberOfDegreeModules.compareTo(getMaximumLimit()) > 0;
    }

    @Override
    public VerifyRuleExecutor createVerifyRuleExecutor() {
        return VerifyRuleExecutor.NULL_VERIFY_EXECUTOR;
    }

}
