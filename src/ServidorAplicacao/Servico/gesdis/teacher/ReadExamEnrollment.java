/*
 * Created on 22/Mai/2003
 *
 */
package ServidorAplicacao.Servico.gesdis.teacher;

import DataBeans.ISiteComponent;
import DataBeans.InfoExam;
import DataBeans.InfoExamEnrollment;
import DataBeans.InfoSiteTeacherExamEnrollment;
import DataBeans.SiteView;
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

	public Object run(
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
			InfoExam infoExam =Cloner.copyIExam2InfoExam(exam);
			ISiteComponent component = new InfoSiteTeacherExamEnrollment(infoExam,infoExamEnrollment);
			SiteView siteView = new SiteView(component);
			return siteView;
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}

	}
}
