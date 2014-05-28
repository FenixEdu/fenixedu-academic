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
package net.sourceforge.fenixedu.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.equivalencyPlan.EquivalencyPlanEntryCurriculumModuleWrapper;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.equivalencyPlan.EquivalencyPlanEntryWrapper;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;

public class StudentCurricularPlanEquivalencePlan extends StudentCurricularPlanEquivalencePlan_Base {

    public StudentCurricularPlanEquivalencePlan(final StudentCurricularPlan studentCurricularPlan) {
        super();

        init(studentCurricularPlan);
    }

    private void init(StudentCurricularPlan oldStudentCurricularPlan) {
        checkParameters(oldStudentCurricularPlan);

        super.setOldStudentCurricularPlan(oldStudentCurricularPlan);
    }

    private void checkParameters(StudentCurricularPlan oldStudentCurricularPlan) {
        if (oldStudentCurricularPlan == null) {
            throw new DomainException("error.StudentCurricularPlanEquivalencePlan.oldStudentCurricularPlan.cannot.be.null");
        }
    }

    public Set<EquivalencePlanEntry> getEquivalencePlanEntries(final DegreeCurricularPlan degreeCurricularPlan) {
        final Set<EquivalencePlanEntry> equivalencePlanEntries = new HashSet<EquivalencePlanEntry>();
        equivalencePlanEntries.addAll(degreeCurricularPlan.getEquivalencePlan().getEntriesSet());
        equivalencePlanEntries.removeAll(getEntriesToRemoveSet());
        for (final EquivalencePlanEntry equivalencePlanEntry : getEntriesSet()) {
            if (equivalencePlanEntry.isFor(degreeCurricularPlan)) {
                equivalencePlanEntries.add(equivalencePlanEntry);
            }
        }
        return equivalencePlanEntries;
    }

    public Set<EquivalencyPlanEntryWrapper> getEquivalencePlanEntryWrappers(final DegreeCurricularPlan degreeCurricularPlan,
            final CurriculumModule curriculumModule) {
        final Set<EquivalencyPlanEntryWrapper> equivalencePlanEntries =
                new TreeSet<EquivalencyPlanEntryWrapper>(EquivalencyPlanEntryWrapper.COMPARATOR);

        for (final EquivalencePlanEntry equivalencePlanEntry : degreeCurricularPlan.getEquivalencePlan().getEntriesSet()) {
            if (hasAllEnrolmentsFor(equivalencePlanEntry, getOldStudentCurricularPlan())
                    && (curriculumModule == null || matchOrigin(equivalencePlanEntry, curriculumModule.getDegreeModule()))) {
                if (getEntriesToRemoveSet().contains(equivalencePlanEntry)) {
                    equivalencePlanEntries.add(new EquivalencyPlanEntryWrapper(equivalencePlanEntry, true));
                } else {
                    equivalencePlanEntries.add(new EquivalencyPlanEntryWrapper(equivalencePlanEntry, false));
                }
            }
        }

        for (final EquivalencePlanEntry equivalencePlanEntry : getEntriesSet()) {
            if (equivalencePlanEntry.isFor(degreeCurricularPlan)
                    && (curriculumModule == null || equivalencePlanEntry.isFor(curriculumModule.getDegreeModule()))) {
                equivalencePlanEntries.add(new EquivalencyPlanEntryWrapper(equivalencePlanEntry, false));
            }
        }

        return equivalencePlanEntries;
    }

    private boolean hasAllEnrolmentsFor(final EquivalencePlanEntry equivalencePlanEntry,
            final StudentCurricularPlan studentCurricularPlan) {
        return equivalencePlanEntry.canApply(studentCurricularPlan);
    }

    private boolean matchOrigin(final EquivalencePlanEntry equivalencePlanEntry, final DegreeModule degreeModule) {
        for (final DegreeModule otherDegreeModule : equivalencePlanEntry.getOldDegreeModulesSet()) {
            if (otherDegreeModule == degreeModule) {
                return true;
            }
        }
        return false;
    }

    public EquivalencyPlanEntryCurriculumModuleWrapper getRootEquivalencyPlanEntryCurriculumModuleWrapper(
            final DegreeCurricularPlan degreeCurricularPlan) {
        return getEquivalencyPlanEntryCurriculumModuleWrapper(degreeCurricularPlan, getOldStudentCurricularPlan().getRoot());
    }

    private EquivalencyPlanEntryCurriculumModuleWrapper getEquivalencyPlanEntryCurriculumModuleWrapper(
            final DegreeCurricularPlan degreeCurricularPlan, final CurriculumModule curriculumModule) {
        final EquivalencyPlanEntryCurriculumModuleWrapper equivalencyPlanEntryCurriculumModuleWrapper =
                new EquivalencyPlanEntryCurriculumModuleWrapper(curriculumModule);

        final DegreeModule degreeModule = curriculumModule.getDegreeModule();

        addEquivalencyPlanEntryCurriculumModuleWrappers(equivalencyPlanEntryCurriculumModuleWrapper,
                degreeCurricularPlan.getEquivalencePlan(), degreeModule);

        for (EquivalencePlanEntry equivalencePlanEntry : getEntriesSet()) {
            if (equivalencePlanEntry.isFor(degreeModule)) {
                equivalencyPlanEntryCurriculumModuleWrapper.addEquivalencePlanEntriesToApply(equivalencePlanEntry);
            }
        }

        if (!curriculumModule.isLeaf()) {
            final CurriculumGroup curriculumGroup = (CurriculumGroup) curriculumModule;
            for (final CurriculumModule childCurriculumModule : curriculumGroup.getCurriculumModulesSet()) {
                if (!childCurriculumModule.isLeaf() && ((CurriculumGroup) childCurriculumModule).isNoCourseGroupCurriculumGroup()) {
                    continue;
                }

                final EquivalencyPlanEntryCurriculumModuleWrapper childEquivalencyPlanEntryCurriculumModuleWrapper =
                        getEquivalencyPlanEntryCurriculumModuleWrapper(degreeCurricularPlan, childCurriculumModule);
                equivalencyPlanEntryCurriculumModuleWrapper.addChildren(childEquivalencyPlanEntryCurriculumModuleWrapper);
            }
        }

        return equivalencyPlanEntryCurriculumModuleWrapper;
    }

    private void addEquivalencyPlanEntryCurriculumModuleWrappers(
            final EquivalencyPlanEntryCurriculumModuleWrapper equivalencyPlanEntryCurriculumModuleWrapper,
            final DegreeCurricularPlanEquivalencePlan equivalencePlan, final DegreeModule degreeModule) {
        final Set<EquivalencePlanEntry> equivalencePlanEntries;
        if (degreeModule.isLeaf()) {
            final CurricularCourse curricularCourse = (CurricularCourse) degreeModule;
            if (getOldStudentCurricularPlan().hasEnrolmentOrAprovalInCurriculumModule(curricularCourse)) {
                equivalencePlanEntries = curricularCourse.getOldEquivalencePlanEntriesSet();
            } else {
                equivalencePlanEntries = Collections.EMPTY_SET;
            }
        } else {
            final CourseGroup courseGroup = (CourseGroup) degreeModule;
            equivalencePlanEntries = courseGroup.getOldEquivalencePlanEntriesSet();
        }
        for (final EquivalencePlanEntry equivalencePlanEntry : equivalencePlanEntries) {
            if (equivalencePlanEntry.getEquivalencePlan() == equivalencePlan) {
                if (getEntriesToRemoveSet().contains(equivalencePlanEntry)) {
                    equivalencyPlanEntryCurriculumModuleWrapper.addRemovedEquivalencePlanEntries(equivalencePlanEntry);
                } else {
                    if (equivalencePlanEntry.canApply(getOldStudentCurricularPlan())) {
                        equivalencyPlanEntryCurriculumModuleWrapper.addEquivalencePlanEntriesToApply(equivalencePlanEntry);
                    }
                }
            }
        }
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.EquivalencePlanEntry> getEntriesToRemove() {
        return getEntriesToRemoveSet();
    }

    @Deprecated
    public boolean hasAnyEntriesToRemove() {
        return !getEntriesToRemoveSet().isEmpty();
    }

    @Deprecated
    public boolean hasOldStudentCurricularPlan() {
        return getOldStudentCurricularPlan() != null;
    }

}
