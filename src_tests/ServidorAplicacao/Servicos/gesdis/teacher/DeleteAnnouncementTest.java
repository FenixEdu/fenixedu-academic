/*
 * DeleteAnnouncementTest.java
 * 
 */

package ServidorAplicacao.Servicos.gesdis.teacher;

/**
 * @author Ivo Brandão
 */


import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import DataBeans.gesdis.InfoAnnouncement;
import DataBeans.gesdis.InfoSite;
import ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices;

public class DeleteAnnouncementTest extends TestCaseDeleteAndEditServices {

	public DeleteAnnouncementTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(DeleteAnnouncementTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	protected String getNameOfServiceToBeTested() {
		return "DeleteAnnouncement";
	}

	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

		Object argsDeleteAnnouncement[] = new Object[2];
//		(InfoSite infoSite, InfoAnnouncement infoAnnouncement)

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 2003);
		calendar.set(Calendar.MONTH, Calendar.JANUARY);
		calendar.set(Calendar.DATE, 21);
		Date date = calendar.getTime();

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
		InfoSite infoSite = new InfoSite(infoExecutionCourse);
		argsDeleteAnnouncement[0] = infoSite;

		//infoAnnouncement
		InfoAnnouncement infoAnnouncement = new InfoAnnouncement("announcement1",(Timestamp) date,(Timestamp) date, "information1", infoSite);
		argsDeleteAnnouncement[1] = infoAnnouncement;

		return argsDeleteAnnouncement;
	}

	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}

	protected HashMap getArgumentListOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}
}