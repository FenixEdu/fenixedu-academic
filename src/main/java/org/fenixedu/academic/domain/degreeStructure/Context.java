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

import java.text.Collator;
import java.util.Collection;
import java.util.Comparator;
import java.util.Set;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DomainObjectUtil;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.curricularPeriod.CurricularPeriod;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumLine;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumModule;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;
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
            return (comparationResult == 0) ? leftContext.getExternalId()
                    .compareTo(rightContext.getExternalId()) : comparationResult;
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
                if (cycleCourseGroup.getParentContextsSet().size() > 1) {
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
                        if (cycleCourseGroup.getParentContextsSet().size() > 0) {
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
            final ExecutionInterval begin, final ExecutionInterval end) {

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
        if (curricularCourse.getCompetenceCourse() != null && curricularCourse.getCompetenceCourse().isAnual(executionYear)
                && !curricularPeriod.hasChildOrderValue(1)) {
            throw new DomainException("competenceCourse.anual.but.trying.to.associate.curricular.course.not.to.first.period");
        }
    }

    private void checkIfCanAddCourseGroupToCourseGroup(final CourseGroup parent, final CourseGroup courseGroup) {
        parent.checkDuplicateChildNames(courseGroup.getName(), courseGroup.getNameEn());
    }

    private void checkParameters(CourseGroup courseGroup, DegreeModule degreeModule, ExecutionInterval beginExecutionPeriod) {
        if (courseGroup == null || degreeModule == null || beginExecutionPeriod == null) {
            throw new DomainException("error.incorrectContextValues");
        }
    }

    private void checkExistingCourseGroupContexts(final CourseGroup courseGroup, final DegreeModule degreeModule,
            final CurricularPeriod curricularPeriod, final ExecutionInterval begin, final ExecutionInterval end) {

        for (final Context context : courseGroup.getChildContextsSet()) {
            if (context != this && context.hasChildDegreeModule(degreeModule) && context.hasCurricularPeriod(curricularPeriod)
                    && context.intersects(begin, end)) {
                throw new DomainException("courseGroup.contextAlreadyExistForCourseGroup");
            }
        }
    }

    public void edit(final CourseGroup parent, final CurricularPeriod curricularPeriod, final ExecutionInterval begin,
            final ExecutionInterval end) {
        setParentCourseGroup(parent);
        setCurricularPeriod(curricularPeriod);
        edit(begin, end);
    }

    protected void edit(final ExecutionInterval begin, final ExecutionInterval end) {
        checkExecutionPeriods(begin, end);
        checkExistingCourseGroupContexts(getParentCourseGroup(), getChildDegreeModule(), getCurricularPeriod(), begin, end);
        setBeginExecutionPeriod(begin);
        setEndExecutionPeriod(end);
        checkCurriculumLines(getChildDegreeModule());
    }

    private void checkCurriculumLines(final DegreeModule degreeModule) {
        for (final CurriculumModule curriculumModule : degreeModule.getCurriculumModulesSet()) {
            if (curriculumModule.isCurriculumLine()) {
                final CurriculumLine curriculumLine = (CurriculumLine) curriculumModule;
                if (curriculumLine.getExecutionInterval() != null
                        && !degreeModule.hasAnyOpenParentContexts(curriculumLine.getExecutionInterval())) {
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

    public boolean isValid(final ExecutionInterval executionInterval) {
        if (isOpen(executionInterval)) {
            if (getChildDegreeModule().isCurricularCourse()) {
                CurricularCourse curricularCourse = (CurricularCourse) getChildDegreeModule();
                if (!curricularCourse.isAnual(executionInterval.getExecutionYear())) {
                    return executionInterval.getAcademicPeriod().equals(getCurricularPeriod().getAcademicPeriod())
                            && executionInterval.getChildOrder().intValue() == getCurricularPeriod().getChildOrder().intValue();
                }
            }
            return true;
        }
        return false;
    }

    public boolean isValid(final ExecutionYear executionYear) {
        for (final ExecutionInterval executionInterval : executionYear.getExecutionPeriodsSet()) {
            if (isValid(executionInterval)) {
                return true;
            }
        }
        return false;
    }

    public boolean isValid(AcademicInterval academicInterval) {
        return isValid(ExecutionInterval.getExecutionInterval(academicInterval));
    }

    protected void checkExecutionPeriods(ExecutionInterval beginExecutionPeriod, ExecutionInterval endExecutionPeriod) {
        if (beginExecutionPeriod == null) {
            throw new DomainException("context.begin.execution.period.cannot.be.null");
        }
        if (endExecutionPeriod != null && beginExecutionPeriod.isAfter(endExecutionPeriod)) {
            throw new DomainException("context.begin.is.after.end.execution.period");
        }
    }

    public boolean isOpen(final ExecutionInterval executionInterval) {
        return getBeginExecutionInterval().isBeforeOrEquals(executionInterval)
                && (getEndExecutionInterval() == null || getEndExecutionInterval().isAfterOrEquals(executionInterval));
    }

    public boolean isOpen(final ExecutionYear executionYear) {
        for (final ExecutionInterval executionInterval : executionYear.getChildIntervals()) {
            if (isOpen(executionInterval)) {
                return true;
            }
        }
        return false;
    }

    public boolean isOpen() {
        final Degree degree = getParentCourseGroup().getDegree();
        return isOpen(ExecutionYear.findCurrent(degree.getCalendar()));
    }

    public boolean intersects(final ExecutionInterval begin, final ExecutionInterval end) {
        if (end != null && begin.isAfter(end)) {
            throw new DomainException("context.begin.is.after.end.execution.period");
        }

        if (begin.isAfterOrEquals(getBeginExecutionInterval())) {
            return getEndExecutionInterval() == null || begin.isBeforeOrEquals(getEndExecutionInterval());
        } else {
            return end == null || end.isAfterOrEquals(getBeginExecutionInterval());
        }
    }

    /**
     * 
     * Beware of {@link #containsInterval(ExecutionInterval, ExecutionInterval)} method because it only checks for intersection,
     * not a
     * full
     * contains.
     * 
     * This method ensures that begin and end are contained within context interval
     * 
     * @return
     */
    public boolean containsInterval(ExecutionInterval begin, ExecutionInterval end) {
        if (begin.isAfterOrEquals(getBeginExecutionInterval())) {
            return (getEndExecutionInterval() == null) || (end != null && !end.isAfter(getEndExecutionInterval()));
        } else {
            return false;
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

    //TODO: DELETE
//    @Deprecated
//    public boolean containsCurricularYear(final Integer curricularYear) {
//        final CurricularPeriod firstCurricularPeriod = getCurricularPeriod().getParent();
//        final int firstCurricularPeriodOrder = firstCurricularPeriod.getAbsoluteOrderOfChild();
//        return curricularYear.intValue() == firstCurricularPeriodOrder;
//    }

    //TODO: DELETE
//    @Deprecated
//    public boolean containsSemester(final Integer semester) {
//        final CurricularPeriod firstCurricularPeriod = getCurricularPeriod();
//        final int firstCurricularPeriodOrder = firstCurricularPeriod.getChildOrder();
//        return semester.intValue() == firstCurricularPeriodOrder;
//    }

    @Deprecated
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

    @Override
    public void setBeginExecutionPeriod(ExecutionInterval beginExecutionPeriod) {
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
        return getChildDegreeModule() != null && getChildDegreeModule().equals(degreeModule);
    }

    public boolean hasCurricularPeriod(final CurricularPeriod curricularPeriod) {
        return getCurricularPeriod() != null && getCurricularPeriod().equals(curricularPeriod);
    }

    public ExecutionInterval getBeginExecutionInterval() {
        return super.getBeginExecutionPeriod();
    }

    public ExecutionInterval getEndExecutionInterval() {
        return super.getEndExecutionPeriod();
    }

}
