package ServidorAplicacao.Servico.student;

import Dominio.Exam;
import Dominio.IExam;
import Dominio.IStudent;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExam;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 *
 */

public class EnrollStudentInExam implements IServico {

	private static EnrollStudentInExam _servico = new EnrollStudentInExam();
	/**
	 * The singleton access method of this class.
	 **/
	public static EnrollStudentInExam getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private EnrollStudentInExam() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "EnrollStudentInExam";
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
			if (exam == null || student == null) {

				throw new InvalidArgumentsServiceException();
			}
			if ( student.getExamsEnrolled().contains(exam)) {
				throw new ExistingServiceException();
			}
			persistentStudent.lockWrite(student);
	
			student.getExamsEnrolled().add(exam);

			
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}

		return new Boolean(true);

	}

}
