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

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationConclusionBean;
import net.sourceforge.fenixedu.domain.DomainObjectUtil;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.candidacy.Ingression;
import net.sourceforge.fenixedu.domain.degreeStructure.BranchCourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.BranchType;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleCourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.curriculum.CycleConclusionProcess;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.predicates.CycleCurriculumGroupPredicates;
import net.sourceforge.fenixedu.predicates.RolePredicates;

import org.apache.commons.collections.comparators.ComparatorChain;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CycleCurriculumGroup extends CycleCurriculumGroup_Base {

    static final private Comparator<CycleCurriculumGroup> COMPARATOR_BY_CYCLE_TYPE = new Comparator<CycleCurriculumGroup>() {
        @Override
        final public int compare(final CycleCurriculumGroup o1, final CycleCurriculumGroup o2) {
            return CycleType.COMPARATOR_BY_LESS_WEIGHT.compare(o1.getCycleType(), o2.getCycleType());
        }
    };

    static final public Comparator<CycleCurriculumGroup> COMPARATOR_BY_CYCLE_TYPE_AND_ID =
            new Comparator<CycleCurriculumGroup>() {
                @Override
                final public int compare(final CycleCurriculumGroup o1, final CycleCurriculumGroup o2) {
                    final ComparatorChain comparatorChain = new ComparatorChain();
                    comparatorChain.addComparator(CycleCurriculumGroup.COMPARATOR_BY_CYCLE_TYPE);
                    comparatorChain.addComparator(DomainObjectUtil.COMPARATOR_BY_ID);

                    return comparatorChain.compare(o1, o2);
                }
            };

    protected CycleCurriculumGroup() {
        super();
    }

    public CycleCurriculumGroup(RootCurriculumGroup rootCurriculumGroup, CycleCourseGroup cycleCourseGroup,
            ExecutionSemester executionSemester) {
        this();
        init(rootCurriculumGroup, cycleCourseGroup, executionSemester);
    }

    public CycleCurriculumGroup(RootCurriculumGroup rootCurriculumGroup, CycleCourseGroup cycleCourseGroup) {
        this();
        init(rootCurriculumGroup, cycleCourseGroup);
    }

    @Override
    protected void init(CurriculumGroup curriculumGroup, CourseGroup courseGroup) {
        checkInitConstraints((RootCurriculumGroup) curriculumGroup, (CycleCourseGroup) courseGroup);
        super.init(curriculumGroup, courseGroup);
    }

    @Override
    protected void init(CurriculumGroup curriculumGroup, CourseGroup courseGroup, ExecutionSemester executionSemester) {
        checkInitConstraints((RootCurriculumGroup) curriculumGroup, (CycleCourseGroup) courseGroup);
        super.init(curriculumGroup, courseGroup, executionSemester);
    }

    private void checkInitConstraints(final RootCurriculumGroup rootCurriculumGroup, final CycleCourseGroup cycleCourseGroup) {
        if (rootCurriculumGroup.getCycleCurriculumGroup(cycleCourseGroup.getCycleType()) != null) {
            throw new DomainException(
                    "error.studentCurriculum.RootCurriculumGroup.cycle.course.group.already.exists.in.curriculum",
                    cycleCourseGroup.getName());
        }
    }

    @Override
    public void setCurriculumGroup(CurriculumGroup curriculumGroup) {
        if (curriculumGroup != null && !(curriculumGroup instanceof RootCurriculumGroup)) {
            throw new DomainException("error.curriculumGroup.CycleParentCanOnlyBeRootCurriculumGroup");
        }
        super.setCurriculumGroup(curriculumGroup);
    }

    @Override
    public void setDegreeModule(DegreeModule degreeModule) {
        if (degreeModule != null && !(degreeModule instanceof CycleCourseGroup)) {
            throw new DomainException("error.curriculumGroup.CycleParentDegreeModuleCanOnlyBeCycleCourseGroup");
        }
        super.setDegreeModule(degreeModule);
    }

    @Override
    public CycleCourseGroup getDegreeModule() {
        return (CycleCourseGroup) super.getDegreeModule();
    }

    @Override
    public boolean isCycleCurriculumGroup() {
        return true;
    }

    public boolean isCycle(final CycleType cycleType) {
        return getCycleType() == cycleType;
    }

    public boolean isFirstCycle() {
        return isCycle(CycleType.FIRST_CYCLE);
    }

    public CycleCourseGroup getCycleCourseGroup() {
        return getDegreeModule();
    }

    public CycleType getCycleType() {
        return getCycleCourseGroup().getCycleType();
    }

    @Override
    public RootCurriculumGroup getCurriculumGroup() {
        return (RootCurriculumGroup) super.getCurriculumGroup();
    }

    @Override
    public void delete() {
        checkRulesToDelete();

        super.delete();
    }

    @Override
    public void deleteRecursive() {
        check(this, RolePredicates.MANAGER_PREDICATE);
        for (final CurriculumModule child : getCurriculumModules()) {
            child.deleteRecursive();
        }

        super.delete();
    }

    private void checkRulesToDelete() {
        if (isFirstCycle()) {
            if (getRegistration().getIngression() == Ingression.DA1C || getRegistration().getIngression() == Ingression.CIA2C) {
                final User userView = Authenticate.getUser();
                if (AcademicAuthorizationGroup.getProgramsForOperation(userView.getPerson(), AcademicOperationType.STUDENT_ENROLMENTS).contains(getRegistration().getDegree())
                        || userView.getPerson().hasRole(RoleType.MANAGER)) {
                    return;
                }
            }
        }
        if (!getCurriculumGroup().getDegreeType().canRemoveEnrolmentIn(getCycleType())) {
            throw new DomainException("error.studentCurriculum.CycleCurriculumGroup.degree.type.requires.this.cycle.to.exist",
                    getName().getContent());
        }

        /* For Integrated master degrees one of the cycles must exists */
        if (getCurriculumGroup().getDegreeType().isIntegratedMasterDegree()) {
            if (getCurriculumGroup().getRootCurriculumGroup().getCycleCurriculumGroups().size() == 1) {
                throw new DomainException(
                        "error.studentCurriculum.CycleCurriculumGroup.degree.type.requires.this.cycle.to.exist", getName()
                                .getContent());
            }
        }
    }

    public boolean isExternal() {
        return false;
    }

    public void conclude() {
        check(this, CycleCurriculumGroupPredicates.MANAGE_CONCLUSION_PROCESS);
        if (isConclusionProcessed()) {
            if (!getRegistration().canRepeatConclusionProcess(AccessControl.getPerson())) {
                throw new DomainException("error.CycleCurriculumGroup.cycle.is.already.concluded", getCycleCourseGroup()
                        .getName());
            }
        }

        if (!isConcluded()) {
            throw new DomainException("error.CycleCurriculumGroup.cycle.is.not.concluded");
        }

        if (hasConclusionProcess()) {
            getConclusionProcess().update(new RegistrationConclusionBean(getRegistration(), this));
        } else {
            CycleConclusionProcess.conclude(new RegistrationConclusionBean(getRegistration(), this));
        }

    }

    @Override
    public boolean isConcluded() {
        return isConclusionProcessed() || super.isConcluded();
    }

    @Override
    public ConclusionValue isConcluded(final ExecutionYear executionYear) {
        return isConclusionProcessed() && !executionYear.getBeginDateYearMonthDay().isBefore(getConclusionDate()) ? ConclusionValue.CONCLUDED : super
                .isConcluded(executionYear);
    }

    final public BigDecimal getAverage() {
        return isConclusionProcessed() ? getConclusionProcess().getAverage() : getAverage((ExecutionYear) null);
    }

    final public BigDecimal getAverage(final ExecutionYear executionYear) {
        return executionYear == null && isConcluded() && isConclusionProcessed() ? BigDecimal.valueOf(getFinalAverage()) : getCurriculum(
                new DateTime(), executionYear).getAverage();
    }

    final public ExecutionYear getConclusionYear() {
        return isConclusionProcessed() ? getConclusionProcess().getConclusionYear() : null;
    }

    final public ExecutionYear calculateConclusionYear() {
        return getLastApprovementExecutionYear();
    }

    final public Integer getFinalAverage() {
        return isConclusionProcessed() ? getConclusionProcess().getFinalAverage() : null;
    }

    @Override
    final public Double getCreditsConcluded() {
        return isConclusionProcessed() ? getConclusionProcess().getCredits().doubleValue() : calculateCreditsConcluded();
    }

    final public Double calculateCreditsConcluded() {
        return super.getCreditsConcluded();
    }

    final public ExecutionYear getIngressionYear() {
        return isConclusionProcessed() ? getConclusionProcess().getIngressionYear() : calculateIngressionYear();
    }

    final public ExecutionYear calculateIngressionYear() {
        return getRegistration().calculateIngressionYear();
    }

    public boolean isConclusionProcessed() {
        return hasConclusionProcess();
    }

    final public YearMonthDay getConclusionDate() {
        return isConclusionProcessed() ? getConclusionProcess().getConclusionYearMonthDay() : null;
    }

    @Override
    public YearMonthDay calculateConclusionDate() {
        YearMonthDay result = super.calculateConclusionDate();

        if (getRegistration().getWasTransition()) {
            final ExecutionSemester firstBolonhaTransitionExecutionPeriod =
                    ExecutionSemester.readFirstBolonhaTransitionExecutionPeriod();
            final YearMonthDay begin = firstBolonhaTransitionExecutionPeriod.getBeginDateYearMonthDay();

            if (result == null || result.isBefore(begin)) {
                result = begin;
            }
        }

        return result;
    }

    final public Person getConclusionProcessResponsible() {
        return isConclusionProcessed() ? getConclusionProcess().getResponsible() : null;
    }

    final public Person getConclusionProcessLastResponsible() {
        return isConclusionProcessed() ? getConclusionProcess().getLastResponsible() : null;
    }

    final public String getConclusionProcessNotes() {
        return isConclusionProcessed() ? getConclusionProcess().getNotes() : null;
    }

    final public DateTime getConclusionProcessCreationDateTime() {
        return isConclusionProcessed() ? getConclusionProcess().getCreationDateTime() : null;
    }

    final public DateTime getConclusionProcessLastModificationDateTime() {
        return isConclusionProcessed() ? getConclusionProcess().getLastModificationDateTime() : null;
    }

    public void editConclusionInformation(final Integer finalAverage, final YearMonthDay conclusion, final String notes) {
        editConclusionInformation(AccessControl.getPerson(), finalAverage, conclusion, notes);
    }

    public void editConclusionInformation(final Person editor, final Integer finalAverage, final YearMonthDay conclusion,
            final String notes) {
        if (!isConclusionProcessed()) {
            throw new DomainException(
                    "error.net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup.its.only.possible.to.edit.after.conclusion.process.has.been.performed");
        }
        String[] args = {};

        if (finalAverage == null) {
            throw new DomainException("error.CycleCurriculumGroup.argument.must.not.be.null", args);
        }
        String[] args1 = {};
        if (conclusion == null) {
            throw new DomainException("error.CycleCurriculumGroup.argument.must.not.be.null", args1);
        }

        getConclusionProcess().update(editor, finalAverage, conclusion.toLocalDate(), notes);
    }

    public Double getCurrentDefaultEcts() {
        return getDegreeModule().getCurrentDefaultEcts();
    }

    public Double getDefaultEcts(final ExecutionYear executionYear) {
        return getDegreeModule().getDefaultEcts(executionYear);
    }

    @Override
    public CycleCurriculumGroup getParentCycleCurriculumGroup() {
        return this;
    }

    /**
     * 
     * Cycle can have only one branch by type
     * 
     * @param branchType
     */
    public BranchCurriculumGroup getBranchCurriculumGroup(final BranchType branchType) {
        final Set<BranchCurriculumGroup> groups = getBranchCurriculumGroups(branchType);
        return groups.isEmpty() ? null : groups.iterator().next();
    }

    public BranchCurriculumGroup getMajorBranchCurriculumGroup() {
        return getBranchCurriculumGroup(BranchType.MAJOR);
    }

    public BranchCurriculumGroup getMinorBranchCurriculumGroup() {
        return getBranchCurriculumGroup(BranchType.MINOR);
    }

    public BranchCourseGroup getBranchCourseGroup(final BranchType branchType) {
        final Set<BranchCourseGroup> groups = getBranchCourseGroups(branchType);
        return groups.isEmpty() ? null : groups.iterator().next();
    }

    public BranchCourseGroup getMajorBranchCourseGroup() {
        return getBranchCourseGroup(BranchType.MAJOR);
    }

    public BranchCourseGroup getMinorBranchCourseGroup() {
        return getBranchCourseGroup(BranchType.MINOR);
    }

    @Deprecated
    public boolean hasConclusionProcess() {
        return getConclusionProcess() != null;
    }

}
