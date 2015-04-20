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

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.Attends;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.WrittenEvaluation;
import org.fenixedu.academic.domain.WrittenEvaluationEnrolment;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.service.filter.ExecutionCourseAndExamLecturingTeacherAuthorizationFilter;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.spaces.domain.Space;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class WrittenEvaluationRoomDistribution {

    protected void run(String executionCourseID, String evaluationID, List<String> roomIDs, Boolean distributeOnlyEnroledStudents)
            throws FenixServiceException {

        final WrittenEvaluation writtenEvaluation = (WrittenEvaluation) FenixFramework.getDomainObject(evaluationID);
        if (writtenEvaluation == null) {
            throw new FenixServiceException("error.noWrittenEvaluation");
        }
        List<Registration> studentsToDistribute;
        if (distributeOnlyEnroledStudents) {
            studentsToDistribute = readEnroledStudentsInWrittenEvaluation(writtenEvaluation);
        } else {
            studentsToDistribute = readAllStudentsAttendingExecutionCourses(writtenEvaluation);
        }
        final List<Space> selectedRooms = readRooms(writtenEvaluation, roomIDs);
        if (!selectedRooms.containsAll(writtenEvaluation.getAssociatedRooms())) {
            // if the selected rooms are different of the evaluation rooms
            // then the user probably selected repeated rooms
            throw new FenixServiceException("error.repeated.rooms");
        }
        writtenEvaluation.distributeStudentsByRooms(studentsToDistribute, selectedRooms);
    }

    private List<Space> readRooms(final WrittenEvaluation writtenEvaluation, final List<String> roomIDs) {

        List<String> selectedRoomIDs = removeDuplicatedEntries(roomIDs);
        final List<Space> writtenEvaluationRooms = writtenEvaluation.getAssociatedRooms();
        final List<Space> selectedRooms = new ArrayList<Space>(selectedRoomIDs.size());

        for (final String roomID : selectedRoomIDs) {
            for (final Space room : writtenEvaluationRooms) {
                if (room.getExternalId().equals(roomID)) {
                    selectedRooms.add(room);
                    break;
                }
            }
        }
        return selectedRooms;
    }

    private List<String> removeDuplicatedEntries(List<String> roomIDs) {
        List<String> result = new ArrayList<String>();
        for (final String roomID : roomIDs) {
            if (!result.contains(roomID)) {
                result.add(roomID);
            }
        }
        return result;
    }

    private List<Registration> readEnroledStudentsInWrittenEvaluation(WrittenEvaluation writtenEvaluation) {
        final List<Registration> result =
                new ArrayList<Registration>(writtenEvaluation.getWrittenEvaluationEnrolmentsSet().size());
        for (final WrittenEvaluationEnrolment writtenEvaluationEnrolment : writtenEvaluation.getWrittenEvaluationEnrolmentsSet()) {
            result.add(writtenEvaluationEnrolment.getStudent());
        }
        return result;
    }

    private List<Registration> readAllStudentsAttendingExecutionCourses(WrittenEvaluation writtenEvaluation) {
        final List<Registration> result = new ArrayList<Registration>();
        for (final ExecutionCourse executionCourse : writtenEvaluation.getAssociatedExecutionCoursesSet()) {
            for (final Attends attend : executionCourse.getAttendsSet()) {
                if (!result.contains(attend.getRegistration())) {
                    result.add(attend.getRegistration());
                }
            }
        }
        return result;
    }

    // Service Invokers migrated from Berserk

    private static final WrittenEvaluationRoomDistribution serviceInstance = new WrittenEvaluationRoomDistribution();

    @Atomic
    public static void runWrittenEvaluationRoomDistribution(String executionCourseID, String evaluationID, List<String> roomIDs,
            Boolean distributeOnlyEnroledStudents) throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseAndExamLecturingTeacherAuthorizationFilter.instance.execute(executionCourseID, evaluationID);
        serviceInstance.run(executionCourseID, evaluationID, roomIDs, distributeOnlyEnroledStudents);
    }

}