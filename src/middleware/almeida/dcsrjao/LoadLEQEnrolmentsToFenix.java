package middleware.almeida.dcsrjao;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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

// TODO DAVID-RICARDO: Ainda falta ter em conta os casos de inscrições em disciplinas de outros cursos e erasmos.
//01			CURSO DE ENGENHARIA CIVIL                               
//01	1	1	PERFIL ESTRUTURAS E CONSTRUÇÃO/ESTRUTURA                
//01	1	2	PERFIL ESTRUTURAS E CONSTRUÇÃO/CONSTRUÇÃO               
//01	2      	PERFIL HIDRÁULICA E  RECURSOS HÍDRICOS                  
//01	3     	PERFIL PLANEAMENTO TRANSPORTES E GESTÃO                 
//
//02	       	CURSO DE ENGENHARIA DE MINAS E GEORRECURSOS             
//02	1      	RAMO DE GEOENGENHARIA                                   
//02	2      	RAMO DE EXPLORAÇÃO E GEOMECÂNICA                        
//02	3      	RAMO DE PROCESSAMENTO E PLANEAMENTO DE GEORRECURSOS     
//
//07	       	CURSO DE ENGENHARIA FÍSICA TECNOLÓGICA                  
//
//09			CURSO DE MATEMÁTICA APLICADA E COMPUTAÇÃO               
//
//12	       	CURSO DE ENGENHARIA DO TERRITÓRIO                       
//
//13	       	CURSO DE ENGENHARIA AEROESPACIAL                        
//13	1      	RAMO DE AERONAVES                                       
//13	2      	RAMO DE AVIÓNICA                                        
//
//14	       	CURSO DE ENGENHARIA ELECTROTÉCNICA E DE COMPUTADORES    
//14	1      	RAMO DE ENERGIA E SISTEMAS                              
//14	2      	RAMO DE TELECOMUNICAÇÕES E ELECTRÓNICA                  
//14	3      	RAMO DE CONTROLO E ROBÓTICA                             
//14	4      	RAMO DE SISTEMAS ELECTRÓNICOS E COMPUTADORES            
//
//16	       	CURSO DE QUÍMICA                                        
//16	1      	PERFIL DE QUÍMICA                                       
//16	2      	PERFIL DE BIOQUÍMICA

public class LoadLEQEnrolmentsToFenix extends LoadDataToFenix {

	private static LoadLEQEnrolmentsToFenix loader = null;
	private static String logString = "";

	private IDegreeCurricularPlan oldDegreeCurricularPlan = null;
	private IBranch branch = null;
	private ICurricularCourse curricularCourse = null;
	private ICurricularSemester curricularSemester = null;
	private IStudentCurricularPlan studentCurricularPlan = null;
	private IExecutionPeriod executionPeriod = null;
	private ICurricularCourseScope curricularCourseScope = null;

	public LoadLEQEnrolmentsToFenix() {
		super.setupDAO();
		this.oldDegreeCurricularPlan = this.processOldDegreeCurricularPlan();
		super.shutdownDAO();
	}

	public static void main(String[] args) {
		if (loader == null) {
			loader = new LoadLEQEnrolmentsToFenix();
		}

		loader.migrationStart("LoadLEQEnrolmentsToFenix");
		
		loader.processAllEnrolments();
		loader.writeToFile(logString);

		loader.migrationEnd("LoadLEQEnrolmentsToFenix", logString);
	}

	private void processAllEnrolments() {
		List almeida_enrolments = persistentObjectOJB.readAllAlmeidaEnrolemts();
		Iterator iterator = almeida_enrolments.iterator();
		while (iterator.hasNext()) {
			Almeida_enrolment almeida_enrolment = (Almeida_enrolment) iterator.next();
			processEnrolment(almeida_enrolment);
		}
	}

	private void processEnrolment(Almeida_enrolment almeida_enrolment) {

		// Estes dados têm que existir na BD ou algo de muito errado aconteceu como por exemplo:
		//   . Não foram corridos os scripts que devem ser corridos antes.
		//   . Não foi carregada a BD com o devido código SQL.
		this.branch = processBranch(almeida_enrolment.getRamo());
		this.curricularCourse = processCurricularCourse(almeida_enrolment);
		this.curricularSemester = processCurricularSemester(almeida_enrolment);

		this.studentCurricularPlan = processStudentCurricularPlan(almeida_enrolment);

		if (this.studentCurricularPlan != null) { // Caso em que o aluno não se encontra na BD, ou seja, não está inscrito no semestre actual.
			this.executionPeriod = processExecutionPeriod(almeida_enrolment.getAnoins(), almeida_enrolment.getSemdis());
			this.curricularCourseScope = processCurricularCourseScope();

			IEnrolment enrolment = persistentObjectOJB.readEnrolment(this.studentCurricularPlan, this.curricularCourse, this.executionPeriod);
			if (enrolment == null) {
				enrolment = new Enrolment();
				enrolment.setCurricularCourseScope(this.curricularCourseScope);
				enrolment.setExecutionPeriod(this.executionPeriod);
				enrolment.setStudentCurricularPlan(this.studentCurricularPlan);
				enrolment.setEnrolmentEvaluationType(processEvaluationType(almeida_enrolment.getEpoca()));
				enrolment.setEnrolmentState(processGrade(almeida_enrolment));
				// TODO DAVID-RICARDO: Por os university codes numa tabela
				// enrolment.setUniversityCode(processUniversityCode(almeida_enrolment.getCoduniv()));

				// TODO David-Ricardo: Falta tratar as inscrições noutra universidade
				writeElement(enrolment);
			}

			if (almeida_enrolment.getEpoca() != 0) { // Caso em que não foi lançada uma nota e por isso não se cria uma avaliação.
				IEnrolmentEvaluation enrolmentEvaluation = processEvaluation(enrolment, almeida_enrolment);
				writeElement(enrolmentEvaluation);
			} else {
				logString += "INFO: O enrolment para o aluno: " + almeida_enrolment.getNumalu() + " na cadeira " + curricularCourse.getName() + " no periodo " + executionPeriod.getName() + " " + executionPeriod.getExecutionYear().getYear() + " no plano curricular " + studentCurricularPlan.getDegreeCurricularPlan().getName() + " não tem nota e por isso não foi adicionada uma avaliação!\n";
			}
		}
	}

	private EnrolmentState processGrade(Almeida_enrolment almeida_enrolment) {
		String grade = almeida_enrolment.getResult();
		EnrolmentState enrolmentState = null;
		if (grade.equals("RE")) {
			enrolmentState = EnrolmentState.NOT_APROVED_OBJ;
		} else if (grade == null) {
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

		if (almeida_enrolment.getDatexa() != null) {
			Calendar newCalendar = Calendar.getInstance();
			int year = new Integer(almeida_enrolment.getDatexa().substring(0, 3)).intValue();
			int month = new Integer(almeida_enrolment.getDatexa().substring(5, 6)).intValue();
			int day = new Integer(almeida_enrolment.getDatexa().substring(8, 9)).intValue();
			newCalendar.set(year, month, day);
			Date examDate = newCalendar.getTime();
			enrolmentEvaluation.setExamDate(examDate);
		} else {
			enrolmentEvaluation.setExamDate(null);
		}

		if (almeida_enrolment.getResult() != null) {
			enrolmentEvaluation.setGrade(almeida_enrolment.getResult());
		} else {
			enrolmentEvaluation.setGrade(null);
		}

		enrolmentEvaluation.setGradeAvailableDate(null);

		if (almeida_enrolment.getNumdoc() != null) {
			enrolmentEvaluation.setPersonResponsibleForGrade(persistentObjectOJB.readPersonByEmployeeNumber(new Integer(almeida_enrolment.getNumdoc())));
		} else {
			enrolmentEvaluation.setPersonResponsibleForGrade(null);
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
		ICurricularCourseScope curricularCourseScope = persistentObjectOJB.readCurricularCourseScopeByUnique(this.curricularCourse, this.branch, this.curricularSemester);
		if (curricularCourseScope == null) {
			curricularCourseScope = new CurricularCourseScope();
			curricularCourseScope.setBranch(this.branch);
			curricularCourseScope.setCurricularCourse(this.curricularCourse);
			curricularCourseScope.setCurricularSemester(this.curricularSemester);
			curricularCourseScope.setMaxIncrementNac(new Integer(2));
			curricularCourseScope.setMinIncrementNac(new Integer(1));
			curricularCourseScope.setWeigth(new Integer(1));
			writeElement(curricularCourseScope);
		}
		return curricularCourseScope;
	}

	private ICurricularCourse processCurricularCourse(Almeida_enrolment almeida_enrolment) {
		ICurricularCourse curricularCourse = persistentObjectOJB.readCurricularCourseByCodeAndDegreeCurricularPlan(almeida_enrolment.getCoddis(), this.oldDegreeCurricularPlan);
		if (curricularCourse == null) {
			logString += "ERRO: A CurricularCourse com o code: " + almeida_enrolment.getCoddis() + " não existe!\n";
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
				enrolmentEvaluationType = EnrolmentEvaluationType.FIRST_TIME_OBJ;
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

			studentCurricularPlan = persistentObjectOJB.readStudentCurricularPlanByUnique(student, this.oldDegreeCurricularPlan, this.branch, studentCurricularPlanState);
			if (studentCurricularPlan == null) {
				studentCurricularPlan = new StudentCurricularPlan();
				studentCurricularPlan.setBranch(this.branch);
				studentCurricularPlan.setCurrentState(studentCurricularPlanState);
				studentCurricularPlan.setDegreeCurricularPlan(this.oldDegreeCurricularPlan);
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
		IDegreeCurricularPlan degreeCurricularPlan = persistentObjectOJB.readDegreeCurricularPlanByName(LoadLEQEnrolmentsToFenix.OLD_DEGREE_CURRICULAR_PLAN_NAME);
		if (degreeCurricularPlan == null) {
			logString += "ERRO: O plano curricular: " + degreeCurricularPlan.getName() + " não existe!\n";
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
		return "etc/migration/dcs-rjao/logs/LoadLEQEnrolmentsToFenix.txt";
	}

	protected void processLine(String line) {		
	}
}