package middleware.almeida.dcsrjao;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import middleware.almeida.Almeida_disc;
import Dominio.CurricularCourse;
import Dominio.CurricularCourseScope;
import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICurricularSemester;
import Dominio.ICurricularYear;
import Dominio.IDegreeCurricularPlan;
import Dominio.IUniversity;
import Util.CurricularCourseExecutionScope;
import Util.CurricularCourseType;
import Util.DegreeCurricularPlanState;

/**
 * @author dcs-rjao
 *
 * 21/Mai/2003
 */

public class LoadCurricularCoursesToFenix extends LoadDataToFenix {

	private static LoadCurricularCoursesToFenix loader = null;
	protected static String logString = "";
	private static HashMap error = new HashMap();
	private static String errorMessage = "";
	private static String errorDBID = "";
//	private IDegreeCurricularPlan newDegreeCurricularPlan = null;
//	private String nameOfNewDegreeCurricularPlan = "";
	
	public LoadCurricularCoursesToFenix() {
	}

	public static void main(String[] args) {
		if (loader == null) {
			loader = new LoadCurricularCoursesToFenix();
		}

		loader.migrationStart(loader.getClassName());

		loader.setupDAO();
		List almeida_curricularCourse = loader.persistentObjectOJB.readAllAlmeidaDisc();
		loader.shutdownDAO();
		Almeida_disc almeida_disc = null;
		Iterator iterator = almeida_curricularCourse.iterator();
		while (iterator.hasNext()) {
			almeida_disc = (Almeida_disc) iterator.next();
			loader.printIteration(loader.getClassName(), almeida_disc.getCodint());
			loader.setupDAO();
			loader.processCurricularCourse(almeida_disc);
			loader.shutdownDAO();
		}
		logString += error.toString();
		loader.migrationEnd(loader.getClassName(), logString);
	}

	public void processCurricularCourse(Almeida_disc almeida_disc) {

//		Integer keyDegree = new Integer("" + almeida_disc.getCodcur());
//		IDegreeCurricularPlan degreeCurricularPlan = persistentObjectOJB.readDegreeCurricularPlanByDegreeKeyAndState(keyDegree, DegreeCurricularPlanState.CONCLUDED_OBJ);
		IDegreeCurricularPlan degreeCurricularPlan = processOldDegreeCurricularPlan(almeida_disc);

		if (degreeCurricularPlan == null) {
//			errorMessage = "\n O plano curricular do curso [" + keyDegree + "] não existe! Registos: ";
//			errorDBID = almeida_disc.getCodint() + ",";
//			error = loader.setErrorMessage(errorMessage, errorDBID, error);
			loader.numberUntreatableElements++;
			return;
		}

		ICurricularCourse curricularCourse = persistentObjectOJB.readCurricularCourseByCodeAndNameAndDegreeCurricularPlan(almeida_disc.getCoddis(), almeida_disc.getNomedis(), degreeCurricularPlan);
		if (curricularCourse == null) {
			curricularCourse = new CurricularCourse();
			curricularCourse.setDegreeCurricularPlan(degreeCurricularPlan);
			curricularCourse.setName(almeida_disc.getNomedis());
			curricularCourse.setCode(almeida_disc.getCoddis());
			
			// nao ha informacao disponivel para deduzir este campo, por isso, todas sao as disciplinas não BASIC
			curricularCourse.setBasic(new Boolean(false));
			
			curricularCourse.setUniversity(processUniversity(almeida_disc));

			// nao ha informacao disponivel para deduzir este campo, por isso, todas sao as disciplinas sao semestrais
			curricularCourse.setCurricularCourseExecutionScope(CurricularCourseExecutionScope.SEMESTRIAL_OBJ);

			if (almeida_disc.getTipo() == 0) {
				curricularCourse.setMandatory(new Boolean(true));
				curricularCourse.setType(CurricularCourseType.NORMAL_COURSE_OBJ);
			} else if (almeida_disc.getTipo() == 1) {
				curricularCourse.setMandatory(new Boolean(false));
				//curricularCourse.setType(CurricularCourseType.OPTIONAL_COURSE_OBJ);
			} else {
				errorMessage = "\n O tipo " + almeida_disc.getTipo() + " é inválido! Registos: ";
				errorDBID = almeida_disc.getCodint() + ",";
				error = loader.setErrorMessage(errorMessage, errorDBID, error);
				loader.numberUntreatableElements++;
				return;
			}
			
			curricularCourse.setMaximumValueForAcumulatedEnrollments(new Integer(2));
			curricularCourse.setMinimumValueForAcumulatedEnrollments(new Integer(1));
			curricularCourse.setEnrollmentWeigth(new Integer(1));
			curricularCourse.setPraticalHours(new Double(almeida_disc.getPra()));
			curricularCourse.setTheoreticalHours(new Double(almeida_disc.getTeo()));
			curricularCourse.setLabHours(new Double(almeida_disc.getLab()));
			curricularCourse.setTheoPratHours(new Double(almeida_disc.getTeopra()));
			curricularCourse.setCredits(new Double(almeida_disc.getCredits()));
			
			writeElement(curricularCourse);
//			processCurricularCourseEquivalence(curricularCourse);
		}

		processCurricularCourseScope(curricularCourse, almeida_disc);
	}

	private void processCurricularCourseScope(ICurricularCourse curricularCourse, Almeida_disc almeida_disc) {
		IBranch branch = processBranch(almeida_disc);
		if (branch == null) {
			return;
		}

		ICurricularSemester curricularSemester = processCurricularSemester(almeida_disc);
		if (curricularSemester == null) {
			return;
		}

		//		Integer executionYear = new Integer("" + almeida_disc.getAnoLectivo());

		//		ICurricularCourseScope curricularCourseScope = persistentObjectOJB.readCurricularCourseScopeByUnique(curricularCourse, branch, curricularSemester, executionYear);
		ICurricularCourseScope curricularCourseScope = persistentObjectOJB.readCurricularCourseScopeByUnique(curricularCourse, branch, curricularSemester);
		if (curricularCourseScope == null) {
			curricularCourseScope = new CurricularCourseScope();
			curricularCourseScope.setBranch(branch);
			curricularCourseScope.setCurricularCourse(curricularCourse);
			curricularCourseScope.setCurricularSemester(curricularSemester);
			//			curricularCourseScope.setExecutionYear(executionYear);
			
			//		TODO: add sets for begin and end dates and ectsCredits
			writeElement(curricularCourseScope);
			loader.numberElementsWritten--;
		}
		//		else {
		//			errorMessage = "\n O scope com o " + "curricular course [" + curricularCourse.getCode() + "] branch [" + branch.getCode() + "] semester [" + curricularSemester.getSemester().intValue() + "] executionYear [" + executionYear + "] já existe! Registos: ";
		//			errorMessage = "\n O scope com o " + "curricular course [" + curricularCourse.getCode() + "] branch [" + branch.getCode() + "] semester [" + curricularSemester.getSemester().intValue() + "] já existe! Registos: ";
		//			errorDBID = almeida_disc.getCodint() + ",";
		//			error = loader.setErrorMessage(errorMessage, errorDBID, error);
		//		}
	}

	private ICurricularSemester processCurricularSemester(Almeida_disc almeida_disc) {
		Integer semester = new Integer("" + almeida_disc.getSemdis());
		Integer year = new Integer("" + almeida_disc.getAnodis());

		ICurricularYear curricularYear = persistentObjectOJB.readCurricularYearByYear(year);
		if (curricularYear == null) {
			errorMessage = "\n O curricularYear [" + year + "] não existe! Registos: ";
			errorDBID = almeida_disc.getCodint() + ",";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
			return null;
		}
		ICurricularSemester curricularSemester = persistentObjectOJB.readCurricularSemesterBySemesterAndCurricularYear(semester, curricularYear);
		if (curricularSemester == null) {
			errorMessage = "\n O curricularSemester [" + semester + "] não existe! Registos: ";
			errorDBID = almeida_disc.getCodint() + ",";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
			return null;
		}
		return curricularSemester;
	}

	private IBranch processBranch(Almeida_disc almeida_disc) {
		String code = "";

		if (almeida_disc.getCodram() != 0) {
			code = code + almeida_disc.getCodcur() + almeida_disc.getCodram() + almeida_disc.getOrientation();
		}

		Integer keyDegree = new Integer("" + almeida_disc.getCodcur());
		IDegreeCurricularPlan degreeCurricularPlan = persistentObjectOJB.readDegreeCurricularPlanByDegreeKeyAndState(keyDegree, DegreeCurricularPlanState.CONCLUDED_OBJ);
		if (degreeCurricularPlan == null) {
			errorMessage = "\n O plano curricular do curso [" + keyDegree + "] não existe! Registos: ";
			errorDBID = almeida_disc.getCodint() + ",";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
			return null;
		}

		IBranch branch = persistentObjectOJB.readBranchByUnique(code, degreeCurricularPlan);
		if (branch == null) {
			errorMessage = "\n O ramo com o code [" + code + "] e plano curricular [" + degreeCurricularPlan.getName() + "] não existe! Registos: ";
			errorDBID = almeida_disc.getCodint() + ",";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
			return null;
		}
		return branch;
	}

//	private IDegreeCurricularPlan processNewDegreeCurricularPlan() {
//		IDegreeCurricularPlan degreeCurricularPlan = persistentObjectOJB.readDegreeCurricularPlanByName(InfoForMigration.NAME_OF_NEW_DEGREE_CURRICULAR_PLAN);
//		if (degreeCurricularPlan == null) {
//			errorMessage = "\n O plano curricular " + InfoForMigration.NAME_OF_NEW_DEGREE_CURRICULAR_PLAN + " não existe!";
//			errorDBID = "";
//			error = loader.setErrorMessage(errorMessage, errorDBID, error);
//			return null;
//		}
//
//		return degreeCurricularPlan;
//	}

	private IDegreeCurricularPlan processOldDegreeCurricularPlan(Almeida_disc almeida_disc) {
		Integer keyDegree;
		try {
			keyDegree = new Integer("" + almeida_disc.getCodcur());
		} catch (NumberFormatException e) {
			errorMessage = "\n O curso " + almeida_disc.getCodcur() + "é inválido! Registos: ";
			errorDBID = almeida_disc.getCodint() + ", ";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
			return null;
		}

		IDegreeCurricularPlan degreeCurricularPlan = persistentObjectOJB.readDegreeCurricularPlanByDegreeKeyAndState(keyDegree, DegreeCurricularPlanState.CONCLUDED_OBJ);
		if (degreeCurricularPlan == null) {
			errorMessage = "\n O degree Curricular Plan do curso " + almeida_disc.getCodcur() + " não existe! Registos: ";
			errorDBID = almeida_disc.getCodint() + ", ";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
			return null;
		}

		return degreeCurricularPlan;
	}
	
	private IUniversity processUniversity(Almeida_disc almeida_disc) {
		String universityCodeRead = almeida_disc.getUniversidade();
		if(universityCodeRead == null){
			return null;
		}
		IUniversity universityCode = persistentObjectOJB.readUniversityByCode(universityCodeRead);
		if (universityCode == null) {
			errorMessage = "\n Não existe a universidade com o code [" + universityCodeRead + "]. Registos: ";
			errorDBID = almeida_disc.getCodint() + ", ";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
			return null;
		}
		return universityCode;
	}

	protected String getFilenameOutput() {
		return "etc/migration/dcs-rjao/logs/" + this.getClassName() + ".txt";
	}

	protected String getClassName() {
		return "LoadCurricularCoursesToFenix";
	}
}