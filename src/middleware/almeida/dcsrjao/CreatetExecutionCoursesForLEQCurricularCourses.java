package middleware.almeida.dcsrjao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import Util.CurricularCourseType;

import Dominio.DisciplinaExecucao;
import Dominio.ICurricularCourse;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;

/**
 *
 * @author  Luis Cruz & Sara Ribeiro
 */

public class CreatetExecutionCoursesForLEQCurricularCourses extends LoadDataToFenix {

	private static CreatetExecutionCoursesForLEQCurricularCourses loader = null;
	protected static String logString = "";
	private static HashMap error = new HashMap();
	private static String errorMessage = "";
	private static String errorDBID = "";
	List executionPeriodList = null;

	public CreatetExecutionCoursesForLEQCurricularCourses() {
	}

	public static void main(String[] args) {

		if (loader == null) {
			loader = new CreatetExecutionCoursesForLEQCurricularCourses();
		}

		loader.migrationStart(loader.getClassName());

		loader.setupDAO();
		IDegreeCurricularPlan newLEQdegreeCurricularPlan = loader.processNewLEQDegreeCurricularPlan();
		loader.shutdownDAO();
		if (newLEQdegreeCurricularPlan == null) {
			logString += error.toString();
			loader.migrationEnd(loader.getClassName(), logString);
			return;
		}

		loader.setupDAO();
		loader.executionPeriodList = loader.persistentObjectOJB.readAllExecutionPeriods();
		loader.shutdownDAO();
		if (loader.executionPeriodList == null) {
			errorMessage = "\n Erro ao ler os execution periods!";
			errorDBID = "";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
			logString += error.toString();
			loader.migrationEnd(loader.getClassName(), logString);
			return;
		}

		loader.setupDAO();
		List leqCurricularCourses = loader.persistentObjectOJB.readAllLEQCurricularCourses(newLEQdegreeCurricularPlan);
		loader.shutdownDAO();
		if (leqCurricularCourses == null) {
			errorMessage = "\n Erro ao ler as cadeiras do plano curricular " + newLEQdegreeCurricularPlan.getName() + "!";
			errorDBID = "";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
			logString += error.toString();
			loader.migrationEnd(loader.getClassName(), logString);
			return;
		}

		Iterator iterator = leqCurricularCourses.iterator();
		ICurricularCourse curricularCourse = null;
		while (iterator.hasNext()) {
			curricularCourse = (ICurricularCourse) iterator.next();
			loader.printIteration(loader.getClassName(), curricularCourse.getIdInternal().intValue());
			loader.setupDAO();
			loader.processCurricularCourse(curricularCourse);
			loader.shutdownDAO();
		}
		logString += error.toString();
		loader.migrationEnd(loader.getClassName(), logString);
	}

	private void processCurricularCourse(ICurricularCourse curricularCourse) {
		if (!curricularCourse.getType().equals(CurricularCourseType.OPTIONAL_COURSE_OBJ)) {

			Iterator iteratorExecutionPeriod = executionPeriodList.iterator();
			while (iteratorExecutionPeriod.hasNext()) {
				IExecutionPeriod executionPeriod = (IExecutionPeriod) iteratorExecutionPeriod.next();
				IExecutionCourse executionCourse =
					loader.persistentObjectOJB.readExecutionCourseByUnique(curricularCourse.getCode(), executionPeriod);
				if (executionCourse != null) {
					errorMessage =
						"\n já existe o executionCourse com o codigo " + executionCourse.getSigla() + " e nome " + executionCourse.getNome() + "! Registos: ";
					errorDBID = curricularCourse.getIdInternal() + ",";
					error = loader.setErrorMessage(errorMessage, errorDBID, error);
					numberUntreatableElements++;
					return;
				} else {
					executionCourse = new DisciplinaExecucao();
					executionCourse.setExecutionPeriod(executionPeriod);
					executionCourse.setNome(curricularCourse.getName());
					executionCourse.setSigla(curricularCourse.getCode());
					List associatedCurricularCourses = new ArrayList();
					associatedCurricularCourses.add(curricularCourse);
					executionCourse.setAssociatedCurricularCourses(associatedCurricularCourses);
					writeElement(executionCourse);
				}
			}
		}
	}

	private IDegreeCurricularPlan processNewLEQDegreeCurricularPlan() {
		IDegreeCurricularPlan degreeCurricularPlan = persistentObjectOJB.readDegreeCurricularPlanByName(InfoForMigration.NAME_OF_NEW_DEGREE_CURRICULAR_PLAN);
		if (degreeCurricularPlan == null) {
			errorMessage = "\n O plano curricular " + InfoForMigration.NAME_OF_NEW_DEGREE_CURRICULAR_PLAN + " não existe!";
			errorDBID = "";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
			return null;
		}

		return degreeCurricularPlan;
	}

	protected String getFilenameOutput() {
		return "etc/migration/dcs-rjao/logs/" + this.getClassName() + ".txt";
	}

	protected String getClassName() {
		return "CreatetExecutionCoursesForLEQCurricularCourses";
	}
}