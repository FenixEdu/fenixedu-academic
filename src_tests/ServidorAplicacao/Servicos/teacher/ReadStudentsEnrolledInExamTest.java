package ServidorAplicacao.Servicos.teacher;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.ISiteComponent;
import DataBeans.InfoExam;
import DataBeans.InfoSiteTeacherStudentsEnrolledList;
import DataBeans.InfoStudent;
import DataBeans.SiteView;
import DataBeans.util.Cloner;
import Dominio.Exam;
import Dominio.IExam;
import Dominio.IExamStudentRoom;
import Dominio.IStudent;
import Dominio.Student;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExam;
import ServidorPersistente.IPersistentExamStudentRoom;
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
			IPersistentExamStudentRoom examStudentRoomDAO = sp.getIPersistentExamStudentRoom();
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
			
			
			List examStudentRoomList = examStudentRoomDAO.readBy(exam);
			List infoExamStudentRoomList = (List) CollectionUtils.collect(examStudentRoomList, new Transformer() {

				public Object transform(Object input) {
					IExamStudentRoom examStudentRoom = (IExamStudentRoom) input;
					return Cloner.copyIExamStudentRoom2InfoExamStudentRoom(examStudentRoom);
				}}  );   
			
			component = new InfoSiteTeacherStudentsEnrolledList(infoStudents,infoExam, infoExamStudentRoomList);
			siteView.setComponent(component);
			sp.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
		}

		return siteView;
	}
}
