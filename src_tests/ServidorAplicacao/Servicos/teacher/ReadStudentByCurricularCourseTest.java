package ServidorAplicacao.Servicos.teacher;

import java.util.ArrayList;
import java.util.Collection;

import DataBeans.InfoRole;
import DataBeans.InfoSiteStudents;
import DataBeans.TeacherAdministrationSiteView;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.TestCaseServices;
import Util.RoleType;

/**
 * @author Tânia Pousão
 *
 */
public class ReadStudentByCurricularCourseTest extends TestCaseServices {
	/**
	 * @param testName
	 */
	public ReadStudentByCurricularCourseTest(String testName) {
		super(testName);
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseServices#getNameOfServiceToBeTested()
	 */
	protected String getNameOfServiceToBeTested() {
		return "ReadStudentsByCurricularCourse";

	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseServices#getDataSetFilePath()
	 */
	//	protected String getDataSetFilePath() {
	//		return "etc/ReadStudentsEnrolledInExam.xml";
	//	}

	public void testSucessfullExecutionWithOutScope() {
		try {
			Integer executionCourseCode = new Integer(25);
			Integer scopeCode = null;

			GestorServicos serviceManager = GestorServicos.manager();

			Object[] args = { executionCourseCode, scopeCode };

			TeacherAdministrationSiteView siteView =
				(TeacherAdministrationSiteView) serviceManager.executar(
					authorizedUserView(),
					"ReadStudentsByCurricularCourse",
					args);

			if (siteView == null) {
				fail("can't execute service");
			}
			InfoSiteStudents infoSiteStudents = (InfoSiteStudents) siteView.getComponent();
			assertTrue("size of List", infoSiteStudents.getStudents().size() == 4);

			//service: insert whith scope 		
			executionCourseCode = new Integer(25);
			scopeCode = new Integer(3);
			siteView = null;
			infoSiteStudents = null;
			serviceManager = GestorServicos.manager();

			siteView =
				(TeacherAdministrationSiteView) serviceManager.executar(
					authorizedUserView(),
					"ReadStudentsByCurricularCourse",
					args);
			if (siteView == null) {
				fail("can't execute service");
			}

			infoSiteStudents = (InfoSiteStudents) siteView.getComponent();

			assertTrue("size of List", infoSiteStudents.getStudents().size() == 4);
		} catch (FenixServiceException e) {
			e.printStackTrace();
			fail("Executing  Service!");
		}
	}

	public void testSucessfullExecutionWithScope() {
		try {
			Integer executionCourseCode = new Integer(25);
			Integer scopeCode = new Integer(3);

			GestorServicos serviceManager = GestorServicos.manager();

			Object[] args = { executionCourseCode, scopeCode };

			TeacherAdministrationSiteView siteView =
				(TeacherAdministrationSiteView) serviceManager.executar(
					authorizedUserView(),
					"ReadStudentsByCurricularCourse",
					args);

			if (siteView == null) {
				fail("can't execute service");
			}
			InfoSiteStudents infoSiteStudents = (InfoSiteStudents) siteView.getComponent();
			assertTrue("size of List", infoSiteStudents.getStudents().size() == 1);

		} catch (FenixServiceException e) {
			e.printStackTrace();
			fail("Executing  Service!");
		}
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
