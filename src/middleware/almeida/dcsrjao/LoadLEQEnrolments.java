package middleware.almeida.dcsrjao;

import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

import middleware.almeida.LoadDataFile;
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
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.StudentCurricularPlan;
import Util.EnrolmentEvaluationState;
import Util.EnrolmentEvaluationType;
import Util.EnrolmentState;
import Util.PeriodState;
import Util.StudentCurricularPlanState;
import Util.TipoCurso;
import Util.UniversityCode;

/**
 * @author dcs-rjao
 *
 * 21/Mai/2003
 */

public class LoadLEQEnrolments extends LoadDataFile {

	private static LoadLEQEnrolments loader = null;
	protected static String logString = "";
	protected static IDegreeCurricularPlan oldDegreeCurricularPlan = null;
	protected static IBranch branch = null;
	protected static ICurricularCourse curricularCourse = null;
	protected static ICurricularSemester curricularSemester = null;

	public LoadLEQEnrolments() {
		super.setupDAO();
		oldDegreeCurricularPlan = this.processOldDegreeCurricularPlan();
		super.shutdownDAO();
	}

	public static void main(String[] args) {
		if (loader == null) {
			loader = new LoadLEQEnrolments();
		}
		loader.run();
	}

	public void run() {
		if (loader == null) {
			loader = new LoadLEQEnrolments();
		}
		loader.load();
		loader.writeToFile(logString);
	}

	protected void processLine(String line) {

		System.out.println(loader.numberLinesProcessed);

		StringTokenizer stringTokenizer = new StringTokenizer(line, getFieldSeparator());

		String studentNumber = stringTokenizer.nextToken();
		String enrolmentYear = stringTokenizer.nextToken();
		String curricularYear = stringTokenizer.nextToken();
		String curricularSemester = stringTokenizer.nextToken();
		String epoca = stringTokenizer.nextToken();
		String curricularCourseCode = stringTokenizer.nextToken();
		String degreeCode = stringTokenizer.nextToken();
		String branchCode = stringTokenizer.nextToken();
		String grade = stringTokenizer.nextToken();
		String teacherNumber = stringTokenizer.nextToken();
		String evaluationDate = stringTokenizer.nextToken();
		String universityCode = stringTokenizer.nextToken();
		String observation = stringTokenizer.nextToken();

		Almeida_enrolment almeida_enrolment = new Almeida_enrolment();
		almeida_enrolment.setId_internal(loader.numberElementsWritten + 1);
		almeida_enrolment.setNumalu(studentNumber);

		if (epoca.equals(" ")) {
			epoca = "5"; // corresponde ao valor de uma inscrição noutra universidade	
		}

		if ((branchCode.equals(" ")) || (branchCode.equals("4"))) {
			branchCode = "0";
		}

		try {
			almeida_enrolment.setAnoins((new Integer(enrolmentYear)).longValue());
			almeida_enrolment.setAnodis((new Integer(curricularYear)).longValue());
			almeida_enrolment.setSemdis((new Integer(curricularSemester)).longValue());
			almeida_enrolment.setEpoca((new Integer(epoca)).longValue());
			almeida_enrolment.setRamo((new Integer(branchCode)).longValue());
		} catch (NumberFormatException e) {
			logString += "ERRO: Os valores lidos do ficheiro são invalidos para a criação de Integers!\n";
		}

		almeida_enrolment.setCoddis(curricularCourseCode);
		almeida_enrolment.setCurso(degreeCode);
		almeida_enrolment.setResult(grade);
		almeida_enrolment.setNumdoc(teacherNumber);
		almeida_enrolment.setDatexa(evaluationDate);
		almeida_enrolment.setCoduniv(universityCode);
		almeida_enrolment.setObserv(observation);

		processEnrolment(almeida_enrolment);
	}

	private void processEnrolment(Almeida_enrolment almeida_enrolment) {

		branch = processBranch(almeida_enrolment.getRamo());
		curricularCourse = processCurricularCourse(almeida_enrolment);
		curricularSemester = processCurricularSemester(almeida_enrolment);

		IStudentCurricularPlan studentCurricularPlan = processStudentCurricularPlan(almeida_enrolment);

		if (studentCurricularPlan != null) {
			IExecutionPeriod executionPeriod = processExecutionPeriod(almeida_enrolment.getAnoins(), almeida_enrolment.getSemdis());
			ICurricularCourseScope curricularCourseScope = processCurricularCourseScope();

			IEnrolment enrolment = persistentObjectOJB.readEnrolment(studentCurricularPlan, curricularCourse, executionPeriod);
			if (enrolment == null) {
				enrolment = new Enrolment();
				enrolment.setCurricularCourse(curricularCourse);
				enrolment.setExecutionPeriod(executionPeriod);
				enrolment.setStudentCurricularPlan(studentCurricularPlan);
				enrolment.setEnrolmentEvaluationType(processEvaluationType(almeida_enrolment.getEpoca()));
				enrolment.setEnrolmentState(processGrade(almeida_enrolment));

				// FIXME DAVID-RICARDO: Por os university codes numa tabela
				enrolment.setUniversityCode(UniversityCode.IST);
				//enrolment.setUniversityCode(processUniversityCode(almeida_enrolment.getCoduniv()));
				writeElement(enrolment);
			}

			IEnrolmentEvaluation enrolmentEvaluation = processEvaluation(enrolment, almeida_enrolment);
			writeElement(enrolmentEvaluation);
			//				logString 	+= "ERRO: O enrolment para o aluno: " + almeida_enrolment.getNumalu() 
			//							+ " na cadeira " + curricularCourse.getName()
			//							+ " no periodo " + executionPeriod.getName() + " " + executionPeriod.getExecutionYear().getYear()
			//							+ " no plano curricular " + studentCurricularPlan.getDegreeCurricularPlan().getName()
			//							+ " já existe! ";
			//				logString += "Linha: " + loader.numberLinesProcessed + "\n"; 
			//				numberUntreatableElements++;
		}
	}

	private EnrolmentState processGrade(Almeida_enrolment almeida_enrolment) {
		String grade = almeida_enrolment.getResult();
		EnrolmentState enrolmentState = null;
		if (grade.equals("RE")) {
			enrolmentState = EnrolmentState.NOT_APROVED_OBJ;
		} else if (grade.equals("  ")) {
			enrolmentState = EnrolmentState.WITHOUT_GRADE_OBJ;
		} else if (almeida_enrolment.getObserv().equals("ANULADO")) {
			enrolmentState = EnrolmentState.ANNULED_OBJ;
		} else {
			enrolmentState = EnrolmentState.APROVED_OBJ;
		}
		return enrolmentState;
	}

	private IEnrolmentEvaluation processEvaluation(IEnrolment enrolment, Almeida_enrolment almeida_enrolment) {

		IEnrolmentEvaluation enrolmentEvaluation = new EnrolmentEvaluation();

		enrolmentEvaluation.setEnrolment(enrolment);

		if (almeida_enrolment.getObserv().equals("RECTIFICADO")) {
			enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.RECTIFIED_OBJ);
		} else if (almeida_enrolment.getObserv().equals("RECTIFICAÇÃO")) {
			enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.RECTIFICATION_OBJ);
		} else {
			enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.NORMAL_OBJ);
		}

		enrolmentEvaluation.setEnrolmentEvaluationType(processEvaluationType(almeida_enrolment.getEpoca()));

		if (!almeida_enrolment.getDatexa().equals("          ")) {
			Calendar newCalendar = Calendar.getInstance();
			int year = new Integer(almeida_enrolment.getDatexa().substring(0, 3)).intValue();
			int month = new Integer(almeida_enrolment.getDatexa().substring(5, 6)).intValue();
			int day = new Integer(almeida_enrolment.getDatexa().substring(8, 9)).intValue();
			newCalendar.set(year, month, day);
			Date examDate = newCalendar.getTime();
			enrolmentEvaluation.setExamDate(examDate);
		}

		if (!almeida_enrolment.getResult().equals("  ")) {
			enrolmentEvaluation.setGrade(almeida_enrolment.getResult());
		}

		enrolmentEvaluation.setGradeAvailableDate(null);

		if (!almeida_enrolment.getNumdoc().equals("    ")) {
			almeida_enrolment.setNumdoc(almeida_enrolment.getNumdoc().substring(1));
			enrolmentEvaluation.setPersonResponsibleForGrade(persistentObjectOJB.readPersonByEmployeeNumber(new Integer(almeida_enrolment.getNumdoc())));
		}

		return enrolmentEvaluation;
	}

	private ICurricularSemester processCurricularSemester(Almeida_enrolment almeida_enrolment) {
		Integer semester = new Integer("" + almeida_enrolment.getSemdis());
		Integer year = new Integer("" + almeida_enrolment.getAnodis());

		ICurricularYear curricularYear = persistentObjectOJB.readCurricularYear(year);
		if (curricularYear == null) {
			logString += "ERRO: O curricularYear: " + year + " não existe!\n";
		}

		ICurricularSemester curricularSemester = persistentObjectOJB.readCurricularSemester(semester, curricularYear);
		if (curricularSemester == null) {
			logString += "ERRO: O curricularSemester: " + semester + " não existe!\n";
		}
		return curricularSemester;
	}

	private ICurricularCourseScope processCurricularCourseScope() {
		ICurricularCourseScope curricularCourseScope = persistentObjectOJB.readCurricularCourseScopeByUnique(curricularCourse, branch, curricularSemester);
		if (curricularCourseScope == null) {
			curricularCourseScope = new CurricularCourseScope();
			curricularCourseScope.setBranch(branch);
			curricularCourseScope.setCurricularCourse(curricularCourse);
			curricularCourseScope.setCurricularSemester(curricularSemester);
			curricularCourseScope.setMaxIncrementNac(new Integer(2));
			curricularCourseScope.setMinIncrementNac(new Integer(1));
			curricularCourseScope.setWeigth(new Integer(1));
			writeElement(curricularCourseScope);
		}
		return curricularCourseScope;
	}

	private ICurricularCourse processCurricularCourse(Almeida_enrolment almeida_enrolment) {
		String code = almeida_enrolment.getCoddis();
		//		Delete blank space in the beggining of code
		if (code.charAt(0) == ' ') {
			code = code.substring(1);
		}

		ICurricularCourse curricularCourse = persistentObjectOJB.readCurricularCourseByCodeAndDegreeCurricularPlan(code, oldDegreeCurricularPlan);
		if (curricularCourse == null) {
			logString += "ERRO: A curricularCourse com o code: " + code + " não existe!\n";
		}

		return curricularCourse;
	}

	private IBranch processBranch(long branchKeyLong) {
		Integer branchKey = new Integer("" + (branchKeyLong + 1));
		IBranch branch = persistentObjectOJB.readBranch(branchKey);
		if (branch == null) {
			logString += "ERRO: O ramo com a key: " + branchKeyLong + " não existe!\n";
		}
		return branch;
	}

	private String processUniversityCode(String universityCodeRead) {
		String universityCode = null;
		if (universityCode.equals("IST")) {
			universityCode = UniversityCode.IST;
		} else if (universityCode.equals("UBI")) {
			universityCode = UniversityCode.UBI;
		} else {
			logString += "Novo University Code: " + universityCodeRead + "\n";
		}
		return universityCode;
	}

	private EnrolmentEvaluationType processEvaluationType(long epocaLong) {
		EnrolmentEvaluationType enrolmentEvaluationType = null;

		int epoca = (new Integer("" + epocaLong)).intValue();
		switch (epoca) {
			case 0 :
				enrolmentEvaluationType = EnrolmentEvaluationType.NORMAL_OBJ;
				break;
			case 1 :
				enrolmentEvaluationType = EnrolmentEvaluationType.NORMAL_OBJ;
				break;
			case 2 :
				enrolmentEvaluationType = EnrolmentEvaluationType.NORMAL_OBJ;
				break;
			case 3 :
				enrolmentEvaluationType = EnrolmentEvaluationType.SPECIAL_OBJ;
				break;
			case 4 :
				enrolmentEvaluationType = EnrolmentEvaluationType.IMPROVEMENT_OBJ;
				break;
			case 5 :
				enrolmentEvaluationType = EnrolmentEvaluationType.EXTERNAL_OBJ;
				break;
			default :
				logString += "ERRO: A epoca [" + epocaLong + "] é invalida!\n";
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
		}

		IExecutionPeriod executionPeriod = persistentObjectOJB.readExecutionPeriodByYearAndSemester(executionYear, semester);
		if (executionPeriod == null) {
			executionPeriod = new ExecutionPeriod();
			executionPeriod.setExecutionYear(executionYear);
			executionPeriod.setSemester(semester);
			executionPeriod.setState(PeriodState.CLOSED);
			String name = null;
			if (semester.intValue() == 1) {
				name = "1º Semestre";
			} else if (semester.intValue() == 2) {
				name = "2º Semestre";
			}
			executionPeriod.setName(name);
			writeElement(executionPeriod);
		}

		return executionPeriod;
	}

	private IStudentCurricularPlan processStudentCurricularPlan(Almeida_enrolment almeida_enrolment) {

		IStudentCurricularPlan studentCurricularPlan = null;

		StudentCurricularPlanState studentCurricularPlanState = StudentCurricularPlanState.INCOMPLETE_OBJ;
		Integer studentNumber = new Integer(almeida_enrolment.getNumalu());
		IStudent student = persistentObjectOJB.readStudent(studentNumber, TipoCurso.LICENCIATURA_OBJ);

		if (student != null) {
			Integer year = new Integer("" + almeida_enrolment.getAnoins());
			int newYear = year.intValue();
			Calendar newCalendar = Calendar.getInstance();
			newCalendar.set(newYear, Calendar.JANUARY, 1);
			Date newDate = newCalendar.getTime();

			studentCurricularPlan = persistentObjectOJB.readStudentCurricularPlanByUnique(student, oldDegreeCurricularPlan, branch, studentCurricularPlanState);
			if (studentCurricularPlan == null) {
				studentCurricularPlan = new StudentCurricularPlan();
				studentCurricularPlan.setBranch(branch);
				studentCurricularPlan.setCurrentState(studentCurricularPlanState);
				studentCurricularPlan.setDegreeCurricularPlan(oldDegreeCurricularPlan);
				studentCurricularPlan.setStudent(student);
				studentCurricularPlan.setStartDate(newDate);
				writeElement(studentCurricularPlan);
			} else {
				Date oldDate = studentCurricularPlan.getStartDate();
				Calendar oldCalendar = Calendar.getInstance();
				oldCalendar.setTime(oldDate);
				int oldYear = oldCalendar.get(Calendar.YEAR);

				if (newYear < oldYear) {
					studentCurricularPlan.setStartDate(newDate);
					writeElement(studentCurricularPlan);
				}
			}
		} else {
			logString += "ERRO: O aluno numero: " + almeida_enrolment.getNumalu() + " não está inscrito este semsstre!\n";
		}

		return studentCurricularPlan;
	}

	private IDegreeCurricularPlan processOldDegreeCurricularPlan() {
		IDegreeCurricularPlan degreeCurricularPlan = persistentObjectOJB.readDegreeCurricularPlanByName("LEQ-OLD");
		if (degreeCurricularPlan == null) {
			logString += "ERRO: O plano curricular: " + degreeCurricularPlan.getName() + " não existe!\n";
		}
		return degreeCurricularPlan;
	}

	protected String getFilename() {
		return "etc/migration/dcs-rjao/almeidaLEQData/curriculo_05.txt";
	}

	protected String getFieldSeparator() {
		return "\t";
	}

	protected String getFilenameOutput() {
		return "etc/migration/dcs-rjao/logs/enrolmentMigrationLog.txt";
	}
}