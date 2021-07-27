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

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.exceptions.DomainException;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;

public class RootCourseGroup extends RootCourseGroup_Base {

    public RootCourseGroup() {
        super();
    }

    public RootCourseGroup(final DegreeCurricularPlan degreeCurricularPlan, final String name, final String nameEn) {
        if (degreeCurricularPlan == null) {
            throw new DomainException("error.degreeStructure.CourseGroup.degreeCurricularPlan.cannot.be.null");
        }
        init(name, nameEn);
        setParentDegreeCurricularPlan(degreeCurricularPlan);
        createCycleCourseGroups(degreeCurricularPlan.getDegreeType());
    }

    private void createCycleCourseGroups(DegreeType degreeType) {
        ExecutionSemester executionSemester = ExecutionSemester.readActualExecutionSemester();
        for (final CycleType cycleType : degreeType.getCycleTypes()) {
            new CycleCourseGroup(this, cycleType.getDescription(Locale.getDefault()), cycleType.getDescription(Locale.ENGLISH),
                    cycleType, executionSemester, null);
        }
    }

    @Override
    public boolean isRoot() {
        return true;
    }

    @Override
    public void delete() {
        if (!getCanBeDeleted()) {
            throw new DomainException("courseGroup.notEmptyCourseGroupContexts");
        }
        removeChildDegreeModules();
        setParentDegreeCurricularPlan(null);
        super.delete();
    }

    private void removeChildDegreeModules() {
        for (final DegreeModule degreeModule : getChildDegreeModules()) {
            degreeModule.delete();
        }
    }

    @Override
    public Boolean getCanBeDeleted() {
        return getCurriculumModulesSet().isEmpty() && childsCanBeDeleted();
    }

    private boolean childsCanBeDeleted() {
        for (final Context context : getChildContextsSet()) {
            final DegreeModule degreeModule = context.getChildDegreeModule();
            if (!degreeModule.getCanBeDeleted()) {
                return false;
            }
        }
        return true;
    }

    static public RootCourseGroup createRoot(final DegreeCurricularPlan degreeCurricularPlan, final String name,
            final String nameEn) {
        return new RootCourseGroup(degreeCurricularPlan, name, nameEn);
    }

    @Override
    public void addParentContexts(Context parentContexts) {
        throw new DomainException("error.degreeStructure.RootCourseGroup.cannot.have.parent.contexts");
    }

    public CycleCourseGroup getFirstCycleCourseGroup() {
        return getCycleCourseGroup(CycleType.FIRST_CYCLE);
    }

    public CycleCourseGroup getSecondCycleCourseGroup() {
        return getCycleCourseGroup(CycleType.SECOND_CYCLE);
    }

    public CycleCourseGroup getThirdCycleCourseGroup() {
        return getCycleCourseGroup(CycleType.THIRD_CYCLE);
    }

    public CycleCourseGroup getCycleCourseGroup(CycleType cycle) {
        for (Context context : getChildContextsSet()) {
            if (context.getChildDegreeModule().isCycleCourseGroup()) {
                CycleCourseGroup cycleCourseGroup = (CycleCourseGroup) context.getChildDegreeModule();
                if (cycle == cycleCourseGroup.getCycleType()) {
                    return (CycleCourseGroup) context.getChildDegreeModule();
                }
            }
        }
        return null;
    }

    public Collection<CycleCourseGroup> getCycleCourseGroups() {
        Collection<CycleCourseGroup> result = new HashSet<CycleCourseGroup>();
        for (Context context : getChildContextsSet()) {
            if (context.getChildDegreeModule().isCycleCourseGroup()) {
                result.add((CycleCourseGroup) context.getChildDegreeModule());
            }
        }
        return result;
    }

    public boolean hasCycleGroups() {
        return !getCycleCourseGroups().isEmpty();
    }

    @Override
    public Collection<CycleCourseGroup> getParentCycleCourseGroups() {
        return Collections.emptySet();
    }

    public boolean hasParentCycleType(final CycleType cycleType) {
        return false;
    }

}
