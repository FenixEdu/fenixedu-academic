package middleware.almeida.dcsrjao;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import middleware.almeida.LoadDataFile;
import Dominio.CurricularCourseScope;
import Dominio.DegreeCurricularPlan;
import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICurricularSemester;
import Dominio.ICurricularYear;
import Dominio.IDegreeCurricularPlan;
import Util.DegreeCurricularPlanState;

/**
 * @author dcs-rjao
 *
 * 21/Mai/2003
 */

public class LoadLEQScopes extends LoadDataFile {

	private static LoadLEQScopes loader = null;
	protected static String logString = "";
	protected static IDegreeCurricularPlan actualDegreeCurricularPlan = null;
	protected static IDegreeCurricularPlan newDegreeCurricularPlan = null;
	protected static List leqCCScopesList = null;

	public LoadLEQScopes() {
		super.setupDAO();
		actualDegreeCurricularPlan = super.persistentObjectOJB.readDegreeCurricularPlanByName("LEQ");
		newDegreeCurricularPlan = processDegreeCurricularPlan();
		leqCCScopesList = super.persistentObjectOJB.readAllLEQScopes();
		super.shutdownDAO();
	}

	public static void main(String[] args) {
		if(loader == null){
			loader = new LoadLEQScopes();
		}
		loader.run();
	}
	
	public void run(){
		if(loader == null){
			loader = new LoadLEQScopes();
		}
		loader.startTime = Calendar.getInstance();
		super.setupDAO();
		loader.processRegistry();
		super.shutdownDAO();
		loader.writeToFile(logString);
		loader.endTime = Calendar.getInstance();
		loader.report();	
	}

	protected void processRegistry() {

		Iterator iterator = leqCCScopesList.iterator();
		while (iterator.hasNext()) {
			Leq_cc_scope leq_cc_scope = (Leq_cc_scope) iterator.next();
			Leq_new_curricular_course curricularCourse = leq_cc_scope.getCurricularCourse();
			ICurricularCourse curricularCourse2 = persistentObjectOJB.readCurricularCourse(curricularCourse.getName(), newDegreeCurricularPlan.getIdInternal(), curricularCourse.getCode());
			Leq_curricular_semester curricularSemester = leq_cc_scope.getCurricularSemester();
			ICurricularYear curricularYear = persistentObjectOJB.readCurricularYear(new Integer("" + curricularSemester.getKey_curricular_year()));
			ICurricularSemester curricularSemester2 = persistentObjectOJB.readCurricularSemester(new Integer("" + curricularSemester.getSemester()), curricularYear);
			IBranch branch = persistentObjectOJB.readBranch(new Integer(1));
			ICurricularCourseScope curricularCourseScope = new CurricularCourseScope();
			curricularCourseScope.setBranch(branch);
			curricularCourseScope.setCurricularCourse(curricularCourse2);
			curricularCourseScope.setCurricularSemester(curricularSemester2);
			loader.writeElement(curricularCourseScope);
			numberLinesProcessed++;
		}
	}

	private IDegreeCurricularPlan processDegreeCurricularPlan() {
		IDegreeCurricularPlan degreeCurricularPlan = persistentObjectOJB.readDegreeCurricularPlanByName("LEQ-2004");
		if (degreeCurricularPlan == null) {
			degreeCurricularPlan = new DegreeCurricularPlan();
			degreeCurricularPlan.setName("LEQ-2004");
			degreeCurricularPlan.setDegree(persistentObjectOJB.readDegreeByCode("LEQ"));
			degreeCurricularPlan.setState(DegreeCurricularPlanState.NOT_ACTIVE_OBJ);
			writeElement(degreeCurricularPlan);
			logString += "O plano curricular: " + degreeCurricularPlan.getName() + " foi acrescentado \n";
		}
		return degreeCurricularPlan;
	}

	protected String getFilename() {
		return null;
	}

	protected String getFieldSeparator() {
		return "\t";
	}

	protected String getFilenameOutput() {
		return "etc/migration/dcs-rjao/logs/LEQCCScopesLog.txt";
	}

	protected void processLine(String line) {
	}
}