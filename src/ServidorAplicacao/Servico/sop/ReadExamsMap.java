/*
 * ReadExamsMap.java  Created on 2003/04/02
 */

package ServidorAplicacao.Servico.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 *          
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoExam;
import DataBeans.InfoExamsMap;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.util.Cloner;
import Dominio.ICurricularCourse;
import Dominio.ICursoExecucao;
import Dominio.IExecutionCourse;
import Dominio.IExam;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadExamsMap implements IServico {

	private static ReadExamsMap servico = new ReadExamsMap();
	/**
	 * The singleton access method of this class.
	 */
	public static ReadExamsMap getService() {
		return servico;
	}

	/**
	 * The actor of this class.
	 */
	private ReadExamsMap() {
	}

	/**
	 * Devolve o nome do servico
	 */
	public String getNome() {
		return "ReadExamsMap";
	}

	public InfoExamsMap run(
		InfoExecutionDegree infoExecutionDegree,
		List curricularYears,
		InfoExecutionPeriod infoExecutionPeriod) {

		// Object to be returned
		InfoExamsMap infoExamsMap = new InfoExamsMap();

		// Set Execution Degree
		infoExamsMap.setInfoExecutionDegree(infoExecutionDegree);

		// Set List of Curricular Years
		infoExamsMap.setCurricularYears(curricularYears);

		// Exam seasons hardcoded because this information
		// is not yet available from the database
		Calendar startSeason1 = Calendar.getInstance();
		startSeason1.set(Calendar.YEAR, 2004);
		startSeason1.set(Calendar.MONTH, Calendar.JANUARY);
		startSeason1.set(Calendar.DAY_OF_MONTH, 5);
		startSeason1.set(Calendar.HOUR_OF_DAY, 0);
		startSeason1.set(Calendar.MINUTE, 0);
		startSeason1.set(Calendar.SECOND, 0);
		startSeason1.set(Calendar.MILLISECOND, 0);
		Calendar endSeason2 = Calendar.getInstance();
		endSeason2.set(Calendar.YEAR, 2004);
		endSeason2.set(Calendar.MONTH, Calendar.FEBRUARY);
		endSeason2.set(Calendar.DAY_OF_MONTH, 14);
		endSeason2.set(Calendar.HOUR_OF_DAY, 0);
		endSeason2.set(Calendar.MINUTE, 0);
		endSeason2.set(Calendar.SECOND, 0);
		endSeason2.set(Calendar.MILLISECOND, 0);

		if (infoExecutionDegree
			.getInfoDegreeCurricularPlan()
			.getInfoDegree()
			.getSigla()
			.equals("LEEC")) {
			startSeason1.set(Calendar.DAY_OF_MONTH, 5);
			endSeason2.set(Calendar.DAY_OF_MONTH, 14);
		}
		if (infoExecutionDegree
			.getInfoDegreeCurricularPlan()
			.getInfoDegree()
			.getSigla()
			.equals("LEC")) {
			startSeason1.set(Calendar.DAY_OF_MONTH, 12);
			endSeason2.set(Calendar.DAY_OF_MONTH, 14);
		}
		if (infoExecutionDegree
				.getInfoDegreeCurricularPlan()
				.getInfoDegree()
				.getSigla()
				.equals("LA")) {
			startSeason1.set(Calendar.DAY_OF_MONTH, 12);
			endSeason2.set(Calendar.DAY_OF_MONTH, 7);
		}
		if (infoExecutionDegree
				.getInfoDegreeCurricularPlan()
				.getInfoDegree()
				.getSigla()
				.equals("LET")) {
			startSeason1.set(Calendar.DAY_OF_MONTH, 12);
			endSeason2.set(Calendar.DAY_OF_MONTH, 14);
		}
		
		// Set Exam Season info
		infoExamsMap.setStartSeason1(startSeason1);
		infoExamsMap.setEndSeason1(null);
		infoExamsMap.setStartSeason2(null);
		infoExamsMap.setEndSeason2(endSeason2);

		// Translate to execute following queries
		ICursoExecucao executionDegree =
			Cloner.copyInfoExecutionDegree2ExecutionDegree(infoExecutionDegree);
		IExecutionPeriod executionPeriod =
			Cloner.copyInfoExecutionPeriod2IExecutionPeriod(
				infoExecutionPeriod);

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			// List of execution courses
			List infoExecutionCourses = new ArrayList();

			// Obtain execution courses and associated information
			// of the given execution degree for each curricular year specified
			for (int i = 0; i < curricularYears.size(); i++) {
				// Obtain list os execution courses
				List executionCourses =
					sp
						.getIDisciplinaExecucaoPersistente()
						.readByCurricularYearAndExecutionPeriodAndExecutionDegree(
							(Integer) curricularYears.get(i),
							executionPeriod,
							executionDegree);

				// For each execution course obtain curricular courses and
				// exams
				for (int j = 0; j < executionCourses.size(); j++) {
					InfoExecutionCourse infoExecutionCourse =
						Cloner.copyIExecutionCourse2InfoExecutionCourse(
							(IExecutionCourse) executionCourses.get(j));

					infoExecutionCourse.setCurricularYear(
						(Integer) curricularYears.get(i));

					List associatedInfoCurricularCourses = new ArrayList();
					List associatedCurricularCourses =
						((IExecutionCourse) executionCourses.get(j))
							.getAssociatedCurricularCourses();
					// Curricular courses
					for (int k = 0;
						k < associatedCurricularCourses.size();
						k++) {
						InfoCurricularCourse infoCurricularCourse =
							Cloner.copyCurricularCourse2InfoCurricularCourse(
								(
									ICurricularCourse) associatedCurricularCourses
										.get(
									k));
						associatedInfoCurricularCourses.add(
							infoCurricularCourse);
					}
					infoExecutionCourse.setAssociatedInfoCurricularCourses(
						associatedInfoCurricularCourses);

					List associatedInfoExams = new ArrayList();
					List associatedExams =
						((IExecutionCourse) executionCourses.get(j))
							.getAssociatedExams();
					// Exams
					for (int k = 0; k < associatedExams.size(); k++) {
						InfoExam infoExam =
							Cloner.copyIExam2InfoExam(
								(IExam) associatedExams.get(k));
						associatedInfoExams.add(infoExam);
					}
					infoExecutionCourse.setAssociatedInfoExams(
						associatedInfoExams);

					infoExecutionCourses.add(infoExecutionCourse);
				}
			}

			infoExamsMap.setExecutionCourses(infoExecutionCourses);
		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace();
		}

		return infoExamsMap;
	}

}