package middleware.almeida.dcsrjao;

import java.util.Calendar;
import java.util.List;

import middleware.almeida.LoadDataFile;
import Dominio.IBranch;
import Dominio.IDegreeCurricularPlan;


/**
 * @author dcs-rjao
 *
 * 21/Mai/2003
 */

public class CreateLEQEquivalences extends LoadDataFile {
	
	private static CreateLEQEquivalences loader = null;
	private String logString = "";

	public CreateLEQEquivalences() {
	}

	public static void main(String[] args) {
	}

	public void run() {
		if (loader == null) {
			loader = new CreateLEQEquivalences();
		}
		loader.writeToFile(this.logString);
	}

	public void create() {
		System.out.println("Starting process of creation of equivalences.");
		super.startTime = Calendar.getInstance();
		super.setupDAO();
		this.processEachEnrolment();
		super.shutdownDAO();
		super.endTime = Calendar.getInstance();
		System.out.println("Done.");
		super.report();
	}

	private void processEachEnrolment() {
		IDegreeCurricularPlan oldDegreeCurricularPlan = persistentObjectOJB.readDegreeCurricularPlanByName("LEQ-OLD");
		IDegreeCurricularPlan newDegreeCurricularPlan = persistentObjectOJB.readDegreeCurricularPlanByName("LEQ-2003");
		IBranch branch = persistentObjectOJB.readBranch(new Integer(1));
		List studentCurricilarPlansList = persistentObjectOJB.readAllStudentCurricularPlansFromDegreeCurricularPlan(oldDegreeCurricularPlan);
	}

	protected String getFilenameOutput() {
		return "etc/migration/dcs-rjao/logs/enrolmentEquivalencesLog.txt";
	}

	protected void processLine(String line) {
	}

	protected String getFilename() {
		return null;
	}

	protected String getFieldSeparator() {
		return null;
	}
}