package middleware.almeida.dcsrjao;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import middleware.almeida.Almeida_enrolment;
import Dominio.CurricularCourse;
import Dominio.CurricularCourseScope;
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
import Dominio.IUniversity;
import Dominio.StudentCurricularPlan;
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

public class LoadEnrolmentsToFenix extends LoadDataToFenix {

	private static LoadEnrolmentsToFenix loader = null;
	private static String logString = "";
//	private static HashMap errorFilterScopeByUnique = new HashMap();
//	private static HashMap errorFilterScopeWithoutSemester = new HashMap();
	private static HashMap errorFilterScopeWithoutSemesterAndBranch = new HashMap();
//	private static HashMap errorNoCourseInRealDegreeCPlan = new HashMap();
//	private static HashMap errorNoCourseInISTDegreeCPlan = new HashMap();
	private static HashMap errorNotExistingCourse = new HashMap();
	private static HashMap error = new HashMap();
	private static String errorMessage = "";
	private static String errorDBID = "";

	private IDegreeCurricularPlan oldDegreeCurricularPlan = null;
	private IDegreeCurricularPlan oldMigratingDegreeCurricularPlan = null;
	private IDegreeCurricularPlan oldISTDegreeCurricularPlan = null;
	private IDegreeCurricularPlan newDegreeCurricularPlan = null;
	private IBranch oldBranch = null;
	private IBranch newBranch = null;
	private ICurricularCourse curricularCourse = null;
	private ICurricularSemester curricularSemester = null;
	private IStudentCurricularPlan oldStudentCurricularPlan = null;
//	private IStudentCurricularPlan newStudentCurricularPlan = null;
	private IExecutionPeriod executionPeriod = null;
	private ICurricularCourseScope curricularCourseScope = null;
	private EnrolmentEvaluationType enrolmentEvaluationType = null;
	private EnrolmentState enrolmentState = null;
	private IUniversity university = null;
		
//	private int filterScopeByUnique = 1;
//	private int filterScopeWithoutSemester = 2;
//	private int filterScopeWithoutSemesterAndBranch = 3;
//	private int filterEnd = 4;
	
	public LoadEnrolmentsToFenix() {
	}

	public static void main(String[] args) {

		if (loader == null) {
			loader = new LoadEnrolmentsToFenix();
		}

		loader.migrationStart(loader.getClassName());
		loader.setupDAO();
		List almeidaEnrolmentsList = loader.persistentObjectOJB.readAllAlmeidaEnrolments();
		loader.shutdownDAO();

		loader.oldMigratingDegreeCurricularPlan = loader.processOldMigratingDegreeCurricularPlan();
		if (loader.oldMigratingDegreeCurricularPlan == null) {
			logString += error.toString();
			loader.migrationEnd(loader.getClassName(), logString);
			return;
		}

		Iterator iterator = almeidaEnrolmentsList.iterator();
		while (iterator.hasNext()) {
			Almeida_enrolment almeida_enrolment = (Almeida_enrolment) iterator.next();
			loader.printIteration(loader.getClassName(), almeida_enrolment.getId_internal());
			loader.setupDAO();
			loader.processEnrolment(almeida_enrolment);
			loader.shutdownDAO();
		}

		logString += error.toString();
		loader.processCoursesErrors();
		loader.migrationEnd(loader.getClassName(), logString);
	}

	private IDegreeCurricularPlan processOldMigratingDegreeCurricularPlan() {
		IDegreeCurricularPlan degreeCurricularPlan = persistentObjectOJB.readDegreeCurricularPlanByName(InfoForMigration.NAME_OF_OLD_DEGREE_CURRICULAR_PLAN);
		if (degreeCurricularPlan == null) {
			errorMessage = "\n O degree Curricular Plan " + InfoForMigration.NAME_OF_OLD_DEGREE_CURRICULAR_PLAN + " não existe!";
			errorDBID = "";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
			return null;
		}

		return degreeCurricularPlan;
	}

	private void processCoursesErrors() {
//		String errorLog = errorFilterScopeByUnique.toString();
//		String filename = "etc/migration/dcs-rjao/logs/errorFilterScopeByUnique.txt";
//		writeErrorToFile(errorLog, filename);
//
//		errorLog = errorFilterScopeWithoutSemester.toString();
//		filename = "etc/migration/dcs-rjao/logs/errorFilterScopeWithoutSemester.txt";
//		writeErrorToFile(errorLog, filename);

		String errorLog = errorFilterScopeWithoutSemesterAndBranch.toString();
		String filename = "etc/migration/dcs-rjao/logs/errorFilterScopeWithoutSemesterAndBranch.txt";
		writeErrorToFile(errorLog, filename);

//		errorLog = errorNoCourseInRealDegreeCPlan.toString();
//		filename = "etc/migration/dcs-rjao/logs/errorNoCourseInRealDegreeCPlan.txt";
//		writeErrorToFile(errorLog, filename);
//
//		errorLog = errorNoCourseInISTDegreeCPlan.toString();
//		filename = "etc/migration/dcs-rjao/logs/errorNoCourseInISTDegreeCPlan.txt";
//		writeErrorToFile(errorLog, filename);

		errorLog = errorNotExistingCourse.toString();
		filename = "etc/migration/dcs-rjao/logs/errorNotExistingCourse.txt";
		writeErrorToFile(errorLog, filename);
	}

	private void writeErrorToFile(String errorLog, String filename) {
		try {
			FileWriter outFile = new FileWriter(filename);
			outFile.write(errorLog);
			outFile.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed to obtain a file writer for " + filename);
		}
	}

	private void processEnrolment(Almeida_enrolment almeida_enrolment) {

		this.oldISTDegreeCurricularPlan = processISTDegreeCurricularPlan(almeida_enrolment);
		if (this.oldISTDegreeCurricularPlan == null) {
			loader.numberUntreatableElements++;
			return;
		}

		this.oldDegreeCurricularPlan = processOldDegreeCurricularPlan(almeida_enrolment);
		if (this.oldDegreeCurricularPlan == null) {
			loader.numberUntreatableElements++;
			return;
		}

		this.newDegreeCurricularPlan = processNewDegreeCurricularPlan(almeida_enrolment);
		if (this.newDegreeCurricularPlan == null) {
			loader.numberUntreatableElements++;
			return;
		}

		this.oldBranch = processOldBranch(almeida_enrolment);
		if (this.oldBranch == null) {
			loader.numberUntreatableElements++;
			return;
		}

		this.newBranch = processNewBranch(almeida_enrolment);
		if (this.newBranch == null) {
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

//		this.newStudentCurricularPlan = processNewStudentCurricularPlan(almeida_enrolment);
//		if (this.newStudentCurricularPlan == null) {
//			loader.numberUntreatableElements++;
//			return;
//		}

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

		IEnrolment enrolment = persistentObjectOJB.readEnrolmentByUnique(this.oldStudentCurricularPlan, this.curricularCourseScope, this.executionPeriod);

		if (enrolment == null) {
			enrolment = processNewEnrolment(almeida_enrolment);
		} else {
			//	Quando a inscricao é anulada é criado um novo enrolment, 
			//	uma melhoria ou rectificacao é o mesmo enrolment mas com um novo evaluation e um estado novo
			//  quando se chumba tem de ver se o chumbo foi no mesmo periodo que a inscrição actual ou noutro
			if ((enrolment.getEnrolmentState().equals(EnrolmentState.ANNULED))
				|| ((enrolment.getEnrolmentState().equals(EnrolmentState.NOT_APROVED)) && (!enrolment.getExecutionPeriod().equals(this.executionPeriod)))) {
				enrolment = processNewEnrolment(almeida_enrolment);
			} else {
				enrolment = updateEnrolmentState(enrolment, almeida_enrolment);
			}
		}

		//	Caso em que foi lançada uma nota e por isso cria-se uma avaliação.
		if (!this.enrolmentState.equals(EnrolmentState.NOT_EVALUATED)) {
			processEnrolmentEvaluation(enrolment, almeida_enrolment);
		}
	}

	private IEnrolment updateEnrolmentState(IEnrolment enrolment, Almeida_enrolment almeida_enrolment) {
		if ((this.enrolmentState.equals(EnrolmentState.ANNULED))
			|| (this.enrolmentState.equals(EnrolmentState.APROVED))
			|| ((this.enrolmentState.equals(EnrolmentState.NOT_APROVED)) && (enrolment.getEnrolmentState().equals(EnrolmentState.NOT_EVALUATED)))) {
			enrolment.setEnrolmentState(this.enrolmentState);
		}

		if (almeida_enrolment.getObserv() != null) {
			// todas as rectificações são para notas positivas
			if (almeida_enrolment.getObserv().equals("RECTIFICAÇÃO")) {
				enrolment.setEnrolmentState(EnrolmentState.APROVED);
			}
		}
		return enrolment;
	}

	private IEnrolment processNewEnrolment(Almeida_enrolment almeida_enrolment) {
		IEnrolment enrolment = new Enrolment();
		enrolment.setCurricularCourseScope(this.curricularCourseScope);
		enrolment.setExecutionPeriod(this.executionPeriod);
		enrolment.setStudentCurricularPlan(this.oldStudentCurricularPlan);
		enrolment.setEnrolmentEvaluationType(EnrolmentEvaluationType.CLOSED_OBJ);
		enrolment.setEnrolmentState(this.enrolmentState);
		writeElement(enrolment);
		return enrolment;
	}

	private IBranch processNewBranch(Almeida_enrolment almeida_enrolment) { // no novo curriculo da LEW não há branches
		String code = "";
		IBranch branch = persistentObjectOJB.readBranchByUnique(code, this.newDegreeCurricularPlan);
		if (branch == null) {
			errorMessage = "\n Não existe o ramo com o code [" + code + "] e plano curricular = " + this.newDegreeCurricularPlan.getName() + ". Registos: ";
			errorDBID = almeida_enrolment.getId_internal() + ",";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
		}
		return branch;
	}

	private IDegreeCurricularPlan processNewDegreeCurricularPlan(Almeida_enrolment almeida_enrolment) {
		IDegreeCurricularPlan degreeCurricularPlan = persistentObjectOJB.readDegreeCurricularPlanByName(InfoForMigration.NAME_OF_NEW_DEGREE_CURRICULAR_PLAN);
		if (degreeCurricularPlan == null) {
			errorMessage = "\n O plano curricular " + InfoForMigration.NAME_OF_NEW_DEGREE_CURRICULAR_PLAN + " não existe! Registos: ";
			errorDBID = almeida_enrolment.getId_internal() + ", ";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
		}

		return degreeCurricularPlan;
	}

//	private IStudentCurricularPlan processNewStudentCurricularPlan(Almeida_enrolment almeida_enrolment) {
//		IStudentCurricularPlan studentCurricularPlan = null;
//		Integer studentNumber = null;
//		try {
//			studentNumber = new Integer(almeida_enrolment.getNumalu());
//		} catch (NumberFormatException e) {
//			errorMessage = "\n O numero de aluno " + almeida_enrolment.getNumalu() + " é inválido! Registos: ";
//			errorDBID = almeida_enrolment.getId_internal() + ", ";
//			error = loader.setErrorMessage(errorMessage, errorDBID, error);
//			return null;
//		}
//		IStudent student = persistentObjectOJB.readStudentByNumberAndDegreeType(studentNumber, TipoCurso.LICENCIATURA_OBJ);
//		if (student != null) {
//			StudentCurricularPlanState studentCurricularPlanState = StudentCurricularPlanState.ACTIVE_OBJ;
//			int newYear = new Integer(2003).intValue();
//			Calendar newCalendar = Calendar.getInstance();
//			newCalendar.set(newYear, Calendar.SEPTEMBER, 1);
//			Date newDate = newCalendar.getTime();
//			studentCurricularPlan =
//				persistentObjectOJB.readStudentCurricularPlanByUnique(student, this.newDegreeCurricularPlan, this.newBranch, studentCurricularPlanState);
//			if (studentCurricularPlan == null) {
//				studentCurricularPlan = new StudentCurricularPlan();
//				studentCurricularPlan.setBranch(this.newBranch);
//				studentCurricularPlan.setCurrentState(studentCurricularPlanState);
//				studentCurricularPlan.setDegreeCurricularPlan(this.newDegreeCurricularPlan);
//				studentCurricularPlan.setStudent(student);
//				studentCurricularPlan.setStartDate(newDate);
//				writeElement(studentCurricularPlan);
//				loader.numberElementsWritten--;
//			}
//		} else {
//			errorMessage = "\n O aluno numero " + almeida_enrolment.getNumalu() + " não existe! Registos: ";
//			errorDBID = almeida_enrolment.getId_internal() + ",";
//			error = loader.setErrorMessage(errorMessage, errorDBID, error);
//		}
//
//		return studentCurricularPlan;
//	}

	private EnrolmentState processGrade(Almeida_enrolment almeida_enrolment) {
		String grade = almeida_enrolment.getResult();
		if (almeida_enrolment.getObserv() != null) {
			if (almeida_enrolment.getObserv().equals("ANULADO")) {
				return EnrolmentState.ANNULED;
			}
		}

		if (grade == null) {
			return EnrolmentState.NOT_EVALUATED;
		}

		if (grade.equals("RE")) {
			return EnrolmentState.NOT_APROVED;
		}

		int intGrade;
		try {
			intGrade = new Integer(grade).intValue();
		} catch (NumberFormatException e) {
			errorMessage = "\n A nota " + grade + " é inválida! Registos: ";
			errorDBID = almeida_enrolment.getId_internal() + ", ";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
			return null;
		}

		if ((intGrade > 20) || (intGrade < 0)) {
			errorMessage = "\n A nota " + intGrade + " é inválida! Registos: ";
			errorDBID = almeida_enrolment.getId_internal() + ", ";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
			return null;
		} else if (intGrade < 10) {
			return EnrolmentState.NOT_APROVED;
		}

		return EnrolmentState.APROVED;
	}

	private void processEnrolmentEvaluation(IEnrolment enrolment, Almeida_enrolment almeida_enrolment) {

		//		IEnrolmentEvaluation enrolmentEvaluation = loader.persistentObjectOJB.readEnrolmentEvaluationByUnique(enrolment, this.enrolmentEvaluationType, almeida_enrolment.getResult());
		//		if (enrolmentEvaluation != null) {
		//			errorMessage = "\n EnrolmentEvaluation repetido: enrolment = " + enrolment.getIdInternal() + " evaluation = " + this.enrolmentEvaluationType.toString() + " result = " + almeida_enrolment.getResult() + ". Registos: ";
		//			errorDBID = almeida_enrolment.getId_internal() + ", ";
		//			error = loader.setErrorMessage(errorMessage, errorDBID, error);
		//			return;
		//		}

		IEnrolmentEvaluation enrolmentEvaluation = new EnrolmentEvaluation();
		enrolmentEvaluation.setEnrolmentEvaluationType(this.enrolmentEvaluationType);
		enrolmentEvaluation.setEnrolment(enrolment);
		enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.FINAL_OBJ); //		}
		
		//FIXME [DAVID]: As datas estão mal migradas
		if (almeida_enrolment.getDatexa() != null) {
			Calendar newCalendar = Calendar.getInstance();
			int year = 0;
			int month = 0;
			int day = 0;
			try {
				year = (new Integer(almeida_enrolment.getDatexa().substring(0, 4))).intValue();
				month = (new Integer(almeida_enrolment.getDatexa().substring(5, 7))).intValue();
				day = (new Integer(almeida_enrolment.getDatexa().substring(8, 10))).intValue();
			} catch (NumberFormatException e) {
				errorMessage = "\n A data de exame " + almeida_enrolment.getDatexa() + " é inválida! Registos: ";
				errorDBID = almeida_enrolment.getId_internal() + ", ";
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
					errorDBID = almeida_enrolment.getId_internal() + ", ";
					error = loader.setErrorMessage(errorMessage, errorDBID, error);
				}
				enrolmentEvaluation.setPersonResponsibleForGrade(pessoa);
			} catch (NumberFormatException e) {
				errorMessage = "\n O numero de identificação " + almeida_enrolment.getNumdoc() + " é inválido! Registos: ";
				errorDBID = almeida_enrolment.getId_internal() + ", ";
				error = loader.setErrorMessage(errorMessage, errorDBID, error);
			}
		} else {
			enrolmentEvaluation.setPersonResponsibleForGrade(null);
		}
		//	TODO [DAVID]: Quando o algoritmo do checksum estiver feito tem de ser actualizar este campo
		enrolmentEvaluation.setCheckSum(null);
		writeElement(enrolmentEvaluation);
		loader.numberElementsWritten--;
	}

	private ICurricularSemester processCurricularSemester(Almeida_enrolment almeida_enrolment) {
		Integer year;
		Integer semester;

		semester = new Integer("" + almeida_enrolment.getSemdis());
		year = new Integer("" + almeida_enrolment.getAnodis());

		ICurricularYear curricularYear = persistentObjectOJB.readCurricularYearByYear(year);
		if (curricularYear == null) {
			errorMessage = "\n Não existe o curricularYear = " + year + ". Registos: ";
			errorDBID = almeida_enrolment.getId_internal() + ", ";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
			return null;
		}
		ICurricularSemester processedCurricularSemester = persistentObjectOJB.readCurricularSemesterBySemesterAndCurricularYear(semester, curricularYear);
		if (processedCurricularSemester == null) {
			errorMessage = "\n Não existe o curricularSemester = " + semester + ". Registos: ";
			errorDBID = almeida_enrolment.getId_internal() + ", ";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
		}
		return processedCurricularSemester;
	}

	private ICurricularCourseScope processCurricularCourseScope(Almeida_enrolment almeida_enrolment) {

		//		Integer anoInscricao;
		//		try {
		//			anoInscricao = new Integer("" + almeida_enrolment.getAnoins());
		//		} catch (NumberFormatException e) {
		//			errorMessage = "\n O ano " + almeida_enrolment.getAnoins() + " é inválido! Registos: ";
		//			errorDBID = almeida_enrolment.getId_internal() + ", ";
		//			error = loader.setErrorMessage(errorMessage, errorDBID, error);
		//			return null;
		//		}
		//		ICurricularCourseScope curricularCourseScope = persistentObjectOJB.readCurricularCourseScopeByUnique(this.curricularCourse, this.oldBranch, this.curricularSemester, anoInscricao);

		ICurricularCourseScope processedCurricularCourseScope = null;
		processedCurricularCourseScope = persistentObjectOJB.readCurricularCourseScopeByUnique(this.curricularCourse, this.oldBranch, this.curricularSemester);
		if (processedCurricularCourseScope == null) {
//			errorMessage =
//				"\n Não existe a disciplina(scope) = "
//					+ this.curricularCourse.getCode()
//					+ "["
//					+ this.curricularCourse.getIdInternal()
//					+ "]"
//					+ " Semestre = "
//					+ this.curricularSemester.getSemester().intValue()
//					+ " Ano = "
//					+ this.curricularSemester.getCurricularYear().getYear().intValue()
//					+ " Ano Inscricao = "
//					+ almeida_enrolment.getAnoins()
//					+ " Ramo = "
//					+ this.oldBranch.getCode()
//					+ "["
//					+ this.oldBranch.getInternalID()
//					+ "]"
//					+ ". Registos: ";
//			errorDBID = almeida_enrolment.getId_internal() + ", ";
//			errorFilterScopeByUnique = loader.setErrorMessage(errorMessage, errorDBID, errorFilterScopeByUnique);

			processedCurricularCourseScope = persistentObjectOJB.readCurricularCourseScopeByCurricularCourseAndBranch(this.curricularCourse, this.oldBranch);
			if (processedCurricularCourseScope == null) {
//				errorMessage =
//					"\n Não existe a disciplina(scope) = "
//						+ this.curricularCourse.getCode()
//						+ "["
//						+ this.curricularCourse.getIdInternal()
//						+ "]"
//						+ " Semestre = "
//						+ this.curricularSemester.getSemester().intValue()
//						+ " Ano = "
//						+ this.curricularSemester.getCurricularYear().getYear().intValue()
//						+ " Ano Inscricao = "
//						+ almeida_enrolment.getAnoins()
//						+ " Ramo = "
//						+ this.oldBranch.getCode()
//						+ "["
//						+ this.oldBranch.getInternalID()
//						+ "]"
//						+ ". Registos: ";
//				errorDBID = almeida_enrolment.getId_internal() + ", ";
//				errorFilterScopeWithoutSemester = loader.setErrorMessage(errorMessage, errorDBID, errorFilterScopeWithoutSemester);

				processedCurricularCourseScope = persistentObjectOJB.readCurricularCourseScopeByCurricularCourse(this.curricularCourse);
				if (processedCurricularCourseScope == null) {
					errorMessage =
						"\n Não existe a disciplina(scope) = "
							+ this.curricularCourse.getCode()
							+ "["
							+ this.curricularCourse.getIdInternal()
							+ "]"
							+ " Semestre = "
							+ this.curricularSemester.getSemester().intValue()
							+ " Ano = "
							+ this.curricularSemester.getCurricularYear().getYear().intValue()
							+ " Ano Inscricao = "
							+ almeida_enrolment.getAnoins()
							+ " Ramo = "
							+ this.oldBranch.getCode()
							+ "["
							+ this.oldBranch.getIdInternal()
							+ "]"
							+ ". Registos: ";
					errorDBID = almeida_enrolment.getId_internal() + ", ";
					errorFilterScopeWithoutSemesterAndBranch = loader.setErrorMessage(errorMessage, errorDBID, errorFilterScopeWithoutSemesterAndBranch);
					numberUntreatableElements++;
					return null;
				}
			}
		}

		return processedCurricularCourseScope;
	}

	private ICurricularCourse processCurricularCourse(Almeida_enrolment almeida_enrolment) {
		ICurricularCourse processedCurricularCourse =
			persistentObjectOJB.readCurricularCourseByCodeAndDegreeCurricularPlan(almeida_enrolment.getCoddis(), this.oldDegreeCurricularPlan);
		if (processedCurricularCourse == null) {
//			errorMessage =
//				"\n Não existe (ou existe mais que um) curricularCourse com o code = "
//					+ almeida_enrolment.getCoddis()
//					+ " no plano curricular "
//					+ this.oldDegreeCurricularPlan.getName()
//					+ ". Registos: ";
//			errorDBID = almeida_enrolment.getId_internal() + ", ";
//			errorNoCourseInRealDegreeCPlan = loader.setErrorMessage(errorMessage, errorDBID, errorNoCourseInRealDegreeCPlan);

			processedCurricularCourse =
				persistentObjectOJB.readCurricularCourseByCodeAndDegreeCurricularPlan(almeida_enrolment.getCoddis(), this.oldISTDegreeCurricularPlan);
			if (processedCurricularCourse == null) {
//				errorMessage =
//					"\n Não existe (ou existe mais que um) curricularCourse com o code = "
//						+ almeida_enrolment.getCoddis()
//						+ " no plano curricular "
//						+ this.oldISTDegreeCurricularPlan.getName()
//						+ ". Registos: ";
//				errorDBID = almeida_enrolment.getId_internal() + ", ";
//				errorNoCourseInISTDegreeCPlan = loader.setErrorMessage(errorMessage, errorDBID, errorNoCourseInISTDegreeCPlan);

				processedCurricularCourse = persistentObjectOJB.readCurricularCourseByCode(almeida_enrolment.getCoddis());
				if (processedCurricularCourse == null) {
//					errorMessage = "\n Não existe (ou existe mais que um) curricularCourse com o code = " + almeida_enrolment.getCoddis() + ". Registos: ";
//					errorDBID = almeida_enrolment.getId_internal() + ", ";
//					errorNotExistingCourse = loader.setErrorMessage(errorMessage, errorDBID, errorNotExistingCourse);
//					return null;
					processedCurricularCourse = persistentObjectOJB.readFirstCurricularCourseByCode(almeida_enrolment.getCoddis());
					if (processedCurricularCourse == null) {
						errorMessage = "\n Não existe o curricularCourse com o code = " + almeida_enrolment.getCoddis() + ". Registos: ";
						errorDBID = almeida_enrolment.getId_internal() + ", ";
						errorNotExistingCourse = loader.setErrorMessage(errorMessage, errorDBID, errorNotExistingCourse);
						return null;						
					}
				}
			}
		}

		if (processedCurricularCourse.getUniversity() == null) {
			processedCurricularCourse.setUniversity(this.university);
			processedCurricularCourse.setDegreeCurricularPlan(this.oldDegreeCurricularPlan);
			writeElement(processedCurricularCourse);
			loader.numberElementsWritten--;
		} else if (!processedCurricularCourse.getUniversity().equals(this.university)) {
			ICurricularCourse newCurricularCourse = new CurricularCourse();
			newCurricularCourse.setCode(processedCurricularCourse.getCode());
			newCurricularCourse.setDegreeCurricularPlan(this.oldDegreeCurricularPlan);
			newCurricularCourse.setMandatory(processedCurricularCourse.getMandatory());
			newCurricularCourse.setName(processedCurricularCourse.getName());
			newCurricularCourse.setType(processedCurricularCourse.getType());
			newCurricularCourse.setUniversity(this.university);
			newCurricularCourse.setBasic(processedCurricularCourse.getBasic());
			writeElement(newCurricularCourse);
			numberElementsWritten--;

			ICurricularCourseScope newCurricularCourseScope = new CurricularCourseScope();
			ICurricularCourseScope tempCurricularCourseScope = (ICurricularCourseScope) processedCurricularCourse.getScopes().get(0);
			newCurricularCourseScope.setCurricularCourse(newCurricularCourse);

			newCurricularCourseScope.setBranch(tempCurricularCourseScope.getBranch());
			newCurricularCourseScope.setCurricularSemester(tempCurricularCourseScope.getCurricularSemester());
			newCurricularCourseScope.setCredits(tempCurricularCourseScope.getCredits());
			newCurricularCourseScope.setLabHours(tempCurricularCourseScope.getLabHours());
			newCurricularCourseScope.setMaxIncrementNac(tempCurricularCourseScope.getMaxIncrementNac());
			newCurricularCourseScope.setMinIncrementNac(tempCurricularCourseScope.getMinIncrementNac());
			newCurricularCourseScope.setPraticalHours(tempCurricularCourseScope.getPraticalHours());
			newCurricularCourseScope.setTheoPratHours(tempCurricularCourseScope.getTheoPratHours());
			newCurricularCourseScope.setTheoreticalHours(tempCurricularCourseScope.getTheoreticalHours());
			newCurricularCourseScope.setWeigth(tempCurricularCourseScope.getWeigth());
//		TODO: add sets for begin and end dates and ectsCredits
			writeElement(newCurricularCourseScope);

			processedCurricularCourse = newCurricularCourse;
		}

		return processedCurricularCourse;
	}

	private IBranch processOldBranch(Almeida_enrolment almeida_enrolment) {
		String code = "";

		if (almeida_enrolment.getRamo() != 0) {
			//	A orientacao esta a 0, pq ainda nao ha informacao sobre ela no ficheiro de inscricoes
			code = code + almeida_enrolment.getCurso() + almeida_enrolment.getRamo() + "0";
		}

		IBranch branch = persistentObjectOJB.readBranchByUnique(code, this.oldDegreeCurricularPlan);
		if (branch == null) {
			errorMessage = "\n Não existe o ramo com o code [" + code + "] e plano curricular = " + this.oldDegreeCurricularPlan.getName() + ". Registos: ";
			errorDBID = almeida_enrolment.getId_internal() + ", ";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
		}
		return branch;
	}

	private IUniversity processUniversity(Almeida_enrolment almeida_enrolment) {
		String universityCodeRead = almeida_enrolment.getCoduniv();
		IUniversity universityCode = persistentObjectOJB.readUniversityByCode(universityCodeRead);
		if (universityCode == null) {
			errorMessage = "\n Não existe a universidade com o code [" + universityCodeRead + "]. Registos: ";
			errorDBID = almeida_enrolment.getId_internal() + ", ";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
			return null;
		}
		return universityCode;
	}

	private EnrolmentEvaluationType processEvaluationType(Almeida_enrolment almeida_enrolment) {
		EnrolmentEvaluationType processedEnrolmentEvaluationType = null;
		int epoca;
		try {
			epoca = (new Integer("" + almeida_enrolment.getEpoca())).intValue();
		} catch (NumberFormatException e) {
			errorMessage = "\n A época " + almeida_enrolment.getEpoca() + " é inválida! Registos: ";
			errorDBID = almeida_enrolment.getId_internal() + ", ";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
			return null;
		}
		switch (epoca) {
			case 0 :
				processedEnrolmentEvaluationType = EnrolmentEvaluationType.NO_SEASON_OBJ;
				break;
			case 1 :
				processedEnrolmentEvaluationType = EnrolmentEvaluationType.FIRST_SEASON_OBJ;
				break;
			case 2 :
				processedEnrolmentEvaluationType = EnrolmentEvaluationType.SECOND_SEASON_OBJ;
				break;
			case 3 :
				processedEnrolmentEvaluationType = EnrolmentEvaluationType.SPECIAL_SEASON_OBJ;
				break;
			case 4 :
				processedEnrolmentEvaluationType = EnrolmentEvaluationType.IMPROVEMENT_OBJ;
				break;
			case 5 :
				processedEnrolmentEvaluationType = EnrolmentEvaluationType.EXTERNAL_OBJ;
				break;
			default :
				errorMessage = "\n A epoca " + epoca + " é invalida" + ". Registos: ";
				errorDBID = almeida_enrolment.getId_internal() + ", ";
				error = loader.setErrorMessage(errorMessage, errorDBID, error);
				break;
		}

		return processedEnrolmentEvaluationType;
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

		IExecutionPeriod processedExecutionPeriod = persistentObjectOJB.readExecutionPeriodByYearAndSemester(executionYear, semester);
		if (processedExecutionPeriod == null) {
			processedExecutionPeriod = new ExecutionPeriod();
			processedExecutionPeriod.setExecutionYear(executionYear);
			processedExecutionPeriod.setSemester(semester);
			processedExecutionPeriod.setState(PeriodState.CLOSED);
			String name = "";
			if (semester.intValue() == 1) {
				name = "1º Semestre";
			} else if (semester.intValue() == 2) {
				name = "2º Semestre";
			}
			processedExecutionPeriod.setName(name);
			writeElement(processedExecutionPeriod);
			loader.numberElementsWritten--;
		}

		return processedExecutionPeriod;
	}

	private IStudentCurricularPlan processOldStudentCurricularPlan(Almeida_enrolment almeida_enrolment) {

		IStudentCurricularPlan studentCurricularPlan = null;
		Integer studentNumber;
		try {
			studentNumber = new Integer(almeida_enrolment.getNumalu());
		} catch (NumberFormatException e) {
			errorMessage = "\n O numero de estudante " + almeida_enrolment.getNumalu() + " é inválido! Registos: ";
			errorDBID = almeida_enrolment.getId_internal() + ", ";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
			return null;
		}
		IStudent student = persistentObjectOJB.readStudentByNumberAndDegreeType(studentNumber, TipoCurso.LICENCIATURA_OBJ);
		if (student != null) {

			//			StudentCurricularPlanState studentCurricularPlanState = StudentCurricularPlanState.INCOMPLETE_OBJ;
			//			int newYear = new Integer("" + almeida_enrolment.getAnoins()).intValue();
			//			Calendar newCalendar = Calendar.getInstance();
			//			newCalendar.set(newYear, Calendar.SEPTEMBER, 1);
			//			Date newDate = newCalendar.getTime();
			//			studentCurricularPlan =
			//				persistentObjectOJB.readStudentCurricularPlanByUnique(student, this.oldDegreeCurricularPlan, this.oldBranch, studentCurricularPlanState);
			//			if (studentCurricularPlan == null) {
			//				studentCurricularPlan = new StudentCurricularPlan();
			//				studentCurricularPlan.setBranch(this.oldBranch);
			//				studentCurricularPlan.setCurrentState(studentCurricularPlanState);
			//				studentCurricularPlan.setDegreeCurricularPlan(this.oldDegreeCurricularPlan);
			//				studentCurricularPlan.setStudent(student);
			//				studentCurricularPlan.setStartDate(newDate);
			//				writeElement(studentCurricularPlan);
			//				loader.numberElementsWritten--;
			//			} else {
			//				Date oldDate = studentCurricularPlan.getStartDate();
			//				Calendar oldCalendar = Calendar.getInstance();
			//				oldCalendar.setTime(oldDate);
			//				int oldYear = oldCalendar.get(Calendar.YEAR);
			//				if (newYear < oldYear) {
			//					studentCurricularPlan.setStartDate(newDate);
			//					writeElement(studentCurricularPlan);
			//					loader.numberElementsWritten--;
			//				}
			//			}

			StudentCurricularPlanState studentCurricularPlanState = StudentCurricularPlanState.INCOMPLETE_OBJ;
			studentCurricularPlan = persistentObjectOJB.readStudentCurricularPlanByStudentAndState(student, StudentCurricularPlanState.INCOMPLETE_OBJ);
			if (studentCurricularPlan == null) {
				int newYear = new Integer("" + almeida_enrolment.getAnoins()).intValue();
				Calendar newCalendar = Calendar.getInstance();
				newCalendar.set(newYear, Calendar.SEPTEMBER, 1);
				Date newDate = newCalendar.getTime();

				studentCurricularPlan = new StudentCurricularPlan();
				studentCurricularPlan.setBranch(this.oldBranch);
				studentCurricularPlan.setCurrentState(studentCurricularPlanState);
				studentCurricularPlan.setDegreeCurricularPlan(this.oldMigratingDegreeCurricularPlan);
				studentCurricularPlan.setStudent(student);
				studentCurricularPlan.setStartDate(newDate);
				writeElement(studentCurricularPlan);
				loader.numberElementsWritten--;
			}

		} else {
			errorMessage = "\n O aluno numero " + almeida_enrolment.getNumalu() + " não está inscrito este semestre! Registos: ";
			errorDBID = almeida_enrolment.getId_internal() + ", ";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
		}

		return studentCurricularPlan;
	}

	private IDegreeCurricularPlan processOldDegreeCurricularPlan(Almeida_enrolment almeida_enrolment) {
		this.university = processUniversity(almeida_enrolment);
		if (this.university == null) {
			return null;
		}

		DegreeCurricularPlanState degreeCurricularPlanState;
		Integer keyDegree;
		String degreeCode = "";
		if (this.university.getCode().equals("IST")) {
			degreeCode = almeida_enrolment.getCurso();
			degreeCurricularPlanState = DegreeCurricularPlanState.CONCLUDED_OBJ;
		} else {
			degreeCode = "65"; // codigo do curso EXTERNAL
			degreeCurricularPlanState = DegreeCurricularPlanState.ACTIVE_OBJ;
		}

		try {
			keyDegree = new Integer("" + degreeCode);
		} catch (NumberFormatException e) {
			errorMessage = "\n O curso " + almeida_enrolment.getCurso() + "é inválido! Registos: ";
			errorDBID = almeida_enrolment.getId_internal() + ", ";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
			return null;
		}

		IDegreeCurricularPlan degreeCurricularPlan = persistentObjectOJB.readDegreeCurricularPlanByDegreeKeyAndState(keyDegree, degreeCurricularPlanState);
		if (degreeCurricularPlan == null) {
			errorMessage = "\n O degree Curricular Plan do curso " + almeida_enrolment.getCurso() + " não existe! Registos: ";
			errorDBID = almeida_enrolment.getId_internal() + ", ";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
			return null;
		}

		return degreeCurricularPlan;
	}

	private IDegreeCurricularPlan processISTDegreeCurricularPlan(Almeida_enrolment almeida_enrolment) {
		IUniversity universityIST = persistentObjectOJB.readUniversityByCode("IST");
		if (universityIST == null) {
			return null;
		}

		Integer keyDegree;
		try {
			keyDegree = new Integer("" + almeida_enrolment.getCurso());
		} catch (NumberFormatException e) {
			errorMessage = "\n O curso " + almeida_enrolment.getCurso() + "é inválido! Registos: ";
			errorDBID = almeida_enrolment.getId_internal() + ", ";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
			return null;
		}

		IDegreeCurricularPlan degreeCurricularPlan =
			persistentObjectOJB.readDegreeCurricularPlanByDegreeKeyAndState(keyDegree, DegreeCurricularPlanState.CONCLUDED_OBJ);
		if (degreeCurricularPlan == null) {
			errorMessage = "\n O degree Curricular Plan do curso " + almeida_enrolment.getCurso() + " não existe! Registos: ";
			errorDBID = almeida_enrolment.getId_internal() + ", ";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
			return null;
		}

		return degreeCurricularPlan;
	}

	protected String getFilenameOutput() {
		return "etc/migration/dcs-rjao/logs/" + this.getClassName() + ".txt";
	}

	protected String getClassName() {
		return "LoadEnrolmentsToFenix";
	}
}