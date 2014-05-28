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
package net.sourceforge.fenixedu.domain.degreeStructure;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

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

    private void createCycleCourseGroups(DegreeType courseGroupType) {
        if (courseGroupType.isBolonhaType()) {
            ExecutionSemester executionSemester = ExecutionSemester.readActualExecutionSemester();
            if (courseGroupType.isFirstCycle()) {
                new CycleCourseGroup(this, "1º Ciclo", "First Cycle", CycleType.FIRST_CYCLE, executionSemester, null);
            }
            if (courseGroupType.isSecondCycle()) {
                new CycleCourseGroup(this, "2º Ciclo", "Second Cycle", CycleType.SECOND_CYCLE, executionSemester, null);
            }
            if (courseGroupType.isThirdCycle()) {
                new CycleCourseGroup(this, "3º Ciclo", "Third Cycle", CycleType.THIRD_CYCLE, executionSemester, null);
            }

            if (courseGroupType.isSpecializationCycle()) {
                new CycleCourseGroup(this, "Especialização", "Specialization", CycleType.SPECIALIZATION_CYCLE, executionSemester,
                        null);
            }
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
        return !hasAnyCurriculumModules() && childsCanBeDeleted();
    }

    private boolean childsCanBeDeleted() {
        for (final Context context : getChildContexts()) {
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

    @Deprecated
    public boolean hasParentDegreeCurricularPlan() {
        return getParentDegreeCurricularPlan() != null;
    }

}
