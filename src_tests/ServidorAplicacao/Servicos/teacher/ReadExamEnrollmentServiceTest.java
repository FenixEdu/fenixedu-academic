/*
 * Created on 22/Mai/2003
 *
 */
package ServidorAplicacao.Servicos.teacher;

import DataBeans.InfoExam;
import DataBeans.InfoExamEnrollment;
import DataBeans.util.Cloner;
import Dominio.Exam;
import Dominio.IExam;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Tânia Nunes
 *
 */
public class ReadExamEnrollmentServiceTest extends TestCaseReadServices {

	/**
	 * @param testName
	 */
	public ReadExamEnrollmentServiceTest(String testName) {
		super(testName);

	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
	 */
	protected String getNameOfServiceToBeTested() {

		return "ReadExamEnrollment";
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {

		Object[] result = { new Integer("1"), new Integer("12")};
		return result;

	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedSuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		Object[] result = { new Integer("24"), new Integer("1")};
		return result;
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getNumberOfItemsToRetrieve()
	 */
	protected int getNumberOfItemsToRetrieve() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getObjectToCompare()
	 */
	protected Object getObjectToCompare() {
		InfoExamEnrollment infoExamEnrollment = new InfoExamEnrollment();
		IExam exam = new Exam();
		exam.setIdInternal(new Integer(1));
		try {
			
			exam =
				(IExam) ((ISuportePersistente) SuportePersistenteOJB
					.getInstance())
					.getIPersistentExam()
					.readByOId(exam, false);
			InfoExam infoExam = Cloner.copyIExam2InfoExam(exam);
			infoExamEnrollment.setInfoExam(infoExam);

		} catch (ExcepcaoPersistencia e) {
		}
		return infoExamEnrollment;
	}

}
