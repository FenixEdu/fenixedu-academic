package ServidorAplicacao.Servicos.teacher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import DataBeans.InfoFrequenta;
import DataBeans.InfoMark;
import DataBeans.InfoRole;
import DataBeans.InfoSiteMarks;
import DataBeans.InfoStudent;
import DataBeans.InfoTeacher;
import DataBeans.TeacherAdministrationSiteView;
import DataBeans.util.Cloner;
import Dominio.Credits;
import Dominio.ICredits;
import Dominio.IExecutionPeriod;
import Dominio.ITeacher;
import Dominio.Teacher;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.TestCaseServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCreditsTeacher;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;

/**
 * @author Fernanda Quitério
 *
 */
public class InsertExamMarksTest extends TestCaseServices {
	/**
	 * @param testName
	 */
	public InsertExamMarksTest(String testName) {
		super(testName);
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseServices#getNameOfServiceToBeTested()
	 */
	protected String getNameOfServiceToBeTested() {
		return "InsertExamMarksTest";
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseServices#getDataSetFilePath()
	 */
	protected String getDataSetFilePath() {
		return "etc/testDataSetForTeacherCredits.xml";
	}

	public static void main(java.lang.String[] args) {
		TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite testSuite = new TestSuite(InsertExamMarksTest.class);
		return testSuite;
	}

	public void testWrite() {
		try {
			//Service: insert
			Integer executionCourseCode = new Integer(4);
			Integer examCode = new Integer(4);

			List infoMarksList = getInfoMarksList(new Integer(22222), "A");

			Object[] args = { executionCourseCode, examCode, infoMarksList };
			GestorServicos serviceManager = GestorServicos.manager();

			TeacherAdministrationSiteView siteView =
				(TeacherAdministrationSiteView) serviceManager.executar(authorizedUserView(), getNameOfServiceToBeTested(), args);

			if (siteView == null) {
				fail("can't execute service");
			}
			InfoSiteMarks infoSiteMarks = (InfoSiteMarks) siteView.getComponent();
			assertTrue("size of List", infoSiteMarks.getMarksList().size() == 2);

			//Service: error in mark

			infoMarksList = getInfoMarksList(new Integer(22222), "A");

			Object[] args2 = { executionCourseCode, examCode, infoMarksList };

			siteView =
				(TeacherAdministrationSiteView) serviceManager.executar(authorizedUserView(), getNameOfServiceToBeTested(), args2);

			if (siteView == null) {
				fail("can't execute service");
			}

			infoSiteMarks = (InfoSiteMarks) siteView.getComponent();
			assertTrue("size of List", infoSiteMarks.getMarksList().size() == 0);

			assertTrue("errors list", infoSiteMarks.getMarksListErrors().size() == 1);

			//Service: error student number
			infoMarksList = getInfoMarksList(new Integer(22222), "A");

			Object[] args3 = { executionCourseCode, examCode, infoMarksList };

			siteView =
				(TeacherAdministrationSiteView) serviceManager.executar(authorizedUserView(), getNameOfServiceToBeTested(), args2);

			if (siteView == null) {
				fail("can't execute service");
			}

			infoSiteMarks = (InfoSiteMarks) siteView.getComponent();
			assertTrue("size of List", infoSiteMarks.getMarksList().size() == 0);

			assertTrue("errors list", infoSiteMarks.getMarksListErrors().size() == 1);
		} catch (FenixServiceException e) {
			e.printStackTrace();
			fail("Executing  Service!");
		}
	}

	private List getInfoMarksList(Integer studentNumber, String mark) {
		InfoStudent infoStudent = new InfoStudent();
		infoStudent.setNumber(studentNumber);
		InfoFrequenta infoFrequenta = new InfoFrequenta();
		infoFrequenta.setAluno(infoStudent);
		InfoMark infoMark = new InfoMark();
		infoMark.setInfoFrequenta(infoFrequenta);
		infoMark.setMark(mark);

		List infoMarksList = new ArrayList();
		infoMarksList.add(infoMark);

		return infoMarksList;
	}

	public IUserView authorizedUserView() {
		InfoRole infoRole = new InfoRole();
		infoRole.setRoleType(RoleType.TEACHER);

		Collection roles = new ArrayList();
		roles.add(infoRole);

		UserView userView = new UserView("user", roles);

		return userView;
	}
}