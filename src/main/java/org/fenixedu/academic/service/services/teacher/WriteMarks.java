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
package org.fenixedu.academic.service.services.teacher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.Attends;
import org.fenixedu.academic.domain.Evaluation;
import org.fenixedu.academic.domain.EvaluationManagementLog;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.Mark;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.exceptions.InvalidMarkDomainException;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationState;
import org.fenixedu.academic.domain.studentCurriculum.CycleCurriculumGroup;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.FenixServiceMultipleException;
import org.fenixedu.academic.util.Bundle;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class WriteMarks {

    @Atomic
    public static void writeByStudent(final String executioCourseOID, final String evaluationOID, final List<StudentMark> marks)
            throws FenixServiceException {

        final Evaluation evaluation = FenixFramework.getDomainObject(evaluationOID);
        final ExecutionCourse executionCourse = FenixFramework.getDomainObject(executioCourseOID);

        writeMarks(convertMarks(executionCourse, marks), executionCourse, evaluation);
    }

    @Atomic
    public static void writeByAttend(final String executioCourseOID, final String evaluationOID, final List<AttendsMark> marks)
            throws FenixServiceException {

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
                addMark(result, studentMark, attend, exceptionList);
            }
        }

        if (!exceptionList.isEmpty()) {
            throw new FenixServiceMultipleException(exceptionList);
        }

        return result;
    }

    private static void addMark(List<AttendsMark> result, StudentMark studentMark, Attends attend,
            List<DomainException> exceptionList) {
        if (studentMark.mark.length() - studentMark.mark.indexOf('.') - 1 > 2) {
            exceptionList.add(new DomainException("error.mark.more.than.two.decimals", studentMark.studentNumber));
        } else {
            result.add(new AttendsMark(attend.getExternalId(), studentMark.mark));
        }
    }

    private static Attends findAttend(final ExecutionCourse executionCourse, final String studentNumber,
            final List<DomainException> exceptionList) {

        final List<Attends> activeAttends = new ArrayList<Attends>(2);
        for (final Attends attend : executionCourse.getAttendsSet()) {
            final Student student = attend.getRegistration().getStudent();
            if ((student.getPerson().getUsername().equals(studentNumber) || student.getNumber().toString().equals(studentNumber))
                    && (isActive(attend) || belongsToActiveExternalCycle(attend))) {
                activeAttends.add(attend);
            }
        }

        if (activeAttends.size() == 1) {
            return activeAttends.iterator().next();
        }

        if (activeAttends.isEmpty()) {
            exceptionList.add(new DomainException("errors.student.without.active.attends", studentNumber));
        } else {
            exceptionList.add(new DomainException("errors.student.with.several.active.attends", studentNumber));
        }

        return null;
    }

    private static boolean belongsToActiveExternalCycle(final Attends attend) {
        if (attend.getEnrolment() != null) {
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
        if (attends.getEnrolment() != null) {
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

            if (attend.getEnrolment() != null && attend.getEnrolment().isImpossible()) {
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
                executionCourse.getNameI18N().getContent(), executionCourse.getDegreePresentationString());
    }

    private static Attends findAttend(final ExecutionCourse executionCourse, final String attendId) {
        for (final Attends attend : executionCourse.getAttendsSet()) {
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
        private final String studentNumber;
        private final String mark;

        public StudentMark(final String studentNumber, final String mark) {
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