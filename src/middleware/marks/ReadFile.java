package middleware.marks;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import middleware.almeida.Almeida_disc;
import middleware.almeida.Almeida_enrolment;
import middleware.almeida.LoadDataFile;
import Dominio.Enrolment;
import Dominio.EnrolmentEvaluation;
import Dominio.ExecutionPeriod;
import Dominio.ExecutionYear;
import Dominio.Frequenta;
import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICurricularSemester;
import Dominio.ICurricularYear;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionCourse;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IFrequenta;
import Dominio.IStudentCurricularPlan;
import Dominio.ITeacher;
import Util.DegreeCurricularPlanState;
import Util.EnrolmentEvaluationState;
import Util.EnrolmentEvaluationType;
import Util.EnrolmentState;
import Util.PeriodState;

/**
 * @author Tânia Pousão
 *
 */
public class ReadFile extends LoadDataFile {
	private static final String ONE_SPACE = " ";
	private static final String TWO_SPACES = "  ";
	private static final String FOUR_SPACES = "    ";
	private static final String TEN_SPACES = "          ";
	private static final String FIFTEEN_SPACES = "               ";

	private static final String EXECUTION_YEAR = "2002/2003";
	private static final int EXECUTION_SEMESTER = 2;

	private static ReadFile loader = null;
	private String fileName = null;
	private String fileOutputName = "outputMarks.txt";

	private static String logString = "";
	private int numberLine = 0;
	public ReadFile() {
	}

	public ReadFile(String fileName) {
		this.fileName = fileName;
	}

	public ReadFile(String fileName, String fileOutputName) {
		this.fileName = fileName;
		this.fileOutputName = fileOutputName;
	}

	public static void main(String[] args) {
		//load data from file with enrolments and marks to database		 
		if (loader == null) {
			if (args.length == 2) {
				loader = new ReadFile(args[0], args[1]);
			} else if (args.length == 1) {
				loader = new ReadFile(args[0]);
			}
		}
		loader.load();
		loader.writeToFile(logString);
	}

	public void processLine(String line) {
		numberLine += 1;
		System.out.print("\n-->" + numberLine);
		StringTokenizer stringTokenizer = new StringTokenizer(line, getFieldSeparator());

		String studentNumber = stringTokenizer.nextToken();
		String enrolmentYear = stringTokenizer.nextToken();
		String curricularYear = stringTokenizer.nextToken();
		String curricularSemester = stringTokenizer.nextToken();
		String season = stringTokenizer.nextToken();
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

		if (season.equals(ReadFile.ONE_SPACE)) {
			season = "5"; // corresponde ao valor de uma inscrição noutra universidade	
		}
		if ((branchCode.equals(ReadFile.ONE_SPACE)) || (branchCode.equals("4"))) {
			branchCode = "0";
		}
		if (curricularCourseCode.startsWith(ReadFile.ONE_SPACE)) {
			curricularCourseCode = curricularCourseCode.substring(1);
		}
		if (grade.equals(ReadFile.TWO_SPACES)) {
			grade = "";
		}
		if (teacherNumber.equals(ReadFile.FOUR_SPACES)) {
			teacherNumber = null;
		}
		if (evaluationDate.equals(ReadFile.TEN_SPACES)) {
			evaluationDate = null;
		}
		if (observation.equals(ReadFile.FIFTEEN_SPACES)) {
			observation = null;
		}

		try {
			almeida_enrolment.setAnoins((new Integer(enrolmentYear)).longValue());
			almeida_enrolment.setAnodis((new Integer(curricularYear)).longValue());
			almeida_enrolment.setSemdis((new Integer(curricularSemester)).longValue());
			almeida_enrolment.setEpoca((new Integer(season)).longValue());
			almeida_enrolment.setRamo((new Integer(branchCode)).longValue());
		} catch (NumberFormatException e) {
			logString += numberLine + "-ERRO: Os valores lidos do ficheiro são invalidos para a criação de Integers!\n";
			return;
		}

		almeida_enrolment.setCoddis(curricularCourseCode);
		almeida_enrolment.setCurso(degreeCode);
		almeida_enrolment.setResult(grade);
		almeida_enrolment.setNumdoc(teacherNumber);
		almeida_enrolment.setDatexa(evaluationDate);
		almeida_enrolment.setCoduniv(universityCode);
		almeida_enrolment.setObserv(observation);

		if (isEnrolmentThisYear(almeida_enrolment)) {
			System.out.print("...");
			System.out.println("-->ENROLMENT: " + almeida_enrolment);

			processEnrolment(almeida_enrolment);
		}
	}

	private boolean isEnrolmentThisYear(Almeida_enrolment almeida_enrolment) {
		String enrolment_year = String.valueOf(almeida_enrolment.getAnoins()) + "/" + String.valueOf(almeida_enrolment.getAnoins() + 1);

		return (enrolment_year.equals(EXECUTION_YEAR)) && (almeida_enrolment.getSemdis() == EXECUTION_SEMESTER);
	}

	private void processEnrolment(Almeida_enrolment almeida_enrolment) {
		IStudentCurricularPlan studentCurricularPlan = readStudentCurricularPlan(almeida_enrolment);
		if (studentCurricularPlan == null) {
			logString += numberLine + "-ERRO: Student Curricular Plan " + almeida_enrolment.getNumalu() + " unknown\n";
			numberUntreatableElements++;
			return;
		}

		IDegreeCurricularPlan degreeCurricularPlan = readDegreeCurricularPlan(almeida_enrolment);
		if (degreeCurricularPlan == null) {
			logString += numberLine + "-ERRO: Degree Curricular Plan" + almeida_enrolment.getCurso() + " unknown\n";
			numberUntreatableElements++;
			return;
		}

		IBranch branch = readBranch(almeida_enrolment, degreeCurricularPlan);
		if (branch == null) {
			logString += numberLine + "-ERRO: Branch " + almeida_enrolment.getCurso() + "-" + almeida_enrolment.getRamo() + " unknown\n";
			numberUntreatableElements++;
			//return;
		}

		List curricularCourseAndScope = readCurricularCourseAndScope(almeida_enrolment, degreeCurricularPlan, branch);
		if (curricularCourseAndScope == null || curricularCourseAndScope.size() < 2) {
			logString += numberLine
				+ "-ERRO: Curricular Course and repective Scope: curricular course = "
				+ almeida_enrolment.getCoddis()
				+ "  degree = "
				+ almeida_enrolment.getCurso()
				+ " unknown\n";
			numberUntreatableElements++;
			return;
		} else if (curricularCourseAndScope == null || curricularCourseAndScope.size() > 2) {
			logString += numberLine
				+ "-ERRO: Curricular Course and repective Scope: curricular course = "
				+ almeida_enrolment.getCoddis()
				+ "  degree = "
				+ almeida_enrolment.getCurso()
				+ " more that one\n";
			numberUntreatableElements++;
			return;
		}

		ICurricularCourse curricularCourse = (ICurricularCourse) curricularCourseAndScope.get(0);
		ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) curricularCourseAndScope.get(1);

		IExecutionPeriod executionPeriod = readActualExecutionPeriod();

		//update enrolment
		IEnrolment enrolment = readEnrolment(studentCurricularPlan, curricularCourseScope, executionPeriod);
		if (enrolment == null) {
			enrolment = new Enrolment();
//			enrolment.setCurricularCourseScope(curricularCourseScope);
			enrolment.setExecutionPeriod(executionPeriod);
			enrolment.setEnrolmentEvaluationType(readEvaluationType(almeida_enrolment.getEpoca()));
			enrolment.setEnrolmentState(readStateEnrolment(almeida_enrolment));
			enrolment.setStudentCurricularPlan(studentCurricularPlan);

			writeElement(enrolment);
		}

		//update evaluation
		IEnrolmentEvaluation enrolmentEvaluation =
			persistentObjectOJB.readEnrolmentEvaluationByEvaluationDate(
				enrolment,
				enrolment.getEnrolmentEvaluationType(),
				almeida_enrolment.getResult(),
				readEvaluationDate(almeida_enrolment.getDatexa()));
		if (enrolmentEvaluation == null) {
			enrolmentEvaluation = new EnrolmentEvaluation();

			enrolmentEvaluation.setGrade(almeida_enrolment.getResult());
			enrolmentEvaluation.setEnrolmentEvaluationType(enrolment.getEnrolmentEvaluationType());
			enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.FINAL_OBJ);
			enrolmentEvaluation.setExamDate(readEvaluationDate(almeida_enrolment.getDatexa()));
			enrolmentEvaluation.setEnrolment(enrolment);

			if (almeida_enrolment.getNumdoc() != null) {
				Integer teacherNumber = null;

				try {
					teacherNumber = Integer.valueOf(almeida_enrolment.getNumdoc());
					ITeacher teacher = persistentObjectOJB.readTeacher(teacherNumber);
					if (teacher != null) {
						enrolmentEvaluation.setPersonResponsibleForGrade(teacher.getPerson());
					} else {
						logString += numberLine + "-ERRO: Teacher " + almeida_enrolment.getNumdoc() + " unknown\n";
					}
				} catch (NumberFormatException exception) {
					logString += numberLine + "-ERRO: Teacher: number = " + almeida_enrolment.getNumdoc() + " isn't a number\n";
				}
			}

			if (almeida_enrolment.getObserv() != null) {
				enrolmentEvaluation.setObservation("carregamentos: " + almeida_enrolment.getObserv());
			} else {
				enrolmentEvaluation.setObservation("carregamentos");
			}

			Calendar calendar = Calendar.getInstance();
			enrolmentEvaluation.setWhen(new Timestamp(calendar.getTimeInMillis()));

			writeElement(enrolmentEvaluation);
		}

		//update frequenta
		IExecutionCourse disciplinaExecucao = readExecutionCourse(curricularCourse, executionPeriod);
		if (disciplinaExecucao != null) {
			IFrequenta frequenta = persistentObjectOJB.readFrequenta(studentCurricularPlan.getStudent(), disciplinaExecucao);
			if (frequenta == null) {
				frequenta = new Frequenta(studentCurricularPlan.getStudent(), disciplinaExecucao);
				frequenta.setEnrolment(enrolment);

				writeElement(frequenta);
			} else {
				frequenta.setEnrolment(enrolment);

				writeElement(frequenta);
			}
		} else {
			logString += numberLine
				+ "-ERRO: Exectution Course: code= "
				+ curricularCourse.getCode()
				+ "  name= "
				+ curricularCourse.getName()
				+ "  dgree= "
				+ almeida_enrolment.getCurso()
				+ " unknown\n";
			//writeElement(almeida_inscricoes);
			numberUntreatableElements++;
			return;
		}
	}

	private IStudentCurricularPlan readStudentCurricularPlan(Almeida_enrolment almeida_enrolment) {
		return persistentObjectOJB.readStudentCurricularPlanByStudentNumber(new Integer("" + almeida_enrolment.getNumalu()));
	} //readStudentCurricularPlan

	private List readCurricularCourseAndScope(
		Almeida_enrolment almeida_enrolment,
		IDegreeCurricularPlan degreeCurricularPlan,
		IBranch branch) {
		List curricularCourseAndScope = null;

		// First search the curricular course in Almeida's table
		Almeida_disc almeida_disc =
			persistentObjectOJB.readAlmeidaCurricularCourseByCodeAndDegreeAndYear(
				almeida_enrolment.getCoddis(),
				Long.valueOf(almeida_enrolment.getCurso()).longValue(),
				almeida_enrolment.getAnoins());
		if (almeida_disc == null) {
			almeida_disc =
				persistentObjectOJB.readAlmeidaCurricularCourseByCodeAndYear(almeida_enrolment.getCoddis(), almeida_enrolment.getAnoins());
			if (almeida_disc == null) {
				//shoud exists
				logString += numberLine + "-ERRO: Almeida disciplina " + almeida_enrolment.getCoddis() + " unknown\n";
				numberUntreatableElements++;
				return null;
			}
		}

		// Read our corresponding curricular couse
		ICurricularCourse curricularCourse = null;
		curricularCourse =
			persistentObjectOJB.readCurricularCourseByNameAndDegreeIDAndCode(
				almeida_disc.getNomedis(),
				degreeCurricularPlan.getIdInternal(),
				almeida_disc.getCoddis());
		if (curricularCourse != null) {
			//read respective scope
			ICurricularCourseScope curricularCourseScope = readCurricularCourseScope(almeida_enrolment, curricularCourse, branch);
			if (curricularCourseScope != null) {
				curricularCourseAndScope = new ArrayList();
				curricularCourseAndScope.add(curricularCourse);
				curricularCourseAndScope.add(curricularCourseScope);
			}
		} else {
			curricularCourseAndScope = findCurricularCourseAndScope(almeida_disc, degreeCurricularPlan);
		}

		return curricularCourseAndScope;
	} //readCurricularCourseAndScope

	private List findCurricularCourseAndScope(Almeida_disc almeida_disc, IDegreeCurricularPlan degreeCurricularPlan) {
		List curricularCourseAndScope = null;

		//Filter curricular courses ocurred in degree active
		List curricularCoursesListInDegreeActive =
			persistentObjectOJB.readCurricularCourseByCodeAndNameInDegreeActive(almeida_disc.getNomedis(), almeida_disc.getCoddis());
		if (curricularCoursesListInDegreeActive == null || curricularCoursesListInDegreeActive.size() == 0) {
			logString += numberLine + "-ERRO: Curricular Course " + almeida_disc.getCoddis() + " unknown in active degree\n";
			return curricularCourseAndScope;
		}

		//Filter curricular courses ocurred in actual semester
		curricularCourseAndScope = new ArrayList();

		ICurricularCourse curricularCourse = null;
		ICurricularCourseScope curricularCourseScope = null;
		Iterator iterator = curricularCoursesListInDegreeActive.listIterator();
		while (iterator.hasNext()) {
			curricularCourse = (ICurricularCourse) iterator.next();
			curricularCourseScope =
				persistentObjectOJB.readCurricularCourseScopeBySemester(curricularCourse, new Integer(EXECUTION_SEMESTER));
			if (curricularCourseScope != null) {
				curricularCourseAndScope.add(curricularCourse);
				curricularCourseAndScope.add(curricularCourseScope);
			}
		}

		return curricularCourseAndScope;
	}

	private ICurricularCourseScope readCurricularCourseScope(
		Almeida_enrolment almeida_enrolment,
		ICurricularCourse curricularCourse,
		IBranch branch) {
		ICurricularCourseScope curricularCourseScope = null;

		ICurricularYear curricularYear =
			persistentObjectOJB.readCurricularYearByYear(new Integer(String.valueOf(almeida_enrolment.getAnodis())));
		if (curricularYear == null) {
			logString += numberLine + "-ERRO: course's Curricular Year  " + almeida_enrolment.getCoddis() + " unknown\n";
			return null;
		}

		ICurricularSemester curricularSemester =
			persistentObjectOJB.readCurricularSemesterBySemesterAndCurricularYear(
				new Integer(String.valueOf(almeida_enrolment.getSemdis())),
				curricularYear);
		if (curricularSemester == null) {
			logString += numberLine + "-ERRO: course's Curricular Semester " + almeida_enrolment.getCoddis() + " unknown\n";
			return null;
		}

		if (branch != null) {
			curricularCourseScope = persistentObjectOJB.readCurricularCourseScopeByUnique(curricularCourse, branch, curricularSemester);
			//new Integer(String.valueOf(almeida_enrolment.getAnoins())));
		} else {
			curricularCourseScope =
				persistentObjectOJB.readCurricularCourseScopeByUniqueWithoutBranch(
					curricularCourse,
					curricularSemester,
					new Integer(String.valueOf(almeida_enrolment.getAnoins())));
		}

		if (curricularCourseScope == null) {
			logString += numberLine
				+ "-ERRO: Curricular Course Scope: curricular course = "
				+ almeida_enrolment.getCoddis()
				+ "  degree = "
				+ almeida_enrolment.getCurso()
				+ " unknown\n";
			numberUntreatableElements++;
		}
		return curricularCourseScope;
	} //readCurricularScope

	private IExecutionPeriod readActualExecutionPeriod() {
		IExecutionPeriod executionPeriod = null;

		//executionPeriod = persistentObjectOJB.readActiveExecutionPeriod();

		//prof.Rito decide that Execution Period is builded hard code
		IExecutionYear executionYear = persistentObjectOJB.readExecutionYearByYear(EXECUTION_YEAR);
		if (executionYear == null) {
			executionYear = new ExecutionYear();
			executionYear.setState(PeriodState.CURRENT);
			executionYear.setYear(EXECUTION_YEAR);
			writeElement(executionYear);
		}

		executionPeriod = persistentObjectOJB.readExecutionPeriodByYearAndSemester(executionYear, new Integer(EXECUTION_SEMESTER));
		if (executionPeriod != null) {
			return executionPeriod;
		} else {
			executionPeriod = new ExecutionPeriod();
			executionPeriod.setExecutionYear(executionYear);
			executionPeriod.setSemester(new Integer(EXECUTION_SEMESTER));
			executionPeriod.setState(PeriodState.CURRENT);
			executionPeriod.setName(EXECUTION_SEMESTER + "º Semestre");
			writeElement(executionPeriod);

			return executionPeriod;
		}
	}

	private IExecutionCourse readExecutionCourse(ICurricularCourse curricularCourse, IExecutionPeriod executionPeriod) {
		return persistentObjectOJB.readExecutionCourse(curricularCourse, executionPeriod);
	}

	private IDegreeCurricularPlan readDegreeCurricularPlan(Almeida_enrolment almeida_enrolment) {
		return persistentObjectOJB.readDegreeCurricularPlanByDegreeKeyAndState(
			new Integer("" + almeida_enrolment.getCurso()),
			DegreeCurricularPlanState.ACTIVE_OBJ);
	}

	private IBranch readBranch(Almeida_enrolment almeida_enrolment, IDegreeCurricularPlan degreeCurricularPlan) {
		String code = "";
		if (almeida_enrolment.getRamo() != 0) {
			code = almeida_enrolment.getCurso() + almeida_enrolment.getRamo() + "0";
		}
		return persistentObjectOJB.readBranchByUnique(code, degreeCurricularPlan);
	}

	private IEnrolment readEnrolment(
		IStudentCurricularPlan studentCurricularPlan,
		ICurricularCourseScope curricularCourseScope,
		IExecutionPeriod executionPeriod) {
		return persistentObjectOJB.readEnrolmentByUnique(studentCurricularPlan, curricularCourseScope, executionPeriod);
	}

	private Date readEvaluationDate(String date) {
		if (date != null && date.length() > 5) {
			int year = new Integer(date.substring(0, 4)).intValue();
			int month = new Integer(date.substring(5, 7)).intValue();
			int day = new Integer(date.substring(8, 10)).intValue();

			Calendar calendar = Calendar.getInstance();
			calendar.set(year, month - 1, day, 00, 00, 00);

			return calendar.getTime();
		} else {
			return null;
		}
	}

	private EnrolmentEvaluationType readEvaluationType(long epocaLong) {
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
				enrolmentEvaluationType = EnrolmentEvaluationType.SPECIAL_SEASON_OBJ;
				break;
			case 4 :
				enrolmentEvaluationType = EnrolmentEvaluationType.IMPROVEMENT_OBJ;
				break;
			case 5 :
				enrolmentEvaluationType = EnrolmentEvaluationType.EXTERNAL_OBJ;
				break;
			default :
				logString += "ERRO: Season [" + epocaLong + "] is invalid!\n";
				break;
		}

		return enrolmentEvaluationType;
	}

	private EnrolmentState readStateEnrolment(Almeida_enrolment almeida_enrolment) {
		String grade = almeida_enrolment.getResult();
		EnrolmentState enrolmentState = null;
		if (grade != null && grade.equals("RE")) {
			enrolmentState = EnrolmentState.NOT_APROVED;
		} else if (grade == null) {
			enrolmentState = EnrolmentState.NOT_EVALUATED;
		} else if (almeida_enrolment.getObserv() != null && almeida_enrolment.getObserv().equals("ANULADO")) {
			enrolmentState = EnrolmentState.ANNULED;
		} else {
			enrolmentState = EnrolmentState.APROVED;
		}
		return enrolmentState;
	}

	protected String getFilename() {
		return fileName;
	}

	protected String getFieldSeparator() {
		return "\t";
	}

	protected String getFilenameOutput() {
		return fileOutputName;
	}
}
