package ServidorAplicacao.Servicos.coordinator;

import java.util.Calendar;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoDegreeCurricularPlan;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorAplicacao.Servicos.ServiceTestCase;
import Util.DegreeCurricularPlanState;
import Util.MarkType;

/**
 * @author Fernanda Quitério 05/Nov/2003
 */
public class ReadActiveDegreeCurricularPlanByExecutionDegreeCodeTest extends ServiceTestCase {

	/**
	 * @param testName
	 */

	public ReadActiveDegreeCurricularPlanByExecutionDegreeCodeTest(String testName) {
		super(testName);
	}

	protected String getApplication() {
		return Autenticacao.EXTRANET;
	}

	protected String getNameOfServiceToBeTested() {
		return "ReadActiveDegreeCurricularPlanByExecutionDegreeCode";
	}

	protected String getDataSetFilePath() {
		return "etc/datasets_templates/servicos/coordinator/testDataSetReadDegreeCurricularPlanByExecutionDegreeCode.xml";
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
		Integer infoExecutionDegreeCode = new Integer(10);

		Object[] args = { infoExecutionDegreeCode };
		return args;
	}
	protected Object[] getExecutionDegreeUnsuccessfullArguments() {
		Integer infoExecutionDegreeCode = new Integer(15);

		Object[] args = { infoExecutionDegreeCode };
		return args;
	}

	public void testReadActiveDegreeCurricularPlanByExecutionDegreeCodeSuccessfull() {
		try {
			Integer infoExecutionDegreeCode = new Integer(10);

			Object[] args = { infoExecutionDegreeCode };

			//Valid user
			String[] argsUser = getAuthenticatedAndAuthorizedUser();
			IUserView id = (IUserView) gestor.executar(null, "Autenticacao", argsUser);

			InfoDegreeCurricularPlan infoDegreeCurricularPlan = null;
			infoDegreeCurricularPlan = (InfoDegreeCurricularPlan) gestor.executar(id, getNameOfServiceToBeTested(), args);

			//read something?
			if (infoDegreeCurricularPlan == null) {
				fail("Reading a Degree Curricular Plan.");
			}

			//Check information read is correct
			assertEquals(new String("LEIC"), infoDegreeCurricularPlan.getName());
			Calendar calendar = Calendar.getInstance();
			calendar.clear();
			calendar.set(2000, 0, 1, 0, 0, 0);
			assertEquals(calendar.getTime().getTime(), infoDegreeCurricularPlan.getInitialDate().getTime());
			calendar.clear();
			calendar.set(2000, 11, 2, 0, 0, 0);
			assertEquals(calendar.getTime().getTime(), infoDegreeCurricularPlan.getEndDate().getTime());
			assertEquals(new MarkType(20), infoDegreeCurricularPlan.getMarkType());
			assertEquals(new Integer(0), infoDegreeCurricularPlan.getMinimalYearForOptionalCourses());
			assertEquals(new Double("0"), infoDegreeCurricularPlan.getNeededCredits());
			assertEquals(new Integer(0), infoDegreeCurricularPlan.getNumerusClausus());
			assertEquals(new DegreeCurricularPlanState(2), infoDegreeCurricularPlan.getState());

			assertEquals(new Integer(infoDegreeCurricularPlan.getCurricularCourses().size()), new Integer(1));

			InfoCurricularCourse infoCurricularCourse = (InfoCurricularCourse) infoDegreeCurricularPlan.getCurricularCourses().get(0);
			assertEquals(new Integer(infoCurricularCourse.getInfoScopes().size()), new Integer(1));
			InfoCurricularCourseScope infoCurricularCourseScope = (InfoCurricularCourseScope) infoCurricularCourse.getInfoScopes().get(0);
			assertEquals(infoCurricularCourseScope.getInfoBranch().getName(), "Ramo da Disciplina Curricular");
			assertEquals(infoCurricularCourseScope.getInfoCurricularCourse().getName(), "INTRODU??O ? PROGRAMA??O");
			assertEquals(infoCurricularCourseScope.getInfoCurricularSemester().getSemester(), new Integer("1"));
			assertEquals(infoCurricularCourseScope.getInfoCurricularSemester().getInfoCurricularYear().getYear(),new Integer(1));
			
			System.out.println(
				"testReadActiveDegreeCurricularPlanByExecutionDegreeCodeSuccessfull was SUCCESSFULY runned by service: " + getNameOfServiceToBeTested());

		} catch (FenixServiceException e) {
			e.printStackTrace();
			fail("Reading a degree Curricular Plan from database " + e);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Reading a degree Curricular Plan from database " + e);
		}
	}

	public void testReadActiveDegreeCurricularPlanByExecutionDegreeCodeUserUnsuccessfull() {
		try {
			Integer infoExecutionDegreeCode = new Integer(10);

			Object[] args = { infoExecutionDegreeCode };

			//Invalid user
			String[] argsUser = getAuthenticatedAndUnauthorizedUser();
			IUserView id = (IUserView) gestor.executar(null, "Autenticacao", argsUser);

			gestor.executar(id, getNameOfServiceToBeTested(), args);

			fail("Reading an active degree curricular plan with invalid user");
		} catch (NotAuthorizedException e) {
			System.out.println(
				"testReadActiveDegreeCurricularPlanByExecutionDegreeCodeUserUnsuccessfull was SUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());
		} catch (FenixServiceException e) {
			fail("Reading an active degree curricular plan with invalid user " + e);
		} catch (Exception e) {
			fail("Reading an active degree curricular plan with invalid user " + e);
		}
	}

	public void testReadActiveDegreeCurricularPlanByExecutionDegreeCodeNoDegreeUnsuccessfull() {
		try {
			//				Non existing execution degree
			Integer infoExecutionDegreeCode = new Integer(55);

			Object[] args = { infoExecutionDegreeCode };

			//Valid user
			String[] argsUser2 = getAuthenticatedAndAuthorizedUser();
			IUserView id2 = (IUserView) gestor.executar(null, "Autenticacao", argsUser2);

			gestor.executar(id2, getNameOfServiceToBeTested(), args);

			fail("Reading an active degree Curricular Plan with non existent execution degree");
		} catch (NonExistingServiceException e) {
			System.out.println(
				"testReadActiveDegreeCurricularPlanByExecutionDegreeCodeNoDegreeUnsuccessfull was SUCCESSFULY runned by service: "
					+ getNameOfServiceToBeTested());
		} catch (FenixServiceException e) {
			fail("Reading an active degree Curricular Plan with non existent execution degree " + e);
		} catch (Exception e) {
			fail("Reading an active degree Curricular Plan with non existent execution degree " + e);
		}
	}

	public void testReadActiveDegreeCurricularPlanByExecutionDegreeCodeNullDCPUnsuccessfull() {
		try {
			//	existing execution degree
			Integer infoExecutionDegreeCode = new Integer(17);

			Object[] args = { infoExecutionDegreeCode };

			//Valid user
			String[] argsUser2 = getAuthenticatedAndAuthorizedUser();
			IUserView id2 = (IUserView) gestor.executar(null, "Autenticacao", argsUser2);

			InfoDegreeCurricularPlan infoDegreeCurricularPlan =
				(InfoDegreeCurricularPlan) gestor.executar(id2, getNameOfServiceToBeTested(), args);

			if (infoDegreeCurricularPlan == null) {
				System.out.println(
					"testReadActiveDegreeCurricularPlanByExecutionDegreeCodeNullDCPUnsuccessfull was SUCCESSFULY runned by service: "
						+ getNameOfServiceToBeTested());
			} else {
				fail("Reading an active degree Curricular Plan with non existing degree curricular plan");
			}
		} catch (FenixServiceException e) {
			fail("Reading an active degree Curricular Plan with non existing degree curricular plan " + e);
		} catch (Exception e) {
			fail("Reading an active degree Curricular Plan with non existing degree curricular plan " + e);
		}
	}
	public void testReadActiveDegreeCurricularPlanByExecutionDegreeCodeNullDegreeUnsuccessfull() {
		try {
			// null argument
			Object[] args = { null };

			//Valid user
			String[] argsUser2 = getAuthenticatedAndAuthorizedUser();
			IUserView id2 = (IUserView) gestor.executar(null, "Autenticacao", argsUser2);

			gestor.executar(id2, getNameOfServiceToBeTested(), args);

			fail("Reading an active degree curricular plan with null execution degree code");
		} catch (FenixServiceException e) {
			if (e.getMessage().equals("nullDegree")) {
				System.out.println(
					"testReadActiveDegreeCurricularPlanByExecutionDegreeCodeNullDegreeUnsuccessfull was SUCCESSFULY runned by service: "
						+ getNameOfServiceToBeTested());
			} else {
				fail("Reading an active degree curricular plan with null execution degree code1 " + e);
			}
		} catch (Exception e) {
			fail("Reading an active degree curricular plan with null execution degree code3 " + e);
		}
	}
}