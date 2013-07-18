package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseAndExamLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.WrittenEvaluationEnrolment;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixWebFramework.services.Service;

public class WrittenEvaluationRoomDistribution {

    protected void run(Integer executionCourseID, Integer evaluationID, List<Integer> roomIDs, Boolean sendSMS,
            Boolean distributeOnlyEnroledStudents) throws FenixServiceException {

        final WrittenEvaluation writtenEvaluation = (WrittenEvaluation) RootDomainObject.getInstance().readEvaluationByOID(evaluationID);
        if (writtenEvaluation == null) {
            throw new FenixServiceException("error.noWrittenEvaluation");
        }
        List<Registration> studentsToDistribute;
        if (distributeOnlyEnroledStudents) {
            studentsToDistribute = readEnroledStudentsInWrittenEvaluation(writtenEvaluation);
        } else {
            studentsToDistribute = readAllStudentsAttendingExecutionCourses(writtenEvaluation);
        }
        final List<AllocatableSpace> selectedRooms = readRooms(writtenEvaluation, roomIDs);
        if (!selectedRooms.containsAll(writtenEvaluation.getAssociatedRooms())) {
            // if the selected rooms are different of the evaluation rooms
            // then the user probably selected repeated rooms
            throw new FenixServiceException("error.repeated.rooms");
        }
        writtenEvaluation.distributeStudentsByRooms(studentsToDistribute, selectedRooms);
        if (sendSMS) {
            sendSMSToStudents(writtenEvaluation);
        }
    }

    private List<AllocatableSpace> readRooms(final WrittenEvaluation writtenEvaluation, final List<Integer> roomIDs) {

        List<Integer> selectedRoomIDs = removeDuplicatedEntries(roomIDs);
        final List<AllocatableSpace> writtenEvaluationRooms = writtenEvaluation.getAssociatedRooms();
        final List<AllocatableSpace> selectedRooms = new ArrayList<AllocatableSpace>(selectedRoomIDs.size());

        for (final Integer roomID : selectedRoomIDs) {
            for (final AllocatableSpace room : writtenEvaluationRooms) {
                if (room.getIdInternal().equals(roomID)) {
                    selectedRooms.add(room);
                    break;
                }
            }
        }
        return selectedRooms;
    }

    private List<Integer> removeDuplicatedEntries(List<Integer> roomIDs) {
        List<Integer> result = new ArrayList<Integer>();
        for (final Integer roomID : roomIDs) {
            if (!result.contains(roomID)) {
                result.add(roomID);
            }
        }
        return result;
    }

    private List<Registration> readEnroledStudentsInWrittenEvaluation(WrittenEvaluation writtenEvaluation) {
        final List<Registration> result = new ArrayList<Registration>(writtenEvaluation.getWrittenEvaluationEnrolmentsCount());
        for (final WrittenEvaluationEnrolment writtenEvaluationEnrolment : writtenEvaluation.getWrittenEvaluationEnrolments()) {
            result.add(writtenEvaluationEnrolment.getStudent());
        }
        return result;
    }

    private List<Registration> readAllStudentsAttendingExecutionCourses(WrittenEvaluation writtenEvaluation) {
        final List<Registration> result = new ArrayList<Registration>();
        for (final ExecutionCourse executionCourse : writtenEvaluation.getAssociatedExecutionCourses()) {
            for (final Attends attend : executionCourse.getAttends()) {
                if (!result.contains(attend.getRegistration())) {
                    result.add(attend.getRegistration());
                }
            }
        }
        return result;
    }

    private void sendSMSToStudents(WrittenEvaluation writtenEvaluation) {
        // TODO: Send SMS method: fill this method when we have sms
    }

    // Service Invokers migrated from Berserk

    private static final WrittenEvaluationRoomDistribution serviceInstance = new WrittenEvaluationRoomDistribution();

    @Service
    public static void runWrittenEvaluationRoomDistribution(Integer executionCourseID, Integer evaluationID,
            List<Integer> roomIDs, Boolean sendSMS, Boolean distributeOnlyEnroledStudents) throws FenixServiceException,
            NotAuthorizedException {
        ExecutionCourseAndExamLecturingTeacherAuthorizationFilter.instance.execute(executionCourseID, evaluationID, roomIDs,
                sendSMS, distributeOnlyEnroledStudents);
        serviceInstance.run(executionCourseID, evaluationID, roomIDs, sendSMS, distributeOnlyEnroledStudents);
    }

}