
/*
 * InseraAnnouncementTest.java
 * 
 */

package ServidorAplicacao.Servicos.gesdis.teacher;

/**
 *
 * @author tfc130
 */
import java.util.ArrayList;
import java.util.HashMap;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import DataBeans.gesdis.InfoSite;
import ServidorAplicacao.Servicos.TestCaseCreateServices;

public class InsertAnnouncementTest extends TestCaseCreateServices {

	public InsertAnnouncementTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(InsertAnnouncementTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	protected String getNameOfServiceToBeTested() {
		return "InsertAnnouncement";
	}

	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

		Object argsInsertAnnouncement[] = new Object[3];
//		(InfoSite infoSite, String newAnnouncementTitle, String newAnnouncementInformation)
		//infoSite
		InfoExecutionYear infoExecutionYear = new InfoExecutionYear("2002/2003");
		InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod("2º Semestre", infoExecutionYear);
		InfoExecutionCourse infoExecutionCourse =
			new InfoExecutionCourse(
				"Trabalho Final de Curso I",
				"TFCI",
				"programa1",
				new Double(1.5),
				new Double(2),
				new Double(1.5),
				new Double(2),
				infoExecutionPeriod);
		argsInsertAnnouncement[0] = new InfoSite(null, new ArrayList(), infoExecutionCourse);

		//title
		argsInsertAnnouncement[1] = "newAnnouncement";
		
		//information
		argsInsertAnnouncement[2] = "newInformation";
		
		return argsInsertAnnouncement;
	}

	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		
		return null;
	}

	protected HashMap getArgumentListOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}
}