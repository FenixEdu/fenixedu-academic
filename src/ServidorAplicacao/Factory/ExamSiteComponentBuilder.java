/*
 * Created on 5/Mai/2003
 *
 */
package ServidorAplicacao.Factory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import DataBeans.ISiteComponent;
import DataBeans.InfoCurricularCourse;
import DataBeans.InfoExam;
import DataBeans.InfoExamsMap;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoSiteExamMap;
import DataBeans.util.Cloner;
import Dominio.ICurricularCourse;
import Dominio.ICursoExecucao;
import Dominio.IExecutionCourse;
import Dominio.IExam;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 *
 * 
 */
public class ExamSiteComponentBuilder {

	private static ExamSiteComponentBuilder instance = null;

	public ExamSiteComponentBuilder() {
	}

	public static ExamSiteComponentBuilder getInstance() {
		if (instance == null) {
			instance = new ExamSiteComponentBuilder();
		}
		return instance;
	}

	public ISiteComponent getComponent(
		ISiteComponent component,
		IExecutionPeriod executionPeriod,
		ICursoExecucao executionDegree, List curricularYears)
		throws FenixServiceException {

		if (component instanceof InfoSiteExamMap) {
			return getInfoSiteExamMap(
				(InfoSiteExamMap) component,executionPeriod,
				executionDegree,curricularYears);
		}

		return null;

	}

	
	private ISiteComponent getInfoSiteExamMap(
		InfoSiteExamMap component,
		IExecutionPeriod executionPeriod,
		ICursoExecucao executionDegree,
		List curricularYears)
		throws FenixServiceException {
//		List classes = new ArrayList();
//		List infoClasses = new ArrayList();

//		Object to be returned
			 InfoExamsMap infoExamsMap = new InfoExamsMap();

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

			if (executionDegree
				.getCurricularPlan()
				.getDegree()
				.getSigla()
				.equals("LEEC")) {
				startSeason1.set(Calendar.DAY_OF_MONTH, 5);
				endSeason2.set(Calendar.DAY_OF_MONTH, 14);
			}
			if (executionDegree
				.getCurricularPlan()
				.getDegree()
				.getSigla()
				.equals("LEC")) {
				startSeason1.set(Calendar.DAY_OF_MONTH, 12);
				endSeason2.set(Calendar.DAY_OF_MONTH, 14);
			}
			if (executionDegree
					.getCurricularPlan()
					.getDegree()
					.getSigla()
					.equals("LA")) {
				startSeason1.set(Calendar.DAY_OF_MONTH, 12);
				endSeason2.set(Calendar.DAY_OF_MONTH, 7);
			}
			if (executionDegree
					.getCurricularPlan()
					.getDegree()
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
							 .getIPersistentExecutionCourse()
							 .readByCurricularYearAndExecutionPeriodAndExecutionDegree(
								 (Integer) curricularYears.get(i),
								 executionPeriod,
								 executionDegree);

					 // For each execution course obtain curricular courses and exams
					 for (int j = 0; j < executionCourses.size(); j++) {
						 InfoExecutionCourse infoExecutionCourse =
							 (InfoExecutionCourse) Cloner.get(
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
				 throw new FenixServiceException(ex);
			 }
			component.setInfoExecutionDegree(Cloner.copyIExecutionDegree2InfoExecutionDegree(executionDegree));	
			component.setInfoExamsMap(infoExamsMap);

		return component;
	}

}
