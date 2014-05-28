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
package net.sourceforge.fenixedu.domain.curricularRules;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.curricularRules.executors.verifyExecutors.VerifyRuleExecutor;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

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
            labelList.add(new GenericPair<Object, Boolean>(getContextCourseGroup().getOneFullName(), false));
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

    @Deprecated
    public boolean hasMinimumLimit() {
        return getMinimumLimit() != null;
    }

    @Deprecated
    public boolean hasMaximumLimit() {
        return getMaximumLimit() != null;
    }

}
