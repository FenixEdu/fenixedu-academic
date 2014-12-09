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
package org.fenixedu.academic.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.util.LogicOperator;
import org.fenixedu.bennu.core.domain.Bennu;

public class EquivalencePlanEntry extends EquivalencePlanEntry_Base {

    public static Comparator<EquivalencePlanEntry> COMPARATOR = new Comparator<EquivalencePlanEntry>() {
        @Override
        public int compare(EquivalencePlanEntry o1, EquivalencePlanEntry o2) {
            return o1.getCompareString().compareTo(o2.getCompareString());
        }
    };

    public static Comparator<EquivalencePlanEntry> COMPARATOR_BY_SOURCE_NUMBER = new Comparator<EquivalencePlanEntry>() {
        @Override
        public int compare(EquivalencePlanEntry o1, EquivalencePlanEntry o2) {
            final Integer count1 = Integer.valueOf(o1.getOldDegreeModulesSet().size());
            final Integer count2 = Integer.valueOf(o2.getOldDegreeModulesSet().size());
            return count1.compareTo(count2);
        }
    };

    protected EquivalencePlanEntry() {
        super();
        super.setRootDomainObject(Bennu.getInstance());
    }

    public EquivalencePlanEntry(final EquivalencePlan equivalencePlan, final CourseGroup oldCourseGroup,
            final CourseGroup newCourseGroup) {
        this();
        init(equivalencePlan, Collections.singleton(oldCourseGroup), Collections.singleton(newCourseGroup), null, null, null,
                true, null);
    }

    public EquivalencePlanEntry(final EquivalencePlan equivalencePlan, final Collection<? extends DegreeModule> oldDegreeModules,
            final Collection<? extends DegreeModule> newDegreeModules, final CourseGroup previousCourseGroupForNewDegreeModules,
            final LogicOperator sourceDegreeModulesOperator, final LogicOperator newDegreeModulesOperator,
            final Boolean transitiveSource, final Double ectsCredits) {
        this();
        init(equivalencePlan, oldDegreeModules, newDegreeModules, previousCourseGroupForNewDegreeModules,
                sourceDegreeModulesOperator, newDegreeModulesOperator, transitiveSource, ectsCredits);

    }

    protected void init(final EquivalencePlan equivalencePlan, final Collection<? extends DegreeModule> oldDegreeModules,
            final Collection<? extends DegreeModule> newDegreeModules, final CourseGroup previousCourseGroupForNewDegreeModules,
            final LogicOperator sourceDegreeModulesOperator, final LogicOperator newDegreeModulesOperator,
            final Boolean transitiveSource, final Double ectsCredits) {

        checkParameters(equivalencePlan);

        checkRulesToCreate(newDegreeModules, previousCourseGroupForNewDegreeModules);

        super.setEquivalencePlan(equivalencePlan);
        super.getOldDegreeModulesSet().addAll(oldDegreeModules);
        super.getNewDegreeModulesSet().addAll(newDegreeModules);
        super.setPreviousCourseGroupForNewDegreeModules(previousCourseGroupForNewDegreeModules);
        super.setSourceDegreeModulesOperator(sourceDegreeModulesOperator);
        super.setNewDegreeModulesOperator(newDegreeModulesOperator);
        super.setTransitiveSource(transitiveSource);
        super.setEctsCredits(ectsCredits);

    }

    private void checkRulesToCreate(final Collection<? extends DegreeModule> newDegreeModules,
            final CourseGroup previousCourseGroupForNewDegreeModules) {

        if (previousCourseGroupForNewDegreeModules != null) {
            for (final DegreeModule degreeModule : newDegreeModules) {
                if (!previousCourseGroupForNewDegreeModules.hasDegreeModule(degreeModule)) {
                    throw new DomainException(
                            "error.EquivalencePlanEntry.new.degree.modules.must.be.children.of.choosen.course.group");
                }
            }
        }

    }

    private void checkParameters(final EquivalencePlan equivalencePlan) {
        if (equivalencePlan == null) {
            throw new DomainException("error.org.fenixedu.academic.domain.EquivalencePlanEntry.equivalencePlan.cannot.be.null");
        }
    }

    @Override
    public void addNewDegreeModules(DegreeModule newDegreeModule) {
        throw new DomainException("error.org.fenixedu.academic.domain.EquivalencePlanEntry.cannot.add.newDegreeModule");
    }

    public List<DegreeModule> getNewDegreeModulesSortedByType() {
        final List<DegreeModule> result = new ArrayList<DegreeModule>(super.getNewDegreeModulesSet());
        Collections.sort(result, new Comparator<DegreeModule>() {
            @Override
            public int compare(final DegreeModule o1, final DegreeModule o2) {
                if (o1.isCourseGroup() && !o2.isCourseGroup()) {
                    return -1;
                }
                if (!o1.isCourseGroup() && o2.isCourseGroup()) {
                    return 1;
                }
                return 0;
            }
        });

        return result;
    }

    @Override
    public Set<DegreeModule> getNewDegreeModulesSet() {
        return Collections.unmodifiableSet(super.getNewDegreeModulesSet());
    }

    @Override
    public void removeNewDegreeModules(DegreeModule newDegreeModule) {
        throw new DomainException("error.org.fenixedu.academic.domain.EquivalencePlanEntry.cannot.remove.newDegreeModule");
    }

    @Override
    public void addOldDegreeModules(DegreeModule oldDegreeModules) {
        throw new DomainException("error.org.fenixedu.academic.domain.EquivalencePlanEntry.cannot.add.oldDegreeModules");
    }

    @Override
    public Set<DegreeModule> getOldDegreeModulesSet() {
        return Collections.unmodifiableSet(super.getOldDegreeModulesSet());
    }

    @Override
    public void removeOldDegreeModules(DegreeModule oldDegreeModules) {
        throw new DomainException("error.org.fenixedu.academic.domain.EquivalencePlanEntry.cannot.remove.oldDegreeModules");
    }

    @Override
    public void setPreviousCourseGroupForNewDegreeModules(CourseGroup previousCourseGroupForNewDegreeModules) {
        throw new DomainException(
                "error.org.fenixedu.academic.domain.EquivalencePlanEntry.cannot.modify.previousCourseGroupForNewDegreeModules");
    }

    @Override
    public void setEctsCredits(Double ectsCredits) {
        throw new DomainException("error.org.fenixedu.academic.domain.EquivalencePlanEntry.cannot.modify.ectsCredits");
    }

    @Override
    public void setSourceDegreeModulesOperator(LogicOperator sourceDegreeModulesOperator) {
        throw new DomainException(
                "error.org.fenixedu.academic.domain.EquivalencePlanEntry.cannot.modify.sourceDegreeModulesOperator");
    }

    @Override
    public void setNewDegreeModulesOperator(LogicOperator newDegreeModulesOperator) {
        throw new DomainException(
                "error.org.fenixedu.academic.domain.EquivalencePlanEntry.cannot.modify.newDegreeModulesOperator");
    }

    private boolean isOneCourseGroup(final Set<DegreeModule> degreeModules) {
        return degreeModules.size() == 1 && !degreeModules.iterator().next().isLeaf();
    }

    public boolean isCourseGroupEntry() {
        return isOneCourseGroup(getOldDegreeModulesSet()) && isOneCourseGroup(getNewDegreeModulesSet());
    }

    public boolean isCurricularCourseEntry() {
        boolean hasAtLeastOneCourseGroup = false;
        for (final DegreeModule degreeModule : getOldDegreeModulesSet()) {
            hasAtLeastOneCourseGroup = true;
            if (!degreeModule.isLeaf()) {
                return false;
            }
        }
        return hasAtLeastOneCourseGroup;
    }

    public void delete() {
        setRootDomainObject(null);
        setEquivalencePlan(null);
        super.setPreviousCourseGroupForNewDegreeModules(null);
        super.getEquivalencePlansSet().clear();
        super.getOldDegreeModulesSet().clear();
        super.getNewDegreeModulesSet().clear();
        super.deleteDomainObject();
    }

    public void removeEquivalencePlan() {
        super.setEquivalencePlan(null);
    }

    public boolean isFor(final DegreeCurricularPlan degreeCurricularPlan) {
        for (final DegreeModule degreeModule : getNewDegreeModulesSet()) {
            if (degreeModule.getParentDegreeCurricularPlan() == degreeCurricularPlan) {
                return true;
            }
        }
        return false;
    }

    public boolean isFor(final DegreeModule degreeModule) {
        return getNewDegreeModulesSet().contains(degreeModule) || getOldDegreeModulesSet().contains(degreeModule);
    }

    protected String getCompareString() {
        final StringBuilder stringBuilder = new StringBuilder();
        appendCompareString(stringBuilder, getOldDegreeModulesSet());
        appendCompareString(stringBuilder, getNewDegreeModulesSet());
        stringBuilder.append(getEctsCredits());
        if (getSourceDegreeModulesOperator() != null) {
            stringBuilder.append(getSourceDegreeModulesOperator().name());
        }
        if (getNewDegreeModulesOperator() != null) {
            stringBuilder.append(getNewDegreeModulesOperator().name());
        }
        stringBuilder.append(getExternalId());
        return stringBuilder.toString();
    }

    protected void appendCompareString(final StringBuilder stringBuilder, final Set<DegreeModule> degreeModules) {
        for (final DegreeModule degreeModule : degreeModules) {
            stringBuilder.append(degreeModule.getName());
        }
    }

    public void checkPermissions(final Person person) {
        for (final DegreeModule degreeModule : getNewDegreeModulesSet()) {
            final DegreeCurricularPlan degreeCurricularPlan = degreeModule.getParentDegreeCurricularPlan();
            for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegreesSet()) {
                for (final Coordinator coordinator : executionDegree.getCoordinatorsListSet()) {
                    if (coordinator.getPerson() == person) {
                        return;
                    }
                }
            }
        }
        throw new Error("error.not.authorized");
    }

    public boolean hasOnlyCourseGroupsInDestination() {
        for (final DegreeModule degreeModule : getNewDegreeModulesSet()) {
            if (degreeModule.isLeaf()) {
                return false;
            }
        }
        return getNewDegreeModulesSet().size() > 0;
    }

    public boolean hasOnlyCurricularCoursesInDestination() {
        for (final DegreeModule degreeModule : getNewDegreeModulesSet()) {
            if (!degreeModule.isLeaf()) {
                return false;
            }
        }
        return getNewDegreeModulesSet().size() > 0;
    }

    public boolean isTransitiveSource() {
        return getTransitiveSource();
    }

    public boolean canApply(StudentCurricularPlan oldStudentCurricularPlan) {
        boolean isApprovedInAll = true;
        for (final DegreeModule degreeModule : getOldDegreeModulesSet()) {
            final boolean isApprovedOrEnroled = oldStudentCurricularPlan.hasEnrolmentOrAprovalInCurriculumModule(degreeModule);
            if (getSourceDegreeModulesOperator().isOR() && isApprovedOrEnroled) {
                return true;
            }
            isApprovedInAll &= isApprovedOrEnroled;
        }
        return isApprovedInAll;
    }

}
