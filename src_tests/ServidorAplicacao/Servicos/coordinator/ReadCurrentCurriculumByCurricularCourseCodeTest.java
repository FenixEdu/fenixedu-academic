package ServidorAplicacao.Servicos.coordinator;

import java.util.Calendar;

import DataBeans.InfoCurriculum;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorAplicacao.Servicos.ServiceTestCase;

/**
 * @author Fernanda Quitério
 * 13/Nov/2003
 * 
 */
public class ReadCurrentCurriculumByCurricularCourseCodeTest extends ServiceTestCase {

	/**
	 * @param testName
	 */

	public ReadCurrentCurriculumByCurricularCourseCodeTest(String testName) {
		super(testName);
	}

	protected String getApplication() {
		return Autenticacao.EXTRANET;
	}

	protected String getNameOfServiceToBeTested() {
		return "ReadCurrentCurriculumByCurricularCourseCode";
	}

	protected String getDataSetFilePath() {
		return "etc/datasets_templates/servicos/coordinator/testDataSetCurriculum.xml";
	}
	protected String[] getAuthenticatedAndAuthorizedUser() {
		String[] args = { "user", "pass", getApplication()};
		return args;
	}
	protected String[] getAuthenticatedAndUnauthorizedUser() {
		String[] args = { "julia", "pass", getApplication()};
		return args;
	}
	protected String[] getNotAuthenticatedUser() {
		String[] args = { "fiado", "pass", getApplication()};
		return args;
	}
	protected Object[] getAuthorizeArguments() {
		Integer infoCurricularCourseCode = new Integer(1);
		Integer infoExecutionDegreeCode = new Integer(10);

		Object[] args = { infoExecutionDegreeCode, infoCurricularCourseCode };
		return args;
	}
	protected Object[] getCurricularCourseUnsuccessfullArguments() {
		Integer infoExecutionDegreeCode = new Integer(10);
		Integer infoCurricularCourseCode = new Integer(15);

		Object[] args = { infoExecutionDegreeCode, infoCurricularCourseCode };
		return args;
	}

	public void testReadCurrentCurriculumByCurricularCourseCodeSuccessfull() {
		try {

			Object[] args = getAuthorizeArguments();

			//Valid user
			String[] argsUser = getAuthenticatedAndAuthorizedUser();
			IUserView id = (IUserView) gestor.executar(null, "Autenticacao", argsUser);

			InfoCurriculum infoCurriculum = null;
			infoCurriculum = (InfoCurriculum) gestor.executar(id, getNameOfServiceToBeTested(), args);

			//read something?
			if (infoCurriculum == null) {
				fail("Reading a Curriculum.");
			}

			//Check information read is correct
			assertEquals(new String("objectivos gerais em portugues"), infoCurriculum.getGeneralObjectives());
			assertEquals(new String("objectivos gerais em ingles"), infoCurriculum.getGeneralObjectivesEn());
			assertEquals(new String("objectivos operacionais em portugues"), infoCurriculum.getOperacionalObjectives());
			assertEquals(new String("objectivos operacionais em ingles"), infoCurriculum.getOperacionalObjectivesEn());
			assertEquals(new String("programa"), infoCurriculum.getProgram());
			assertEquals(new String("programa em ingles"), infoCurriculum.getProgramEn());
			Calendar calendar = Calendar.getInstance();
			calendar.clear();
			calendar.set(2003, 10-1, 20, 0, 0, 0);
			assertEquals(calendar.getTime().getTime(), infoCurriculum.getLastModificationDate().getTime());

			assertEquals(infoCurriculum.getInfoCurricularCourse().getIdInternal(), new Integer(1));
			assertEquals(new Integer(infoCurriculum.getInfoCurricularCourse().getInfoScopes().size()), new Integer(1));
			assertEquals(new Integer(infoCurriculum.getInfoCurricularCourse().getInfoAssociatedExecutionCourses().size()), new Integer(0));

			System.out.println(
				"testReadCurrentCurriculumByCurricularCourseCodeSuccessfull was SUCCESSFULY runned by service: " + getNameOfServiceToBeTested());

		} catch (FenixServiceException e) {
			e.printStackTrace();
			fail("Reading a Curriculum from database " + e);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Reading a Curriculum from database " + e);
		}
	}

	public void testReadCurrentCurriculumByCurricularCourseCodeUserUnsuccessfull() {
		try {
			Object[] args = getAuthorizeArguments();

			//Invalid user
			String[] argsUser = getAuthenticatedAndUnauthorizedUser();
			IUserView id = (IUserView) gestor.executar(null, "Autenticacao", argsUser);

			gestor.executar(id, getNameOfServiceToBeTested(), args);

			fail("Reading Curriculum with invalid user");
		} catch (NotAuthorizedException e) {
			System.out.println(
				"testReadCurrentCurriculumByCurricularCourseCodeUserUnsuccessfull was SUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());
		} catch (FenixServiceException e) {
			fail("Reading Curriculum with invalid user " + e);
		} catch (Exception e) {
			fail("Reading Curriculum with invalid user " + e);
		}
	}

	public void testReadCurrentCurriculumByCurricularCourseCodeNoCurricularCourseUnsuccessfull() {
		try {
			//				Non existing curricular course
			Object[] args = getCurricularCourseUnsuccessfullArguments();

			//Valid user
			String[] argsUser2 = getAuthenticatedAndAuthorizedUser();
			IUserView id2 = (IUserView) gestor.executar(null, "Autenticacao", argsUser2);

			gestor.executar(id2, getNameOfServiceToBeTested(), args);

			fail("Reading Curriculum with non existent curricular course");
		} catch (NonExistingServiceException e) {
			System.out.println(
				"testReadCurrentCurriculumByCurricularCourseCodeNoCurricularCourseUnsuccessfull was SUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());
		} catch (FenixServiceException e) {
			fail("Reading Curriculum with non existent curricular course " + e);
		} catch (Exception e) {
			fail("Reading Curriculum with non existent curricular course " + e);
		}
	}

	public void testReadCurrentCurriculumByCurricularCourseCodeNullCurriculumUnsuccessfull() {
		try {
			//	existing curricular course
			Integer infoExecutionDegreeCode = new Integer(10);
			Integer infoCurricularCourseCode = new Integer(2);

			Object[] args = { infoExecutionDegreeCode, infoCurricularCourseCode };

			//Valid user
			String[] argsUser2 = getAuthenticatedAndAuthorizedUser();
			IUserView id2 = (IUserView) gestor.executar(null, "Autenticacao", argsUser2);

			InfoCurriculum infoCurriculum = null;
			infoCurriculum = (InfoCurriculum) gestor.executar(id2, getNameOfServiceToBeTested(), args);

			if (infoCurriculum == null) {
				fail("Reading Curriculum with non existing curriculum");
			} else {
				System.out.println("infoCurriculum nao é null.");
				assertEquals(infoCurriculum.getIdInternal(), new Integer(0));
//				assertNotNull(infoCurriculum.getInfoCurricularCourse());
//				assertEquals(infoCurriculum.getInfoCurricularCourse().getIdInternal(), infoCurricularCourseCode);
				
				
				System.out.println(
					"testReadCurrentCurriculumByCurricularCourseCodeNullCurriculumUnsuccessfull was SUCCESSFULY runned by service: "
						+ getNameOfServiceToBeTested());
			}
		} catch (FenixServiceException e) {
			fail("Reading Curriculum with non existing curriculum " + e);
		} catch (Exception e) {
			fail("Reading Curriculum with non existing curriculum " + e);
		}
	}
	public void testReadCurrentCurriculumByCurricularCourseCodeNullCodeUnsuccessfull() {
		try {
			// null argument
			Integer infoExecutionDegreeCode = new Integer(10);
			Object[] args = { infoExecutionDegreeCode,  null };

			//Valid user
			String[] argsUser2 = getAuthenticatedAndAuthorizedUser();
			IUserView id2 = (IUserView) gestor.executar(null, "Autenticacao", argsUser2);

			gestor.executar(id2, getNameOfServiceToBeTested(), args);

			fail("Reading Curriculum with null curricular course code");
		} catch (FenixServiceException e) {
			if (e.getMessage().equals("nullCurricularCourse")) {
				System.out.println(
					"testReadCurrentCurriculumByCurricularCourseCodeNullCodeUnsuccessfull was SUCCESSFULY runned by service: "
						+ getNameOfServiceToBeTested());
			} else {
				fail("Reading Curriculum with null curricular course code " + e);
			}
		} catch (Exception e) {
			fail("Reading Curriculum with null curricular course code " + e);
		}
	}
}