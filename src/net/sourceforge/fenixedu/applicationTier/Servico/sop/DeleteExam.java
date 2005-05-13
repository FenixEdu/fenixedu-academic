/*
 * ApagarAula.java
 *
 * Created on 27 de Outubro de 2002, 14:30
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Service DeleteExam.
 * 
 * @author tfc130
 */
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExamExecutionCourse;
import net.sourceforge.fenixedu.domain.ExamStudentRoom;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IExamExecutionCourse;
import net.sourceforge.fenixedu.domain.IExamStudentRoom;
import net.sourceforge.fenixedu.domain.IRoomOccupation;
import net.sourceforge.fenixedu.domain.IWrittenEvaluationCurricularCourseScope;
import net.sourceforge.fenixedu.domain.RoomOccupation;
import net.sourceforge.fenixedu.domain.WrittenEvaluationCurricularCourseScope;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class DeleteExam implements IService {

    public Object run(Integer examId) throws FenixServiceException,
            ExcepcaoPersistencia {

        boolean result = false;

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IExam examToDelete = (IExam) sp.getIPersistentExam().readByOID(Exam.class,examId);

        List<IExamStudentRoom> examStudentRooms = examToDelete.getExamStudentRooms();
        for (IExamStudentRoom examStudentRoom : examStudentRooms) {
            examStudentRoom.setStudent(null);
            examStudentRoom.setRoom(null);
            examStudentRoom.setExam(null);
            sp.getIPersistentExamStudentRoom().deleteByOID(ExamStudentRoom.class,
                    examStudentRoom.getIdInternal());
        }

        examToDelete.getExamStudentRooms().clear();
        for (IExamExecutionCourse examExecutionCourse : (List<IExamExecutionCourse>) examToDelete
                .getExamExecutionCourses()) {
            examExecutionCourse.setExecutionCourse(null);
            examExecutionCourse.setExam(null);
            sp.getIPersistentExamExecutionCourse().deleteByOID(ExamExecutionCourse.class,
                    examExecutionCourse.getIdInternal());
        }
        examToDelete.getExamExecutionCourses().clear();

        examToDelete.getAssociatedExecutionCourses().clear();
        for (IRoomOccupation roomOccupation : (List<IRoomOccupation>) examToDelete
                .getAssociatedRoomOccupation()) {
            roomOccupation.setPeriod(null);
            roomOccupation.setRoom(null);
            roomOccupation.setWrittenEvaluation(null);
            sp.getIPersistentRoomOccupation().deleteByOID(RoomOccupation.class,
                    roomOccupation.getIdInternal());
        }
        examToDelete.getAssociatedRoomOccupation().clear();
        for (IWrittenEvaluationCurricularCourseScope writtenEvaluationCurricularCourseScope : (List<IWrittenEvaluationCurricularCourseScope>) examToDelete
                .getWrittenEvaluationCurricularCourseScopes()) {
            writtenEvaluationCurricularCourseScope.setCurricularCourseScope(null);
            writtenEvaluationCurricularCourseScope.setWrittenEvaluation(null);
            sp.getIPersistentWrittenEvaluationCurricularCourseScope().deleteByOID(
                    WrittenEvaluationCurricularCourseScope.class,
                    writtenEvaluationCurricularCourseScope.getIdInternal());
        }
        examToDelete.getWrittenEvaluationCurricularCourseScopes().clear();
        sp.getIPersistentExam().deleteByOID(Exam.class, examToDelete.getIdInternal());
        result = true;

        return new Boolean(result);

    }

}