/*
 * Created on 28/Mai/2003
 *
 */
package ServidorAplicacao.Servico.gesdis.teacher;

import java.util.Calendar;

import Dominio.Exam;
import Dominio.ExamEnrollment;
import Dominio.IExam;
import Dominio.IExamEnrollment;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidTimeIntervalServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExam;
import ServidorPersistente.IPersistentExamEnrollment;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Tânia Nunes
 *
 */
public class InsertExamEnrollment implements IServico {

	/**
		 * Returns the service name.
		 **/

	public final String getNome() {

		return "InsertExamEnrollment";

	}
	private static InsertExamEnrollment service = new InsertExamEnrollment();

	/**
	 * The singleton access method of this class.
	 **/

	public static InsertExamEnrollment getService() {

		return service;

	}

	public Boolean run(
		Integer disciplinaExecucaoIdInternal,
		Integer examIdInternal,
		Calendar newBeginDate,
		Calendar newEndDate)
		throws FenixServiceException {
		try {

			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentExamEnrollment persistentExamEnrollment =
				sp.getIPersistentExamEnrollment();

			IExamEnrollment examEnrollment = new ExamEnrollment();

			IPersistentExam persistentExam = sp.getIPersistentExam();

			IExam exam = new Exam();
			exam.setIdInternal(examIdInternal);
			exam = (IExam) persistentExam.readByOId(exam, false);
			
			if (exam != null){			
				examEnrollment.setExam(exam);}

			else{			
				throw new InvalidArgumentsServiceException();}

			examEnrollment.setBeginDate(newBeginDate);
			examEnrollment.setEndDate(newEndDate);
		
			if (exam.getDay()==null ||exam.getDay().getTimeInMillis()<examEnrollment.getEndDate().getTimeInMillis()) {
				throw new InvalidTimeIntervalServiceException();
			}
		
			persistentExamEnrollment.lockWrite(examEnrollment);

		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}

		return new Boolean(true);
	}

}
