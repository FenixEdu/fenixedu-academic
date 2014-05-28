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
package net.sourceforge.fenixedu.domain.student;

import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.student.ManageStudentStatuteBean;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

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

    public StudentStatute(Student student, StudentStatuteType statuteType, ExecutionSemester beginExecutionPeriod,
            ExecutionSemester endExecutionPeriod) {
        this();
        setBeginExecutionPeriod(beginExecutionPeriod);
        setEndExecutionPeriod(endExecutionPeriod);
        setStatuteType(statuteType);

        for (StudentStatute statute : student.getStudentStatutes()) {
            if (statute.overlapsWith(this)) {
                throw new DomainException("error.studentStatute.alreadyExistsOneOverlapingStatute");
            }
        }

        setStudent(student);
    }

    public boolean isValidInExecutionPeriod(final ExecutionSemester executionSemester) {

        if (getBeginExecutionPeriod() != null && getBeginExecutionPeriod().isAfter(executionSemester)) {
            return false;
        }

        if (getEndExecutionPeriod() != null && getEndExecutionPeriod().isBefore(executionSemester)) {
            return false;
        }

        return true;
    }

    public boolean isValidOn(final ExecutionYear executionYear) {
        for (final ExecutionSemester executionSemester : executionYear.getExecutionPeriods()) {
            if (!isValidInExecutionPeriod(executionSemester)) {
                return false;
            }
        }

        return true;
    }

    public boolean isValidOnAnyExecutionPeriodFor(final ExecutionYear executionYear) {
        for (final ExecutionSemester executionSemester : executionYear.getExecutionPeriods()) {
            if (isValidInExecutionPeriod(executionSemester)) {
                return true;
            }
        }

        return false;
    }

    public boolean isValidInCurrentExecutionPeriod() {
        return this.isValidInExecutionPeriod(ExecutionSemester.readActualExecutionSemester());
    }

    public void delete() {
        checkRules();
        setBeginExecutionPeriod(null);
        setEndExecutionPeriod(null);
        setStudent(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public static class CreateStudentStatuteFactory extends ManageStudentStatuteBean implements FactoryExecutor {

        public CreateStudentStatuteFactory(Student student) {
            super(student);
        }

        @Override
        public Object execute() {
            switch (getStatuteType()) {

            case SENIOR:
                return new SeniorStatute(getStudent(), getRegistration(), getStatuteType(), getBeginExecutionPeriod(),
                        getEndExecutionPeriod());

            default:
                return new StudentStatute(getStudent(), getStatuteType(), getBeginExecutionPeriod(), getEndExecutionPeriod());

            }
        }
    }

    public boolean overlapsWith(StudentStatute statute) {

        ExecutionSemester statuteBegin =
                statute.getBeginExecutionPeriod() != null ? statute.getBeginExecutionPeriod() : ExecutionSemester
                        .readFirstExecutionSemester();
        ExecutionSemester statuteEnd =
                statute.getEndExecutionPeriod() != null ? statute.getEndExecutionPeriod() : ExecutionSemester
                        .readLastExecutionSemester();

        return overlapsWith(statute.getStatuteType(), statuteBegin, statuteEnd);

    }

    public boolean overlapsWith(StudentStatuteType statuteType, ExecutionSemester statuteBegin, ExecutionSemester statuteEnd) {

        if (statuteType != getStatuteType()) {
            return false;
        }

        ExecutionSemester thisStatuteBegin =
                getBeginExecutionPeriod() != null ? getBeginExecutionPeriod() : ExecutionSemester.readFirstExecutionSemester();
        ExecutionSemester thisStatuteEnd =
                getEndExecutionPeriod() != null ? getEndExecutionPeriod() : ExecutionSemester.readLastExecutionSemester();

        return statuteBegin.isAfterOrEquals(thisStatuteBegin) && statuteBegin.isBeforeOrEquals(thisStatuteEnd)
                || statuteEnd.isAfterOrEquals(thisStatuteBegin) && statuteEnd.isBeforeOrEquals(thisStatuteEnd);

    }

    public void add(StudentStatute statute) {
        if (this.overlapsWith(statute)) {
            if (statute.getBeginExecutionPeriod() == null
                    || (getBeginExecutionPeriod() != null && statute.getBeginExecutionPeriod()
                            .isBefore(getBeginExecutionPeriod()))) {
                setBeginExecutionPeriod(statute.getBeginExecutionPeriod());
            }

            if (statute.getEndExecutionPeriod() == null
                    || (getEndExecutionPeriod() != null && statute.getEndExecutionPeriod().isAfter(getEndExecutionPeriod()))) {
                setEndExecutionPeriod(statute.getEndExecutionPeriod());
            }
        }
    }

    public static class DeleteStudentStatuteFactory implements FactoryExecutor {

        StudentStatute studentStatute;

        public DeleteStudentStatuteFactory(StudentStatute studentStatute) {
            this.studentStatute = studentStatute;
        }

        @Override
        public Object execute() {
            this.studentStatute.delete();
            return true;
        }

    }

    public boolean isGrantOwnerStatute() {
        return getStatuteType() == StudentStatuteType.SAS_GRANT_OWNER;
    }

    public String toDetailedString() {
        return (getBeginExecutionPeriod() != null ? getBeginExecutionPeriod().getQualifiedName() : " - ") + " ..... "
                + (getEndExecutionPeriod() != null ? getEndExecutionPeriod().getQualifiedName() : " - ");
    }

    public void checkRules() {
        if (hasSpecialSeasonEnrolments()) {
            throw new DomainException("error.student.StudentStatute.has.special.season.enrolment");
        }
    }

    public boolean hasSpecialSeasonEnrolments() {

        ExecutionSemester lastSemester = getEndExecutionPeriod();

        Set<Registration> registrations = getStudent().getRegistrationsSet();
        for (Registration registration : registrations) {
            Set<StudentCurricularPlan> plans = registration.getStudentCurricularPlansSet();
            for (StudentCurricularPlan scp : plans) {
                ExecutionSemester semesterIterator = getBeginExecutionPeriod();
                while (semesterIterator != null && semesterIterator.isBeforeOrEquals(lastSemester)) {
                    if (scp.isEnroledInSpecialSeason(semesterIterator)) {
                        return true;
                    }
                    semesterIterator = semesterIterator.getNextExecutionPeriod();
                }
            }
        }
        return false;
    }

    public boolean hasSeniorStatuteForRegistration(Registration registration) {
        return false;
    }

    @Deprecated
    public boolean hasStudent() {
        return getStudent() != null;
    }

    @Deprecated
    public boolean hasStatuteType() {
        return getStatuteType() != null;
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
    public boolean hasCreationDate() {
        return getCreationDate() != null;
    }

    @Deprecated
    public boolean hasEndExecutionPeriod() {
        return getEndExecutionPeriod() != null;
    }

}
