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
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.util.LogicOperator;

public class RestrictionBetweenDegreeModules extends RestrictionBetweenDegreeModules_Base {

    private RestrictionBetweenDegreeModules(final DegreeModule precedenceDegreeModule, final Double minimumCredits) {
        super();
        checkParameters(precedenceDegreeModule, minimumCredits);
        setPrecedenceDegreeModule(precedenceDegreeModule);
        setMinimumCredits(minimumCredits);
        setCurricularRuleType(CurricularRuleType.PRECEDENCY_BETWEEN_DEGREE_MODULES);
    }

    private void checkParameters(final DegreeModule precedenceDegreeModule, final Double minimumCredits) throws DomainException {
        if (precedenceDegreeModule == null || minimumCredits == null) {
            throw new DomainException("curricular.rule.invalid.parameters");
        }
    }

    protected RestrictionBetweenDegreeModules(final DegreeModule degreeModuleToApplyRule,
            final DegreeModule precedenceDegreeModule, final Double minimumCredits, final CourseGroup contextCourseGroup,
            final ExecutionSemester begin, final ExecutionSemester end) {

        this(precedenceDegreeModule, minimumCredits);
        init(degreeModuleToApplyRule, contextCourseGroup, begin, end);
    }

    protected void edit(final DegreeModule precedenceDegreeModule, final Double minimumCredits,
            final CourseGroup contextCourseGroup) {
        checkParameters(precedenceDegreeModule, minimumCredits);
        setPrecedenceDegreeModule(precedenceDegreeModule);
        setMinimumCredits(minimumCredits);
        setContextCourseGroup(contextCourseGroup);
    }

    @Override
    public List<GenericPair<Object, Boolean>> getLabel() {
        final List<GenericPair<Object, Boolean>> labelList = new ArrayList<GenericPair<Object, Boolean>>();

        if (belongsToCompositeRule() && getParentCompositeRule().getCompositeRuleType().equals(LogicOperator.NOT)) {
            labelList.add(new GenericPair<Object, Boolean>("label.precedence", true));
        } else {
            labelList.add(new GenericPair<Object, Boolean>("label.precedence", true));
        }

        labelList.add(new GenericPair<Object, Boolean>(" ", false));
        labelList.add(new GenericPair<Object, Boolean>("label.of", true));
        labelList.add(new GenericPair<Object, Boolean>(" ", false));
        labelList.add(new GenericPair<Object, Boolean>("label.module", true));
        labelList.add(new GenericPair<Object, Boolean>(": ", false));

        // getting full name only for course groups
        String precedenceDegreeModule =
                (getPrecedenceDegreeModule().isLeaf()) ? getPrecedenceDegreeModule().getName() : getPrecedenceDegreeModule()
                        .getOneFullName();
        labelList.add(new GenericPair<Object, Boolean>(precedenceDegreeModule, false));

        if (getMinimumCredits().doubleValue() != 0.0) {
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>("label.with", true));
            labelList.add(new GenericPair<Object, Boolean>(", ", false));

            labelList.add(new GenericPair<Object, Boolean>("label.in", true));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>("label.minimum", true));
            labelList.add(new GenericPair<Object, Boolean>(", ", false));

            labelList.add(new GenericPair<Object, Boolean>(getMinimumCredits(), false));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>("label.credits", true));
        }

        if (getContextCourseGroup() != null) {
            labelList.add(new GenericPair<Object, Boolean>(", ", false));
            labelList.add(new GenericPair<Object, Boolean>("label.inContext", true));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>(getContextCourseGroup().getOneFullName(), false));
        }
        return labelList;
    }

    @Override
    protected boolean appliesToPeriod(final Context context) {
        return getDegreeModuleToApplyRule().isCourseGroup() || super.appliesToPeriod(context);
    }

    public boolean hasMinimumCredits() {
        return getMinimumCredits() != null && getMinimumCredits().doubleValue() != 0.0;
    }

    public boolean allowCredits(final Double credits) {
        return credits.doubleValue() >= getMinimumCredits().doubleValue();
    }

    @Override
    public VerifyRuleExecutor createVerifyRuleExecutor() {
        return VerifyRuleExecutor.NULL_VERIFY_EXECUTOR;
    }

}
