/*
 * ReadCourseByStudentTest.java JUnit based test
 * 
 * Created on February 26th, 2003, 15:33
 */

package ServidorAplicacao.Servicos.student;

/**
 * @author Nuno Nunes & Joana Mota
 */
import java.util.List;

import DataBeans.InfoExamStudentRoom;
import DataBeans.InfoPerson;
import DataBeans.InfoShiftEnrolment;
import DataBeans.InfoStudentSiteExams;
import DataBeans.SiteView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase;
import ServidorAplicacao.Servicos.UtilsTestCase;

public class ReadExamsByStudentTest
	extends ServiceNeedsAuthenticationTestCase {

	private InfoPerson infoPerson = null;
	private InfoShiftEnrolment infoShiftEnrolment = null;

	public ReadExamsByStudentTest(java.lang.String testName) {
		super(testName);
	}

	protected String getApplication() {
		return Autenticacao.EXTRANET;
	}

	protected String[] getAuthenticatedAndAuthorizedUser() {
		String[] args = { "13", "pass", getApplication()};
		return args;
	}

	protected String[] getAuthenticatedAndUnauthorizedUser() {
		String[] args = { "nmsn", "pass", getApplication()};
		return args;
	}

	protected Object[] getAuthorizeArguments() {
		Object[] args = { "13" };
		return args;
	}

	protected String getDataSetFilePath() {
		return "etc/datasets/servicos/student/testReadExamsByStudentDataSet.xml";
	}

	protected String getNameOfServiceToBeTested() {
		return "ReadExamsByStudent";
	}

	protected String[] getNotAuthenticatedUser() {
		String[] args = { "fiado", "pass", getApplication()};
		return args;
	}

	public void testNonExistingStudent() {

		Object[] args = { "100" };
		SiteView result = null;

		try {
			result =
				(SiteView) gestor.executar(
					userView,
					getNameOfServiceToBeTested(),
					args);
			InfoStudentSiteExams infoStudentsSiteExams =
				(InfoStudentSiteExams) result.getComponent();
			List examsToEnroll = infoStudentsSiteExams.getExamsToEnroll();
			assertEquals(examsToEnroll.size(), 0);

			System.out.println(
				"testNonExistingStudent was SUCCESSFULY runned by class: "
					+ this.getClass().getName());

		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(
				"testNonExistingStudent was UNSUCCESSFULY runned by class: "
					+ this.getClass().getName());
			fail("testNonExistingStudent");
		}
	}

	public void testReadExamsExistingStudent() {
		Object[] args = getAuthorizeArguments();
		SiteView result = null;

		try {
			result =
				(SiteView) gestor.executar(
					userView,
					getNameOfServiceToBeTested(),
					args);

			InfoStudentSiteExams infoStudentsSiteExams =
				(InfoStudentSiteExams) result.getComponent();

			List infoExamsStudentRoomList =
				infoStudentsSiteExams.getExamsEnrolledDistributions();
			assertEquals(infoExamsStudentRoomList.size(), 3);

			Object[] values = { new Integer(1), new Integer(2), new Integer(3)};

			System.out.println(InfoExamStudentRoom.class);
			
			assertTrue(
				UtilsTestCase.readTestList(
					infoExamsStudentRoomList,
					values,
					"idInternal",
					InfoExamStudentRoom.class));

			compareDataSetUsingExceptedDataSetTableColumns("etc/datasets/servicos/student/testReadExamsByStudentExpectedDataSet.xml");

			System.out.println(
				"testReadExamsExistingStudent was SUCCESSFULY runned by class: "
					+ this.getClass().getName());
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(
				"testReadExamsExistingStudent was UNSUCCESSFULY runned by class: "
					+ this.getClass().getName());
			fail("testReadExamsExistingStudent");
		}

	}

}
