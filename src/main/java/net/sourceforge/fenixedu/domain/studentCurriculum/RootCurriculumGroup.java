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
package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRuleType;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleCourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.degreeStructure.RootCourseGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.curriculum.Curriculum;

import org.apache.commons.collections.comparators.ReverseComparator;
import org.joda.time.DateTime;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class RootCurriculumGroup extends RootCurriculumGroup_Base {

    protected RootCurriculumGroup() {
        super();
        createExtraCurriculumGroup();
        createPropaedeuticsCurriculumGroup();
    }

    protected RootCurriculumGroup(StudentCurricularPlan studentCurricularPlan, RootCourseGroup rootCourseGroup,
            ExecutionSemester executionSemester, CycleType cycleType) {
        this();
        init(studentCurricularPlan, rootCourseGroup, executionSemester, cycleType);
        createStandaloneCurriculumGroupIfNecessary();
    }

    private void init(StudentCurricularPlan studentCurricularPlan, RootCourseGroup courseGroup,
            ExecutionSemester executionSemester, CycleType cycleType) {

        checkParameters(studentCurricularPlan, courseGroup, executionSemester);
        checkInitConstraints(studentCurricularPlan, courseGroup);

        setParentStudentCurricularPlan(studentCurricularPlan);
        setDegreeModule(courseGroup);

        addChildCurriculumGroups(courseGroup, executionSemester, cycleType);
    }

    private void checkParameters(final StudentCurricularPlan studentCurricularPlan, final RootCourseGroup courseGroup,
            final ExecutionSemester executionSemester) {
        checkParameters(studentCurricularPlan, courseGroup);
        if (executionSemester == null) {
            throw new DomainException("error.studentCurriculum.executionPeriod.cannot.be.null");
        }
    }

    protected RootCurriculumGroup(StudentCurricularPlan studentCurricularPlan, RootCourseGroup rootCourseGroup,
            CycleType cycleType) {
        this();
        init(studentCurricularPlan, rootCourseGroup, cycleType);
        createStandaloneCurriculumGroupIfNecessary();
    }

    private void init(final StudentCurricularPlan studentCurricularPlan, final RootCourseGroup rootCourseGroup,
            final CycleType cycleType) {
        checkParameters(studentCurricularPlan, rootCourseGroup);
        checkInitConstraints(studentCurricularPlan, rootCourseGroup);

        setParentStudentCurricularPlan(studentCurricularPlan);
        setDegreeModule(rootCourseGroup);
        addChildCurriculumGroups(rootCourseGroup, cycleType);
    }

    private void checkParameters(final StudentCurricularPlan studentCurricularPlan, final RootCourseGroup rootCourseGroup) {
        if (studentCurricularPlan == null) {
            throw new DomainException("error.studentCurriculum.studentCurricularPlan.cannot.be.null");
        }
        if (rootCourseGroup == null) {
            throw new DomainException("error.studentCurriculum.rootCourseGroup.cannot.be.null");
        }
    }

    private void addChildCurriculumGroups(final RootCourseGroup rootCourseGroup, final ExecutionSemester executionSemester,
            CycleType cycle) {
        if (rootCourseGroup.hasCycleGroups()) {
            createCycle(rootCourseGroup, executionSemester, cycle);
        } else {
            super.addChildCurriculumGroups(rootCourseGroup, executionSemester);
        }
    }

    private void addChildCurriculumGroups(final RootCourseGroup rootCourseGroup, CycleType cycle) {
        if (rootCourseGroup.hasCycleGroups()) {
            createCycle(rootCourseGroup, null, cycle);
        }
    }

    private void createCycle(final RootCourseGroup rootCourseGroup, final ExecutionSemester executionSemester, CycleType cycle) {
        if (cycle == null) {
            cycle = rootCourseGroup.getDegree().getDegreeType().getFirstOrderedCycleType();
        }
        if (cycle != null) {
            CurriculumGroupFactory.createGroup(this, rootCourseGroup.getCycleCourseGroup(cycle), executionSemester);
        }
    }

    private void checkInitConstraints(final StudentCurricularPlan studentCurricularPlan, final RootCourseGroup rootCourseGroup) {
        if (studentCurricularPlan.getDegreeCurricularPlan() != rootCourseGroup.getParentDegreeCurricularPlan()) {
            throw new DomainException("error.rootCurriculumGroup.scp.and.root.have.different.degreeCurricularPlan");
        }
    }

    public void setRootCourseGroup(final RootCourseGroup rootCourseGroup) {
        setDegreeModule(rootCourseGroup);
    }

    @Override
    public void setDegreeModule(DegreeModule degreeModule) {
        if (degreeModule != null && !(degreeModule instanceof RootCourseGroup)) {
            throw new DomainException("error.curriculumGroup.RootCurriculumGroup.degreeModuleMustBeRootCourseGroup");
        }
        super.setDegreeModule(degreeModule);
    }

    @Override
    public void setCurriculumGroup(CurriculumGroup curriculumGroup) {
        if (curriculumGroup != null) {
            throw new DomainException("error.curriculumGroup.RootCurriculumGroupCannotHaveParent");
        }
    }

    @Override
    public boolean isRoot() {
        return true;
    }

    @Override
    public StudentCurricularPlan getStudentCurricularPlan() {
        return getParentStudentCurricularPlan();
    }

    public boolean hasStudentCurricularPlan() {
        return hasParentStudentCurricularPlan();
    }

    private void createExtraCurriculumGroup() {
        NoCourseGroupCurriculumGroup.create(NoCourseGroupCurriculumGroupType.EXTRA_CURRICULAR, this);
    }

    private void createPropaedeuticsCurriculumGroup() {
        NoCourseGroupCurriculumGroup.create(NoCourseGroupCurriculumGroupType.PROPAEDEUTICS, this);
    }

    private void createStandaloneCurriculumGroupIfNecessary() {
        if (hasStudentCurricularPlan() && getStudentCurricularPlan().isEmptyDegree()) {
            NoCourseGroupCurriculumGroup.create(NoCourseGroupCurriculumGroupType.STANDALONE, this);
        }
    }

    public CycleCurriculumGroup getCycleCurriculumGroup(CycleType cycleType) {
        for (CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (curriculumModule.isCycleCurriculumGroup()) {
                CycleCurriculumGroup cycleCurriculumGroup = (CycleCurriculumGroup) curriculumModule;
                if (cycleCurriculumGroup.isCycle(cycleType)) {
                    return cycleCurriculumGroup;
                }
            }
        }
        return null;
    }

    public CycleCurriculumGroup getFirstOrderedCycleCurriculumGroup() {
        for (final CycleType cycleType : getDegreeType().getOrderedCycleTypes()) {
            CycleCurriculumGroup cycleCurriculumGroup = getCycleCurriculumGroup(cycleType);
            if (cycleCurriculumGroup != null) {
                return cycleCurriculumGroup;
            }
        }

        return null;
    }

    public CycleCurriculumGroup getLastOrderedCycleCurriculumGroup() {
        final SortedSet<CycleCurriculumGroup> cycleCurriculumGroups =
                new TreeSet<CycleCurriculumGroup>(CycleCurriculumGroup.COMPARATOR_BY_CYCLE_TYPE_AND_ID);
        cycleCurriculumGroups.addAll(getInternalCycleCurriculumGroups());

        return cycleCurriculumGroups.isEmpty() ? null : cycleCurriculumGroups.last();
    }

    public CycleCurriculumGroup getLastConcludedCycleCurriculumGroup() {
        final SortedSet<CycleCurriculumGroup> cycleCurriculumGroups =
                new TreeSet<CycleCurriculumGroup>(new ReverseComparator(CycleCurriculumGroup.COMPARATOR_BY_CYCLE_TYPE_AND_ID));
        cycleCurriculumGroups.addAll(getInternalCycleCurriculumGroups());

        for (final CycleCurriculumGroup curriculumGroup : cycleCurriculumGroups) {
            if (curriculumGroup.isConcluded()) {
                return curriculumGroup;
            }
        }

        return null;
    }

    public Collection<CycleCurriculumGroup> getCycleCurriculumGroups() {
        Collection<CycleCurriculumGroup> cycleCurriculumGroups = new HashSet<CycleCurriculumGroup>();
        for (CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (curriculumModule.isCycleCurriculumGroup()) {
                cycleCurriculumGroups.add((CycleCurriculumGroup) curriculumModule);
            }
        }
        return cycleCurriculumGroups;
    }

    public DegreeType getDegreeType() {
        return getStudentCurricularPlan().getDegreeType();
    }

    /**
     * Must not implement this method depending on
     * hasConcludedCycle(CycleType,ExecutionYear). Each group knows what
     * ExecutionYear to invoke by internal invocation of the
     * getApprovedCurriculumLinesLastExecutionYear() method.
     * 
     */
    public boolean hasConcludedCycle(CycleType cycleType) {
        for (CycleType degreeCycleType : getDegreeType().getCycleTypes()) {
            if (cycleType == null || degreeCycleType == cycleType) {
                if (!checkIfCycleIsConcluded(degreeCycleType)) {
                    return false;
                }
            }
        }

        return cycleType == null || getDegreeType().getCycleTypes().contains(cycleType);
    }

    private boolean checkIfCycleIsConcluded(CycleType cycleType) {
        final CycleCurriculumGroup cycleCurriculumGroup = getCycleCurriculumGroup(cycleType);
        return cycleCurriculumGroup != null && cycleCurriculumGroup.isConcluded();
    }

    public boolean hasConcludedCycle(CycleType cycleType, final ExecutionYear executionYear) {
        for (CycleType degreeCycleType : getDegreeType().getCycleTypes()) {
            if (cycleType == null || degreeCycleType == cycleType) {
                if (!checkIfCycleIsConcluded(degreeCycleType, executionYear)) {
                    return false;
                }
            }
        }

        return cycleType == null || getDegreeType().getCycleTypes().contains(cycleType);
    }

    private boolean checkIfCycleIsConcluded(CycleType cycleType, final ExecutionYear executionYear) {
        final CycleCurriculumGroup cycleCurriculumGroup = getCycleCurriculumGroup(cycleType);
        return cycleCurriculumGroup != null && cycleCurriculumGroup.isConcluded(executionYear).value();
    }

    /**
     * Only the DegreeType's CycleTypes should be inspected.
     * CycleCurriculumGroups of other CycleType might exist and shouldn't be
     * taken into account.
     * 
     */
    @Override
    public Curriculum getCurriculum(final DateTime when, final ExecutionYear executionYear) {
        final Curriculum curriculum = Curriculum.createEmpty(this, executionYear);
        if (!wasCreated(when)) {
            return curriculum;
        }

        final DegreeType degreeType = getDegreeType();
        if (degreeType.hasAnyCycleTypes()) {
            for (final CycleCurriculumGroup cycleCurriculumGroup : getInternalCycleCurriculumGroups()) {
                curriculum.add(cycleCurriculumGroup.getCurriculum(when, executionYear));
            }
        } else {
            curriculum.add(super.getCurriculum(when, executionYear));
        }

        return curriculum;
    }

    @Override
    public void delete() {
        setParentStudentCurricularPlan(null);
        super.delete();
    }

    @Override
    public RootCourseGroup getDegreeModule() {
        return (RootCourseGroup) super.getDegreeModule();
    }

    public boolean hasExternalCycles() {
        for (final CycleCurriculumGroup cycleCurriculumGroup : getCycleCurriculumGroups()) {
            if (cycleCurriculumGroup.isExternal()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public ICurricularRule getMostRecentActiveCurricularRule(final CurricularRuleType ruleType, final ExecutionYear executionYear) {
        return getDegreeModule().getMostRecentActiveCurricularRule(ruleType, null, executionYear);
    }

    public CycleCurriculumGroup getCycleCurriculumGroupFor(final CurriculumModule curriculumModule) {
        for (final CycleCurriculumGroup cycleCurriculumGroup : getCycleCurriculumGroups()) {
            if (cycleCurriculumGroup.hasCurriculumModule(curriculumModule)) {
                return cycleCurriculumGroup;
            }
        }

        return null;
    }

    public CycleCourseGroup getCycleCourseGroup(final CurriculumModule curriculumModule) {
        final CycleCurriculumGroup cycleCurriculumGroup = getCycleCurriculumGroupFor(curriculumModule);
        return cycleCurriculumGroup != null ? cycleCurriculumGroup.getDegreeModule() : null;
    }

    public CycleCourseGroup getCycleCourseGroup(final CycleType cycleType) {
        return getDegreeModule().getCycleCourseGroup(cycleType);
    }

    public List<CycleCurriculumGroup> getInternalCycleCurriculumGroups() {
        final List<CycleCurriculumGroup> result = new ArrayList<CycleCurriculumGroup>();

        for (final CycleCurriculumGroup cycleCurriculumGroup : getCycleCurriculumGroups()) {
            if (!cycleCurriculumGroup.isExternal()) {
                result.add(cycleCurriculumGroup);
            }
        }

        return result;
    }

    public List<ExternalCurriculumGroup> getExternalCycleCurriculumGroups() {
        final List<ExternalCurriculumGroup> result = new ArrayList<>();

        for (final CycleCurriculumGroup cycleCurriculumGroup : getCycleCurriculumGroups()) {
            if (cycleCurriculumGroup.isExternal()) {
                result.add((ExternalCurriculumGroup) cycleCurriculumGroup);
            }
        }

        return result;
    }

    public double getDefaultEcts(final ExecutionYear executionYear) {
        double result = 0d;

        for (final CycleCurriculumGroup cycleCurriculumGroup : getInternalCycleCurriculumGroups()) {
            result += cycleCurriculumGroup.getDefaultEcts(executionYear);
        }

        return result;
    }

    @Override
    public Set<CurriculumGroup> getAllCurriculumGroups() {
        Set<CurriculumGroup> result = new HashSet<CurriculumGroup>();

        for (final CurriculumModule curriculumModule : getCurriculumModules()) {
            result.addAll(curriculumModule.getAllCurriculumGroups());
        }
        return result;
    }

    @Override
    public Set<CurriculumGroup> getAllCurriculumGroupsWithoutNoCourseGroupCurriculumGroups() {
        Set<CurriculumGroup> result = new HashSet<CurriculumGroup>();

        for (final CurriculumModule curriculumModule : getCurriculumModules()) {
            result.addAll(curriculumModule.getAllCurriculumGroupsWithoutNoCourseGroupCurriculumGroups());
        }
        return result;
    }

    @Deprecated
    public boolean hasParentStudentCurricularPlan() {
        return getParentStudentCurricularPlan() != null;
    }

}
