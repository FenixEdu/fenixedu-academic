package middleware.almeida;

import java.util.Iterator;
import java.util.List;

import Dominio.CurricularCourse;
import Dominio.DegreeCurricularPlan;
import Dominio.ICurricularCourse;
import Dominio.IDegreeCurricularPlan;
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

	private LoadLEQNewCurricularCourses() {
		super.setupDAO();
		degreeCurricularPlanActual = super.persistentObjectOJB.readDegreeCurricularPlanByName("LEQ");
		degreeCurricularPlanNew = processDegreeCurricularPlan();
		LEQNewCurricularCourseList = super.persistentObjectOJB.readAllLEQNewCC();
		super.shutdownDAO();
	}

	public void main(String[] args) {
		loader = new LoadLEQNewCurricularCourses();

		processRegistry();
		loader.writeToFile(logString);
	}

	protected void processRegistry() {

		Iterator iterator = LEQNewCurricularCourseList.iterator();
		while (iterator.hasNext()) {
			Leq_new_curricular_course leq_new_curricular_course = (Leq_new_curricular_course) iterator.next();

			String code = leq_new_curricular_course.getCode();
			String name = leq_new_curricular_course.getName();

			ICurricularCourse curricularCourse = persistentObjectOJB.readCurricularCourseByCodeAndNameAndDegreeCurricularPlan(code, name, degreeCurricularPlanActual);

			if (curricularCourse == null) {
				curricularCourse = new CurricularCourse();
				curricularCourse.setDegreeCurricularPlan(degreeCurricularPlanNew);
				curricularCourse.setName(leq_new_curricular_course.getName());
				curricularCourse.setCode(leq_new_curricular_course.getCode());
				// TODO: David-Ricardo: Por o CCEnrolmentInfo
				writeElement(curricularCourse);
				logString += "A disciplina: " + leq_new_curricular_course.getName() + " foi acrescentada no Plano Curricular Novo\n";
			} else {
				logString += "A disciplina: " + leq_new_curricular_course.getName() + " já existe no Plano Curricular Actual\n";
				loader.numberUntreatableElements++;
			}
		}
	}

	private IDegreeCurricularPlan processDegreeCurricularPlan() {
		IDegreeCurricularPlan degreeCurricularPlan = persistentObjectOJB.readDegreeCurricularPlanByName("LEQ-2004");
		if (degreeCurricularPlan == null) {
			degreeCurricularPlan = new DegreeCurricularPlan();
			degreeCurricularPlan.setName("LEQ-2004");
			degreeCurricularPlan.setDegree(persistentObjectOJB.readDegreeByCode("LEQ"));
			degreeCurricularPlan.setState(DegreeCurricularPlanState.NOT_ACTIVE_OBJ);
//			TODO: David-Ricardo: Por o DCPEnrolmentInfo
			writeElement(degreeCurricularPlan);
			logString += "O plano curricular: " + degreeCurricularPlan.getName() + " foi acrescentado \n";
		}
		return degreeCurricularPlan;
	}

	protected String getFilename() {
		return "";
	}

	protected String getFieldSeparator() {
		return "\t";
	}

	protected String getFilenameOutput() {
		return "etc/migration/LEQNewCurricularCoursesMigrationLog.txt";
	}

	protected void processLine(String line) {
	}
}