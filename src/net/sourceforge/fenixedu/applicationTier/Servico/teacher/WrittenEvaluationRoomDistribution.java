package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IWrittenEvaluation;
import net.sourceforge.fenixedu.domain.IWrittenEvaluationEnrolment;
import net.sourceforge.fenixedu.domain.space.IRoom;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class WrittenEvaluationRoomDistribution implements IService {

    public void run(Integer executionCourseID, Integer evaluationID, List<Integer> roomIDs,
            Boolean sendSMS, Boolean distributeOnlyEnroledStudents) throws FenixServiceException,
            ExcepcaoPersistencia {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();

        final IWrittenEvaluation writtenEvaluation = (IWrittenEvaluation) persistentSupport
                .getIPersistentEvaluation().readByOID(Exam.class, evaluationID);
        if (writtenEvaluation == null) {
            throw new FenixServiceException("error.noWrittenEvaluation");
        }
        List<IStudent> studentsToDistribute;
        if (distributeOnlyEnroledStudents) {
            studentsToDistribute = readEnroledStudentsInWrittenEvaluation(writtenEvaluation);
        } else {
            studentsToDistribute = readAllStudentsAttendingExecutionCourses(writtenEvaluation);
        }
        final List<IRoom> selectedRooms = readRooms(writtenEvaluation, roomIDs);
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
    
    private List<IRoom> readRooms(final IWrittenEvaluation writtenEvaluation,
            final List<Integer> roomIDs) throws ExcepcaoPersistencia {
        
        List<Integer> selectedRoomIDs = removeDuplicatedEntries(roomIDs);
        final List<IRoom> writtenEvaluationRooms = writtenEvaluation.getAssociatedRooms();        
        final List<IRoom> selectedRooms = new ArrayList<IRoom>(selectedRoomIDs.size());
        
        for (final Integer roomID : selectedRoomIDs) {
            for (final IRoom room : writtenEvaluationRooms) {
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

    private List<IStudent> readEnroledStudentsInWrittenEvaluation(IWrittenEvaluation writtenEvaluation) {
        final List<IStudent> result = new ArrayList<IStudent>(writtenEvaluation
                .getWrittenEvaluationEnrolmentsCount());
        for (final IWrittenEvaluationEnrolment writtenEvaluationEnrolment : writtenEvaluation
                .getWrittenEvaluationEnrolments()) {
            result.add(writtenEvaluationEnrolment.getStudent());
        }
        return result;
    }

    private List<IStudent> readAllStudentsAttendingExecutionCourses(IWrittenEvaluation writtenEvaluation) {
        final List<IStudent> result = new ArrayList<IStudent>();
        for (final IExecutionCourse executionCourse : writtenEvaluation.getAssociatedExecutionCourses()) {
            for (final IAttends attend : executionCourse.getAttends()) {
                if (!result.contains(attend.getAluno())) {
                    result.add(attend.getAluno());
                }
            }
        }
        return result;
    }

    private void sendSMSToStudents(IWrittenEvaluation writtenEvaluation) {
        // TODO: Send SMS method: fill this method when we have sms
    }
}