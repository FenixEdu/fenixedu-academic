package middleware.almeida.dcsrjao.velhos;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import middleware.almeida.LoadDataFile;
import Dominio.CurricularCourse;
import Dominio.DegreeCurricularPlan;
import Dominio.ICurricularCourse;
import Dominio.IDegreeCurricularPlan;
import Util.CurricularCourseExecutionScope;
import Util.CurricularCourseType;
import Util.DegreeCurricularPlanState;

/**
 * @author dcs-rjao
 *
 * 21/Mai/2003
 */

public class LoadLEQNewCurricularCourses extends LoadDataFile {

	private static LoadLEQNewCurricularCourses loader = null;
	protected static String logString = "";
	protected static IDegreeCurricularPlan degreeCurricularPlanActual = null;
	protected static IDegreeCurricularPlan degreeCurricularPlanNew = null;
	protected static List LEQNewCurricularCourseList = null;

	public LoadLEQNewCurricularCourses() {
		super.setupDAO();
		degreeCurricularPlanActual = super.persistentObjectOJB.readDegreeCurricularPlanByName("LEQ");
		degreeCurricularPlanNew = processDegreeCurricularPlan();
		LEQNewCurricularCourseList = super.persistentObjectOJB.readAllLEQNewCC();
		super.shutdownDAO();
	}

	public static void main(String[] args) {
		if(loader == null){
			loader = new LoadLEQNewCurricularCourses();
		}
		loader.run();
	}
	
	public void run(){
		if(loader == null){
			loader = new LoadLEQNewCurricularCourses();
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

		Iterator iterator = LEQNewCurricularCourseList.iterator();
		while (iterator.hasNext()) {
			Leq_new_curricular_course leq_new_curricular_course = (Leq_new_curricular_course) iterator.next();

			ICurricularCourse curricularCourseAux = persistentObjectOJB.readCurricularCourseByNameAndDegreeCurricularPlan(leq_new_curricular_course.getName(), degreeCurricularPlanActual);

			if (curricularCourseAux == null) {
				logString += "Nova\t" + leq_new_curricular_course.getName() + "\n";
			} else {
				logString += "Antiga\t" + leq_new_curricular_course.getName() + "\n";
			}

			curricularCourseAux = persistentObjectOJB.readCurricularCourseByCodeAndNameAndDegreeCurricularPlan(leq_new_curricular_course.getCode(), leq_new_curricular_course.getName(), degreeCurricularPlanNew);
			if (curricularCourseAux == null) {

				ICurricularCourse curricularCourse = new CurricularCourse();
				curricularCourse.setDegreeCurricularPlan(degreeCurricularPlanNew);
				curricularCourse.setName(leq_new_curricular_course.getName());
				curricularCourse.setCode(leq_new_curricular_course.getCode());

				int type = new Integer("" + leq_new_curricular_course.getType()).intValue();
				curricularCourse.setType(new CurricularCourseType(type));
				int executionScope = new Integer("" + leq_new_curricular_course.getExecution_scope()).intValue();
				curricularCourse.setCurricularCourseExecutionScope(new CurricularCourseExecutionScope(executionScope));

				if (leq_new_curricular_course.getMandatory() == 0) {
					curricularCourse.setMandatory(new Boolean(false));
				} else {
					curricularCourse.setMandatory(new Boolean(true));
				}

				writeElement(curricularCourse);
				//logString += "A disciplina: " + leq_new_curricular_course.getName() + " foi acrescentada no Plano Curricular Novo\n";
			} else {
				logString += "Já existe no plano novo\t" + leq_new_curricular_course.getName() + "\n";
			}
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
		return "etc/migration/dcs-rjao/logs/LEQNewCurricularCoursesMigrationLog.txt";
	}

	protected void processLine(String line) {
	}
}