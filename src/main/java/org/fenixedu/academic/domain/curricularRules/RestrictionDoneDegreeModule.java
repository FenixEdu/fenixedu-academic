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
import org.fenixedu.academic.domain.curricularRules.executors.verifyExecutors.RestrictionDoneDegreeModuleVerifier;
import org.fenixedu.academic.domain.curricularRules.executors.verifyExecutors.VerifyRuleExecutor;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.util.LogicOperator;
import org.fenixedu.academic.dto.CurricularPeriodInfoDTO;
import org.fenixedu.academic.dto.GenericPair;

public class RestrictionDoneDegreeModule extends RestrictionDoneDegreeModule_Base {

    private RestrictionDoneDegreeModule(final DegreeModule done) {
        super();
        if (done == null) {
            throw new DomainException("curricular.rule.invalid.parameters");
        }
        setPrecedenceDegreeModule(done);
        setCurricularRuleType(CurricularRuleType.PRECEDENCY_APPROVED_DEGREE_MODULE);
    }

    public RestrictionDoneDegreeModule(CurricularCourse toApply, CurricularCourse done, CourseGroup contextCourseGroup,
            CurricularPeriodInfoDTO curricularPeriodInfoDTO, ExecutionSemester begin, ExecutionSemester end) {

        this(done);
        init(toApply, contextCourseGroup, begin, end);
        if (curricularPeriodInfoDTO != null) {
            setAcademicPeriod(curricularPeriodInfoDTO.getPeriodType());
            setCurricularPeriodOrder(curricularPeriodInfoDTO.getOrder());
        }
    }

    @Override
    public CurricularCourse getDegreeModuleToApplyRule() {
        return (CurricularCourse) super.getDegreeModuleToApplyRule();
    }

    @Override
    public CurricularCourse getPrecedenceDegreeModule() {
        return (CurricularCourse) super.getPrecedenceDegreeModule();
    }

    protected void edit(DegreeModule done, CourseGroup contextCourseGroup, CurricularPeriodInfoDTO curricularPeriodInfoDTO) {
        setPrecedenceDegreeModule(done);
        setContextCourseGroup(contextCourseGroup);
        setAcademicPeriod(curricularPeriodInfoDTO.getPeriodType());
        setCurricularPeriodOrder(curricularPeriodInfoDTO.getOrder());
    }

    @Override
    public List<GenericPair<Object, Boolean>> getLabel() {
        return getLabel(null);
    }

    @Override
    public List<GenericPair<Object, Boolean>> getLabel(final ExecutionSemester executionSemester) {
        List<GenericPair<Object, Boolean>> labelList = new ArrayList<GenericPair<Object, Boolean>>();

        if (belongsToCompositeRule() && getParentCompositeRule().getCompositeRuleType().equals(LogicOperator.NOT)) {
            labelList.add(new GenericPair<Object, Boolean>("label.precedenceNotDone", true));
        } else {
            labelList.add(new GenericPair<Object, Boolean>("label.precedenceDone", true));
        }

        labelList.add(new GenericPair<Object, Boolean>(": ", false));

        // getting full name only for course groups
        String precedenceDegreeModule =
                (getPrecedenceDegreeModule().isLeaf()) ? getPrecedenceDegreeModule().getName(executionSemester) : getPrecedenceDegreeModule()
                        .getOneFullName(executionSemester);
        labelList.add(new GenericPair<Object, Boolean>(precedenceDegreeModule, false));

        if (getContextCourseGroup() != null) {
            labelList.add(new GenericPair<Object, Boolean>(", ", false));
            labelList.add(new GenericPair<Object, Boolean>("label.inContext", true));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>(getContextCourseGroup().getOneFullName(executionSemester), false));
        }

        if (!hasNoCurricularPeriodOrder()) {
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>("label.and", true));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>("label.in", true));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>(getAcademicPeriod().getName(), true));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>(getCurricularPeriodOrder(), false));
        }

        return labelList;
    }

    @Override
    public VerifyRuleExecutor createVerifyRuleExecutor() {
        return new RestrictionDoneDegreeModuleVerifier();
    }
}
