/*
 * Created on May 15, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package middleware.almeida;

import java.util.StringTokenizer;

import org.apache.log4j.helpers.CyclicBuffer;

import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.StudentCurricularPlan;
import Util.StudentCurricularPlanState;
import Util.TipoCurso;

/**
 *
 * @author  Luis Cruz & Sara Ribeiro
 */
public class LoadInscricoes extends LoadDataFile {

	private static LoadInscricoes loader = null;

	private LoadInscricoes() {
	}

	public static void main(String[] args) {
		loader = new LoadInscricoes();
		loader.load();
	}

	protected void processLine(String line) {
		StringTokenizer stringTokenizer =
			new StringTokenizer(line, getFieldSeperator());

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
		almeida_inscricoes.setAnoinscricao(
			(new Integer(anoInscricao)).longValue());
		almeida_inscricoes.setCurso((new Integer(curso)).longValue());
		almeida_inscricoes.setRamo(ramo);

		processEnrolement(almeida_inscricoes);
	}

	private void processEnrolement(Almeida_inscricoes almeida_inscricoes) {
		//IStudentCurricularPlan studentCurricularPlan =
		//	readStudentCurricularPlan(almeida_inscricoes);
		ICurricularCourse curricularCourse =
			readCurricularCourse(almeida_inscricoes);
		//		IExecutionPeriod executionPeriod = readActiveExecutionPeriod();
		//		IDisciplinaExecucao disciplinaExecucao =
		//			readExecutionCourse(curricularCourse, executionPeriod);

		//		Enrolment enrolment =
		//			new Enrolment(
		//				studentCurricularPlan,
		//				curricularCourse,
		//				new EnrolmentState(EnrolmentState.ENROLED),
		//				executionPeriod);
		//
		//		Frequenta frequenta =
		//			new Frequenta(
		//				studentCurricularPlan.getStudent(),
		//				disciplinaExecucao);
		//
		//		writeElement(enrolment);
		//		writeElement(frequenta);
	}

	/**
	 * @param curricularCourse
	 * @param executionPeriod
	 * @return
	 */
	private IDisciplinaExecucao readExecutionCourse(
		ICurricularCourse curricularCourse,
		IExecutionPeriod executionPeriod) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return
	 */
	private IExecutionPeriod readActiveExecutionPeriod() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param string
	 * @return
	 */
	private ICurricularCourse readCurricularCourse(Almeida_inscricoes almeida_inscricoes) {
		ICurricularCourse curricularCourse = null;

		// First read Almeidas curricular course
		Almeida_disc almeida_disc =
			persistentObjectOJB.readAlmeidaCurricularCourse(
				almeida_inscricoes.getCoddis());

		// Log the ones that don't exist in his database!
		if (almeida_disc == null) {
			System.out.println(
				"Failed to read Almeidas curricular course: "
					+ almeida_inscricoes.getCoddis());
			writeElement(almeida_inscricoes);
			numberUntreatableElements++;
		} else {

			curricularCourse =
				persistentObjectOJB.readCurricularCourse(
					almeida_disc.getNomedis(),
					new Integer("" + almeida_disc.getCodcur()));
			if (curricularCourse == null) {
				numberUntreatableElements++;
			}
		}

		//					persistentObjectOJB.readCurricularCourse(
		//						almeida_inscricoes.getCoddis());
		//
		//		if (curricularCourse == null) {
		//			untreatableCurricularCourses++;
		////			System.out.println(
		////				"Failed to read curricular course: "
		////					+ almeida_inscricoes.getCoddis());
		//		}

		return curricularCourse;
	}

	/**
	 * @param almeida_inscricoes
	 * @return
	 */
	private IStudentCurricularPlan readStudentCurricularPlan(Almeida_inscricoes almeida_inscricoes) {
		IStudentCurricularPlan studentCurricularPlan =
			persistentObjectOJB.readStudentCurricularPlan(
				new Integer("" + almeida_inscricoes.getNumero()));

		if (studentCurricularPlan == null) {
			IStudent student =
				persistentObjectOJB.readStudent(
					new Integer("" + almeida_inscricoes.getNumero()),
					new TipoCurso(TipoCurso.LICENCIATURA));

			studentCurricularPlan =
				new StudentCurricularPlan(
					student,
					persistentObjectOJB.readDegreeCurricularPlan(
						new Integer("" + almeida_inscricoes.getCurso())),
					getBranch(almeida_inscricoes),
					null,
					new StudentCurricularPlanState(
						StudentCurricularPlanState.ACTIVE));
		}

		return studentCurricularPlan;
	}

	/**
	 * @param string
	 */
	private IBranch getBranch(Almeida_inscricoes almeida_inscricoes) {
		// TODO Auto-generated method stub
		return null;
	}

	protected String getFilename() {
		return "etc/migration/INSCRICOES.TXT";
	}

	protected String getFieldSeperator() {
		return "\t";
	}

}