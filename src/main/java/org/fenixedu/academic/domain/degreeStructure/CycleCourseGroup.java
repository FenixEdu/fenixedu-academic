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
package org.fenixedu.academic.domain.degreeStructure;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.exceptions.DomainException;
import pt.ist.fenixframework.Atomic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class CycleCourseGroup extends CycleCourseGroup_Base {

    protected CycleCourseGroup() {
        super();
    }

    public CycleCourseGroup(final RootCourseGroup parentCourseGroup, final String name, final String nameEn,
            final CycleType cycleType, final ExecutionSemester begin, final ExecutionSemester end) {
        if (cycleType == null) {
            throw new DomainException("error.degreeStructure.CycleCourseGroup.cycle.type.cannot.be.null");
        }
        init(parentCourseGroup, name, nameEn, begin, end);
        setCycleType(cycleType);
    }

    @Override
    public void delete() {
        getSourceAffinitiesSet().clear();
        getDestinationAffinitiesSet().clear();
        super.delete();
    }

    @Override
    public boolean isCycleCourseGroup() {
        return true;
    }

    final public String getGraduateTitleSuffix(final ExecutionYear executionYear, final Locale locale) {
        CycleCourseGroupInformation courseGroupInformationForSuffix =
                getMostRecentCycleCourseGroupInformation(executionYear, true);
        if (courseGroupInformationForSuffix != null) {
            return courseGroupInformationForSuffix.getGraduateTitleSuffix().getContent(locale);
        }
        return null;
    }

    public boolean isFirstCycle() {
        return getCycleType() == CycleType.FIRST_CYCLE;
    }

    public boolean isSecondCycle() {
        return getCycleType() == CycleType.SECOND_CYCLE;
    }

    public boolean isThirdCycle() {
        return getCycleType() == CycleType.THIRD_CYCLE;
    }

    public boolean isSpecializationCycle() {
        return getCycleType() == CycleType.SPECIALIZATION_CYCLE;
    }

    @Override
    public Collection<CycleCourseGroup> getParentCycleCourseGroups() {
        return Collections.singletonList(this);
    }

    public Double getCurrentDefaultEcts() {
        return getDefaultEcts(ExecutionYear.readCurrentExecutionYear());
    }

    public List<CycleCourseGroupInformation> getCycleCourseGroupInformationOrderedByExecutionYear() {
        List<CycleCourseGroupInformation> groupInformationList =
                new ArrayList<CycleCourseGroupInformation>(getCycleCourseGroupInformationSet());
        Collections.sort(groupInformationList, CycleCourseGroupInformation.COMPARATOR_BY_EXECUTION_YEAR);

        return groupInformationList;
    }

    public CycleCourseGroupInformation getCycleCourseGroupInformationByExecutionYear(final ExecutionYear executionYear) {
        for (CycleCourseGroupInformation cycleInformation : getCycleCourseGroupInformationSet()) {
            if (cycleInformation.getExecutionYear() == executionYear) {
                return cycleInformation;
            }
        }

        return null;
    }

    public CycleCourseGroupInformation getMostRecentCycleCourseGroupInformation(final ExecutionYear executionYear,
            boolean isForSuffix) {
        CycleCourseGroupInformation mostRecent = null;

        for (CycleCourseGroupInformation cycleInformation : getCycleCourseGroupInformationSet()) {
            if (cycleInformation.getExecutionYear().isAfter(executionYear)) {
                continue;
            }

            if ((mostRecent == null) || cycleInformation.getExecutionYear().isAfter(mostRecent.getExecutionYear())) {
                mostRecent = cycleInformation;
            }
        }

        return mostRecent;
    }

    @Atomic
    public CycleCourseGroupInformation createCycleCourseGroupInformation(final ExecutionYear executionYear,
            String graduatedTitleSuffix, String graduatedTitleSuffixEn) {
        if (getCycleCourseGroupInformationByExecutionYear(executionYear) != null) {
            throw new DomainException("cycle.course.group.information.exists.in.execution.year");
        }

        return new CycleCourseGroupInformation(this, executionYear, graduatedTitleSuffix, graduatedTitleSuffixEn);
    }

    public Set<CycleCourseGroup> getAllPossibleAffinities() {

        final Set<CycleType> affinityCycles = new HashSet<CycleType>();
        for (final CycleType cycleType : CycleType.values()) {
            if (cycleType.getSourceCycleAffinity() == getCycleType()) {
                affinityCycles.add(cycleType);
            }
        }

        final Set<CycleCourseGroup> result = new HashSet<CycleCourseGroup>();
        for (final Degree degree : Degree.readNotEmptyDegrees()) {
            for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlansSet()) {
                for (final CycleType affinityCycle : affinityCycles) {
                    final CycleCourseGroup cycleCourseGroup = degreeCurricularPlan.getCycleCourseGroup(affinityCycle);
                    if (cycleCourseGroup != null && cycleCourseGroup != this) {
                        result.add(cycleCourseGroup);
                    }
                }
            }
        }

        return result;

    }

    public CycleCourseGroupInformation findCycleCourseGroupInformationBy(final ExecutionInterval executionInterval) {
        for (final CycleCourseGroupInformation each : getCycleCourseGroupInformationSet()) {
            if (each.isFor(executionInterval)) {
                return each;
            }
        }

        return null;
    }

    public boolean hasParentCycleType(final CycleType cycleType) {
        return cycleType == getCycleType();
    }

}
