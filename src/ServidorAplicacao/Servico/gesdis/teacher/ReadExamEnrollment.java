/*
 * Created on 22/Mai/2003
 *
 */
package ServidorAplicacao.Servico.gesdis.teacher;

import DataBeans.InfoExamEnrollment;
import DataBeans.util.Cloner;
import Dominio.Exam;
import Dominio.IExam;
import Dominio.IExamEnrollment;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExam;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Tânia Nunes
 *
 */
public class ReadExamEnrollment implements IServico {

	/**
	 * Returns the service name.
	 **/

	public final String getNome() {

		return "ReadExamEnrollment";

	}
	private static ReadExamEnrollment service = new ReadExamEnrollment();

	/**
	 * The singleton access method of this class.
	 **/

	public static ReadExamEnrollment getService() {

		return service;

	}

	/**
	 * Executes the service. Returns the current exam enrollment
	 * 
	 **/

	public InfoExamEnrollment run(
		Integer disciplinaExecucaoIdInternal,
		Integer examIdInternal)
		throws FenixServiceException {
		try {
			IExam exam = new Exam();
			exam.setIdInternal(examIdInternal);
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentExam persistentExam = sp.getIPersistentExam();
			exam = (IExam) persistentExam.readByOId(exam);

			ServidorPersistente.IPersistentExamEnrollment persistentExamEnrollment =
				sp.getIPersistentExamEnrollment();
			IExamEnrollment examEnrollment =
				persistentExamEnrollment.readIExamEnrollmentByExam(exam);
			InfoExamEnrollment infoExamEnrollment = Cloner.copyIExamEnrollment2InfoExamEnrollment(examEnrollment);
			return infoExamEnrollment;
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}

	}
}
