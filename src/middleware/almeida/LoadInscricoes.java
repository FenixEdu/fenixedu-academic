/*
 * Created on May 15, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package middleware.almeida;

import java.util.Date;
import java.util.StringTokenizer;

import Dominio.Branch;
import Dominio.Enrolment;
import Dominio.Frequenta;
import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.IExecutionCourse;
import Dominio.IEnrolment;
import Dominio.IExecutionPeriod;
import Dominio.IFrequenta;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.StudentCurricularPlan;
import Util.EnrolmentEvaluationType;
import Util.EnrolmentState;
import Util.StudentCurricularPlanState;
import Util.TipoCurso;

/**
 *
 * @author  Luis Cruz & Sara Ribeiro
 */
public class LoadInscricoes extends LoadDataFile {

	private static LoadInscricoes loader = null;

	static String executionCoursesNotFound = "";

	private LoadInscricoes() {
	}

	public static void main(String[] args) {
		loader = new LoadInscricoes();
		loader.load();

		loader.writeToFile(executionCoursesNotFound);
	}

	protected void processLine(String line) {
		StringTokenizer stringTokenizer = new StringTokenizer(line, getFieldSeparator());

		String numero = stringTokenizer.nextToken();
		String ano = stringTokenizer.nextToken();
		String semestre = stringTokenizer.nextToken();
		String codigoDisciplina = stringTokenizer.nextToken();
		String anoInscricao = stringTokenizer.nextToken();
		String curso = stringTokenizer.nextToken();
		String ramo = stringTokenizer.nextToken();
		//String nota = stringTokenizer.nextToken();

		Almeida_inscricoes almeida_inscricoes = new Almeida_inscricoes();
		almeida_inscricoes.setCodint(loader.numberElementsWritten + 1);
		almeida_inscricoes.setNumero((new Integer(numero)).longValue());
		almeida_inscricoes.setAno((new Integer(ano)).longValue());
		almeida_inscricoes.setSemestre((new Integer(semestre)).longValue());
		almeida_inscricoes.setCoddis(codigoDisciplina);
		almeida_inscricoes.setAnoinscricao((new Integer(anoInscricao)).longValue());
		almeida_inscricoes.setCurso((new Integer(curso)).longValue());
		almeida_inscricoes.setRamo(ramo);

		processEnrolement(almeida_inscricoes);
	}

	private void processEnrolement(Almeida_inscricoes almeida_inscricoes) {
		IStudentCurricularPlan studentCurricularPlan = readStudentCurricularPlan(almeida_inscricoes);
		ICurricularCourse curricularCourse = readCurricularCourse(almeida_inscricoes);
		IExecutionPeriod executionPeriod = null;
		IExecutionCourse disciplinaExecucao = null;

		if (curricularCourse != null) {
			executionPeriod = readActiveExecutionPeriod();

			IEnrolment enrolment = persistentObjectOJB.readEnrolmentByOldUnique(studentCurricularPlan, curricularCourse, executionPeriod);
			if (enrolment == null) {
				enrolment = new Enrolment();
				//				enrolment.setCurricularCourse(curricularCourse);
				enrolment.setEnrolmentEvaluationType(EnrolmentEvaluationType.CLOSED_OBJ);
				enrolment.setExecutionPeriod(executionPeriod);
				enrolment.setEnrolmentState(EnrolmentState.ENROLED);
				enrolment.setStudentCurricularPlan(studentCurricularPlan);

				writeElement(enrolment);
			} else {
				//				enrolment.setCurricularCourse(curricularCourse);
				enrolment.setExecutionPeriod(executionPeriod);
				enrolment.setEnrolmentState(EnrolmentState.ENROLED);
				enrolment.setStudentCurricularPlan(studentCurricularPlan);
				writeElement(enrolment);
			}

			disciplinaExecucao = readExecutionCourse(curricularCourse, executionPeriod);

			if (disciplinaExecucao == null) {
				executionCoursesNotFound += "Curricular: code= "
					+ curricularCourse.getCode()
					+ "  name= "
					+ curricularCourse.getName()
					+ "  curso= "
					+ almeida_inscricoes.getCurso()
					+ "\n";
				//writeElement(almeida_inscricoes);
				numberUntreatableElements++;
			} else {
				IFrequenta frequenta = persistentObjectOJB.readFrequenta(studentCurricularPlan.getStudent(), disciplinaExecucao);
				if (frequenta == null) {
					frequenta = new Frequenta(studentCurricularPlan.getStudent(), disciplinaExecucao);
					writeElement(frequenta);
				} else {
					frequenta.setAluno(studentCurricularPlan.getStudent());
					frequenta.setDisciplinaExecucao(disciplinaExecucao);
					writeElement(frequenta);
				}
			}
		}
	}

	/**
	 * @param curricularCourse
	 * @param executionPeriod
	 * @return
	 */
	private IExecutionCourse readExecutionCourse(ICurricularCourse curricularCourse, IExecutionPeriod executionPeriod) {
		return persistentObjectOJB.readExecutionCourse(curricularCourse, executionPeriod);

	}

	/**
	 * @return
	 */
	private IExecutionPeriod readActiveExecutionPeriod() {
		return persistentObjectOJB.readActiveExecutionPeriod();
	}

	/**
	 * @param string
	 * @return
	 */
	private ICurricularCourse readCurricularCourse(Almeida_inscricoes almeida_inscricoes) {
		ICurricularCourse curricularCourse = null;

		// First read Almeidas curricular course
		Almeida_disc almeida_disc =
			persistentObjectOJB.readAlmeidaCurricularCourseByCodeAndDegree(almeida_inscricoes.getCoddis(), almeida_inscricoes.getCurso());

		// Log the ones that don't exist in his database!
		if (almeida_disc == null) {
			writeElement(almeida_inscricoes);
			numberUntreatableElements++;
		} else {
			// Read our corresponding curricular couse
			curricularCourse =
				persistentObjectOJB.readCurricularCourseByNameAndDegreeIDAndCode(
					almeida_disc.getNomedis(),
					new Integer("" + almeida_disc.getCodcur()),
					almeida_disc.getCoddis());
			if (curricularCourse == null) {
				// Log the ones we can't match
				writeElement(almeida_inscricoes);
				numberUntreatableElements++;
			}
		}

		return curricularCourse;
	}

	/**
	 * @param almeida_inscricoes
	 * @return
	 */
	private IStudentCurricularPlan readStudentCurricularPlan(Almeida_inscricoes almeida_inscricoes) {
		IStudentCurricularPlan studentCurricularPlan =
			persistentObjectOJB.readStudentCurricularPlanByStudentNumber(new Integer("" + almeida_inscricoes.getNumero()));

		if (studentCurricularPlan == null) {
			IStudent student =
				persistentObjectOJB.readStudentByNumberAndDegreeType(new Integer("" + almeida_inscricoes.getNumero()), new TipoCurso(TipoCurso.LICENCIATURA));

			studentCurricularPlan =
				new StudentCurricularPlan(
					student,
					persistentObjectOJB.readDegreeCurricularPlanByDegreeID(new Integer("" + almeida_inscricoes.getCurso())),
					getBranch(almeida_inscricoes),
					new Date(),
					new StudentCurricularPlanState(StudentCurricularPlanState.ACTIVE));
			studentCurricularPlan.setBranch(new Branch());
			writeElement(studentCurricularPlan);
		}

		return studentCurricularPlan;
	}

	/**
	 * @param string
	 */
	private IBranch getBranch(Almeida_inscricoes almeida_inscricoes) {

		return null;
	}

	protected String getFilename() {
		return "etc/migration/INSCRICOES.TXT";
	}

	protected String getFieldSeparator() {
		return "\t";
	}

	/* (non-Javadoc)
	 * @see middleware.almeida.LoadDataFile#getFilenameOutput()
	 */
	protected String getFilenameOutput() {
		return "ExecutionCoursesNotFound.txt";
	}

}