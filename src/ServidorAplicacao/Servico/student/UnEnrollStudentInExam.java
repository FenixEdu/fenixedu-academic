package ServidorAplicacao.Servico.student;

import Dominio.Exam;
import Dominio.ExamStudentRoom;
import Dominio.IExam;
import Dominio.IStudent;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
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

	public Boolean run(String username, Integer examId) throws FenixServiceException {

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			IPersistentStudent persistentStudent = sp.getIPersistentStudent();
			IStudent student = persistentStudent.readByUsername(username);

			if (student != null) {
				IPersistentExam persistentExam = sp.getIPersistentExam();

				IExam exam = new Exam();
				exam.setIdInternal(examId);
				exam = (IExam) persistentExam.readByOId(exam, true);

				persistentStudent.lockWrite(student);
				student.getExamsEnrolled().remove(exam);

				IPersistentExamStudentRoom persistentExamStudentRoom = sp.getIPersistentExamStudentRoom();
				ExamStudentRoom examStudentRoom = (ExamStudentRoom) persistentExamStudentRoom.readBy(exam, student);
				if (examStudentRoom != null) {
					persistentExamStudentRoom.delete(examStudentRoom);
				}
			}

		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}

		return new Boolean(true);

	}

}
