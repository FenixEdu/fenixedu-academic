
/*
 * EditAnnouncementTest.java
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

public class EditAnnouncementTest extends TestCaseDeleteAndEditServices {

	public EditAnnouncementTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(EditAnnouncementTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}

	protected String getNameOfServiceToBeTested() {
		return "EditAnnouncement";
	}

	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

		Object argsEditAnnouncement[] = new Object[4];
//		(InfoSite infoSite, InfoAnnouncement infoAnnouncement, String announcementNewTitle, String announcementNewInformation)

		// write non existing
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
		argsEditAnnouncement[0] = infoSite;

		//infoAnnouncement
		InfoAnnouncement infoAnnouncement = new InfoAnnouncement("announcement1",(Timestamp) date,(Timestamp) date, "information1", infoSite);
		argsEditAnnouncement[1] = infoAnnouncement;

		//title
		argsEditAnnouncement[2] = "newAnnouncement";
		
		//information
		argsEditAnnouncement[3] = "newInformation";

		return argsEditAnnouncement;
	}

	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
		Object argsEditAnnouncement[] = new Object[4];
//		(InfoSite infoSite, InfoAnnouncement infoAnnouncement, String announcementNewTitle, String announcementNewInformation)

		// write non existing
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
		argsEditAnnouncement[0] = infoSite;

		//infoAnnouncement
		InfoAnnouncement infoAnnouncement = new InfoAnnouncement("unexistingAnnouncement",(Timestamp) date,(Timestamp) date, "information1", infoSite);
		argsEditAnnouncement[1] = infoAnnouncement;

		//title
		argsEditAnnouncement[2] = "newAnnouncement";
		
		//information
		argsEditAnnouncement[3] = "newInformation";

		return argsEditAnnouncement;
	}

	protected HashMap getArgumentListOfServiceToBeTestedUnsuccessfuly() {
		return null;
	}
}