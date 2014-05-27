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

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.text.Collator;
import java.util.Collection;
import java.util.Comparator;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.DomainObjectUtil;
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumLine;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.predicates.ContextPredicates;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.dml.runtime.RelationAdapter;

public class Context extends Context_Base implements Comparable<Context> {

    public static final Comparator<Context> COMPARATOR_BY_DEGREE_MODULE_NAME = new Comparator<Context>() {

        @Override
        public int compare(Context o1, Context o2) {
            final DegreeModule d1 = o1.getChildDegreeModule();
            final DegreeModule d2 = o2.getChildDegreeModule();
            final int c = Collator.getInstance().compare(d1.getName(), d2.getName());
            return c == 0 ? DomainObjectUtil.COMPARATOR_BY_ID.compare(d1, d2) : c;
        }

    };

    public static Comparator<Context> COMPARATOR_BY_CURRICULAR_YEAR = new Comparator<Context>() {
        @Override
        public int compare(Context leftContext, Context rightContext) {
            int comparationResult = leftContext.getCurricularYear().compareTo(rightContext.getCurricularYear());
            return (comparationResult == 0) ? leftContext.getExternalId().compareTo(rightContext.getExternalId()) : comparationResult;
        }
    };

    static {
        getRelationCourseGroupContext().addListener(new RelationAdapter<CourseGroup, Context>() {

            @Override
            public void beforeAdd(CourseGroup courseGroup, Context context) {
                if (context != null && courseGroup != null) {
                    if (context.getChildDegreeModule() != null && context.getChildDegreeModule().isCycleCourseGroup()) {
                        validateCycleCourseGroupParent(context, courseGroup);
                    }

                    if (context.getChildDegreeModule() != null && context.getParentCourseGroup() != null
                            && context.getParentCourseGroup().hasAnyParentBranchCourseGroup()
                            && context.getChildDegreeModule().isBranchCourseGroup()) {
                        throw new DomainException("error.degreeStructure.BranchCourseGroup.cant.have.branch.parent");
                    }
                }
            }

            private void validateCycleCourseGroupParent(Context context, CourseGroup courseGroup) {
                CycleCourseGroup cycleCourseGroup = (CycleCourseGroup) context.getChildDegreeModule();
                if (cycleCourseGroup.getParentContexts().size() > 1) {
                    throw new DomainException("error.degreeStructure.CycleCourseGroup.can.only.have.one.parent");
                }
                if (!courseGroup.isRoot()) {
                    throw new DomainException("error.degreeStructure.CycleCourseGroup.parent.must.be.RootCourseGroup");
                }
            }

        });

        getRelationDegreeModuleContext().addListener(new RelationAdapter<Context, DegreeModule>() {
            @Override
            public void beforeAdd(Context context, DegreeModule degreeModule) {
                if (context != null && degreeModule != null) {
                    if (degreeModule.isRoot()) {
                        throw new DomainException("error.degreeStructure.RootCourseGroup.cannot.have.parent.contexts");
                    }
                    if (degreeModule.isCycleCourseGroup()) {
                        CycleCourseGroup cycleCourseGroup = (CycleCourseGroup) degreeModule;
                        if (cycleCourseGroup.getParentContexts().size() > 0) {
                            throw new DomainException("error.degreeStructure.CycleCourseGroup.can.only.have.one.parent");
                        }
                        if (context.getParentCourseGroup() != null && !context.getParentCourseGroup().isRoot()) {
                            throw new DomainException("error.degreeStructure.CycleCourseGroup.parent.must.be.RootCourseGroup");
                        }
                    }
                }
            }
        });

    }

    protected Context() {
        super();
        setRootDomainObject(Bennu.getInstance());
        this.setChildOrder(0);
    }

    public Context(final CourseGroup courseGroup, final DegreeModule degreeModule, final CurricularPeriod curricularPeriod,
            final ExecutionSemester begin, final ExecutionSemester end) {

        this();

        checkParameters(courseGroup, degreeModule, begin);
        checkExecutionPeriods(begin, end);
        checkIfCanAddDegreeModuleToCourseGroup(courseGroup, degreeModule, curricularPeriod, begin.getExecutionYear());
        checkExistingCourseGroupContexts(courseGroup, degreeModule, curricularPeriod, begin, end);

        super.setParentCourseGroup(courseGroup);
        super.setChildDegreeModule(degreeModule);
        super.setCurricularPeriod(curricularPeriod);
        super.setBeginExecutionPeriod(begin);
        super.setEndExecutionPeriod(end);
    }

    private void checkIfCanAddDegreeModuleToCourseGroup(final CourseGroup courseGroup, final DegreeModule degreeModule,
            final CurricularPeriod curricularPeriod, final ExecutionYear executionYear) {
        if (degreeModule.isLeaf()) {
            checkIfCanAddCurricularCourseToCourseGroup(courseGroup, (CurricularCourse) degreeModule, curricularPeriod,
                    executionYear);
        } else {
            checkIfCanAddCourseGroupToCourseGroup(courseGroup, (CourseGroup) degreeModule);
        }
    }

    private void checkIfCanAddCurricularCourseToCourseGroup(final CourseGroup parent, final CurricularCourse curricularCourse,
            final CurricularPeriod curricularPeriod, final ExecutionYear executionYear) {
        if (curricularCourse.hasCompetenceCourse() && curricularCourse.getCompetenceCourse().isAnual(executionYear)
                && !curricularPeriod.hasChildOrderValue(1)) {
            throw new DomainException("competenceCourse.anual.but.trying.to.associate.curricular.course.not.to.first.period");
        }
    }

    private void checkIfCanAddCourseGroupToCourseGroup(final CourseGroup parent, final CourseGroup courseGroup) {
        parent.checkDuplicateChildNames(courseGroup.getName(), courseGroup.getNameEn());
    }

    private void checkParameters(CourseGroup courseGroup, DegreeModule degreeModule, ExecutionSemester beginExecutionPeriod) {
        if (courseGroup == null || degreeModule == null || beginExecutionPeriod == null) {
            throw new DomainException("error.incorrectContextValues");
        }
    }

    private void checkExistingCourseGroupContexts(final CourseGroup courseGroup, final DegreeModule degreeModule,
            final CurricularPeriod curricularPeriod, final ExecutionSemester begin, final ExecutionSemester end) {

        for (final Context context : courseGroup.getChildContexts()) {
            if (context != this && context.hasChildDegreeModule(degreeModule) && context.hasCurricularPeriod(curricularPeriod)
                    && context.contains(begin, end)) {
                throw new DomainException("courseGroup.contextAlreadyExistForCourseGroup");
            }
        }
    }

    public void edit(final CourseGroup parent, final CurricularPeriod curricularPeriod, final ExecutionSemester begin,
            final ExecutionSemester end) {
        setParentCourseGroup(parent);
        setCurricularPeriod(curricularPeriod);
        edit(begin, end);
    }

    protected void edit(final ExecutionSemester begin, final ExecutionSemester end) {
        checkExecutionPeriods(begin, end);
        checkExistingCourseGroupContexts(getParentCourseGroup(), getChildDegreeModule(), getCurricularPeriod(), begin, end);
        setBeginExecutionPeriod(begin);
        setEndExecutionPeriod(end);
        checkCurriculumLines(getChildDegreeModule());
    }

    private void checkCurriculumLines(final DegreeModule degreeModule) {
        for (final CurriculumModule curriculumModule : degreeModule.getCurriculumModules()) {
            if (curriculumModule.isCurriculumLine()) {
                final CurriculumLine curriculumLine = (CurriculumLine) curriculumModule;
                if (curriculumLine.hasExecutionPeriod()
                        && !degreeModule.hasAnyOpenParentContexts(curriculumLine.getExecutionPeriod())) {
                    throw new DomainException("error.Context.cannot.modify.begin.and.end.because.of.enroled.curriculumLines");
                }
            }
        }
    }

    public void delete() {

        final DegreeModule degreeModule = getChildDegreeModule();
        setChildDegreeModule(null);
        /*
         * First remove child and then check if all curriculum lines remain
         * valid
         */
        checkCurriculumLines(degreeModule);

        setCurricularPeriod(null);
        setParentCourseGroup(null);
        super.setBeginExecutionPeriod(null);
        setEndExecutionPeriod(null);
        setRootDomainObject(null);
        getAssociatedWrittenEvaluations().clear();
        super.deleteDomainObject();
    }

    @Override
    public int compareTo(Context o) {
        int orderCompare = this.getChildOrder().compareTo(o.getChildOrder());
        if (this.getParentCourseGroup().equals(o.getParentCourseGroup()) && orderCompare != 0) {
            return orderCompare;
        } else {
            if (this.getChildDegreeModule() instanceof CurricularCourse) {
                int periodsCompare = this.getCurricularPeriod().compareTo(o.getCurricularPeriod());
                if (periodsCompare != 0) {
                    return periodsCompare;
                }
                return Collator.getInstance().compare(this.getChildDegreeModule().getName(), o.getChildDegreeModule().getName());
            } else {
                return Collator.getInstance().compare(this.getChildDegreeModule().getName(), o.getChildDegreeModule().getName());
            }
        }
    }

    @Override
    public void setParentCourseGroup(CourseGroup courseGroup) {
        check(this, ContextPredicates.curricularPlanMemberWritePredicate);
        super.setParentCourseGroup(courseGroup);
    }

    @Override
    public void setCurricularPeriod(CurricularPeriod curricularPeriod) {
        check(this, ContextPredicates.curricularPlanMemberWritePredicate);
        super.setCurricularPeriod(curricularPeriod);
    }

    @Override
    public void setChildDegreeModule(DegreeModule degreeModule) {
        check(this, ContextPredicates.curricularPlanMemberWritePredicate);
        super.setChildDegreeModule(degreeModule);
    }

    public boolean isValid(final ExecutionSemester executionSemester) {
        if (isOpen(executionSemester)) {
            if (getChildDegreeModule().isCurricularCourse()) {
                CurricularCourse curricularCourse = (CurricularCourse) getChildDegreeModule();
                if (!curricularCourse.isAnual(executionSemester.getExecutionYear())) {
                    return containsSemester(executionSemester.getSemester());
                }
            }
            return true;
        }
        return false;
    }

    public boolean isValid(final ExecutionYear executionYear) {
        for (final ExecutionSemester executionSemester : executionYear.getExecutionPeriods()) {
            if (isValid(executionSemester)) {
                return true;
            }
        }
        return false;
    }

    public boolean isValid(AcademicInterval academicInterval) {
        ExecutionInterval interval = ExecutionInterval.getExecutionInterval(academicInterval);
        if (interval instanceof ExecutionSemester) {
            return isValid((ExecutionSemester) interval);
        }
        if (interval instanceof ExecutionYear) {
            return isValid((ExecutionYear) interval);
        }
        throw new UnsupportedOperationException("Unknown academicIntervalType: " + academicInterval);
    }

    protected void checkExecutionPeriods(ExecutionSemester beginExecutionPeriod, ExecutionSemester endExecutionPeriod) {
        if (beginExecutionPeriod == null) {
            throw new DomainException("context.begin.execution.period.cannot.be.null");
        }
        if (endExecutionPeriod != null && beginExecutionPeriod.isAfter(endExecutionPeriod)) {
            throw new DomainException("context.begin.is.after.end.execution.period");
        }
    }

    public boolean isOpen(final ExecutionSemester executionSemester) {
        return getBeginExecutionPeriod().isBeforeOrEquals(executionSemester)
                && (!hasEndExecutionPeriod() || getEndExecutionPeriod().isAfterOrEquals(executionSemester));
    }

    public boolean isOpen(final ExecutionYear executionYear) {
        for (final ExecutionSemester executionSemester : executionYear.getExecutionPeriods()) {
            if (isOpen(executionSemester)) {
                return true;
            }
        }
        return false;
    }

    public boolean isOpen() {
        return isOpen(ExecutionSemester.readActualExecutionSemester());
    }

    public boolean contains(final ExecutionSemester begin, final ExecutionSemester end) {
        if (end != null && begin.isAfter(end)) {
            throw new DomainException("context.begin.is.after.end.execution.period");
        }

        if (begin.isAfterOrEquals(getBeginExecutionPeriod())) {
            return !hasEndExecutionPeriod() || begin.isBeforeOrEquals(getEndExecutionPeriod());
        } else {
            return end == null || end.isAfterOrEquals(getBeginExecutionPeriod());
        }
    }

    @Deprecated
    public Integer getOrder() {
        return super.getChildOrder();
    }

    @Deprecated
    public void setOrder(Integer order) {
        super.setChildOrder(order);
    }

    public boolean containsCurricularYear(final Integer curricularYear) {
        final CurricularPeriod firstCurricularPeriod = getCurricularPeriod().getParent();
        final int firstCurricularPeriodOrder = firstCurricularPeriod.getAbsoluteOrderOfChild();
        return curricularYear.intValue() == firstCurricularPeriodOrder;
    }

    public boolean containsSemester(final Integer semester) {
        final CurricularPeriod firstCurricularPeriod = getCurricularPeriod();
        final int firstCurricularPeriodOrder = firstCurricularPeriod.getChildOrder();
        return semester.intValue() == firstCurricularPeriodOrder;
    }

    public boolean containsSemesterAndCurricularYear(final Integer semester, final Integer curricularYear,
            final RegimeType regimeType) {

        final int argumentOrder = (curricularYear - 1) * 2 + semester.intValue();
        final CurricularPeriod firstCurricularPeriod = getCurricularPeriod();
        final int firstCurricularPeriodOrder = firstCurricularPeriod.getAbsoluteOrderOfChild();
        final int duration;
        if (regimeType == RegimeType.ANUAL) {
            duration = 2;
        } else if (regimeType == RegimeType.SEMESTRIAL) {
            duration = 1;
        } else {
            throw new IllegalArgumentException("Unknown regimeType: " + regimeType);
        }
        final int lastCurricularPeriodOrder = firstCurricularPeriodOrder + duration - 1;
        return firstCurricularPeriodOrder <= argumentOrder && argumentOrder <= lastCurricularPeriodOrder;
    }

    public DegreeModuleScopeContext getDegreeModuleScopeContext() {
        return new DegreeModuleScopeContext(this);
    }

    @Override
    public void setBeginExecutionPeriod(ExecutionSemester beginExecutionPeriod) {
        if (beginExecutionPeriod == null) {
            throw new DomainException("curricular.rule.begin.execution.period.cannot.be.null");
        }
        super.setBeginExecutionPeriod(beginExecutionPeriod);
    }

    public void removeBeginExecutionPeriod() {
        super.setBeginExecutionPeriod(null);
    }

    public Integer getCurricularYear() {
        return getCurricularPeriod().getParent().getAbsoluteOrderOfChild();
    }

    public void addAllCourseGroups(Set<CourseGroup> courseGroups) {
        final DegreeModule degreeModule = getChildDegreeModule();
        if (!degreeModule.isLeaf()) {
            final CourseGroup courseGroup = (CourseGroup) degreeModule;
            courseGroups.add(courseGroup);
            courseGroup.getAllCoursesGroupse(courseGroups);
        }
    }

    public void getAllDegreeModules(final Collection<DegreeModule> degreeModules) {
        final DegreeModule degreeModule = getChildDegreeModule();
        degreeModule.getAllDegreeModules(degreeModules);
    }

    public boolean hasChildDegreeModule(final DegreeModule degreeModule) {
        return hasChildDegreeModule() && getChildDegreeModule().equals(degreeModule);
    }

    public boolean hasCurricularPeriod(final CurricularPeriod curricularPeriod) {
        return hasCurricularPeriod() && getCurricularPeriod().equals(curricularPeriod);
    }

    public class DegreeModuleScopeContext extends DegreeModuleScope {

        private final Context context;

        private DegreeModuleScopeContext(Context context) {
            this.context = context;
        }

        @Override
        public String getExternalId() {
            return context.getExternalId();
        }

        @Override
        public Integer getCurricularSemester() {
            return context.getCurricularPeriod().getChildOrder();
        }

        @Override
        public Integer getCurricularYear() {
            return context.getCurricularYear();
        }

        @Override
        public String getBranch() {
            return "";
        }

        public Context getContext() {
            return context;
        }

        @Override
        public boolean isActiveForExecutionPeriod(final ExecutionSemester executionSemester) {
            final ExecutionYear executionYear = executionSemester.getExecutionYear();
            return getCurricularCourse().isAnual(executionYear) ? getContext().isValid(executionYear) : getContext().isValid(
                    executionSemester);
        }

        @Override
        public boolean isActiveForAcademicInterval(final AcademicInterval academicInterval) {
            return getContext().isValid(academicInterval);
        }

        @Override
        public CurricularCourse getCurricularCourse() {
            return (CurricularCourse) context.getChildDegreeModule();
        }

        @Override
        public String getAnotation() {
            return null;
        }

        @Override
        public String getClassName() {
            return context.getClass().getName();
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof DegreeModuleScopeContext)) {
                return false;
            }
            return context.equals(((DegreeModuleScopeContext) obj).getContext());
        }

        @Override
        public int hashCode() {
            return context.hashCode();
        }
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.WrittenEvaluation> getAssociatedWrittenEvaluations() {
        return getAssociatedWrittenEvaluationsSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedWrittenEvaluations() {
        return !getAssociatedWrittenEvaluationsSet().isEmpty();
    }

    @Deprecated
    public boolean hasCurricularPeriod() {
        return getCurricularPeriod() != null;
    }

    @Deprecated
    public boolean hasParentCourseGroup() {
        return getParentCourseGroup() != null;
    }

    @Deprecated
    public boolean hasBeginExecutionPeriod() {
        return getBeginExecutionPeriod() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasChildOrder() {
        return getChildOrder() != null;
    }

    @Deprecated
    public boolean hasChildDegreeModule() {
        return getChildDegreeModule() != null;
    }

    @Deprecated
    public boolean hasEndExecutionPeriod() {
        return getEndExecutionPeriod() != null;
    }

}
