package middleware.almeida.dcsrjao;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import Dominio.Enrolment;
import Dominio.EnrolmentEvaluation;
import Dominio.ExecutionPeriod;
import Dominio.ExecutionYear;
import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICurricularSemester;
import Dominio.ICurricularYear;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IPessoa;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.IUniversityCode;
import Dominio.StudentCurricularPlan;
import Dominio.UniversityCode;
import Util.DegreeCurricularPlanState;
import Util.EnrolmentEvaluationState;
import Util.EnrolmentEvaluationType;
import Util.EnrolmentState;
import Util.PeriodState;
import Util.StudentCurricularPlanState;
import Util.TipoCurso;

/**
 * @author dcs-rjao
 *
 * 21/Mai/2003
 */

public class LoadLEQEnrolmentsToFenix extends LoadDataToFenix {

	private static LoadLEQEnrolmentsToFenix loader = null;
	private static String logString = "";
	private static HashMap error = new HashMap();
	private static String errorMessage = "";
	private static String errorDBID = "";

	private IDegreeCurricularPlan oldDegreeCurricularPlan = null;
	private IDegreeCurricularPlan newLEQDegreeCurricularPlan = null;
	private IBranch oldBranch = null;
	private IBranch newLEQBranch = null;
	private ICurricularCourse curricularCourse = null;
	private ICurricularSemester curricularSemester = null;
	private IStudentCurricularPlan oldStudentCurricularPlan = null;
	private IStudentCurricularPlan newLEQStudentCurricularPlan = null;
	private IExecutionPeriod executionPeriod = null;
	private ICurricularCourseScope curricularCourseScope = null;
	private EnrolmentEvaluationType enrolmentEvaluationType = null;
	private EnrolmentState enrolmentState = null;

	public LoadLEQEnrolmentsToFenix() {
	}

	public static void main(String[] args) {

		if (loader == null) {
			loader = new LoadLEQEnrolmentsToFenix();
		}

		loader.migrationStart("LoadLEQEnrolmentsToFenix");
		loader.setupDAO();
		List almeidaEnrolmentsList = loader.persistentObjectOJB.readAllAlmeidaEnrolments();
		loader.shutdownDAO();

		Iterator iterator = almeidaEnrolmentsList.iterator();
		while (iterator.hasNext()) {
			Almeida_enrolment almeida_enrolment = (Almeida_enrolment) iterator.next();
			loader.printIteration(almeida_enrolment.getId_internal());
			loader.setupDAO();
			loader.processEnrolment(almeida_enrolment);
			loader.shutdownDAO();
		}

		logString += error.toString();
		loader.migrationEnd("LoadLEQEnrolmentsToFenix", logString);
	}

	private void processEnrolment(Almeida_enrolment almeida_enrolment) {

		this.oldDegreeCurricularPlan = processOldDegreeCurricularPlan(almeida_enrolment);
		if (this.oldDegreeCurricularPlan == null) {
			loader.numberUntreatableElements++;
			return;
		}

		this.newLEQDegreeCurricularPlan = processNewLEQDegreeCurricularPlan(almeida_enrolment);
		if (this.newLEQDegreeCurricularPlan == null) {
			loader.numberUntreatableElements++;
			return;
		}

		this.oldBranch = processOldBranch(almeida_enrolment);
		if (this.oldBranch == null) {
			loader.numberUntreatableElements++;
			return;
		}

		this.newLEQBranch = processNewLEQBranch(almeida_enrolment);
		if (this.newLEQBranch == null) {
			loader.numberUntreatableElements++;
			return;
		}

		this.curricularCourse = processCurricularCourse(almeida_enrolment);
		if (this.curricularCourse == null) {
			loader.numberUntreatableElements++;
			return;
		}
		this.curricularSemester = processCurricularSemester(almeida_enrolment);
		if (this.curricularSemester == null) {
			loader.numberUntreatableElements++;
			return;
		}

		this.curricularCourseScope = processCurricularCourseScope(almeida_enrolment);
		if (this.curricularCourseScope == null) {
			loader.numberUntreatableElements++;
			return;
		}

		this.oldStudentCurricularPlan = processOldStudentCurricularPlan(almeida_enrolment);
		if (this.oldStudentCurricularPlan == null) {
			loader.numberUntreatableElements++;
			return;
		}

		this.newLEQStudentCurricularPlan = processNewLEQStudentCurricularPlan(almeida_enrolment);
		if (this.newLEQStudentCurricularPlan == null) {
			loader.numberUntreatableElements++;
			return;
		}

		this.executionPeriod = processExecutionPeriod(almeida_enrolment.getAnoins(), almeida_enrolment.getSemdis());
		if (this.executionPeriod == null) {
			loader.numberUntreatableElements++;
			return;
		}

		this.enrolmentEvaluationType = processEvaluationType(almeida_enrolment);
		if (this.enrolmentEvaluationType == null) {
			loader.numberUntreatableElements++;
			return;
		}

		this.enrolmentState = processGrade(almeida_enrolment);
		if (this.enrolmentState == null) {
			loader.numberUntreatableElements++;
			return;
		}

		IEnrolment enrolment = persistentObjectOJB.readEnrolmentByStudentCurricularPlanAndScope(this.oldStudentCurricularPlan, this.curricularCourseScope);

		if (enrolment == null) {
			enrolment = processNewEnrolment();
		} else {
			//	Quando a inscricao é anulada é criado um novo enrolment, 
			//	uma melhoria ou rectificacao é o mesmo enrolment mas com um novo evaluation e um estado novo
			//  quando se chumba tem de ver se o chumbo foi no mesmo periodo que a inscrição actual ou noutro
			if ((enrolment.getEnrolmentState().equals(EnrolmentState.ANNULED_OBJ)) || ((enrolment.getEnrolmentState().equals(EnrolmentState.NOT_APROVED_OBJ)) && (!enrolment.getExecutionPeriod().equals(this.executionPeriod)))) {
				enrolment = processNewEnrolment();
			} else {
				enrolment = updateEnrolmentState(enrolment, almeida_enrolment);
			}
		}

		//	Caso em que foi lançada uma nota e por isso cria-se uma avaliação.
		if (!this.enrolmentState.equals(EnrolmentState.NOT_EVALUATED_OBJ)) {
			processEnrolmentEvaluation(enrolment, almeida_enrolment);
		}
	}

	private IEnrolment updateEnrolmentState(IEnrolment enrolment, Almeida_enrolment almeida_enrolment) {
		if ((this.enrolmentState.equals(EnrolmentState.ANNULED_OBJ)) || (this.enrolmentState.equals(EnrolmentState.APROVED_OBJ)) || ((this.enrolmentState.equals(EnrolmentState.NOT_APROVED_OBJ)) && (enrolment.getEnrolmentState().equals(EnrolmentState.NOT_EVALUATED_OBJ)))) {
			enrolment.setEnrolmentState(this.enrolmentState);
		}

		if (almeida_enrolment.getObserv() != null) {
			// todas as rectificações são para notas positivas
			if (almeida_enrolment.getObserv().equals("RECTIFICAÇÃO")) {
				enrolment.setEnrolmentState(EnrolmentState.APROVED_OBJ);
			}
		}
		return enrolment;
	}

	private IEnrolment processNewEnrolment() {
		IEnrolment enrolment = new Enrolment();
		enrolment.setCurricularCourseScope(this.curricularCourseScope);
		enrolment.setExecutionPeriod(this.executionPeriod);
		enrolment.setStudentCurricularPlan(this.oldStudentCurricularPlan);
		enrolment.setEnrolmentEvaluationType(EnrolmentEvaluationType.CLOSED_OBJ);
		enrolment.setEnrolmentState(this.enrolmentState);
		writeElement(enrolment);
		return enrolment;
	}

	private IBranch processNewLEQBranch(Almeida_enrolment almeida_enrolment) { // no novo curriculo da LEW não há branches
		String code = "";
		IBranch branch = persistentObjectOJB.readBranchByUnique(code, this.newLEQDegreeCurricularPlan);
		if (branch == null) {
			errorMessage = "\n Não existe o ramo com o code [" + code + "] e plano curricular = " + this.newLEQDegreeCurricularPlan.getName() + ". Registos: ";
			errorDBID = almeida_enrolment.getId_internal() + ",";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
		}
		return branch;
	}

	private IDegreeCurricularPlan processNewLEQDegreeCurricularPlan(Almeida_enrolment almeida_enrolment) {
		IDegreeCurricularPlan degreeCurricularPlan = persistentObjectOJB.readDegreeCurricularPlanByName("LEQ-2003");
		if (degreeCurricularPlan == null) {
			errorMessage = "\n O plano curricular LEQ-2003 não existe! Registos: ";
			errorDBID = almeida_enrolment.getId_internal() + ",";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
		}

		return degreeCurricularPlan;
	}

	private IStudentCurricularPlan processNewLEQStudentCurricularPlan(Almeida_enrolment almeida_enrolment) {
		IStudentCurricularPlan studentCurricularPlan = null;
		Integer studentNumber = null;
		try {
			studentNumber = new Integer(almeida_enrolment.getNumalu());
		} catch (NumberFormatException e) {
			errorMessage = "\n O numero de aluno " + almeida_enrolment.getNumalu() + " é inválido! Registos: ";
			errorDBID = almeida_enrolment.getId_internal() + ",";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
			return null;
		}
		IStudent student = persistentObjectOJB.readStudent(studentNumber, TipoCurso.LICENCIATURA_OBJ);
		if (student != null) {
			StudentCurricularPlanState studentCurricularPlanState = StudentCurricularPlanState.ACTIVE_OBJ;
			int newYear = new Integer(2003).intValue();
			Calendar newCalendar = Calendar.getInstance();
			newCalendar.set(newYear, Calendar.SEPTEMBER, 1);
			Date newDate = newCalendar.getTime();
			studentCurricularPlan = persistentObjectOJB.readStudentCurricularPlanByUnique(student, this.newLEQDegreeCurricularPlan, this.newLEQBranch, studentCurricularPlanState);
			if (studentCurricularPlan == null) {
				studentCurricularPlan = new StudentCurricularPlan();
				studentCurricularPlan.setBranch(this.newLEQBranch);
				studentCurricularPlan.setCurrentState(studentCurricularPlanState);
				studentCurricularPlan.setDegreeCurricularPlan(this.newLEQDegreeCurricularPlan);
				studentCurricularPlan.setStudent(student);
				studentCurricularPlan.setStartDate(newDate);
				writeElement(studentCurricularPlan);
				loader.numberElementsWritten--;
			}
		} else {
			errorMessage = "\n O aluno numero " + almeida_enrolment.getNumalu() + " não está inscrito este semestre! Registos: ";
			errorDBID = almeida_enrolment.getId_internal() + ",";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
		}

		return studentCurricularPlan;
	}

	private EnrolmentState processGrade(Almeida_enrolment almeida_enrolment) {
		String grade = almeida_enrolment.getResult();
		if (almeida_enrolment.getObserv() != null) {
			if (almeida_enrolment.getObserv().equals("ANULADO")) {
				return EnrolmentState.ANNULED_OBJ;
			}
		}

		if (grade == null) {
			return EnrolmentState.NOT_EVALUATED_OBJ;
		}

		if (grade.equals("RE")) {
			return EnrolmentState.NOT_APROVED_OBJ;
		}

		int intGrade;
		try {
			intGrade = new Integer(grade).intValue();
		} catch (NumberFormatException e) {
			errorMessage = "\n A nota " + grade + " é inválida! Registos: ";
			errorDBID = almeida_enrolment.getId_internal() + ",";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
			return null;
		}

		if ((intGrade > 20) || (intGrade < 0)) {
			errorMessage = "\n A nota " + intGrade + " é inválida! Registos: ";
			errorDBID = almeida_enrolment.getId_internal() + ",";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
			return null;
		} else if (intGrade < 10) {
			return EnrolmentState.NOT_APROVED_OBJ;
		}

		return EnrolmentState.APROVED_OBJ;
	}

	private void processEnrolmentEvaluation(IEnrolment enrolment, Almeida_enrolment almeida_enrolment) {

		IEnrolmentEvaluation enrolmentEvaluation = loader.persistentObjectOJB.readEnrolmentEvaluationByUnique(enrolment, this.enrolmentEvaluationType, almeida_enrolment.getResult());
		if (enrolmentEvaluation != null) {
			errorMessage = "\n EnrolmentEvaluation repetido: enrolment = " + enrolment.getIdInternal() + " evaluation = " + this.enrolmentEvaluationType.toString() + " result = " + almeida_enrolment.getResult() + ". Registos: ";
			errorDBID = almeida_enrolment.getId_internal() + ",";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
			return;
		}

		enrolmentEvaluation = new EnrolmentEvaluation();
		enrolmentEvaluation.setEnrolmentEvaluationType(this.enrolmentEvaluationType);
		enrolmentEvaluation.setEnrolment(enrolment);
		enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.FINAL_OBJ); //		}

		if (almeida_enrolment.getDatexa() != null) {
			Calendar newCalendar = Calendar.getInstance();
			int year = 0;
			int month = 0;
			int day = 0;
			try {
				year = new Integer(almeida_enrolment.getDatexa().substring(0, 3)).intValue();
				month = new Integer(almeida_enrolment.getDatexa().substring(5, 6)).intValue();
				day = new Integer(almeida_enrolment.getDatexa().substring(8, 9)).intValue();
			} catch (NumberFormatException e) {
				errorMessage = "\n A data de exame " + almeida_enrolment.getDatexa() + " é inválida! Registos: ";
				errorDBID = almeida_enrolment.getId_internal() + ",";
				error = loader.setErrorMessage(errorMessage, errorDBID, error);
			}
			newCalendar.set(year, month, day);
			Date examDate = newCalendar.getTime();
			enrolmentEvaluation.setExamDate(examDate);
		} else {
			enrolmentEvaluation.setExamDate(null);
		}

		enrolmentEvaluation.setGrade(almeida_enrolment.getResult());
		enrolmentEvaluation.setGradeAvailableDate(null);
		enrolmentEvaluation.setEmployee(null);
		enrolmentEvaluation.setObservation(null);
		if (almeida_enrolment.getNumdoc() != null) {
			Integer numDoc = null;
			try {
				numDoc = new Integer(almeida_enrolment.getNumdoc());
				IPessoa pessoa = persistentObjectOJB.readPersonByEmployeeNumber(numDoc);
				if (pessoa == null) {
					errorMessage = "\n Pessoa inexistente: " + almeida_enrolment.getNumdoc() + ". Registos: ";
					errorDBID = almeida_enrolment.getId_internal() + ",";
					error = loader.setErrorMessage(errorMessage, errorDBID, error);
				}
				enrolmentEvaluation.setPersonResponsibleForGrade(pessoa);
			} catch (NumberFormatException e) {
				errorMessage = "\n O numero de identificação " + almeida_enrolment.getNumdoc() + " é inválido! Registos: ";
				errorDBID = almeida_enrolment.getId_internal() + ",";
				error = loader.setErrorMessage(errorMessage, errorDBID, error);
			}
		} else {
			enrolmentEvaluation.setPersonResponsibleForGrade(null);
		}
		//	TODO DAVID-RICARDO: Quando o algoritmo do checksum estiver feito tem de ser actualizar este campo
		enrolmentEvaluation.setCheckSum(null);
		writeElement(enrolmentEvaluation);
		loader.numberElementsWritten--;
	}

	private ICurricularSemester processCurricularSemester(Almeida_enrolment almeida_enrolment) {
		Integer year;
		Integer semester;

		semester = new Integer("" + almeida_enrolment.getSemdis());
		year = new Integer("" + almeida_enrolment.getAnodis());

		ICurricularYear curricularYear = persistentObjectOJB.readCurricularYear(year);
		if (curricularYear == null) {
			errorMessage = "\n Não existe o curricularYear = " + year + ". Registos: ";
			errorDBID = almeida_enrolment.getId_internal() + ",";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
			return null;
		}
		ICurricularSemester curricularSemester = persistentObjectOJB.readCurricularSemester(semester, curricularYear);
		if (curricularSemester == null) {
			errorMessage = "\n Não existe o curricularSemester = " + semester + ". Registos: ";
			errorDBID = almeida_enrolment.getId_internal() + ",";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
		}
		return curricularSemester;
	}

	private ICurricularCourseScope processCurricularCourseScope(Almeida_enrolment almeida_enrolment) {

		Integer anoInscricao;
		try {
			anoInscricao = new Integer("" + almeida_enrolment.getAnoins());
		} catch (NumberFormatException e) {
			errorMessage = "\n O ano " + almeida_enrolment.getAnoins() + " é inválido! Registos: ";
			errorDBID = almeida_enrolment.getId_internal() + ",";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
			return null;
		}
		ICurricularCourseScope curricularCourseScope = persistentObjectOJB.readCurricularCourseScopeByUnique(this.curricularCourse, this.oldBranch, this.curricularSemester, anoInscricao);
		if (curricularCourseScope == null) {
			errorMessage = "\n Não existe o curricularCourseScope no course = " + this.curricularCourse.getCode() + "[" + this.curricularCourse.getIdInternal() + "]" + " Semestre = " + this.curricularSemester.getSemester().intValue() + " Ano = " + this.curricularSemester.getCurricularYear().getYear().intValue() + " Ano Inscricao = " + almeida_enrolment.getAnoins() + " Ramo = " + this.oldBranch.getCode() + "[" + this.oldBranch.getInternalID() + "]" + ". Registos: ";
			errorDBID = almeida_enrolment.getId_internal() + ",";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
		}
		return curricularCourseScope;
	}

	private ICurricularCourse processCurricularCourse(Almeida_enrolment almeida_enrolment) {

		ICurricularCourse curricularCourse = persistentObjectOJB.readCurricularCourseByCodeAndDegreeCurricularPlan(almeida_enrolment.getCoddis(), this.oldDegreeCurricularPlan);
		if (curricularCourse == null) {
			errorMessage = "\n Não existe o curricularCourse com o code = " + almeida_enrolment.getCoddis() + " do plano curricular =  " + this.oldDegreeCurricularPlan.getName() + "[" + this.oldDegreeCurricularPlan.getIdInternal() + "]" + ". Registos: ";
			errorDBID = almeida_enrolment.getId_internal() + ",";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
			return null;
		}

		if (curricularCourse.getUniversityCode() == null) {
			curricularCourse.setUniversityCode(processUniversityCode(almeida_enrolment.getCoduniv()));
			writeElement(curricularCourse);
			loader.numberElementsWritten--;
		}

		return curricularCourse;
	}

	private IBranch processOldBranch(Almeida_enrolment almeida_enrolment) {
		String code = "";

		if (almeida_enrolment.getRamo() != 0) {
			//	FIXME DAVID-RICARDO: a orientacao esta a 0, pq ainda nao ha informacao sobre ela no ficheiro de inscricoes
			code = code + almeida_enrolment.getCurso() + almeida_enrolment.getRamo() + "0";
		}

		IBranch branch = persistentObjectOJB.readBranchByUnique(code, this.oldDegreeCurricularPlan);
		if (branch == null) {
			errorMessage = "\n Não existe o ramo com o code [" + code + "] e plano curricular = " + this.oldDegreeCurricularPlan.getName() + ". Registos: ";
			errorDBID = almeida_enrolment.getId_internal() + ",";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
		}
		return branch;
	}

	private IUniversityCode processUniversityCode(String universityCodeRead) {
		IUniversityCode universityCode = persistentObjectOJB.readUniversityCodeByCode(universityCodeRead);
		if (universityCode == null) {
			universityCode = new UniversityCode();
			universityCode.setUniversityCode(universityCodeRead);
			writeElement(universityCode);
			loader.numberElementsWritten--;
		}
		return universityCode;
	}

	private EnrolmentEvaluationType processEvaluationType(Almeida_enrolment almeida_enrolment) {
		EnrolmentEvaluationType enrolmentEvaluationType = null;
		int epoca;
		try {
			epoca = (new Integer("" + almeida_enrolment.getEpoca())).intValue();
		} catch (NumberFormatException e) {
			errorMessage = "\n A época " + almeida_enrolment.getEpoca() + " é inválida! Registos: ";
			errorDBID = almeida_enrolment.getId_internal() + ",";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
			return null;
		}
		switch (epoca) {
			case 1 :
				enrolmentEvaluationType = EnrolmentEvaluationType.FIRST_SEASON_OBJ;
				break;
			case 2 :
				enrolmentEvaluationType = EnrolmentEvaluationType.SECOND_SEASON_OBJ;
				break;
			case 3 :
				enrolmentEvaluationType = EnrolmentEvaluationType.SPECIAL_SEASON_OBJ;
				break;
			case 4 :
				enrolmentEvaluationType = EnrolmentEvaluationType.IMPROVEMENT_OBJ;
				break;
			case 5 :
				enrolmentEvaluationType = EnrolmentEvaluationType.EXTERNAL_OBJ;
				break;
			default :
				errorMessage = "\n A epoca " + epoca + " é invalida" + ". Registos: ";
				errorDBID = almeida_enrolment.getId_internal() + ",";
				error = loader.setErrorMessage(errorMessage, errorDBID, error);
				break;
		}

		return enrolmentEvaluationType;
	}

	private IExecutionPeriod processExecutionPeriod(long yearLong, long semesterLong) {

		Integer semester = new Integer("" + semesterLong);
		Integer year = new Integer("" + yearLong);
		String yearStr = new String((year.intValue() - 1) + "/" + year.intValue());
		IExecutionYear executionYear = persistentObjectOJB.readExecutionYearByYear(yearStr);
		if (executionYear == null) {
			executionYear = new ExecutionYear();
			executionYear.setState(PeriodState.CLOSED);
			executionYear.setYear(yearStr);
			writeElement(executionYear);
			loader.numberElementsWritten--;
		}

		IExecutionPeriod executionPeriod = persistentObjectOJB.readExecutionPeriodByYearAndSemester(executionYear, semester);
		if (executionPeriod == null) {
			executionPeriod = new ExecutionPeriod();
			executionPeriod.setExecutionYear(executionYear);
			executionPeriod.setSemester(semester);
			executionPeriod.setState(PeriodState.CLOSED);
			String name = "";
			if (semester.intValue() == 1) {
				name = "1º Semestre";
			} else if (semester.intValue() == 2) {
				name = "2º Semestre";
			}
			executionPeriod.setName(name);
			writeElement(executionPeriod);
			loader.numberElementsWritten--;
		}

		return executionPeriod;
	}

	private IStudentCurricularPlan processOldStudentCurricularPlan(Almeida_enrolment almeida_enrolment) {

		IStudentCurricularPlan studentCurricularPlan = null;
		Integer studentNumber;
		try {
			studentNumber = new Integer(almeida_enrolment.getNumalu());
		} catch (NumberFormatException e) {
			errorMessage = "\n O numero de estudante " + almeida_enrolment.getNumalu() + " é inválido! Registos: ";
			errorDBID = almeida_enrolment.getId_internal() + ",";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
			return null;
		}
		IStudent student = persistentObjectOJB.readStudent(studentNumber, TipoCurso.LICENCIATURA_OBJ);
		if (student != null) {

			StudentCurricularPlanState studentCurricularPlanState = StudentCurricularPlanState.INCOMPLETE_OBJ;
			int newYear = new Integer("" + almeida_enrolment.getAnoins()).intValue();
			Calendar newCalendar = Calendar.getInstance();
			newCalendar.set(newYear, Calendar.SEPTEMBER, 1);
			Date newDate = newCalendar.getTime();
			studentCurricularPlan = persistentObjectOJB.readStudentCurricularPlanByUnique(student, this.oldDegreeCurricularPlan, this.oldBranch, studentCurricularPlanState);
			if (studentCurricularPlan == null) {
				studentCurricularPlan = new StudentCurricularPlan();
				studentCurricularPlan.setBranch(this.oldBranch);
				studentCurricularPlan.setCurrentState(studentCurricularPlanState);
				studentCurricularPlan.setDegreeCurricularPlan(this.oldDegreeCurricularPlan);
				studentCurricularPlan.setStudent(student);
				studentCurricularPlan.setStartDate(newDate);
				writeElement(studentCurricularPlan);
				loader.numberElementsWritten--;
			} else {
				Date oldDate = studentCurricularPlan.getStartDate();
				Calendar oldCalendar = Calendar.getInstance();
				oldCalendar.setTime(oldDate);
				int oldYear = oldCalendar.get(Calendar.YEAR);
				if (newYear < oldYear) {
					studentCurricularPlan.setStartDate(newDate);
					writeElement(studentCurricularPlan);
					loader.numberElementsWritten--;
				}
			}
		} else {
			errorMessage = "\n O aluno numero " + almeida_enrolment.getNumalu() + " não está inscrito este semestre! Registos: ";
			errorDBID = almeida_enrolment.getId_internal() + ",";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
		}

		return studentCurricularPlan;
	}

	private IDegreeCurricularPlan processOldDegreeCurricularPlan(Almeida_enrolment almeida_enrolment) {
		Integer keyDegree;
		try {
			keyDegree = new Integer("" + almeida_enrolment.getCurso());
		} catch (NumberFormatException e) {
			errorMessage = "\n O curso " + almeida_enrolment.getCurso() + "é inválido! Registos: ";
			errorDBID = almeida_enrolment.getId_internal() + ",";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
			return null;
		}
		IDegreeCurricularPlan degreeCurricularPlan = persistentObjectOJB.readDegreeCurricularPlanByDegreeKeyAndState(keyDegree, DegreeCurricularPlanState.CONCLUDED_OBJ);
		if (degreeCurricularPlan == null) {
			errorMessage = "\n O plano curricular do curso [" + keyDegree + "] não existe! Registos: ";
			errorDBID = almeida_enrolment.getId_internal() + ",";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
		}

		return degreeCurricularPlan;
	}

	protected String getFilenameOutput() {
		return "etc/migration/dcs-rjao/logs/LoadLEQEnrolmentsToFenix.txt";
	}
}