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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.curricularRules.executors.verifyExecutors.VerifyRuleExecutor;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.degreeStructure.OptionalCurricularCourse;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicPeriod;
import org.fenixedu.academic.dto.GenericPair;

public class AnyCurricularCourse extends AnyCurricularCourse_Base {

    public AnyCurricularCourse(final OptionalCurricularCourse toApplyRule, final CourseGroup contextCourseGroup,
            final ExecutionInterval begin, final ExecutionInterval end, final Double minimumCredits, Double maximumCredits) {

        super();

        // TODO: check if already has this rule in toApplyRule

        init(toApplyRule, contextCourseGroup, begin, end, CurricularRuleType.ANY_CURRICULAR_COURSE);

        setMinimumCredits(minimumCredits);
        setMaximumCredits(maximumCredits);
        setCurricularPeriodOrder(0);//Deprecated (compatibility reasons only)
    }

    @Override
    public OptionalCurricularCourse getDegreeModuleToApplyRule() {
        return (OptionalCurricularCourse) super.getDegreeModuleToApplyRule();
    }

    @Override
    public boolean appliesToContext(final Context context) {
        return super.appliesToContext(context) && appliesToPeriod(context);
    }

    private boolean appliesToPeriod(final Context context) {
        return !hasCurricularPeriodOrder() || hasCurricularPeriodOrderFor(context);
    }

    /**
     * @deprecated
     *             Replaced by {@link CurricularRule#getCurricularPeriod()}
     */
    @Deprecated
    private boolean hasCurricularPeriodOrderFor(final Context context) {
        return context != null && context.getCurricularPeriod().getAcademicPeriod().equals(AcademicPeriod.SEMESTER)
                && getCurricularPeriodOrder().intValue() == context.getCurricularPeriod().getChildOrder().intValue();
    }

    /**
     * @deprecated
     *             Replaced by {@link CurricularRule#getCurricularPeriod()}
     */
    @Deprecated
    private boolean hasCurricularPeriodOrder() {
        return getCurricularPeriodOrder() != null && getCurricularPeriodOrder().intValue() != 0;
    }

    /**
     * @deprecated
     *             Replaced by {@link CurricularRule#getCurricularPeriod()}
     */
    @Override
    public Integer getCurricularPeriodOrder() {
        return super.getCurricularPeriodOrder();
    }

    @Override
    public List<GenericPair<Object, Boolean>> getLabel() {
        final List<GenericPair<Object, Boolean>> labelList = new ArrayList<GenericPair<Object, Boolean>>();

        if (Boolean.TRUE.equals(getNegation())) {
            labelList.add(new GenericPair<Object, Boolean>("label.except.upper", true));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
        }

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

        //deprecated
        if (getCurricularPeriodOrder().intValue() != 0) {
            labelList.add(new GenericPair<Object, Boolean>(", ", false));
            labelList.add(new GenericPair<Object, Boolean>("label.in", true));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>(getCurricularPeriodOrder(), false));
            labelList.add(new GenericPair<Object, Boolean>("º ", false));
            labelList.add(new GenericPair<Object, Boolean>("SEMESTER", true));
        }

        if (!getDegreesSet().isEmpty()) {
            labelList.add(new GenericPair<Object, Boolean>(", ", false));
            labelList.add(new GenericPair<Object, Boolean>("label.ofDegrees", true));
            labelList.add(new GenericPair<Object, Boolean>(": [", false));
            labelList.add(new GenericPair<Object, Boolean>(
                    getDegreesSet().stream().map(d -> d.getAcronym()).collect(Collectors.joining(", ")), false));
            labelList.add(new GenericPair<Object, Boolean>("]", false));
        } else if (!getDegreeTypesSet().isEmpty()) {
            labelList.add(new GenericPair<Object, Boolean>(", ", false));
            labelList.add(new GenericPair<Object, Boolean>("label.ofDegreeTypes", true));
            labelList.add(new GenericPair<Object, Boolean>(": [", false));
            labelList.add(new GenericPair<Object, Boolean>(
                    getDegreeTypesSet().stream().map(dt -> dt.getName().getContent()).collect(Collectors.joining(", ")), false));
            labelList.add(new GenericPair<Object, Boolean>("]", false));
        }

        if (!getCompetenceCourseLevelTypesSet().isEmpty() && getCompetenceCoursesSet().isEmpty()) {
            labelList.add(new GenericPair<Object, Boolean>(", ", false));
            labelList.add(new GenericPair<Object, Boolean>("label.ofLevels", true));
            labelList.add(new GenericPair<Object, Boolean>(": [", false));
            labelList.add(new GenericPair<Object, Boolean>(getCompetenceCourseLevelTypesSet().stream()
                    .map(l -> l.getName().getContent()).sorted().collect(Collectors.joining(", ")), false));
            labelList.add(new GenericPair<Object, Boolean>("]", false));
        }

        if (!getCompetenceCoursesSet().isEmpty()) {
            labelList.add(new GenericPair<Object, Boolean>(", ", false));
            labelList.add(new GenericPair<Object, Boolean>("label.ofList", true));
            labelList.add(new GenericPair<Object, Boolean>(": [", false));
            labelList.add(new GenericPair<Object, Boolean>(getCompetenceCoursesSet().stream()
                    .map(c -> c.getNameI18N().getContent()).sorted().collect(Collectors.joining(", ")), false));
            labelList.add(new GenericPair<Object, Boolean>("]", false));
        }

        if (!getUnitsSet().isEmpty()) {
            labelList.add(new GenericPair<Object, Boolean>(", ", false));
            final Map<String, List<Unit>> unitsByType = getUnitsSet().stream().sorted(Unit.COMPARATOR_BY_NAME).collect(
                    Collectors.groupingBy((Unit u) -> u.getPartyType().getName(), LinkedHashMap::new, Collectors.toList()));

            final AtomicBoolean firstPartyType = new AtomicBoolean(true);
            unitsByType.forEach((partyType, units) -> {
                if (!firstPartyType.get()) {
                    labelList.add(new GenericPair<Object, Boolean>(" ", false));
                    labelList.add(new GenericPair<Object, Boolean>("label.or.upper", true));
                    labelList.add(new GenericPair<Object, Boolean>(" ", false));
                } else {
                    labelList.add(new GenericPair<Object, Boolean>("label.belongingTo", true));
                    labelList.add(new GenericPair<Object, Boolean>(" ", false));
                    firstPartyType.set(false);
                }

                labelList.add(new GenericPair<Object, Boolean>(partyType, false));
                labelList.add(new GenericPair<Object, Boolean>(": [", false));
                labelList.add(new GenericPair<Object, Boolean>(
                        units.stream().map(u -> u.getNameI18n().getContent()).collect(Collectors.joining(", ")), false));
                labelList.add(new GenericPair<Object, Boolean>("]", false));
            });

        }

        return labelList;
    }

    @Override
    protected void removeOwnParameters() {
        getCompetenceCourseLevelTypesSet().clear();
        getCompetenceCoursesSet().clear();
        getDegreeTypesSet().clear();
        getDegreesSet().clear();
        getUnitsSet().clear();
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
