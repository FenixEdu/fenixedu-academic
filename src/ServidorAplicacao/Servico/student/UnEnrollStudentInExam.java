package ServidorAplicacao.Servico.student;

import java.util.Calendar;

import Dominio.Exam;
import Dominio.ExamStudentRoom;
import Dominio.IExam;
import Dominio.IStudent;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.notAuthorizedServiceDeleteException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExam;
import ServidorPersistente.IPersistentExamStudentRoom;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 *
 */

public class UnEnrollStudentInExam implements IServico {

	private static UnEnrollStudentInExam _servico = new UnEnrollStudentInExam();
	/**
	 * The singleton access method of this class.
	 **/
	public static UnEnrollStudentInExam getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private UnEnrollStudentInExam() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "UnEnrollStudentInExam";
	}

	public Boolean run(String username, Integer examId)
		throws FenixServiceException {

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			IPersistentStudent persistentStudent = sp.getIPersistentStudent();
			IStudent student = persistentStudent.readByUsername(username);
			IPersistentExam persistentExam = sp.getIPersistentExam();

			IExam exam = new Exam();
			exam.setIdInternal(examId);
			exam = (IExam) persistentExam.readByOId(exam, true);

			if (student != null && exam != null) {
				if (!validUnEnrollment(exam)) {
					throw new notAuthorizedServiceDeleteException();
				}

				IPersistentExamStudentRoom persistentExamStudentRoom =
					sp.getIPersistentExamStudentRoom();
				ExamStudentRoom examStudentRoom =
					(ExamStudentRoom) persistentExamStudentRoom.readBy(
						exam,
						student);
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
		if (exam.getEnrollmentEndDay() != null
			&& exam.getEnrollmentEndTime() != null) {
			Calendar enrollmentEnd = Calendar.getInstance();
			enrollmentEnd.set(
				Calendar.DAY_OF_MONTH,
				exam.getEnrollmentEndDay().get(Calendar.DAY_OF_MONTH));
			enrollmentEnd.set(
				Calendar.MONTH,
				exam.getEnrollmentEndDay().get(Calendar.MONTH));
			enrollmentEnd.set(
				Calendar.YEAR,
				exam.getEnrollmentEndDay().get(Calendar.YEAR));
			enrollmentEnd.set(
				Calendar.HOUR_OF_DAY,
				exam.getEnrollmentEndTime().get(Calendar.HOUR_OF_DAY));
			enrollmentEnd.set(
				Calendar.MINUTE,
				exam.getEnrollmentEndTime().get(Calendar.MINUTE));
			Calendar now = Calendar.getInstance();
			if (enrollmentEnd.getTimeInMillis() > now.getTimeInMillis()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

}
