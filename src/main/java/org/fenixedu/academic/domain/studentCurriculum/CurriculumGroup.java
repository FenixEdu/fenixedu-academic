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
package org.fenixedu.academic.domain.studentCurriculum;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Grade;
import org.fenixedu.academic.domain.OptionalEnrolment;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.curricularRules.CreditsLimit;
import org.fenixedu.academic.domain.curricularRules.CurricularRule;
import org.fenixedu.academic.domain.curricularRules.CurricularRuleType;
import org.fenixedu.academic.domain.curricularRules.CurricularRuleValidationType;
import org.fenixedu.academic.domain.curricularRules.DegreeModulesSelectionLimit;
import org.fenixedu.academic.domain.degreeStructure.BranchCourseGroup;
import org.fenixedu.academic.domain.degreeStructure.BranchType;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.degreeStructure.ProgramConclusion;
import org.fenixedu.academic.domain.enrolment.EnroledCurriculumModuleWrapper;
import org.fenixedu.academic.domain.enrolment.IDegreeModuleToEvaluate;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.curriculum.ConclusionProcess;
import org.fenixedu.academic.domain.student.curriculum.Curriculum;
import org.fenixedu.academic.domain.student.curriculum.ProgramConclusionProcess;
import org.fenixedu.academic.dto.student.RegistrationConclusionBean;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.predicates.AndPredicate;
import org.fenixedu.academic.util.predicates.ResultCollection;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CurriculumGroup extends CurriculumGroup_Base {

    private static final Logger logger = LoggerFactory.getLogger(CurriculumGroup.class);

    static final public Comparator<CurriculumGroup> COMPARATOR_BY_CHILD_ORDER_AND_ID = new Comparator<CurriculumGroup>() {
        @Override
        public int compare(CurriculumGroup o1, CurriculumGroup o2) {
            int result = o1.getChildOrder().compareTo(o2.getChildOrder());
            return (result != 0) ? result : o1.getExternalId().compareTo(o2.getExternalId());
        }
    };

    protected CurriculumGroup() {
        super();
    }

    public CurriculumGroup(final CurriculumGroup curriculumGroup, final CourseGroup courseGroup) {
        this();
        init(curriculumGroup, courseGroup);
    }

    protected void init(final CurriculumGroup curriculumGroup, final CourseGroup courseGroup) {
        if (courseGroup == null || curriculumGroup == null) {
            throw new DomainException("error.studentCurriculum.curriculumGroup.courseGroup.cannot.be.null");
        }
        checkInitConstraints(curriculumGroup, courseGroup);
        setDegreeModule(courseGroup);
        setCurriculumGroup(curriculumGroup);
    }

    protected void checkInitConstraints(final CurriculumGroup parent, final CourseGroup courseGroup) {
        if (parent.getRootCurriculumGroup().hasCourseGroup(courseGroup)) {
            throw new DomainException("error.studentCurriculum.CurriculumGroup.duplicate.courseGroup", courseGroup.getName());
        }
    }

    protected void checkParameters(CourseGroup courseGroup, ExecutionInterval executionInterval) {
        if (courseGroup == null) {
            throw new DomainException("error.studentCurriculum.curriculumGroup.courseGroup.cannot.be.null");
        }
        if (executionInterval == null) {
            throw new DomainException("error.studentCurriculum.curriculumGroup.executionPeriod.cannot.be.null");
        }
    }

    public CurriculumGroup(CurriculumGroup parentCurriculumGroup, CourseGroup courseGroup, ExecutionInterval executionSemester) {
        super();
        init(parentCurriculumGroup, courseGroup, executionSemester);
    }

    protected void init(final CurriculumGroup curriculumGroup, final CourseGroup courseGroup,
            final ExecutionInterval executionInterval) {

        checkInitConstraints(curriculumGroup, courseGroup);
        checkParameters(curriculumGroup, courseGroup, executionInterval);

        setDegreeModule(courseGroup);
        setCurriculumGroup(curriculumGroup);
        addChildCurriculumGroups(courseGroup, executionInterval);
    }

    private void checkParameters(CurriculumGroup parentCurriculumGroup, CourseGroup courseGroup,
            ExecutionInterval executionInterval) {

        if (parentCurriculumGroup == null) {
            throw new DomainException("error.studentCurriculum.curriculumGroup.parentCurriculumGroup.cannot.be.null");
        }

        checkParameters(courseGroup, executionInterval);
    }

    protected void addChildCurriculumGroups(final CourseGroup courseGroup, final ExecutionInterval executionInterval) {
        if (!canCreateGroupOrChilds(courseGroup, executionInterval)) {
            return;
        }

        for (final CourseGroup iter : courseGroup.getNotOptionalChildCourseGroups(executionInterval)) {
            if (!canCreateGroupOrChilds(iter, executionInterval)) {
                continue;
            }

            CurriculumGroupFactory.createGroup(this, iter, executionInterval);
        }
    }

    private boolean canCreateGroupOrChilds(final CourseGroup courseGroup, final ExecutionInterval executionInterval) {

        for (final CurricularRule iter : courseGroup.getCurricularRules(executionInterval)) {
            if (iter.isRulePreventingAutomaticEnrolment()) {
                return false;
            }
        }

        return true;
    }

    @Override
    final public boolean isLeaf() {
        return false;
    }

    @Override
    protected void checkForDeletionBlockers(Collection<String> blockers) {
        super.checkForDeletionBlockers(blockers);
        if (!getCurriculumModulesSet().isEmpty()) {
            blockers.add(BundleUtil.getString(Bundle.APPLICATION,
                    "error.studentCurriculum.CurriculumGroup.notEmptyCurriculumGroupModules", getName().getContent()));
        }
    }

    public boolean isDeletable() {
        return getDeletionBlockers().isEmpty();
    }

    @Override
    public void deleteRecursive() {
        for (final CurriculumModule child : getCurriculumModulesSet()) {
            child.deleteRecursive();
        }

        delete();
    }

    /**
     * Before trying to delete, try to delete only empty child groups, leaving leafs untouched
     */
    protected void deleteRecursiveEmptyChildGroups() {

        for (final Iterator<CurriculumGroup> iterator = getChildCurriculumGroups().iterator(); iterator.hasNext();) {
            iterator.next().deleteRecursiveEmptyChildGroups();
        }

        deleteRecursive();
    }

    @Override
    final public StringBuilder print(String tabs) {
        final StringBuilder builder = new StringBuilder();
        builder.append(tabs);
        builder.append("[CG ").append(getName().getContent()).append(" ]\n");
        final String tab = tabs + "\t";
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            builder.append(curriculumModule.print(tab));
        }
        return builder;
    }

    @Override
    public CourseGroup getDegreeModule() {
        return (CourseGroup) super.getDegreeModule();
    }

    @Override
    final public List<Enrolment> getEnrolments() {
        final List<Enrolment> result = new ArrayList<Enrolment>();
        for (final CurriculumModule curriculumModule : this.getCurriculumModulesSet()) {
            result.addAll(curriculumModule.getEnrolments());
        }
        return result;
    }

    final public Set<Enrolment> getEnrolmentsSet() {
        final Set<Enrolment> result = new HashSet<Enrolment>();
        for (final CurriculumModule curriculumModule : this.getCurriculumModulesSet()) {
            result.addAll(curriculumModule.getEnrolments());
        }
        return result;
    }

    @Override
    final public boolean hasAnyEnrolments() {
        return hasAnyCurriculumModules(new CurriculumModulePredicateByType(Enrolment.class));
    }

    @Override
    public boolean hasAnyCurriculumModules(final Predicate<CurriculumModule> predicate) {
        if (super.hasAnyCurriculumModules(predicate)) {
            return true;
        }

        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (curriculumModule.hasAnyCurriculumModules(predicate)) {
                return true;
            }
        }

        return false;
    }

    @Override
    final public void collectDismissals(final List<Dismissal> result) {
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            curriculumModule.collectDismissals(result);
        }
    }

    public List<Dismissal> getChildDismissals() {
        final List<Dismissal> result = new ArrayList<Dismissal>();
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (curriculumModule.isDismissal()) {
                result.add((Dismissal) curriculumModule);
            }
        }
        return result;
    }

    public double getChildCreditsDismissalEcts() {
        double total = 0;
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (curriculumModule.isCreditsDismissal()) {
                total += curriculumModule.getEctsCredits();
            }
        }

        return total;
    }

    public List<Enrolment> getChildEnrolments() {
        final List<Enrolment> result = new ArrayList<Enrolment>();
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (curriculumModule.isEnrolment()) {
                result.add((Enrolment) curriculumModule);
            }
        }

        return result;
    }

    public List<CurriculumLine> getChildCurriculumLines() {
        final List<CurriculumLine> result = new ArrayList<CurriculumLine>();
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (curriculumModule.isLeaf()) {
                result.add((CurriculumLine) curriculumModule);
            }
        }

        return result;
    }

    public List<CurriculumGroup> getChildCurriculumGroups() {
        final List<CurriculumGroup> result = new ArrayList<CurriculumGroup>();
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (!curriculumModule.isLeaf()) {
                result.add((CurriculumGroup) curriculumModule);
            }
        }

        return result;
    }

    @Override
    public boolean isRoot() {
        return false;
    }

    @Override
    public StudentCurricularPlan getStudentCurricularPlan() {
        return getCurriculumGroup().getStudentCurricularPlan();
    }

    private Collection<Context> getDegreeModulesFor(ExecutionInterval executionInterval) {
        return this.getDegreeModule().getValidChildContexts(executionInterval);
    }

    public List<Context> getCurricularCourseContextsToEnrol(ExecutionInterval executionInterval) {
        List<Context> result = new ArrayList<Context>();
        for (Context context : this.getDegreeModulesFor(executionInterval)) {
            if (context.getChildDegreeModule().isLeaf()) {

                final CurricularCourse curricularCourse = (CurricularCourse) context.getChildDegreeModule();

                if (getDegreeCurricularPlanOfStudent().getCurricularRuleValidationType() == CurricularRuleValidationType.YEAR) {

                    if (!getStudentCurricularPlan().isApproved(curricularCourse, executionInterval)
                            && !getStudentCurricularPlan().getRoot().hasEnrolmentWithEnroledState(curricularCourse,
                                    executionInterval.getExecutionYear())
                            && !hasEnrolmentForInterval(curricularCourse, executionInterval)
                            && matchesIntervalCurricularPeriod(executionInterval, context)) {
                        result.add(context);
                    }

                } else {

                    if (!this.getStudentCurricularPlan().isApproved(curricularCourse, executionInterval)
                            && !this.getStudentCurricularPlan().isEnroledInExecutionPeriod(curricularCourse, executionInterval)
                            && matchesIntervalCurricularPeriod(executionInterval, context)) {
                        result.add(context);
                    }
                }

            }
        }
        return result;
    }

    private boolean matchesIntervalCurricularPeriod(ExecutionInterval executionInterval, Context context) {
        final CurricularCourse curricularCourse = (CurricularCourse) context.getChildDegreeModule();
        return !curricularCourse.isAnual(executionInterval.getExecutionYear())
                || (context.getCurricularPeriod().getChildOrder().intValue() == executionInterval.getChildOrder().intValue()
                        && context.getCurricularPeriod().getAcademicPeriod().equals(executionInterval.getAcademicPeriod()));
    }

    private boolean hasEnrolmentForInterval(CurricularCourse curricularCourse, ExecutionInterval executionInterval) {
        for (final Enrolment enrolment : getStudentCurricularPlan().getEnrolments(curricularCourse)) {
            if (enrolment.isValid(executionInterval)) {
                return true;
            }
        }

        return false;
    }

    public List<Context> getCourseGroupContextsToEnrol(ExecutionInterval executionInterval) {
        List<Context> result = new ArrayList<Context>();
        for (Context context : this.getDegreeModulesFor(executionInterval)) {
            if (!context.getChildDegreeModule().isLeaf()) {
                if (!this.getStudentCurricularPlan().getRoot().hasDegreeModule(context.getChildDegreeModule())) {
                    result.add(context);
                }
            }
        }
        return result;
    }

    public Collection<CurricularCourse> getCurricularCoursesToDismissal(final ExecutionInterval executionInterval) {
        final Set<CurricularCourse> result = new HashSet<CurricularCourse>();
        for (final Context context : getDegreeModule().getOpenChildContexts(CurricularCourse.class, executionInterval)) {
            final CurricularCourse curricularCourse = (CurricularCourse) context.getChildDegreeModule();
            if (!getStudentCurricularPlan().getRoot().isApproved(curricularCourse, null)) {
                result.add(curricularCourse);
            }
        }
        return result;
    }

    @Override
    final public boolean isApproved(CurricularCourse curricularCourse, ExecutionInterval executionInterval) {
        for (final CurriculumModule curriculumModule : this.getCurriculumModulesSet()) {
            if (curriculumModule.isApproved(curricularCourse, executionInterval)) {
                return true;
            }
        }
        return false;
    }

    @Override
    final public boolean isEnroledInExecutionPeriod(CurricularCourse curricularCourse, ExecutionInterval executionInterval) {
        for (final CurriculumModule curriculumModule : this.getCurriculumModulesSet()) {
            if (curriculumModule.isEnroledInExecutionPeriod(curricularCourse, executionInterval)) {
                return true;
            }
        }
        return false;
    }

    @Override
    final public boolean hasEnrolmentWithEnroledState(final CurricularCourse curricularCourse,
            final ExecutionInterval executionInterval) {
        for (final CurriculumModule curriculumModule : this.getCurriculumModulesSet()) {
            if (curriculumModule.hasEnrolmentWithEnroledState(curricularCourse, executionInterval)) {
                return true;
            }
        }
        return false;
    }

    @Override
    final public ExecutionYear getIEnrolmentsLastExecutionYear() {
        ExecutionYear result = null;

        for (final CurriculumModule curriculumModule : this.getCurriculumModulesSet()) {
            final ExecutionYear lastExecutionYear = curriculumModule.getIEnrolmentsLastExecutionYear();
            if (result == null || result.isBefore(lastExecutionYear)) {
                result = lastExecutionYear;
            }
        }

        return result;
    }

    @Override
    public boolean hasDegreeModule(DegreeModule degreeModule) {
        if (super.hasDegreeModule(degreeModule)) {
            return true;
        } else {
            for (final CurriculumModule curriculumModule : this.getCurriculumModulesSet()) {
                if (curriculumModule.hasDegreeModule(degreeModule)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    final public boolean hasCurriculumModule(CurriculumModule curriculumModule) {
        if (super.hasCurriculumModule(curriculumModule)) {
            return true;
        }
        for (final CurriculumModule module : getCurriculumModulesSet()) {
            if (module.hasCurriculumModule(curriculumModule)) {
                return true;
            }
        }
        return false;
    }

    @Override
    final public Enrolment findEnrolmentFor(final CurricularCourse curricularCourse, final ExecutionInterval executionInterval) {
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            final Enrolment search = curriculumModule.findEnrolmentFor(curricularCourse, executionInterval);
            if (search != null) {
                return search;
            }
        }
        return null;
    }

    @Override
    final public Enrolment getApprovedEnrolment(final CurricularCourse curricularCourse) {
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            final Enrolment enrolment = curriculumModule.getApprovedEnrolment(curricularCourse);
            if (enrolment != null) {
                return enrolment;
            }
        }
        return null;
    }

    @Override
    final public Dismissal getDismissal(final CurricularCourse curricularCourse) {
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            final Dismissal dismissal = curriculumModule.getDismissal(curricularCourse);
            if (dismissal != null) {
                return dismissal;
            }
        }
        return null;
    }

    @Override
    final public CurriculumLine getApprovedCurriculumLine(final CurricularCourse curricularCourse) {
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            final CurriculumLine curriculumLine = curriculumModule.getApprovedCurriculumLine(curricularCourse);
            if (curriculumLine != null) {
                return curriculumLine;
            }
        }
        return null;
    }

    public CurriculumGroup findCurriculumGroupFor(final CourseGroup courseGroup) {
        if (getDegreeModule() == courseGroup) {
            return this;
        }
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (!curriculumModule.isLeaf()) {
                final CurriculumGroup curriculumGroup = (CurriculumGroup) curriculumModule;
                final CurriculumGroup searchCurriculumGroup = curriculumGroup.findCurriculumGroupFor(courseGroup);
                if (searchCurriculumGroup != null) {
                    return searchCurriculumGroup;
                }
            }
        }
        return null;
    }

    @Override
    public void getCurriculumModules(final ResultCollection<CurriculumModule> collection) {
        collection.condicionalAdd(this);
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            curriculumModule.getCurriculumModules(collection);
        }
    }

    final public Set<CurriculumLine> getCurriculumLines() {
        Set<CurriculumLine> result = new TreeSet<CurriculumLine>(CurriculumModule.COMPARATOR_BY_NAME_AND_ID);

        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (curriculumModule.isLeaf()) {
                result.add((CurriculumLine) curriculumModule);
            }
        }

        return result;
    }

    final public boolean hasCurriculumLines() {
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (curriculumModule.isLeaf()) {
                return true;
            }
        }

        return false;
    }

    @Override
    final public void addApprovedCurriculumLines(final Collection<CurriculumLine> result) {
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            curriculumModule.addApprovedCurriculumLines(result);
        }
    }

    @Override
    final public boolean hasAnyApprovedCurriculumLines() {
        final AndPredicate<CurriculumModule> andPredicate = new AndPredicate<CurriculumModule>();
        andPredicate.add(new CurriculumModulePredicateByType(CurriculumLine.class));
        andPredicate.add(new CurriculumModulePredicateByApproval());

        return hasAnyCurriculumModules(andPredicate);
    }

    final public Set<CurriculumGroup> getCurriculumGroups() {
        Set<CurriculumGroup> result = new TreeSet<CurriculumGroup>(CurriculumModule.COMPARATOR_BY_NAME_AND_ID);

        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (!curriculumModule.isLeaf()) {
                result.add((CurriculumGroup) curriculumModule);
            }
        }

        return result;
    }

    public Set<CurriculumGroup> getCurriculumGroupsToEnrolmentProcess() {
        final Set<CurriculumGroup> result = new TreeSet<CurriculumGroup>(CurriculumModule.COMPARATOR_BY_NAME_AND_ID);

        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (!curriculumModule.isLeaf() && !curriculumModule.isNoCourseGroupCurriculumGroup()) {
                result.add((CurriculumGroup) curriculumModule);
            }
        }

        return result;
    }

    public Set<BranchCurriculumGroup> getBranchCurriculumGroups() {
        final Set<BranchCurriculumGroup> result = new HashSet<BranchCurriculumGroup>(1);

        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (curriculumModule instanceof CurriculumGroup) {
                final CurriculumGroup curriculumGroup = (CurriculumGroup) curriculumModule;
                result.addAll(curriculumGroup.getBranchCurriculumGroups());
            }
        }

        return result;
    }

    public Set<BranchCurriculumGroup> getBranchCurriculumGroups(final BranchType branchType) {
        final Set<BranchCurriculumGroup> result = new HashSet<BranchCurriculumGroup>(1);

        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (curriculumModule instanceof CurriculumGroup) {
                final CurriculumGroup curriculumGroup = (CurriculumGroup) curriculumModule;
                result.addAll(curriculumGroup.getBranchCurriculumGroups(branchType));
            }
        }

        return result;
    }

    public boolean hasBranchCurriculumGroup(final BranchType type) {
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (curriculumModule instanceof CurriculumGroup) {
                final CurriculumGroup curriculumGroup = (CurriculumGroup) curriculumModule;
                if (curriculumGroup.hasBranchCurriculumGroup(type)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Set<BranchCurriculumGroup> getMajorBranchCurriculumGroups() {
        return getBranchCurriculumGroups(BranchType.MAJOR);
    }

    public Set<BranchCurriculumGroup> getMinorBranchCurriculumGroups() {
        return getBranchCurriculumGroups(BranchType.MINOR);
    }

    public Set<BranchCourseGroup> getBranchCourseGroups(BranchType branchType) {
        final Set<BranchCourseGroup> result = new HashSet<BranchCourseGroup>();
        for (final BranchCurriculumGroup group : getBranchCurriculumGroups(branchType)) {
            result.add(group.getDegreeModule());
        }
        return result;
    }

    public Set<BranchCourseGroup> getMajorBranchCourseGroups() {
        return getBranchCourseGroups(BranchType.MAJOR);
    }

    public Set<BranchCourseGroup> getMinorBranchCourseGroups() {
        return getBranchCourseGroups(BranchType.MINOR);
    }

    @Override
    public Set<CurriculumGroup> getAllCurriculumGroups() {
        Set<CurriculumGroup> result = new HashSet<CurriculumGroup>();
        result.add(this);

        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            result.addAll(curriculumModule.getAllCurriculumGroups());
        }
        return result;
    }

    @Override
    public Set<CurriculumGroup> getAllCurriculumGroupsWithoutNoCourseGroupCurriculumGroups() {
        Set<CurriculumGroup> result = new HashSet<CurriculumGroup>();
        result.add(this);

        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            result.addAll(curriculumModule.getAllCurriculumGroupsWithoutNoCourseGroupCurriculumGroups());
        }
        return result;
    }

    @Override
    public Set<CurriculumLine> getAllCurriculumLines() {
        Set<CurriculumLine> result = new HashSet<CurriculumLine>();
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            result.addAll(curriculumModule.getAllCurriculumLines());
        }
        return result;
    }

    public Integer getChildOrder() {
        return getChildOrder(null);
    }

    public Integer getChildOrder(final ExecutionInterval executionInterval) {
        final Integer childOrder = getParentCurriculumGroup().searchChildOrderForChild(this, executionInterval);
        return childOrder != null ? childOrder : Integer.MAX_VALUE;
    }

    private CurriculumGroup getParentCurriculumGroup() {
        return getCurriculumGroup();
    }

    protected Integer searchChildOrderForChild(final CurriculumGroup child, final ExecutionInterval executionInterval) {
        for (final Context context : getDegreeModule().getValidChildContexts(executionInterval)) {
            if (context.getChildDegreeModule() == child.getDegreeModule()) {
                return context.getChildOrder();
            }
        }
        return null;
    }

    public boolean hasCourseGroup(final CourseGroup courseGroup) {
        if (getDegreeModule().equals(courseGroup)) {
            return true;
        }

        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (!curriculumModule.isLeaf()) {
                CurriculumGroup group = (CurriculumGroup) curriculumModule;
                if (group.hasCourseGroup(courseGroup)) {
                    return true;
                }
            }
        }

        return false;
    }

    final public NoCourseGroupCurriculumGroup getNoCourseGroupCurriculumGroup(NoCourseGroupCurriculumGroupType groupType) {
        for (final CurriculumGroup curriculumGroup : getCurriculumGroups()) {
            if (curriculumGroup.isNoCourseGroupCurriculumGroup()) {
                NoCourseGroupCurriculumGroup noCourseGroupCurriculumGroup = (NoCourseGroupCurriculumGroup) curriculumGroup;
                if (noCourseGroupCurriculumGroup.getNoCourseGroupCurriculumGroupType().equals(groupType)) {
                    return noCourseGroupCurriculumGroup;
                }
            }
        }

        return null;
    }

    @Override
    final public Double getEctsCredits() {
        BigDecimal bigDecimal = BigDecimal.ZERO;
        for (CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            bigDecimal = bigDecimal.add(new BigDecimal(curriculumModule.getEctsCredits()));
        }
        return Double.valueOf(bigDecimal.doubleValue());
    }

    @Override
    public Double getAprovedEctsCredits() {
        BigDecimal bigDecimal = BigDecimal.ZERO;
        for (CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            bigDecimal = bigDecimal.add(new BigDecimal(curriculumModule.getAprovedEctsCredits()));
        }
        return Double.valueOf(bigDecimal.doubleValue());
    }

    @Override
    final public Double getEnroledEctsCredits(final ExecutionInterval executionInterval) {
        BigDecimal bigDecimal = BigDecimal.ZERO;
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            bigDecimal = bigDecimal.add(new BigDecimal(curriculumModule.getEnroledEctsCredits(executionInterval)));
        }
        return Double.valueOf(bigDecimal.doubleValue());
    }

    @Override
    final public Double getEnroledEctsCredits(final ExecutionYear executionYear) {
        //NOTE: this method cannot be implemented iterating over semesters, because annual curricular courses would be accounted twice (they are valid on both semesters)
        BigDecimal bigDecimal = BigDecimal.ZERO;
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            bigDecimal = bigDecimal.add(new BigDecimal(curriculumModule.getEnroledEctsCredits(executionYear)));
        }
        return Double.valueOf(bigDecimal.doubleValue());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Double getCreditsConcluded(ExecutionYear executionYear) {
        final CreditsLimit creditsLimit =
                (CreditsLimit) getMostRecentActiveCurricularRule(CurricularRuleType.CREDITS_LIMIT, executionYear);

        Double creditsConcluded = 0d;
        for (CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            creditsConcluded += curriculumModule.getCreditsConcluded(executionYear);
        }

        if (creditsLimit == null) {
            return creditsConcluded;
        } else {
            return Math.min(creditsLimit.getMaximumCredits(), creditsConcluded);
        }
    }

    final public int getNumberOfChildCurriculumGroupsWithCourseGroup() {
        int result = 0;
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (!curriculumModule.isLeaf()) {
                final CurriculumGroup curriculumGroup = (CurriculumGroup) curriculumModule;
                if (!curriculumGroup.isNoCourseGroupCurriculumGroup()) {
                    result++;
                }
            }
        }
        return result;
    }

    /**
     * This method returns the number of approved child CurriculumLines
     */
    final public int getNumberOfApprovedChildCurriculumLines() {
        int result = 0;
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (curriculumModule.isCurriculumLine()) {
                final CurriculumLine curriculumLine = (CurriculumLine) curriculumModule;
                if (curriculumLine.isDismissal() && curriculumLine.hasCurricularCourse()) {
                    result++;
                } else if (curriculumLine.isEnrolment() && ((Enrolment) curriculumLine).isApproved()) {
                    result++;
                }
            }
        }
        return result;
    }

    /**
     * This method makes a deep search to count number of all enrolled
     * CurriculumLines (except NoCourseGroupCurriculumGroups)
     */
    public int getNumberOfAllEnroledCurriculumLines() {
        int result = 0;
        for (CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (curriculumModule.isCurriculumLine()) {
                final CurriculumLine curriculumLine = (CurriculumLine) curriculumModule;
                if (curriculumLine.isEnrolment()) {
                    result++;
                }
            } else {
                result += ((CurriculumGroup) curriculumModule).getNumberOfAllEnroledCurriculumLines();
            }
        }
        return result;
    }

    /**
     * This method makes a deep search to count number of all approved
     * CurriculumLines (except NoCourseGroupCurriculumGroups)
     */
    public int getNumberOfAllApprovedCurriculumLines() {
        int result = 0;
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (curriculumModule.isCurriculumLine()) {
                final CurriculumLine curriculumLine = (CurriculumLine) curriculumModule;
                if (curriculumLine.isDismissal() && curriculumLine.hasCurricularCourse()) {
                    result++;
                } else if (curriculumLine.isEnrolment() && ((Enrolment) curriculumLine).isApproved()) {
                    result++;
                }
            } else {
                result += ((CurriculumGroup) curriculumModule).getNumberOfAllApprovedCurriculumLines();
            }
        }
        return result;
    }

    final public int getNumberOfChildEnrolments(final ExecutionInterval executionInterval) {
        int result = 0;
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (curriculumModule instanceof Enrolment) {
                final Enrolment enrolment = (Enrolment) curriculumModule;
                if (enrolment.isValid(executionInterval) && enrolment.isEnroled()) {
                    result++;
                }
            }
        }
        return result;
    }

    final public int getNumberOfChildEnrolments(final ExecutionYear executionYear) {
        //NOTE: this method cannot be implemented iterating over semesters, because annual curricular courses would be 
        //accounted twice (they are valid on both semesters)
        int result = 0;
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (curriculumModule instanceof Enrolment) {
                final Enrolment enrolment = (Enrolment) curriculumModule;
                if (enrolment.isValid(executionYear) && enrolment.isEnroled()) {
                    result++;
                }
            }
        }
        return result;
    }

    @Override
    public int getNumberOfAllApprovedEnrolments(final ExecutionInterval executionInterval) {
        int result = 0;
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            result += curriculumModule.getNumberOfAllApprovedEnrolments(executionInterval);
        }
        return result;
    }

    @Override
    public Set<IDegreeModuleToEvaluate> getDegreeModulesToEvaluate(final ExecutionInterval executionInterval) {
        final Set<IDegreeModuleToEvaluate> result = new HashSet<IDegreeModuleToEvaluate>();
        result.add(new EnroledCurriculumModuleWrapper(this, executionInterval));

        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            result.addAll(curriculumModule.getDegreeModulesToEvaluate(executionInterval));
        }
        return result;
    }

    @Override
    final public void getAllDegreeModules(final Collection<DegreeModule> degreeModules) {
        degreeModules.add(getDegreeModule());
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            curriculumModule.getAllDegreeModules(degreeModules);
        }
    }

    @Override
    public ConclusionValue isConcluded(final ExecutionYear executionYear) {
        if (isToCheckCreditsLimits(executionYear)) {
            return ConclusionValue.create(checkCreditsLimits(executionYear));
        } else if (isToCheckDegreeModulesSelectionLimit(executionYear)) {
            return ConclusionValue.create(checkDegreeModulesSelectionLimit(executionYear));
        } else {
            return ConclusionValue.UNKNOWN;
        }
    }

    public void assertCorrectStructure(final Collection<CurriculumGroup> result, ExecutionYear lastApprovedYear) {

        if (isSkipConcluded()) {
            return;
        }

        for (final CurriculumGroup curriculumGroup : getCurriculumGroups()) {
            if (curriculumGroup.getCurriculumGroups().isEmpty() && curriculumGroup.hasUnexpectedCredits(lastApprovedYear)) {
                result.add(curriculumGroup);
            } else {
                curriculumGroup.assertCorrectStructure(result, lastApprovedYear);
            }
        }
    }

    public boolean hasUnexpectedCredits(ExecutionYear lastApprovedYear) {
        return getAprovedEctsCredits().doubleValue() != getCreditsConcluded(lastApprovedYear).doubleValue();
    }

    public boolean hasInsufficientCredits() {
        return getAprovedEctsCredits().doubleValue() < getCreditsConcluded().doubleValue();
    }

    private boolean isToCheckDegreeModulesSelectionLimit(ExecutionYear executionYear) {
        return getMostRecentActiveCurricularRule(CurricularRuleType.DEGREE_MODULES_SELECTION_LIMIT, executionYear) != null;
    }

    private boolean isToCheckCreditsLimits(ExecutionYear executionYear) {
        return getMostRecentActiveCurricularRule(CurricularRuleType.CREDITS_LIMIT, executionYear) != null;
    }

    @SuppressWarnings("unchecked")
    private boolean checkCreditsLimits(ExecutionYear executionYear) {
        final CreditsLimit creditsLimit =
                (CreditsLimit) getMostRecentActiveCurricularRule(CurricularRuleType.CREDITS_LIMIT, executionYear);

        if (creditsLimit == null) {
            return false;
        } else {
            Double creditsConcluded = 0d;
            for (CurriculumModule curriculumModule : getCurriculumModulesSet()) {
                if (curriculumModule.isConcluded(executionYear).isValid()) {
                    creditsConcluded += curriculumModule.getCreditsConcluded(executionYear);
                }
            }

            return creditsConcluded >= creditsLimit.getMinimumCredits();
        }
    }

    @SuppressWarnings("unchecked")
    private boolean checkDegreeModulesSelectionLimit(ExecutionYear executionYear) {
        final DegreeModulesSelectionLimit degreeModulesSelectionLimit =
                (DegreeModulesSelectionLimit) getMostRecentActiveCurricularRule(CurricularRuleType.DEGREE_MODULES_SELECTION_LIMIT,
                        executionYear);

        if (degreeModulesSelectionLimit == null) {
            return false;
        } else {
            int modulesConcluded = 0;
            for (CurriculumModule curriculumModule : getCurriculumModulesSet()) {
                if (curriculumModule.isConcluded(executionYear).value()) {
                    modulesConcluded++;
                }
            }

            return modulesConcluded >= degreeModulesSelectionLimit.getMinimumLimit();
        }
    }

    @Override
    public boolean hasConcluded(final DegreeModule degreeModule, final ExecutionYear executionYear) {
        if (getDegreeModule() == degreeModule) {
            return isConcluded(executionYear).value();
        }

        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (curriculumModule.hasConcluded(degreeModule, executionYear)) {
                return true;
            }
        }

        return false;

    }

    @Override
    public YearMonthDay calculateConclusionDate() {
        final Collection<CurriculumModule> curriculumModules = new HashSet<CurriculumModule>(getCurriculumModulesSet());
        YearMonthDay result = null;
        for (final CurriculumModule curriculumModule : curriculumModules) {
            if (curriculumModule.isConcluded(getApprovedCurriculumLinesLastExecutionYear()).isValid()
                    && curriculumModule.hasAnyApprovedCurriculumLines()) {
                final YearMonthDay curriculumModuleConclusionDate = curriculumModule.calculateConclusionDate();
                if (curriculumModuleConclusionDate != null
                        && (result == null || curriculumModuleConclusionDate.isAfter(result))) {
                    result = curriculumModuleConclusionDate;
                }
            }
        }

        return result;
    }

    public void assertConclusionDate(final Collection<CurriculumModule> result) {
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (curriculumModule.isConcluded(getApprovedCurriculumLinesLastExecutionYear()).isValid()
                    && curriculumModule.hasAnyApprovedCurriculumLines()) {
                final YearMonthDay curriculumModuleConclusionDate = curriculumModule.calculateConclusionDate();
                if (curriculumModuleConclusionDate == null) {
                    result.add(curriculumModule);
                }
            }
        }
    }

    @Override
    public Curriculum getCurriculum(final DateTime when, final ExecutionYear executionYear) {
        return getCurriculumSupplier().get(this, when, executionYear);
    }

    static public interface CurriculumSupplier {

        public Curriculum get(final CurriculumGroup curriculumGroup, final DateTime when, final ExecutionYear executionYear);
    }

    static private Supplier<CurriculumSupplier> CURRICULUM_SUPPLIER = () -> new CurriculumSupplier() {

        @Override
        public Curriculum get(final CurriculumGroup curriculumGroup, final DateTime when, final ExecutionYear executionYear) {

            final Curriculum curriculum = Curriculum.createEmpty(curriculumGroup, executionYear);
            if (!curriculumGroup.wasCreated(when)) {
                return curriculum;
            }

            for (final CurriculumModule curriculumModule : curriculumGroup.getCurriculumModulesSet()) {
                curriculum.add(curriculumModule.getCurriculum(when, executionYear));
            }

            return curriculum;
        }
    };

    static public CurriculumSupplier getCurriculumSupplier() {
        return CURRICULUM_SUPPLIER.get();
    }

    static public void setCurriculumSupplier(final Supplier<CurriculumSupplier> input) {
        if (input != null && input.get() != null) {
            CURRICULUM_SUPPLIER = input;
        } else {
            logger.error("Could not set CURRICULUM_SUPPLIER to null");
        }
    }

    @Override
    public boolean isPropaedeutic() {
        return getCurriculumGroup() != null && getCurriculumGroup().isPropaedeutic();
    }

    public boolean isExtraCurriculum() {
        return false;
    }

    public boolean isStandalone() {
        return false;
    }

    public boolean isInternalCreditsSourceGroup() {
        return false;
    }

    public boolean isExternal() {
        return false;
    }

    public boolean canAdd(final CurriculumLine curriculumLine) {

        final CurricularCourse curricularCourse =
                curriculumLine instanceof OptionalEnrolment ? ((OptionalEnrolment) curriculumLine)
                        .getOptionalCurricularCourse() : curriculumLine.getCurricularCourse();

        if (curricularCourse == null) {
            return true;
        }

        return getDegreeModule().hasDegreeModuleOnChilds(curricularCourse);
    }

    public Collection<CurriculumGroup> getCurricularCoursePossibleGroups(final CurricularCourse curricularCourse) {
        Collection<CurriculumGroup> result = new HashSet<CurriculumGroup>();
        if (getDegreeModule().hasDegreeModuleOnChilds(curricularCourse)) {
            result.add(this);
        }

        for (CurriculumGroup curriculumGroup : this.getCurriculumGroups()) {
            result.addAll(curriculumGroup.getCurricularCoursePossibleGroups(curricularCourse));
        }

        return result;
    }

    public Collection<CurriculumGroup> getCurricularCoursePossibleGroupsWithoutNoCourseGroupCurriculumGroups(
            final CurricularCourse curricularCourse) {
        Collection<CurriculumGroup> result = new HashSet<CurriculumGroup>();
        if (getDegreeModule().hasDegreeModuleOnChilds(curricularCourse)) {
            result.add(this);
        }

        for (CurriculumGroup curriculumGroup : this.getCurriculumGroups()) {
            result.addAll(
                    curriculumGroup.getCurricularCoursePossibleGroupsWithoutNoCourseGroupCurriculumGroups(curricularCourse));
        }

        return result;
    }

    public Collection<NoCourseGroupCurriculumGroup> getNoCourseGroupCurriculumGroups() {
        Collection<NoCourseGroupCurriculumGroup> res = new HashSet<NoCourseGroupCurriculumGroup>();
        for (CurriculumGroup curriculumGroup : getCurriculumGroups()) {
            res.addAll(curriculumGroup.getNoCourseGroupCurriculumGroups());
        }
        return res;
    }

    public int getNoCourseGroupCurriculumGroupsCount() {
        return getNoCourseGroupCurriculumGroups().size();
    }

    public boolean hasChildDegreeModule(final DegreeModule degreeModule) {
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (curriculumModule.getDegreeModule() == degreeModule) {
                return true;
            }
        }

        return false;
    }

    public CurriculumModule getChildCurriculumModule(final DegreeModule degreeModule) {
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (curriculumModule.getDegreeModule() == degreeModule) {
                return curriculumModule;
            }
        }
        return null;
    }

    @Override
    public boolean hasEnrolment(ExecutionYear executionYear) {
        final AndPredicate<CurriculumModule> andPredicate = new AndPredicate<CurriculumModule>();
        andPredicate.add(new CurriculumModulePredicateByType(Enrolment.class));
        andPredicate.add(new CurriculumModulePredicateByExecutionYear(executionYear));

        return hasAnyCurriculumModules(andPredicate);
    }

    @Override
    public boolean hasEnrolment(ExecutionInterval executionInterval) {
        final AndPredicate<CurriculumModule> andPredicate = new AndPredicate<CurriculumModule>();
        andPredicate.add(new CurriculumModulePredicateByType(Enrolment.class));
        andPredicate.add(new CurriculumModulePredicateByExecutionInterval(executionInterval));

        return hasAnyCurriculumModules(andPredicate);
    }

    @Override
    public boolean isEnroledInSpecialSeason(final ExecutionInterval executionInterval) {
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (curriculumModule.isEnroledInSpecialSeason(executionInterval)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isEnroledInSpecialSeason(final ExecutionYear executionYear) {
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (curriculumModule.isEnroledInSpecialSeason(executionYear)) {
                return true;
            }
        }
        return false;
    }

    public Set<Enrolment> getEnrolmentsBy(final ExecutionYear executionYear) {
        final Set<Enrolment> result = new HashSet<Enrolment>();
        for (final Enrolment enrolment : getEnrolmentsSet()) {
            if (enrolment.getExecutionYear() == executionYear) {
                result.add(enrolment);
            }
        }

        return result;
    }

    public Set<Enrolment> getEnrolmentsBy(final ExecutionInterval executionInterval) {
        final Set<Enrolment> result = new HashSet<Enrolment>();
        for (final Enrolment enrolment : getEnrolmentsSet()) {
            if (enrolment.getExecutionInterval() == executionInterval) {
                result.add(enrolment);
            }
        }

        return result;
    }

    public boolean hasEnrolmentInCurricularCourseBefore(final CurricularCourse curricularCourse,
            final ExecutionInterval executionInterval) {
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (curriculumModule.isEnrolment()) {
                final Enrolment enrolment = (Enrolment) curriculumModule;
                if (!enrolment.isAnnulled() && enrolment.getExecutionInterval().isBefore(executionInterval)
                        && enrolment.getCurricularCourse() == curricularCourse) {
                    return true;
                }
            } else if (curriculumModule instanceof CurriculumGroup) {
                final CurriculumGroup curriculumGroup = (CurriculumGroup) curriculumModule;
                if (curriculumGroup.hasEnrolmentInCurricularCourseBefore(curricularCourse, executionInterval)) {
                    return true;
                }
            }
        }
        return false;
    }

    public int calculateStudentAcumulatedEnrollments(CurricularCourse curricularCourse, ExecutionInterval executionInterval) {
        int result = 0;
        for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
            if (curriculumModule.isEnrolment()) {
                final Enrolment enrolment = (Enrolment) curriculumModule;
                if (!enrolment.isAnnulled() && enrolment.getExecutionInterval().isBefore(executionInterval)
                        && enrolment.getCurricularCourse().getCurricularCourseUniqueKeyForEnrollment()
                                .equalsIgnoreCase(curricularCourse.getCurricularCourseUniqueKeyForEnrollment())) {
                    result++;
                }
            } else if (curriculumModule instanceof CurriculumGroup) {
                final CurriculumGroup curriculumGroup = (CurriculumGroup) curriculumModule;
                result += curriculumGroup.calculateStudentAcumulatedEnrollments(curricularCourse, executionInterval);
            }
        }
        return result;
    }

    @Override
    public void setConclusionProcess(ConclusionProcess process) {
        if (process != null && !process.isActive()) {
            throw new DomainException("error.conclusion.process.not.valid");
        }
        super.setConclusionProcess(process);
    }

    @Override
    public ConclusionProcess getConclusionProcess() {
        final ConclusionProcess conclusionProcess = super.getConclusionProcess();
        if (conclusionProcess != null && conclusionProcess.isActive()) {
            return conclusionProcess;
        }
        return null;
    }

    public ConclusionProcess readConclusionProcessEvenIfInactive() {
        return super.getConclusionProcess();
    }

    // Stuff moved from CycleCurriculumGroup

    public void conclude() {
        if (isConclusionProcessed()) {
            if (!getRegistration().canRepeatConclusionProcess(AccessControl.getPerson())) {
                throw new DomainException("error.CycleCurriculumGroup.cycle.is.already.concluded", getDegreeModule().getName());
            }
        }

        ProgramConclusion conclusion = getDegreeModule().getProgramConclusion();

        if (conclusion == null) {
            throw new DomainException("error.program.conclusion.empty");
        }

        if (!getConclusionProcessEnabler().isAllowed(this)) {
            throw new DomainException("error.CycleCurriculumGroup.cycle.is.not.concluded");
        }

        final RegistrationConclusionBean bean =
                new RegistrationConclusionBean(getStudentCurricularPlan(), getDegreeModule().getProgramConclusion());
        if (super.getConclusionProcess() != null) {
            super.getConclusionProcess().update(bean);
        } else {
            super.setConclusionProcess(new ProgramConclusionProcess(bean));
        }
    }

    static public interface ConclusionProcessEnabler {

        public boolean isAllowed(final CurriculumGroup curriculumGroup);
    }

    static private Supplier<ConclusionProcessEnabler> CONCLUSION_PROCESS_ENABLER = () -> new ConclusionProcessEnabler() {

        @Override
        public boolean isAllowed(final CurriculumGroup curriculumGroup) {

            return curriculumGroup != null && curriculumGroup.isConcluded();
        }
    };

    static public ConclusionProcessEnabler getConclusionProcessEnabler() {
        return CONCLUSION_PROCESS_ENABLER.get();
    }

    static public void setConclusionProcessEnabler(final Supplier<ConclusionProcessEnabler> input) {
        if (input != null && input.get() != null) {
            CONCLUSION_PROCESS_ENABLER = input;
        } else {
            logger.error("Could not set CONCLUSION_PROCESS_ENABLER to null");
        }
    }

    public boolean isSkipConcluded() {
        return getDegreeModule() != null && getDegreeModule().getProgramConclusion() != null
                && getDegreeModule().getProgramConclusion().isSkipValidation();
    }

    @Override
    public boolean isConcluded() {
        return isConclusionProcessed() || isSkipConcluded() || super.isConcluded();
    }

    public boolean isConclusionProcessed() {
        return getConclusionProcess() != null;
    }

    final public ExecutionYear getIngressionYear() {
        return isConclusionProcessed() ? getConclusionProcess().getIngressionYear() : getRegistration().calculateIngressionYear();
    }

    final public ExecutionYear calculateIngressionYear() {
        return getRegistration().calculateIngressionYear();
    }

    final public Grade getRawGrade() {
        return isConclusionProcessed() ? getConclusionProcess().getRawGrade() : null;
    }

    final public Grade getFinalGrade() {
        return isConclusionProcessed() ? getConclusionProcess().getFinalGrade() : null;
    }

    final public Grade getDescriptiveGrade() {
        return isConclusionProcessed() ? getConclusionProcess().getDescriptiveGrade() : null;
    }

    final public ExecutionYear getConclusionYear() {
        return isConclusionProcessed() ? getConclusionProcess().getConclusionYear() : null;
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

    public void editConclusionInformation(final Person editor, final Grade finalGrade, final Grade rawGrade,
            final Grade descriptiveGrade, final YearMonthDay conclusion, final String notes) {
        if (!isConclusionProcessed()) {
            throw new DomainException(
                    "error.org.fenixedu.academic.domain.studentCurriculum.CycleCurriculumGroup.its.only.possible.to.edit.after.conclusion.process.has.been.performed");
        }

        if (finalGrade == null || rawGrade == null || conclusion == null) {
            throw new DomainException("error.CycleCurriculumGroup.argument.must.not.be.null");
        }

        getConclusionProcess().update(editor, finalGrade, rawGrade, descriptiveGrade, conclusion.toLocalDate(), notes);
    }

    final public ExecutionYear calculateConclusionYear() {
        return getLastApprovementExecutionYear();
    }

    final public YearMonthDay getConclusionDate() {
        return isConclusionProcessed() ? getConclusionProcess().getConclusionYearMonthDay() : null;
    }

    @Override
    public Double getCreditsConcluded() {
        return isConclusionProcessed() ? getConclusionProcess().getCredits().doubleValue() : calculateCreditsConcluded();
    }

    final public Double calculateCreditsConcluded() {
        return super.getCreditsConcluded();
    }

    @Override
    public Stream<CurriculumLine> getCurriculumLineStream() {
        return getCurriculumModulesSet().stream().flatMap(m -> m.getCurriculumLineStream());
    }

}
