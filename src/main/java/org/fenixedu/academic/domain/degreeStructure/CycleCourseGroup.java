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

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.exceptions.DomainException;

public class CycleCourseGroup extends CycleCourseGroup_Base {

    protected CycleCourseGroup() {
        super();
    }

    public CycleCourseGroup(final RootCourseGroup parentCourseGroup, final String name, final String nameEn,
            final CycleType cycleType, final ExecutionInterval begin, final ExecutionInterval end) {
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

}
