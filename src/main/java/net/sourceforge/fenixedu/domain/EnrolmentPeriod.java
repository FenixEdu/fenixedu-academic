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
/*
 * Created on 28/Abr/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.Comparator;
import java.util.Date;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import pt.ist.fenixframework.Atomic;

/**
 * @author jpvl
 */
public abstract class EnrolmentPeriod extends EnrolmentPeriod_Base {

    public static final Comparator<EnrolmentPeriod> COMPARATOR_BY_DEGREE_NAME = new Comparator<EnrolmentPeriod>() {

        @Override
        public int compare(EnrolmentPeriod o1, EnrolmentPeriod o2) {
            final DegreeCurricularPlan dcp1 = o1.getDegreeCurricularPlan();
            final DegreeCurricularPlan dcp2 = o2.getDegreeCurricularPlan();
            final int dcp = DegreeCurricularPlan.COMPARATOR_BY_PRESENTATION_NAME.compare(dcp1, dcp2);
            return dcp == 0 ? o2.hashCode() - o1.hashCode() : dcp;
        }

    };

    public static final Comparator<EnrolmentPeriod> COMPARATOR_BY_EXECUTION_SEMESTER = new Comparator<EnrolmentPeriod>() {

        @Override
        public int compare(EnrolmentPeriod period1, EnrolmentPeriod period2) {
            final ExecutionSemester semester1 = period1.getExecutionPeriod();
            final ExecutionSemester semester2 = period2.getExecutionPeriod();
            return ExecutionSemester.COMPARATOR_BY_SEMESTER_AND_YEAR.compare(semester1, semester2);
        }

    };

    public EnrolmentPeriod() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    protected void init(final DegreeCurricularPlan degreeCurricularPlan, final ExecutionSemester executionSemester,
            final Date startDate, final Date endDate) {
        init(degreeCurricularPlan, executionSemester, new DateTime(startDate), new DateTime(endDate));
    }

    protected void init(final DegreeCurricularPlan degreeCurricularPlan, final ExecutionSemester executionSemester,
            final DateTime startDate, final DateTime endDate) {

        if (!endDate.isAfter(startDate)) {
            throw new DomainException("EnrolmentPeriod.end.date.must.be.after.start.date");
        }

        setDegreeCurricularPlan(degreeCurricularPlan);
        setExecutionPeriod(executionSemester);
        setStartDateDateTime(startDate);
        setEndDateDateTime(endDate);
    }

    public boolean isValid() {
        return containsDate(new DateTime());
    }

    public boolean isValid(final Date date) {
        return containsDate(new DateTime(date));
    }

    public boolean isUpcomingPeriod() {
        return getStartDateDateTime().isAfterNow();
    }

    public boolean containsDate(DateTime date) {
        return !(getStartDateDateTime().isAfter(date) || getEndDateDateTime().isBefore(date));
    }

    public boolean isFor(final ExecutionSemester executionSemester) {
        return getExecutionPeriod() == executionSemester;
    }

    public Degree getDegree() {
        return getDegreeCurricularPlan().getDegree();
    }

    @Atomic
    public void delete() {
        setDegreeCurricularPlan(null);
        setExecutionPeriod(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    @Deprecated
    public java.util.Date getEndDate() {
        org.joda.time.DateTime dt = getEndDateDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public void setEndDate(java.util.Date date) {
        if (date == null) {
            setEndDateDateTime(null);
        } else {
            setEndDateDateTime(new org.joda.time.DateTime(date.getTime()));
        }
    }

    @Deprecated
    public java.util.Date getStartDate() {
        org.joda.time.DateTime dt = getStartDateDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public void setStartDate(java.util.Date date) {
        if (date == null) {
            setStartDateDateTime(null);
        } else {
            setStartDateDateTime(new org.joda.time.DateTime(date.getTime()));
        }
    }

    public Interval getInterval() {
        return new Interval(getStartDateDateTime(), getEndDateDateTime());
    }

    @Deprecated
    public boolean hasDegreeCurricularPlan() {
        return getDegreeCurricularPlan() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasStartDateDateTime() {
        return getStartDateDateTime() != null;
    }

    @Deprecated
    public boolean hasEndDateDateTime() {
        return getEndDateDateTime() != null;
    }

    @Deprecated
    public boolean hasExecutionPeriod() {
        return getExecutionPeriod() != null;
    }

}
