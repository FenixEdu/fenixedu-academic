/*
 * ReadExamsByExecutionCourse.java
 *
 * Created on 2003/03/29
 */

package ServidorAplicacao.Servico.sop;

/**
  * @author Luis Cruz & Sara Ribeiro
  * 
 **/
import java.util.ArrayList;
import java.util.List;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionCourseAndExams;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.util.Cloner;
import Dominio.ICursoExecucao;
import Dominio.IExam;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.Season;

public class ReadExamsByExecutionDegreeAndCurricularYear implements IServico {

	private static ReadExamsByExecutionDegreeAndCurricularYear _servico =
		new ReadExamsByExecutionDegreeAndCurricularYear();
	/**
	 * The singleton access method of this class.
	 **/
	public static ReadExamsByExecutionDegreeAndCurricularYear getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ReadExamsByExecutionDegreeAndCurricularYear() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "ReadExamsByExecutionDegreeAndCurricularYear";
	}

	public List run(
		InfoExecutionDegree infoExecutionDegree,
		InfoExecutionPeriod infoExecutionPeriod,
		Integer curricularYear) {
		ArrayList infoExecutionCourseAndExamsList = new ArrayList();

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			ICursoExecucao executionDegree =
				Cloner.copyInfoExecutionDegree2ExecutionDegree(infoExecutionDegree);
			IExecutionPeriod executionPeriod =
				Cloner.copyInfoExecutionPeriod2IExecutionPeriod(infoExecutionPeriod);

			List executionCourses = sp.getIPersistentExecutionCourse()
					.readByCurricularYearAndExecutionPeriodAndExecutionDegree(
					curricularYear,
					executionPeriod, executionDegree);

			for (int i = 0; i < executionCourses.size(); i++) {
				IExecutionCourse executionCourse =
					(IExecutionCourse) executionCourses.get(i);

				InfoExecutionCourseAndExams infoExecutionCourseAndExams =
					new InfoExecutionCourseAndExams();

				infoExecutionCourseAndExams.setInfoExecutionCourse(
					 (InfoExecutionCourse) Cloner.get(
						executionCourse));

				for (int j = 0; j < executionCourse.getAssociatedExams().size(); j++) {
					IExam exam = (IExam) executionCourse.getAssociatedExams().get(j);
					if (exam.getSeason().getseason().intValue() == Season.SEASON1) {
						infoExecutionCourseAndExams.setInfoExam1(Cloner.copyIExam2InfoExam(exam));
					} else if (exam.getSeason().getseason().intValue() == Season.SEASON2) {
						infoExecutionCourseAndExams.setInfoExam2(Cloner.copyIExam2InfoExam(exam));
					}
				}

				// Number of students attending execution course.
				// TODO : In this context should we realy count the number of
				//        students attending the course or just the ones from
				//        the indicated degree????
				infoExecutionCourseAndExams.setNumberStudentesAttendingCourse(
					sp
						.getIFrequentaPersistente()
						.countStudentsAttendingExecutionCourse(
						executionCourse));
			
				infoExecutionCourseAndExamsList.add(infoExecutionCourseAndExams);
			}

		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace();
		}
		return infoExecutionCourseAndExamsList;
	}
}