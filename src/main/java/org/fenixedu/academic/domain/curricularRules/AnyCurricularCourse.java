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
/*
 * Created on Feb 17, 2006
 */
package org.fenixedu.academic.domain.curricularRules;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.curricularRules.executors.verifyExecutors.VerifyRuleExecutor;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.degreeStructure.OptionalCurricularCourse;
import org.fenixedu.academic.domain.degreeStructure.RegimeType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.DepartmentUnit;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicPeriod;
import org.fenixedu.academic.dto.GenericPair;

public class AnyCurricularCourse extends AnyCurricularCourse_Base {

    protected AnyCurricularCourse(final OptionalCurricularCourse toApplyRule, final CourseGroup contextCourseGroup,
            final ExecutionSemester begin, final ExecutionSemester end, final Double minimumCredits, Double maximumCredits,
            final Integer curricularPeriodOrder, final Integer minimumYear, final Integer maximumYear,
            final DegreeType degreeType, final Degree degree, final DepartmentUnit departmentUnit) {

        super();

        // TODO: check if already has this rule in toApplyRule

        init(toApplyRule, contextCourseGroup, begin, end, CurricularRuleType.ANY_CURRICULAR_COURSE);

        checkYears(minimumYear, maximumYear);

        setMinimumCredits(minimumCredits);
        setMaximumCredits(maximumCredits);
        setCurricularPeriodOrder(curricularPeriodOrder);
        setMinimumYear(minimumYear);
        setMaximumYear(maximumYear);
        setBolonhaDegreeType(degreeType);
        setDegree(degree);
        setDepartmentUnit(departmentUnit);
    }

    protected void edit(final CourseGroup contextCourseGroup, final Double credits, final Integer curricularPeriodOrder,
            final Integer minimumYear, final Integer maximumYear, final DegreeType degreeType, final Degree degree,
            final DepartmentUnit departmentUnit) {

        checkYears(minimumYear, maximumYear);
        setContextCourseGroup(contextCourseGroup);
        setCredits(credits);
        setCurricularPeriodOrder(curricularPeriodOrder);
        setMinimumYear(minimumYear);
        setMaximumYear(maximumYear);
        setBolonhaDegreeType(degreeType);
        setDegree(degree);
        setDepartmentUnit(departmentUnit);
    }

    private void checkYears(Integer minimumYear, Integer maximumYear) throws DomainException {
        if (minimumYear != null && maximumYear != null && minimumYear.intValue() > maximumYear.intValue()) {
            throw new DomainException("error.minimum.greater.than.maximum");
        }
    }

    @Override
    public OptionalCurricularCourse getDegreeModuleToApplyRule() {
        return (OptionalCurricularCourse) super.getDegreeModuleToApplyRule();
    }

    @Override
    public boolean appliesToContext(final Context context) {
        return super.appliesToContext(context) && appliesToPeriod(context) && appliesToYears(context);
    }

    private boolean appliesToPeriod(final Context context) {
        return !hasCurricularPeriodOrder() || hasCurricularPeriodOrderFor(context);
    }

    private boolean hasCurricularPeriodOrderFor(final Context context) {
        return context != null && context.containsSemester(getCurricularPeriodOrder())
                && context.getCurricularPeriod().getAcademicPeriod().equals(AcademicPeriod.SEMESTER);
    }

    private boolean hasCurricularPeriodOrder() {
        return getCurricularPeriodOrder() != null && getCurricularPeriodOrder().intValue() != 0;
    }

    private boolean hasYearsLimit() {
        return getMinimumYear() != null && getMinimumYear().intValue() != 0 && getMaximumYear() != null
                && getMaximumYear().intValue() != 0;
    }

    private boolean appliesToYears(final Context context) {
        if (!hasYearsLimit()) {
            return true;
        }

        if (hasCurricularPeriodOrder()) {
            for (int year = getMinimumYear().intValue(); year <= getMaximumYear().intValue(); year++) {
                if (context.containsSemesterAndCurricularYear(getCurricularPeriodOrder(), Integer.valueOf(year),
                        RegimeType.SEMESTRIAL)) {
                    return true;
                }
            }
        } else {
            for (int year = getMinimumYear().intValue(); year <= getMaximumYear().intValue(); year++) {
                if (context.containsSemesterAndCurricularYear(Integer.valueOf(1), Integer.valueOf(year), RegimeType.SEMESTRIAL)
                        || context.containsSemesterAndCurricularYear(Integer.valueOf(2), Integer.valueOf(year),
                                RegimeType.SEMESTRIAL)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<GenericPair<Object, Boolean>> getLabel() {
        return getLabel(null);
    }

    @Override
    public List<GenericPair<Object, Boolean>> getLabel(final ExecutionSemester executionSemester) {
        final List<GenericPair<Object, Boolean>> labelList = new ArrayList<GenericPair<Object, Boolean>>();

        labelList.add(new GenericPair<Object, Boolean>("label.anyCurricularCourse", true));

        if (getMinimumCredits() != null && getMaximumCredits() != null) {
            labelList.add(new GenericPair<Object, Boolean>(", ", false));

            if (getMinimumCredits().doubleValue() == getMaximumCredits().doubleValue()) {
                labelList.add(new GenericPair<Object, Boolean>("label.with", true));
                labelList.add(new GenericPair<Object, Boolean>(" ", false));
                labelList.add(new GenericPair<Object, Boolean>(getMinimumCredits(), false));
            } else {
                labelList.add(new GenericPair<Object, Boolean>("label.with.minimum", true));
                labelList.add(new GenericPair<Object, Boolean>(" ", false));
                labelList.add(new GenericPair<Object, Boolean>(getMinimumCredits(), false));
                labelList.add(new GenericPair<Object, Boolean>(" ", false));
                labelList.add(new GenericPair<Object, Boolean>("label.to", true));
                labelList.add(new GenericPair<Object, Boolean>(" ", false));
                labelList.add(new GenericPair<Object, Boolean>(getMaximumCredits(), false));
            }
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>("label.credits", true));

        }

        if ((getMinimumCredits() == null && getMaximumCredits() != null)
                || (getMinimumCredits() != null && getMaximumCredits() == null)) {
            labelList.add(new GenericPair<Object, Boolean>("label.with", true));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>(getMinimumCredits(), false));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>("label.credits", true));
        }

        if (getCurricularPeriodOrder().intValue() != 0) {
            labelList.add(new GenericPair<Object, Boolean>(", ", false));
            labelList.add(new GenericPair<Object, Boolean>("label.in", true));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>(getCurricularPeriodOrder(), false));
            labelList.add(new GenericPair<Object, Boolean>("º ", false));
            labelList.add(new GenericPair<Object, Boolean>("SEMESTER", true));
        }
        if (hasYearsLimit()) {
            if (getMinimumYear().compareTo(getMaximumYear()) == 0) {
                labelList.add(new GenericPair<Object, Boolean>(", ", false));
                labelList.add(new GenericPair<Object, Boolean>("label.of", true));
                labelList.add(new GenericPair<Object, Boolean>("º ", false));
                labelList.add(new GenericPair<Object, Boolean>(getMinimumYear(), false));
                labelList.add(new GenericPair<Object, Boolean>(" ", false));
                labelList.add(new GenericPair<Object, Boolean>("label.year", true));
            } else {
                labelList.add(new GenericPair<Object, Boolean>(", ", false));
                labelList.add(new GenericPair<Object, Boolean>("label.of", true));
                labelList.add(new GenericPair<Object, Boolean>(" ", false));
                labelList.add(new GenericPair<Object, Boolean>(getMinimumYear(), false));
                labelList.add(new GenericPair<Object, Boolean>("º ", false));
                labelList.add(new GenericPair<Object, Boolean>("label.to1", true));
                labelList.add(new GenericPair<Object, Boolean>(" ", false));
                labelList.add(new GenericPair<Object, Boolean>(getMaximumYear(), false));
                labelList.add(new GenericPair<Object, Boolean>("º ", false));
                labelList.add(new GenericPair<Object, Boolean>("label.year", true));
            }
        }

        labelList.add(new GenericPair<Object, Boolean>(", ", false));
        if (getDegree() == null) {
            if (!hasBolonhaDegreeType()) {
                labelList.add(new GenericPair<Object, Boolean>("label.of", true));
                labelList.add(new GenericPair<Object, Boolean>(" ", false));
                labelList.add(new GenericPair<Object, Boolean>(Unit.getInstitutionAcronym(), false));
            } else {
                labelList.add(new GenericPair<Object, Boolean>("label.of1", true));
                labelList.add(new GenericPair<Object, Boolean>(" ", false));
                labelList.add(new GenericPair<Object, Boolean>(getBolonhaDegreeType().getName().getContent(), false));
            }
        } else {
            labelList.add(new GenericPair<Object, Boolean>("label.of", true));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>("label.degree", true));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>(getDegree().getNameFor(executionSemester).getContent(), false));
        }

        if (getDepartmentUnit() != null) {
            labelList.add(new GenericPair<Object, Boolean>(", ", false));
            labelList.add(new GenericPair<Object, Boolean>("label.of", true));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>(getDepartmentUnit().getName(), false));
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
        setDegree(null);
        setDepartmentUnit(null);
        setBolonhaDegreeType(null);
    }

    public boolean hasBolonhaDegreeType() {
        return getBolonhaDegreeType() != null;
    }

    public boolean hasCredits() {
        return getCredits() != null && getCredits().doubleValue() != 0d;
    }

    public boolean hasMinimumCredits() {
        return getMinimumCredits() != null && getMinimumCredits().doubleValue() != 0d;
    }

    public boolean hasMaximumCredits() {
        return getMaximumCredits() != null && getMaximumCredits().doubleValue() != 0d;
    }

    @Override
    public VerifyRuleExecutor createVerifyRuleExecutor() {
        return VerifyRuleExecutor.NULL_VERIFY_EXECUTOR;
    }

}
