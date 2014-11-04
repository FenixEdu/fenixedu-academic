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
package org.fenixedu.academic.domain.teacher.workTime;

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.util.date.TimePeriod;

public class TeacherInstitutionWorkTime extends TeacherInstitutionWorkTime_Base {

    public double hours() {
        TimePeriod timePeriod = new TimePeriod(this.getStartTime(), this.getEndTime());
        return timePeriod.hours().doubleValue();
    }

    public boolean belongsToExecutionPeriod(ExecutionSemester executionSemester) {
        return this.getExecutionPeriod().equals(executionSemester);
    }

    public void delete() {
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    @Deprecated
    public java.util.Date getEndTime() {
        org.fenixedu.academic.util.HourMinuteSecond hms = getEndTimeHourMinuteSecond();
        return (hms == null) ? null : new java.util.Date(0, 0, 1, hms.getHour(), hms.getMinuteOfHour(), hms.getSecondOfMinute());
    }

    @Deprecated
    public void setEndTime(java.util.Date date) {
        if (date == null) {
            setEndTimeHourMinuteSecond(null);
        } else {
            setEndTimeHourMinuteSecond(org.fenixedu.academic.util.HourMinuteSecond.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Date getStartTime() {
        org.fenixedu.academic.util.HourMinuteSecond hms = getStartTimeHourMinuteSecond();
        return (hms == null) ? null : new java.util.Date(0, 0, 1, hms.getHour(), hms.getMinuteOfHour(), hms.getSecondOfMinute());
    }

    @Deprecated
    public void setStartTime(java.util.Date date) {
        if (date == null) {
            setStartTimeHourMinuteSecond(null);
        } else {
            setStartTimeHourMinuteSecond(org.fenixedu.academic.util.HourMinuteSecond.fromDateFields(date));
        }
    }

}
