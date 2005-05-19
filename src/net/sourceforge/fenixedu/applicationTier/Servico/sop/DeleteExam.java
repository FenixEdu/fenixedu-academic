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

import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExamStudentRoom;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IExamStudentRoom;
import net.sourceforge.fenixedu.domain.IRoomOccupation;
import net.sourceforge.fenixedu.domain.RoomOccupation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class DeleteExam implements IService {

    public Object run(Integer examId) throws ExcepcaoPersistencia {

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        final IExam examToDelete = (IExam) sp.getIPersistentExam().readByOID(Exam.class,examId);

        if (examToDelete == null) {
            return new Boolean(false);
        }

        final List<IExamStudentRoom> examStudentRooms = examToDelete.getExamStudentRooms();
        for (final IExamStudentRoom examStudentRoom : examStudentRooms) {
            examStudentRoom.setStudent(null);
            examStudentRoom.setRoom(null);
            examStudentRoom.setExam(null);
            sp.getIPersistentExamStudentRoom().deleteByOID(ExamStudentRoom.class,
                    examStudentRoom.getIdInternal());
        }
        examToDelete.getExamStudentRooms().clear();

        examToDelete.getAssociatedExecutionCourses().clear();
        for (final IRoomOccupation roomOccupation : (List<IRoomOccupation>) examToDelete
                .getAssociatedRoomOccupation()) {
            roomOccupation.setPeriod(null);
            roomOccupation.setRoom(null);
            roomOccupation.setWrittenEvaluation(null);
            sp.getIPersistentRoomOccupation().deleteByOID(RoomOccupation.class,
                    roomOccupation.getIdInternal());
        }
        examToDelete.getAssociatedRoomOccupation().clear();

        for (final ICurricularCourseScope curricularCourseScope : (List<ICurricularCourseScope>) examToDelete
                .getAssociatedCurricularCourseScope()) {
            curricularCourseScope.getAssociatedWrittenEvaluations().remove(examToDelete);
        }
        examToDelete.getAssociatedCurricularCourseScope().clear();
        sp.getIPersistentExam().deleteByOID(Exam.class, examToDelete.getIdInternal());

        return new Boolean(true);
    }

}