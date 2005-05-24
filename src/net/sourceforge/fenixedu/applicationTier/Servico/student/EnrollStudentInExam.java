package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExamStudentRoom;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IExamStudentRoom;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExam;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExamStudentRoom;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author João Mota
 * 
 */

public class EnrollStudentInExam implements IService {

    public EnrollStudentInExam() {
    }

    public Boolean run(String username, Integer examId) throws FenixServiceException {

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentStudent persistentStudent = sp.getIPersistentStudent();
            IStudent student = persistentStudent.readByUsername(username);
            IPersistentExam persistentExam = sp.getIPersistentExam();
            IPersistentExamStudentRoom persistentExamStudentRoom = sp.getIPersistentExamStudentRoom();
            IExam exam = (IExam) persistentExam.readByOID(Exam.class, examId, true);
            if (exam == null || student == null) {

                throw new InvalidArgumentsServiceException();
            }

            IExamStudentRoom examStudentRoom = persistentExamStudentRoom.readBy(exam.getIdInternal(),
                    student.getIdInternal());
            if (examStudentRoom != null) {
                throw new ExistingServiceException();
            }
            examStudentRoom = new ExamStudentRoom();
            persistentExamStudentRoom.simpleLockWrite(examStudentRoom);
            examStudentRoom.setExam(exam);
            examStudentRoom.setStudent(student);

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        return new Boolean(true);

    }

}