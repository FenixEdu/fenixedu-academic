package ServidorAplicacao.Servicos.teacher;

import java.util.ArrayList;
import java.util.List;

import DataBeans.ISiteComponent;
import DataBeans.InfoExam;
import DataBeans.InfoSiteTeacherStudentsEnrolledList;
import DataBeans.InfoStudent;
import DataBeans.SiteView;
import DataBeans.util.Cloner;
import Dominio.Exam;
import Dominio.IExam;
import Dominio.IStudent;
import Dominio.Student;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExam;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 *
 */
public class ReadStudentsEnrolledInExamTest extends TestCaseReadServices {
	/**
	 * @param testName
	 */
	public ReadStudentsEnrolledInExamTest(String testName) {
		super(testName);
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseServices#getNameOfServiceToBeTested()
	 */
	protected String getNameOfServiceToBeTested() {
		return "ReadStudentsEnrolledInExam";
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {

		return null;
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedSuccessfuly()
	 */
	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		Object[] args = { new Integer(25), new Integer(2)};
		return args;
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
		ISiteComponent component = null;

		List infoStudents = new ArrayList();
		SiteView siteView = new SiteView();
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();
			IStudent student = new Student(new Integer(3));
			student = (IStudent) sp.getIPersistentStudent().readByOId(student, false);
			InfoStudent infoStudent = Cloner.copyIStudent2InfoStudent(student);
			infoStudents.add(infoStudent);
			IExam exam = new Exam();
			exam.setIdInternal(new Integer(2));
			IPersistentExam persistentExam = sp.getIPersistentExam();
			exam = (IExam) persistentExam.readByOId(exam, false);
			InfoExam infoExam = Cloner.copyIExam2InfoExam(exam);
			component = new InfoSiteTeacherStudentsEnrolledList(infoStudents,infoExam);
			siteView.setComponent(component);
			sp.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
		}

		return siteView;
	}
}
