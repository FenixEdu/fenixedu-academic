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
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceMultipleException;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.EvaluationManagementLog;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Mark;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.InvalidMarkDomainException;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import net.sourceforge.fenixedu.util.Bundle;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class WriteMarks {

    @Atomic
    public static void writeByStudent(final String executioCourseOID, final String evaluationOID, final List<StudentMark> marks)
            throws FenixServiceException {
        check(RolePredicates.TEACHER_PREDICATE);

        final Evaluation evaluation = FenixFramework.getDomainObject(evaluationOID);
        final ExecutionCourse executionCourse = FenixFramework.getDomainObject(executioCourseOID);

        writeMarks(convertMarks(executionCourse, marks), executionCourse, evaluation);
    }

    @Atomic
    public static void writeByAttend(final String executioCourseOID, final String evaluationOID, final List<AttendsMark> marks)
            throws FenixServiceException {
        check(RolePredicates.TEACHER_PREDICATE);

        final Evaluation evaluation = FenixFramework.getDomainObject(evaluationOID);
        final ExecutionCourse executionCourse = FenixFramework.getDomainObject(executioCourseOID);

        writeMarks(marks, executionCourse, evaluation);
    }

    private static List<AttendsMark> convertMarks(final ExecutionCourse executionCourse, final List<StudentMark> marks)
            throws FenixServiceMultipleException {

        final List<DomainException> exceptionList = new ArrayList<DomainException>();
        final List<AttendsMark> result = new ArrayList<AttendsMark>();

        for (final StudentMark studentMark : marks) {
            final Attends attend = findAttend(executionCourse, studentMark.studentNumber, exceptionList);
            if (attend != null) {
                result.add(new AttendsMark(attend.getExternalId(), studentMark.mark));
            }
        }

        if (!exceptionList.isEmpty()) {
            throw new FenixServiceMultipleException(exceptionList);
        }

        return result;
    }

    private static Attends findAttend(final ExecutionCourse executionCourse, final Integer studentNumber,
            final List<DomainException> exceptionList) {

        final List<Attends> activeAttends = new ArrayList<Attends>(2);
        for (final Attends attend : executionCourse.getAttends()) {
            if (attend.getRegistration().getNumber().equals(studentNumber)
                    && (isActive(attend) || belongsToActiveExternalCycle(attend))) {
                activeAttends.add(attend);
            }
        }

        if (activeAttends.size() == 1) {
            return activeAttends.iterator().next();
        }

        if (activeAttends.isEmpty()) {
            exceptionList.add(new DomainException("errors.student.without.active.attends", studentNumber.toString()));
        } else {
            exceptionList.add(new DomainException("errors.student.with.several.active.attends", studentNumber.toString()));
        }

        return null;
    }

    private static boolean belongsToActiveExternalCycle(final Attends attend) {
        if (attend.hasEnrolment()) {
            final CycleCurriculumGroup cycle = attend.getEnrolment().getParentCycleCurriculumGroup();
            if (cycle != null && cycle.isExternal()) {
                final Student student = attend.getRegistration().getStudent();
                return student.getActiveRegistrationFor(cycle.getDegreeCurricularPlanOfDegreeModule()) != null;
            }
        }
        return false;
    }

    private static boolean isActive(final Attends attends) {
        final RegistrationState state;
        if (attends.hasEnrolment()) {
            state = attends.getEnrolment().getRegistration().getLastRegistrationState(attends.getExecutionYear());
        } else {
            state = attends.getRegistration().getLastRegistrationState(attends.getExecutionYear());
        }
        return state != null && state.isActive();
    }

    private static void writeMarks(final List<AttendsMark> marks, final ExecutionCourse executionCourse,
            final Evaluation evaluation) throws FenixServiceMultipleException {

        final List<DomainException> exceptionList = new ArrayList<DomainException>();

        for (final AttendsMark entry : marks) {

            final Attends attend = findAttend(executionCourse, entry.attendId);
            final String markValue = entry.mark;

            if (attend.hasEnrolment() && attend.getEnrolment().isImpossible()) {
                exceptionList.add(new DomainException("errors.student.with.impossible.enrolment", attend.getRegistration()
                        .getStudent().getNumber().toString()));
            } else {
                final Mark mark = attend.getMarkByEvaluation(evaluation);

                if (isToDeleteMark(markValue)) {
                    if (mark != null) {
                        mark.delete();
                    }
                } else {
                    try {
                        if (mark == null) {
                            evaluation.addNewMark(attend, markValue);
                        } else {
                            mark.setMark(markValue);
                        }
                    } catch (InvalidMarkDomainException e) {
                        exceptionList.add(e);
                    }
                }
            }
        }

        if (!exceptionList.isEmpty()) {
            throw new FenixServiceMultipleException(exceptionList);
        }

        EvaluationManagementLog.createLog(executionCourse, Bundle.MESSAGING,
                "log.executionCourse.evaluation.generic.edited.marks", evaluation.getPresentationName(),
                executionCourse.getName(), executionCourse.getDegreePresentationString());
    }

    private static Attends findAttend(final ExecutionCourse executionCourse, final String attendId) {
        for (final Attends attend : executionCourse.getAttends()) {
            if (attend.getExternalId().equals(attendId)) {
                return attend;
            }
        }
        return null;
    }

    private static boolean isToDeleteMark(final String markValue) {
        return markValue == null || markValue.isEmpty();
    }

    public static class StudentMark implements Serializable {
        private final Integer studentNumber;
        private final String mark;

        public StudentMark(final Integer studentNumber, final String mark) {
            this.studentNumber = studentNumber;
            this.mark = mark;
        }
    }

    public static class AttendsMark implements Serializable {
        private final String attendId;
        private final String mark;

        public AttendsMark(final String attendId, final String mark) {
            this.attendId = attendId;
            this.mark = mark;
        }
    }

}