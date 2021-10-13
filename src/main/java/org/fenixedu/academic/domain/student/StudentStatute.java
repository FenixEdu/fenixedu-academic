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
package org.fenixedu.academic.domain.student;

import java.util.Optional;
import java.util.Set;

import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

/**
 *
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 *
 */
public class StudentStatute extends StudentStatute_Base {

    protected StudentStatute() {
        super();
        super.setRootDomainObject(Bennu.getInstance());
        setCreationDate(new DateTime());
    }

    public StudentStatute(final Student student, final StatuteType statuteType, final ExecutionInterval beginExecutionPeriod,
            final ExecutionInterval endExecutionPeriod, final LocalDate beginDate, final LocalDate endDate, final String comment,
            final Registration registration) {
        this();
        setType(statuteType);
        setRegistration(registration);
        edit(student, beginExecutionPeriod, endExecutionPeriod, beginDate, endDate, comment);
    }

    protected void checkRules() {
        if (getBeginExecutionInterval() != null && getEndExecutionInterval() != null) {
            if (getBeginExecutionInterval().isAfter(getEndExecutionInterval())) {
                throw new DomainException("error.studentStatute.beginPeriod.after.endPeriod");
            }
        }
        if (getBeginDate() != null && getEndDate() != null) {
            if (getBeginDate().isAfter(getEndDate())) {
                throw new DomainException("error.studentStatute.beginDate.after.endPeriod");
            }

        }
        if (getType() == null) {
            throw new DomainException("error.studentStatute.missing.StatuteType");
        }

        if (getType().isAppliedOnRegistration() && getRegistration() == null) {
            throw new DomainException("error.studentStatute.registration.required");
        }

        if (!getType().isAppliedOnRegistration() && getRegistration() != null) {
            throw new DomainException("error.studentStatute.not.applied.for.registration");
        }

    }

    //TODO: DELETE
//    public boolean isValidInExecutionPeriod(final ExecutionSemester executionSemester) {
//        if (getBeginExecutionInterval() != null && getBeginExecutionInterval().isAfter(executionSemester)) {
//            return false;
//        }
//
//        if (getEndExecutionInterval() != null && getEndExecutionInterval().isBefore(executionSemester)) {
//            return false;
//        }
//
//        return true;
//    }

    public boolean isValidInExecutionInterval(final ExecutionInterval interval) {
        if (interval instanceof ExecutionYear) {
            return isValidOn(ExecutionInterval.assertExecutionIntervalType(ExecutionYear.class, interval));
        }

        if (getBeginExecutionInterval() != null && getBeginExecutionInterval().isAfter(interval)) {
            return false;
        }

        if (getEndExecutionInterval() != null && getEndExecutionInterval().isBefore(interval)) {
            return false;
        }

        return true;

    }

    public boolean isValidOn(final ExecutionYear executionYear) {
        for (final ExecutionInterval executionInterval : executionYear.getChildIntervals()) {
            if (!isValidInExecutionInterval(executionInterval)) {
                return false;
            }
        }

        return true;
    }

    public boolean isValidOnAnyExecutionPeriodFor(final ExecutionYear executionYear) {
        for (final ExecutionInterval executionInterval : executionYear.getChildIntervals()) {
            if (isValidInExecutionInterval(executionInterval)) {
                return true;
            }
        }

        return false;
    }

    public void edit(final Student student, final ExecutionInterval beginExecutionPeriod,
            final ExecutionInterval endExecutionPeriod, final LocalDate beginDate, final LocalDate endDate,
            final String comment) {

        setBeginExecutionPeriod(beginExecutionPeriod);
        setEndExecutionPeriod(endExecutionPeriod);
        setBeginDate(beginDate);
        setEndDate(endDate);
        setComment(comment);

        for (StudentStatute statute : student.getStudentStatutesSet()) {
            if (statute.overlapsWith(this)) {
                throw new DomainException(Optional.of(Bundle.ACADEMIC), "error.studentStatute.alreadyExistsOneOverlapingStatute");
            }
        }

        setStudent(student);

        checkRules();
    }

    /*
     * Validation at Registration Level
     */

    public boolean isValidInExecutionInterval(final Registration registration, final ExecutionInterval interval) {
        return isValidInExecutionInterval(interval)
                && (!getType().isAppliedOnRegistration() || getRegistration() == registration);
    }

    public boolean isValidOn(final Registration registration, final ExecutionYear executionYear) {
        return isValidOn(executionYear) && (!getType().isAppliedOnRegistration() || getRegistration() == registration);
    }

    public boolean isValidOnAnyExecutionPeriodFor(final Registration registration, final ExecutionYear executionYear) {
        return isValidOnAnyExecutionPeriodFor(executionYear)
                && (!getType().isAppliedOnRegistration() || getRegistration() == registration);
    }

    public void delete() {
        setBeginExecutionPeriod(null);
        setEndExecutionPeriod(null);
        setStudent(null);
        setType(null);
        setRegistration(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public boolean overlapsWith(final StudentStatute statute) {

        if (statute == this) {
            return false;
        }
        ExecutionInterval statuteBegin = statute.getBeginExecutionInterval() != null ? statute
                .getBeginExecutionInterval() : ExecutionInterval.findFirstChild();
        ExecutionInterval statuteEnd =
                statute.getEndExecutionInterval() != null ? statute.getEndExecutionInterval() : ExecutionInterval.findLastChild();

        return overlapsWith(statute.getType(), statuteBegin, statuteEnd, statute.getRegistration());

    }

    public boolean overlapsWith(final StatuteType statuteType, final ExecutionInterval statuteBegin,
            final ExecutionInterval statuteEnd, final Registration registration) {

        if (statuteType != getType()) {
            return false;
        }

        if (getType().isAppliedOnRegistration() && getRegistration() != registration) {
            return false;
        }

        ExecutionInterval thisStatuteBegin =
                getBeginExecutionInterval() != null ? getBeginExecutionInterval() : ExecutionInterval.findFirstChild();
        ExecutionInterval thisStatuteEnd =
                getEndExecutionInterval() != null ? getEndExecutionInterval() : ExecutionInterval.findLastChild();

        return statuteBegin.isAfterOrEquals(thisStatuteBegin) && statuteBegin.isBeforeOrEquals(thisStatuteEnd)
                || statuteEnd.isAfterOrEquals(thisStatuteBegin) && statuteEnd.isBeforeOrEquals(thisStatuteEnd);

    }

//    public void add(final StudentStatute statute) {
//        if (this.overlapsWith(statute)) {
//            if (statute.getBeginExecutionInterval() == null || getBeginExecutionInterval() != null
//                    && statute.getBeginExecutionInterval().isBefore(getBeginExecutionInterval())) {
//                setBeginExecutionPeriod(statute.getBeginExecutionInterval());
//            }
//
//            if (statute.getEndExecutionInterval() == null || getEndExecutionInterval() != null
//                    && statute.getEndExecutionInterval().isAfter(getEndExecutionInterval())) {
//                setEndExecutionPeriod(statute.getEndExecutionInterval());
//            }
//        }
//    }

    public String toDetailedString() {
        return (getBeginExecutionInterval() != null ? getBeginExecutionInterval().getQualifiedName() : " - ") + " ..... "
                + (getEndExecutionInterval() != null ? getEndExecutionInterval().getQualifiedName() : " - ");
    }

    
    public boolean hasSeniorStatuteForRegistration(final Registration registration) {
        return false;
    }

    /**
     * @deprecated use {@link #getBeginExecutionInterval()} instead.
     */
    @Deprecated
    @Override
    public ExecutionInterval getBeginExecutionPeriod() {
        return getBeginExecutionInterval();
    }

    public ExecutionInterval getBeginExecutionInterval() {
        return super.getBeginExecutionPeriod();
    }

    /**
     * @deprecated use {@link #getEndExecutionInterval()} instead.
     */
    @Deprecated
    @Override
    public ExecutionInterval getEndExecutionPeriod() {
        return getEndExecutionInterval();
    }

    public ExecutionInterval getEndExecutionInterval() {
        return super.getEndExecutionPeriod();
    }
}
