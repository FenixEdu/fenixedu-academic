package middleware.marks;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import middleware.almeida.Almeida_disc;
import middleware.almeida.Almeida_enrolment;
import middleware.almeida.LoadDataFile;
import Dominio.Enrolment;
import Dominio.EnrolmentEvaluation;
import Dominio.Frequenta;
import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.IDegreeCurricularPlan;
import Dominio.IDisciplinaExecucao;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IExecutionPeriod;
import Dominio.IFrequenta;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.ITeacher;
import Dominio.StudentCurricularPlan;
import Util.EnrolmentEvaluationState;
import Util.EnrolmentEvaluationType;
import Util.EnrolmentState;
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

	private static ReadFile loader = null;
	private static String logString = "";
	private List enrolmentsAlmeida = new ArrayList();

	public ReadFile() {
	}

	public static void main(String[] args) {
		//load data from file with enrolments and marks to database 
		if (loader == null) {
			loader = new ReadFile();
		}
		loader.load();
		loader.writeToFile(logString);
	}

	public void processLine(String line) {
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
			grade = null;
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
			logString += "ERRO: Os valores lidos do ficheiro são invalidos para a criação de Integers!\n";
			return;
		}

		almeida_enrolment.setCoddis(curricularCourseCode);
		almeida_enrolment.setCurso(degreeCode);
		almeida_enrolment.setResult(grade);
		almeida_enrolment.setNumdoc(teacherNumber);
		almeida_enrolment.setDatexa(evaluationDate);
		almeida_enrolment.setCoduniv(universityCode);
		almeida_enrolment.setObserv(observation);

		processEnrolement(almeida_enrolment);
	}

	private void processEnrolement(Almeida_enrolment almeida_enrolment) {
		IStudentCurricularPlan studentCurricularPlan = readStudentCurricularPlan(almeida_enrolment);
		ICurricularCourse curricularCourse = readCurricularCourse(almeida_enrolment);
		IExecutionPeriod executionPeriod = null;
		IDisciplinaExecucao disciplinaExecucao = null;

		if (studentCurricularPlan != null && curricularCourse != null) {
			executionPeriod = readActiveExecutionPeriod();

			//update enrolment
			IEnrolment enrolment = persistentObjectOJB.readEnrolment(studentCurricularPlan, curricularCourse, executionPeriod);
			if (enrolment == null) {
				enrolment = new Enrolment();
				//enrolment.setCurricularCourse(curricularCourse);
				enrolment.setExecutionPeriod(executionPeriod);
				enrolment.setEnrolmentEvaluationType(EnrolmentEvaluationType.CLOSED_OBJ);
				enrolment.setEnrolmentState(EnrolmentState.ENROLED_OBJ);
				enrolment.setStudentCurricularPlan(studentCurricularPlan);

				writeElement(enrolment);
			} else {
				//enrolment.setCurricularCourse(curricularCourse);
				//enrolment.setExecutionPeriod(executionPeriod);
				enrolment.setEnrolmentState(EnrolmentState.ENROLED_OBJ);
				//enrolment.setStudentCurricularPlan(studentCurricularPlan);

				writeElement(enrolment);
			}

			//update frequenta
			disciplinaExecucao = readExecutionCourse(curricularCourse, executionPeriod);
			if (disciplinaExecucao != null) {
				IFrequenta frequenta = persistentObjectOJB.readFrequenta(studentCurricularPlan.getStudent(), disciplinaExecucao);
				if (frequenta == null) {
					frequenta = new Frequenta(studentCurricularPlan.getStudent(), disciplinaExecucao);
					writeElement(frequenta);
				}
				//else {
				//frequenta.setAluno(studentCurricularPlan.getStudent());
				//frequenta.setDisciplinaExecucao(disciplinaExecucao);
				//writeElement(frequenta);
				//}
			} else {
				logString += "Curricular: code= "
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

			//update evaluation
			IEnrolmentEvaluation enrolmentEvaluation =
				persistentObjectOJB.readEnrolmentEvaluationByUnique(
					enrolment,
					enrolment.getEnrolmentEvaluationType(),
					almeida_enrolment.getResult());
			if (enrolmentEvaluation == null) {
				enrolmentEvaluation = new EnrolmentEvaluation();
				enrolmentEvaluation.setGrade(almeida_enrolment.getResult());
				enrolmentEvaluation.setEnrolmentEvaluationType(enrolment.getEnrolmentEvaluationType());
				enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.FINAL_OBJ);
				try {
					DateFormat dateFormat = DateFormat.getDateInstance();
					enrolmentEvaluation.setExamDate(dateFormat.parse(almeida_enrolment.getDatexa()));
				} catch (ParseException e) {
					e.printStackTrace();
					logString += "ERRO: A data de avaliação "
						+ almeida_enrolment.getDatexa()
						+ " do ficheiro é invalida para a criação Datas!\n";
				}

				enrolmentEvaluation.setEnrolment(enrolment);
				
				ITeacher teacher = persistentObjectOJB.readTeacher(Integer.valueOf(almeida_enrolment.getNumdoc()));
				if(teacher != null){
					enrolmentEvaluation.setPersonResponsibleForGrade(teacher.getPerson());
				} else {
					logString += "ERRO: Professor "
										+ almeida_enrolment.getNumdoc()
										+ " desconhecidos\n";
				}
				enrolmentEvaluation.setObservation(almeida_enrolment.getObserv());
			}
		} else {
			logString += "ERRO: Plano curricular do estudante "
				+ almeida_enrolment.getNumalu()
				+ " ou Disciplina curricular "
				+ almeida_enrolment.getCoddis()
				+ " desconhecidos\n";
		}
	}

	private IStudentCurricularPlan readStudentCurricularPlan(Almeida_enrolment almeida_enrolment) {
		IStudentCurricularPlan studentCurricularPlan =
			persistentObjectOJB.readStudentCurricularPlan(new Integer("" + almeida_enrolment.getNumalu()));
		if (studentCurricularPlan == null) {
			IStudent student =
				persistentObjectOJB.readStudent(new Integer("" + almeida_enrolment.getNumalu()), new TipoCurso(TipoCurso.LICENCIATURA));

			IDegreeCurricularPlan degreeCurricularPlan =
				persistentObjectOJB.readDegreeCurricularPlanByDegreeID(new Integer("" + almeida_enrolment.getCurso()));

			if (student != null && degreeCurricularPlan != null) {
				IBranch branch = getBranch(almeida_enrolment, degreeCurricularPlan);

				studentCurricularPlan =
					new StudentCurricularPlan(
						student,
						degreeCurricularPlan,
						branch,
						new Date(),
						new StudentCurricularPlanState(StudentCurricularPlanState.ACTIVE));

				studentCurricularPlan.setBranch(branch);
			} else {
				logString += "ERRO: Estudante "
					+ almeida_enrolment.getNumalu()
					+ " ou curso curricular "
					+ almeida_enrolment.getCurso()
					+ " desconhecidos\n";
			}

			writeElement(studentCurricularPlan);
		}

		return studentCurricularPlan;
	} //readStudentCurricularPlan

	private ICurricularCourse readCurricularCourse(Almeida_enrolment almeida_enrolment) {
		ICurricularCourse curricularCourse = null;

		// First read Almeidas curricular course
		Almeida_disc almeida_disc =
			persistentObjectOJB.readAlmeidaCurricularCourse(
				almeida_enrolment.getCoddis(),
				Long.valueOf(almeida_enrolment.getCurso()).longValue());

		// Log the ones that don't exist in his database!
		if (almeida_disc == null) {
			writeElement(almeida_enrolment);
			numberUntreatableElements++;
		} else {
			// Read our corresponding curricular couse
			curricularCourse =
				persistentObjectOJB.readCurricularCourse(
					almeida_disc.getNomedis(),
					new Integer("" + almeida_disc.getCodcur()),
					almeida_disc.getCoddis());
			if (curricularCourse == null) {
				// Log the ones we can't match
				writeElement(almeida_enrolment);
				numberUntreatableElements++;
			}
		}

		return curricularCourse;
	} //readCurricularCourse

	private IExecutionPeriod readActiveExecutionPeriod() {
		return persistentObjectOJB.readActiveExecutionPeriod();
	}

	private IDisciplinaExecucao readExecutionCourse(ICurricularCourse curricularCourse, IExecutionPeriod executionPeriod) {
		return persistentObjectOJB.readExecutionCourse(curricularCourse, executionPeriod);
	}

	private IBranch getBranch(Almeida_enrolment almeida_enrolment, IDegreeCurricularPlan degreeCurricularPlan) {
		return persistentObjectOJB.readBranchByUnique(String.valueOf(almeida_enrolment.getRamo()), degreeCurricularPlan);
	}

	protected String getFilename() {
		return "etc/migration/dcs-rjao/almeidaLEQData/curriculo_05.txt";
	}

	protected String getFieldSeparator() {
		return "\t";
	}

	protected String getFilenameOutput() {
		return "etc/migration/dcs-rjao/logs/ReadFile.txt";
	}
}
