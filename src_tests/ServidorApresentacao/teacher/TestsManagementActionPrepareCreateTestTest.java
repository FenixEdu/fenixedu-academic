/*
 * Created on 18/Ago/2003
 *
 */
package ServidorApresentacao.teacher;

import java.util.Hashtable;
import java.util.Map;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoSite;
import ServidorApresentacao.TestCasePresentationTeacherPortal;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author Susana Fernandes
 */
public class TestsManagementActionPrepareCreateTestTest
	extends TestCasePresentationTeacherPortal {

	public TestsManagementActionPrepareCreateTestTest(String testName) {
		super(testName);
	}

	protected Map getItemsToPutInSessionForActionToBeTestedSuccessfuly() {
		Map result = new Hashtable();

		InfoExecutionYear infoExecutionYear =
			new InfoExecutionYear("2002/2003");
		InfoExecutionPeriod infoExecutionPeriod =
			new InfoExecutionPeriod("2º Semestre", infoExecutionYear);
		InfoExecutionCourse infoExecutionCourse =
			new InfoExecutionCourse(
				"Introducao a Programacao",
				"IP",
				"programa?",
				new Double(1.5),
				new Double(2),
				new Double(1.5),
				new Double(2),
				infoExecutionPeriod);
		InfoSite infoSite = new InfoSite(infoExecutionCourse);
		result.put(SessionConstants.INFO_SITE, infoSite);

		return result;
	}

	protected Map getItemsToPutInSessionForActionToBeTestedUnsuccessfuly() {
		return null;
	}

	protected Map getItemsToPutInRequestForActionToBeTestedSuccessfuly() {

		Map result = new Hashtable();
		Integer objectCode = new Integer(26);
		result.put("method", "prepareCreateTest");
		result.put("objectCode", objectCode.toString());
		return result;
	}

	protected Map getItemsToPutInRequestForActionToBeTestedUnsuccessfuly() {
		return null;
	}

	protected Map getExistingAttributesListToVerifyInSuccessfulExecution() {
		return null;
	}

	protected Map getNonExistingAttributesListToVerifyInSuccessfulExecution() {
		return null;
	}

	protected Map getExistingAttributesListToVerifyInUnsuccessfulExecution() {
		return null;
	}

	protected Map getNonExistingAttributesListToVerifyInUnsuccessfulExecution() {
		return null;
	}

	protected String getServletConfigFile() {
		return "/WEB-INF/web.xml";
	}

	protected String getRequestPathInfoPathAction() {
		return "/teacher";
	}

	protected String getRequestPathInfoNameAction() {
		return "/testsManagement";
	}

	protected String getSuccessfulForward() {
		return "createTest";
	}
}
