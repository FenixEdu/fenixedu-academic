package middleware.marks;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import Dominio.IDisciplinaExecucao;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IFrequenta;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.ITeacher;
import Dominio.StudentCurricularPlan;
import Util.EnrolmentEvaluationState;
import Util.EnrolmentEvaluationType;
import Util.EnrolmentState;
import Util.PeriodState;
import Util.StudentCurricularPlanState;
import Util.TipoCurso;

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
	private List enrolmentsAlmeida = new ArrayList();

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
		System.out.println("-->A tratar linha " + numberLine);
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
			grade = "NA";
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

		if (enrolmentThisYear(almeida_enrolment)) {
			processEnrolment(almeida_enrolment);
		}
	}

	private boolean enrolmentThisYear(Almeida_enrolment almeida_enrolment) {
		String enrolment_year = String.valueOf(almeida_enrolment.getAnoins()) + "/" + String.valueOf(almeida_enrolment.getAnoins() + 1);

		return enrolment_year.equals(EXECUTION_YEAR);
	}

	private void processEnrolment(Almeida_enrolment almeida_enrolment) {
		IDegreeCurricularPlan degreeCurricularPlan = readDegreeCurricularPlan(almeida_enrolment);
		IBranch branch = readBranch(almeida_enrolment, degreeCurricularPlan);

		IStudentCurricularPlan studentCurricularPlan = readStudentCurricularPlan(almeida_enrolment, degreeCurricularPlan, branch);
		ICurricularCourse curricularCourse = readCurricularCourse(almeida_enrolment, degreeCurricularPlan);

		ICurricularCourseScope curricularCourseScope = null;
		IExecutionPeriod executionPeriod = null;
		IDisciplinaExecucao disciplinaExecucao = null;

		if (studentCurricularPlan == null) {
			logString += numberLine + "-ERRO: Plano curricular do estudante " + almeida_enrolment.getNumalu() + " desconhecido\n";
		} else if (curricularCourse == null) {
			logString += numberLine + "-ERRO: Disciplina curricular " + almeida_enrolment.getCoddis() + " desconhecida\n";
		} else {

			if (degreeCurricularPlan == null) {
				logString += numberLine + "-ERRO: Plano curricular do curso " + almeida_enrolment.getCurso() + " desconhecidos\n";
			} else {
				curricularCourseScope = readCurricularCourseScope(almeida_enrolment, curricularCourse, branch);

				if (curricularCourseScope == null) {
					logString += numberLine
						+ "-ERRO: CurricularCourseScope: disciplina= "
						+ almeida_enrolment.getCoddis()
						+ "  curso= "
						+ almeida_enrolment.getCurso()
						+ "\n";
					//writeElement(almeida_inscricoes);
					numberUntreatableElements++;
					return;
				} else {
					//update enrolment
					executionPeriod = readActualExecutionPeriod();
					IEnrolment enrolment = readEnrolment(studentCurricularPlan, curricularCourseScope, executionPeriod);
					if (enrolment == null) {
						enrolment = new Enrolment();
						enrolment.setCurricularCourseScope(curricularCourseScope);
						enrolment.setExecutionPeriod(executionPeriod);
						enrolment.setEnrolmentEvaluationType(readEvaluationType(almeida_enrolment.getEpoca()));
						enrolment.setEnrolmentState(readStateEnrolment(almeida_enrolment));
						enrolment.setStudentCurricularPlan(studentCurricularPlan);

						writeElement(enrolment);
					}

					if (enrolment != null) {
						//update evaluation
						IEnrolmentEvaluation enrolmentEvaluation =
							persistentObjectOJB.readEnrolmentEvaluationByUnique(
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
								ITeacher teacher = persistentObjectOJB.readTeacher(Integer.valueOf(almeida_enrolment.getNumdoc()));
								if (teacher != null) {
									enrolmentEvaluation.setPersonResponsibleForGrade(teacher.getPerson());
								} else {
									logString += numberLine + "-ERRO: Professor " + almeida_enrolment.getNumdoc() + " desconhecidos\n";
								}
							}
							if (almeida_enrolment.getObserv() != null) {
								enrolmentEvaluation.setObservation("carregamentos: " + almeida_enrolment.getObserv());
							} else {
								enrolmentEvaluation.setObservation("carregamentos");
							}

							Calendar calendar = Calendar.getInstance();
							enrolmentEvaluation.setWhen(calendar.getTime());

							writeElement(enrolmentEvaluation);
						}

						//update frequenta
						disciplinaExecucao = readExecutionCourse(curricularCourse, executionPeriod);
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
								+ "-ERRO: disciplina Execucao: code= "
								+ curricularCourse.getCode()
								+ "  name= "
								+ curricularCourse.getName()
								+ "  curso= "
								+ almeida_enrolment.getCurso()
								+ "\n";
							//writeElement(almeida_inscricoes);
							numberUntreatableElements++;
							return;
						}
					}
				}
			}
		}
	}

	private IStudentCurricularPlan readStudentCurricularPlan(
		Almeida_enrolment almeida_enrolment,
		IDegreeCurricularPlan degreeCurricularPlan,
		IBranch branch) {
		IStudentCurricularPlan studentCurricularPlan =
			persistentObjectOJB.readStudentCurricularPlan(new Integer("" + almeida_enrolment.getNumalu()));
		if (studentCurricularPlan == null) {
			IStudent student =
				persistentObjectOJB.readStudent(new Integer("" + almeida_enrolment.getNumalu()), new TipoCurso(TipoCurso.LICENCIATURA));

			if (student != null && degreeCurricularPlan != null) {

				studentCurricularPlan =
					new StudentCurricularPlan(
						student,
						degreeCurricularPlan,
						branch,
						new Date(),
						new StudentCurricularPlanState(StudentCurricularPlanState.ACTIVE));
			} else {
				logString += numberLine
					+ "-ERRO: Estudante "
					+ almeida_enrolment.getNumalu()
					+ " ou curso curricular "
					+ almeida_enrolment.getCurso()
					+ " desconhecidos\n";
			}

			writeElement(studentCurricularPlan);
		}

		return studentCurricularPlan;
	} //readStudentCurricularPlan

	private ICurricularCourse readCurricularCourse(Almeida_enrolment almeida_enrolment, IDegreeCurricularPlan degreeCurricularPlan) {
		ICurricularCourse curricularCourse = null;

		// First read Almeidas curricular course
		Almeida_disc almeida_disc =
			persistentObjectOJB.readAlmeidaCurricularCourse(
				almeida_enrolment.getCoddis(),
				Long.valueOf(almeida_enrolment.getCurso()).longValue(),
				almeida_enrolment.getAnoins());

		// Log the ones that don't exist in his database!
		if (almeida_disc == null) {
			writeElement(almeida_enrolment);
			numberUntreatableElements++;
		} else {
			// Read our corresponding curricular couse
			if (degreeCurricularPlan != null) {
				curricularCourse =
					persistentObjectOJB.readCurricularCourse(
						almeida_disc.getNomedis(),
						degreeCurricularPlan.getIdInternal(),
						almeida_disc.getCoddis());
				if (curricularCourse == null) {
					// Log the ones we can't match
					writeElement(almeida_enrolment);
					numberUntreatableElements++;
				}
			}
		}

		return curricularCourse;
	} //readCurricularCourse

	private ICurricularCourseScope readCurricularCourseScope(
		Almeida_enrolment almeida_enrolment,
		ICurricularCourse curricularCourse,
		IBranch branch) {

		ICurricularYear curricularYear =
			persistentObjectOJB.readCurricularYearByYear(new Integer(String.valueOf(almeida_enrolment.getAnodis())));
		if (curricularYear == null) {
			logString += numberLine + "-ERRO: curricularYear da disciplina " + almeida_enrolment.getCoddis() + " desconhecido\n";
			return null;
		}

		ICurricularSemester curricularSemester =
			persistentObjectOJB.readCurricularSemester(new Integer(String.valueOf(almeida_enrolment.getSemdis())), curricularYear);
		if (curricularSemester != null) {
			if (branch != null) {
				return persistentObjectOJB.readCurricularCourseScopeByUnique(
					curricularCourse,
					branch,
					curricularSemester,
					new Integer(String.valueOf(almeida_enrolment.getAnoins())));
			} else {
				return persistentObjectOJB.readCurricularCourseScopeByUniqueWithoutBranch(
					curricularCourse,
					curricularSemester,
					new Integer(String.valueOf(almeida_enrolment.getAnoins())));
			}
		} else {
			logString += numberLine + "-ERRO: curricularSemester da disciplina " + almeida_enrolment.getCoddis() + " desconhecido\n";
			return null;
		}
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

	//Read enrolment's execution period
	private IExecutionPeriod readExecutionPeriod(Almeida_enrolment almeida_enrolment) {
		Integer semester = new Integer("" + almeida_enrolment.getSemdis());
		Integer year = new Integer("" + almeida_enrolment.getAnoins());

		String yearStr = null;
		if (semester.equals(new Integer(1))) {
			yearStr = new String(year.intValue() + "/" + (year.intValue() + 1));
		} else if (semester.equals(new Integer(2))) {
			yearStr = new String((year.intValue() - 1) + "/" + year.intValue());
		}

		IExecutionPeriod executionPeriod = null;
		IExecutionYear executionYear = persistentObjectOJB.readExecutionYearByYear(yearStr);
		if (executionYear == null) {
			executionYear = new ExecutionYear();
			executionYear.setState(PeriodState.CLOSED);
			executionYear.setYear(yearStr);
			writeElement(executionYear);
		}

		executionPeriod =
			persistentObjectOJB.readExecutionPeriodByYearAndSemester(
				executionYear,
				new Integer(new Float(almeida_enrolment.getSemdis()).intValue()));
		if (executionPeriod != null) {
			return executionPeriod;
		} else {
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

			return executionPeriod;
		}
	} //readExecutionPeriod

	private IDisciplinaExecucao readExecutionCourse(ICurricularCourse curricularCourse, IExecutionPeriod executionPeriod) {
		return persistentObjectOJB.readExecutionCourse(curricularCourse, executionPeriod);
	}

	private IDegreeCurricularPlan readDegreeCurricularPlan(Almeida_enrolment almeida_enrolment) {
		IDegreeCurricularPlan degreeCurricularPlan =
			persistentObjectOJB.readDegreeCurricularPlanByDegreeIDAndActiveState(new Integer("" + almeida_enrolment.getCurso()));
		if (degreeCurricularPlan != null) {
			return degreeCurricularPlan;
		} else {
			logString += numberLine + "-ERRO: Degree " + almeida_enrolment.getCurso() + " desconhecido\n";
			return null;
		}
	}

	private IBranch readBranch(Almeida_enrolment almeida_enrolment, IDegreeCurricularPlan degreeCurricularPlan) {
		String code = "";
		if (almeida_enrolment.getRamo() != 0) {
			code = almeida_enrolment.getCurso() + almeida_enrolment.getRamo() + "0";
		}

		if (degreeCurricularPlan != null) {
			return persistentObjectOJB.readBranchByUnique(code, degreeCurricularPlan);
		} else {
			return null;
		}
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
				logString += "ERRO: A epoca [" + epocaLong + "] é invalida!\n";
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
	//D:/projectos/_carregamentos/inscricoes/curriculo_05.txt
	//D:/projectos/_carregamentos/inscricoes/outputReadFile.txt
}
