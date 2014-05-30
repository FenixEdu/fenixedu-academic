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

import java.text.Collator;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import net.sourceforge.fenixedu.dataTransferObject.comparators.CalendarDateComparator;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendarEntry;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendarRootEntry;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicYearCE;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.YearMonthDay;

public class CurricularCourseScope extends CurricularCourseScope_Base {

    public static Comparator<CurricularCourseScope> CURRICULAR_COURSE_NAME_COMPARATOR = new ComparatorChain();
    static {
        ((ComparatorChain) CURRICULAR_COURSE_NAME_COMPARATOR).addComparator(new BeanComparator("curricularCourse.name", Collator
                .getInstance()));
        ((ComparatorChain) CURRICULAR_COURSE_NAME_COMPARATOR).addComparator(new BeanComparator("curricularCourse.externalId"));
    }

    public CurricularCourseScope() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public CurricularCourseScope(Branch branch, CurricularCourse curricularCourse, CurricularSemester curricularSemester,
            Calendar beginDate, Calendar endDate, String Annotation) {
        this();
        // check that there isn't another scope active with the same curricular
        // course, branch and semester

        if (curricularCourse.hasActiveScopeInGivenSemesterForGivenBranch(curricularSemester, branch)) {
            throw new DomainException("error.curricular.course.scope.conflict.creation");
        }

        setBranch(branch);
        setCurricularCourse(curricularCourse);
        setCurricularSemester(curricularSemester);

        setBeginDate(beginDate);
        setEndDate(endDate);
        setAnotation(Annotation);
    }

    public Calendar getBeginDate() {
        if (this.getBegin() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getBegin());
            return result;
        }
        return null;
    }

    public void setBeginDate(Calendar beginDate) {
        if (beginDate != null) {
            this.setBegin(beginDate.getTime());
        } else {
            this.setBegin(null);
        }
    }

    public Calendar getEndDate() {
        if (this.getEnd() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getEnd());
            return result;
        }
        return null;
    }

    public void setEndDate(Calendar endDate) {
        if (endDate != null) {
            this.setEnd(endDate.getTime());
        } else {
            this.setEnd(null);
        }
    }

    public Boolean isActive() {
        return isActive(new Date());
    }

    public boolean getActive() {
        return this.isActive();
    }

    public Boolean canBeDeleted() {
        return !hasAnyAssociatedWrittenEvaluations();
    }

    public void edit(Branch branch, CurricularSemester curricularSemester, Calendar beginDate, Calendar endDate, String Annotation) {

        setBranch(branch);
        setCurricularSemester(curricularSemester);
        setBeginDate(beginDate);
        setEndDate(endDate);
        setAnotation(Annotation);
    }

    public void end(Calendar endDate) {
        setEndDate(endDate);
    }

    public void delete() throws DomainException {
        if (canBeDeleted()) {
            setCurricularSemester(null);
            setCurricularCourse(null);
            setBranch(null);

            setRootDomainObject(null);
            super.deleteDomainObject();
        } else {
            throw new DomainException("error.curricular.course.scope.has.written.evaluations");
        }
    }

    public Boolean isActive(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        CalendarDateComparator calendarDateComparator = new CalendarDateComparator();
        Boolean result = Boolean.FALSE;
        if (calendarDateComparator.compare(getBeginDate(), calendar) <= 0) {
            if (getEndDate() == null || calendarDateComparator.compare(getEndDate(), calendar) >= 0) {
                result = Boolean.TRUE;
            }
        }
        return result;
    }

    public boolean intersects(final Date begin, final Date end) {
        return intersects(YearMonthDay.fromDateFields(begin), YearMonthDay.fromDateFields(end));
    }

    public boolean intersects(final YearMonthDay begin, final YearMonthDay end) {
        return !getBeginYearMonthDay().isAfter(end) && (getEndYearMonthDay() == null || !getEndYearMonthDay().isBefore(begin));
    }

    public boolean isActiveForAcademicInterval(AcademicInterval academicInterval) {
        return isActiveForCalendarEntry(academicInterval.getAcademicCalendarEntry());
    }

    private boolean isActiveForCalendarEntry(AcademicCalendarEntry entry) {
        if (entry instanceof AcademicCalendarRootEntry) {
            return false;
        }

        if (entry instanceof AcademicYearCE) {
            return intersects(entry.getBegin().toDate(), entry.getEnd().toDate());
        }

        if (intersects(entry.getBegin().toDate(), entry.getEnd().toDate())
                && new Integer(entry.getAcademicSemesterOfAcademicYear(entry.getAcademicChronology()))
                        .equals(getCurricularSemester().getSemester())) {
            return true;
        }
        return isActiveForCalendarEntry(entry.getParentEntry());
    }

    public boolean isActiveForExecutionPeriod(final ExecutionSemester executionSemester) {
        return intersects(executionSemester.getBeginDateYearMonthDay(), executionSemester.getEndDateYearMonthDay())
                && executionSemester.getSemester().equals(getCurricularSemester().getSemester());
    }

    public boolean isActiveForExecutionYear(final ExecutionYear executionYear) {
        for (final ExecutionSemester executionSemester : executionYear.getExecutionPeriodsSet()) {
            if (isActiveForExecutionPeriod(executionSemester)) {
                return true;
            }
        }
        return false;
    }

    public DegreeModuleScopeCurricularCourseScope getDegreeModuleScopeCurricularCourseScope() {
        return new DegreeModuleScopeCurricularCourseScope(this);
    }

    public boolean hasEndYearMonthDay() {
        return getEndYearMonthDay() != null;
    }

    public class DegreeModuleScopeCurricularCourseScope extends DegreeModuleScope {

        private final CurricularCourseScope curricularCourseScope;

        private DegreeModuleScopeCurricularCourseScope(CurricularCourseScope curricularCourseScope) {
            this.curricularCourseScope = curricularCourseScope;
        }

        @Override
        public String getExternalId() {
            return curricularCourseScope.getExternalId();
        }

        @Override
        public Integer getCurricularSemester() {
            return curricularCourseScope.getCurricularSemester().getSemester();
        }

        @Override
        public Integer getCurricularYear() {
            return curricularCourseScope.getCurricularSemester().getCurricularYear().getYear();
        }

        @Override
        public String getBranch() {
            return curricularCourseScope.getBranch() == null ? "" : curricularCourseScope.getBranch().getName();
        }

        public CurricularCourseScope getCurricularCourseScope() {
            return curricularCourseScope;
        }

        @Override
        public boolean isActiveForExecutionPeriod(final ExecutionSemester executionSemester) {
            return curricularCourseScope.isActiveForExecutionPeriod(executionSemester);
        }

        @Override
        public boolean isActiveForAcademicInterval(final AcademicInterval academicInterval) {
            return curricularCourseScope.isActiveForAcademicInterval(academicInterval);
        }

        @Override
        public CurricularCourse getCurricularCourse() {
            return curricularCourseScope.getCurricularCourse();
        }

        @Override
        public String getAnotation() {
            return curricularCourseScope.getAnotation();
        }

        @Override
        public String getClassName() {
            return curricularCourseScope.getClass().getName();
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof DegreeModuleScopeCurricularCourseScope)) {
                return false;
            }
            return curricularCourseScope.equals(((DegreeModuleScopeCurricularCourseScope) obj).getCurricularCourseScope());
        }

        @Override
        public int hashCode() {
            return curricularCourseScope.hashCode();
        }
    }

    @Deprecated
    public java.util.Date getBegin() {
        org.joda.time.YearMonthDay ymd = getBeginYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setBegin(java.util.Date date) {
        if (date == null) {
            setBeginYearMonthDay(null);
        } else {
            setBeginYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Date getEnd() {
        org.joda.time.YearMonthDay ymd = getEndYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setEnd(java.util.Date date) {
        if (date == null) {
            setEndYearMonthDay(null);
        } else {
            setEndYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
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
    public boolean hasBranch() {
        return getBranch() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasCurricularSemester() {
        return getCurricularSemester() != null;
    }

    @Deprecated
    public boolean hasAnotation() {
        return getAnotation() != null;
    }

    @Deprecated
    public boolean hasBeginYearMonthDay() {
        return getBeginYearMonthDay() != null;
    }

    @Deprecated
    public boolean hasCurricularCourse() {
        return getCurricularCourse() != null;
    }

}
