package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.Calendar;

import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExamStudentRoom;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.notAuthorizedServiceDeleteException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExam;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExamStudentRoom;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 *  
 */

public class UnEnrollStudentInExam implements IServico {

    private static UnEnrollStudentInExam _servico = new UnEnrollStudentInExam();

    /**
     * The singleton access method of this class.
     */
    public static UnEnrollStudentInExam getService() {
        return _servico;
    }

    /**
     * The actor of this class.
     */
    private UnEnrollStudentInExam() {
    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome() {
        return "UnEnrollStudentInExam";
    }

    public Boolean run(String username, Integer examId) throws FenixServiceException {

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IPersistentStudent persistentStudent = sp.getIPersistentStudent();
            IStudent student = persistentStudent.readByUsername(username);
            IPersistentExam persistentExam = sp.getIPersistentExam();

            IExam exam = (IExam) persistentExam.readByOID(Exam.class, examId, true);

            if (student != null && exam != null) {
                if (!validUnEnrollment(exam)) {
                    throw new notAuthorizedServiceDeleteException();
                }

                IPersistentExamStudentRoom persistentExamStudentRoom = sp
                        .getIPersistentExamStudentRoom();
                ExamStudentRoom examStudentRoom = (ExamStudentRoom) persistentExamStudentRoom.readBy(
                        exam, student);
                if (examStudentRoom != null) {
                    if (examStudentRoom.getRoom() != null) {
                        throw new notAuthorizedServiceDeleteException();
                    }
                    persistentExamStudentRoom.delete(examStudentRoom);
                }
            }

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        return new Boolean(true);

    }

    /**
     * @param exam
     * @return
     */
    private boolean validUnEnrollment(IExam exam) {
        if (exam.getEnrollmentEndDay() != null && exam.getEnrollmentEndTime() != null) {
            Calendar enrollmentEnd = Calendar.getInstance();
            enrollmentEnd.set(Calendar.DAY_OF_MONTH, exam.getEnrollmentEndDay().get(
                    Calendar.DAY_OF_MONTH));
            enrollmentEnd.set(Calendar.MONTH, exam.getEnrollmentEndDay().get(Calendar.MONTH));
            enrollmentEnd.set(Calendar.YEAR, exam.getEnrollmentEndDay().get(Calendar.YEAR));
            enrollmentEnd.set(Calendar.HOUR_OF_DAY, exam.getEnrollmentEndTime()
                    .get(Calendar.HOUR_OF_DAY));
            enrollmentEnd.set(Calendar.MINUTE, exam.getEnrollmentEndTime().get(Calendar.MINUTE));
            Calendar now = Calendar.getInstance();
            if (enrollmentEnd.getTimeInMillis() > now.getTimeInMillis()) {
                return true;
            }
            return false;

        }
        return false;

    }

}