package middleware.almeida.dcsrjao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import Dominio.DisciplinaExecucao;
import Dominio.ICurricularCourse;
import Dominio.IDegreeCurricularPlan;
import Dominio.IDisciplinaExecucao;
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
	IExecutionPeriod executionPeriod = null;

	public CreatetExecutionCoursesForLEQCurricularCourses() {
	}

	public static void main(String[] args) {

		if (loader == null) {
			loader = new CreatetExecutionCoursesForLEQCurricularCourses();
		}

		loader.migrationStart("CreatetExecutionCoursesForLEQCurricularCourses");

		loader.setupDAO();
		IDegreeCurricularPlan degreeCurricularPlan = loader.processNewLEQDegreeCurricularPlan();
		loader.shutdownDAO();
		if (degreeCurricularPlan == null) {
			logString += error.toString();
			loader.migrationEnd("CreatetExecutionCoursesForLEQCurricularCourses", logString);
			return;
		}

		loader.setupDAO();
		loader.executionPeriod = loader.persistentObjectOJB.readActiveExecutionPeriod();
		loader.shutdownDAO();
		if (loader.executionPeriod == null) {
			errorMessage = "\n Erro ao ler o active execution period!";
			errorDBID = "";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
			logString += error.toString();
			loader.migrationEnd("CreatetExecutionCoursesForLEQCurricularCourses", logString);
			return;
		}

		loader.setupDAO();
		List leqCurricularCourses = loader.persistentObjectOJB.readAllLEQCurricularCourses(degreeCurricularPlan);
		loader.shutdownDAO();
		if (leqCurricularCourses == null) {
			errorMessage = "\n Erro ao ler as cadeiras do plano curricular LEQ - 2003!";
			errorDBID = "";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
			logString += error.toString();
			loader.migrationEnd("CreatetExecutionCoursesForLEQCurricularCourses", logString);
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
		loader.migrationEnd("CreatetExecutionCoursesForLEQCurricularCourses", logString);
	}

	private void processCurricularCourse(ICurricularCourse curricularCourse) {
		if (!curricularCourse.getName().startsWith("OPÇÃO")) {

			IDisciplinaExecucao executionCourse = loader.persistentObjectOJB.readExecutionCourseByUnique(curricularCourse.getCode(), this.executionPeriod);
			if (executionCourse != null) {
				errorMessage = "\n já existe o executionCourse com o codigo " + executionCourse.getSigla() + " e nome " + executionCourse.getNome() + "! Registos: ";
				errorDBID = curricularCourse.getIdInternal() + ",";
				error = loader.setErrorMessage(errorMessage, errorDBID, error);
				numberUntreatableElements++;
				return;
			} else {
				executionCourse = new DisciplinaExecucao();
				executionCourse.setExecutionPeriod(this.executionPeriod);
				executionCourse.setNome(curricularCourse.getName());
				executionCourse.setSigla(curricularCourse.getCode());
				List associatedCurricularCourses = new ArrayList();
				associatedCurricularCourses.add(curricularCourse);
				executionCourse.setAssociatedCurricularCourses(associatedCurricularCourses);
				writeElement(executionCourse);
			}
		}
	}

	private IDegreeCurricularPlan processNewLEQDegreeCurricularPlan() {
		IDegreeCurricularPlan degreeCurricularPlan = persistentObjectOJB.readDegreeCurricularPlanByName("LEQ-2003");
		if (degreeCurricularPlan == null) {
			errorMessage = "\n O plano curricular LEQ-2003 não existe!";
			errorDBID = "";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
			return null;
		}

		return degreeCurricularPlan;
	}

	protected String getFilenameOutput() {
		return "etc/migration/dcs-rjao/logs/CreatetExecutionCoursesForLEQCurricularCourses.txt";
	}

	protected String getClassName() {
		return "CreatetExecutionCoursesForLEQCurricularCourses";
	}
}