/*
 * Created on 2003/10/20
 */

package ServidorAplicacao.Servico.sop.exams;

/**
 * @author Ana & Ricardo
 *  
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoCurricularYear;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoExam;
import DataBeans.InfoExamWithRoomOccupationsAndScopesWithCurricularCoursesWithDegreeAndSemesterAndYear;
import DataBeans.InfoExamsMap;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionCourseWithExecutionPeriodAndExams;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionDegreeWithInfoDegreeCurricularPlan;
import DataBeans.InfoExecutionPeriod;
import Dominio.ICurricularCourse;
import Dominio.IExecutionDegree;
import Dominio.IExam;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEnrollment;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadFilteredExamsMapList implements IServico {

    private static ReadFilteredExamsMapList servico = new ReadFilteredExamsMapList();

    /**
     * The singleton access method of this class.
     */
    public static ReadFilteredExamsMapList getService() {
        return servico;
    }

    /**
     * The actor of this class.
     */
    private ReadFilteredExamsMapList() {
    }

    /**
     * Devolve o nome do servico
     */
    public String getNome() {
        return "ReadFilteredExamsMapList";
    }

  
    
    
	public InfoExamsMap run(List infoExecutionDegreeList, List curricularYears,
			   InfoExecutionPeriod infoExecutionPeriod) {

		   // Object to be returned
		   InfoExamsMap infoExamsMap = new InfoExamsMap();

		   // Set Execution Degree
		   InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree();
		   infoExecutionDegree = (InfoExecutionDegree) infoExecutionDegreeList.get(0);
		   infoExamsMap.setInfoExecutionDegree(infoExecutionDegree);

		   // Set List of Curricular Years
		   infoExamsMap.setCurricularYears(curricularYears);
 
		   // TODO: change this code when exams season available from database
		   // Exam seasons hardcoded because this information
		   // is not yet available from the database
		   Calendar startSeason1 = Calendar.getInstance();
		   startSeason1.set(Calendar.YEAR, 2005);
		   startSeason1.set(Calendar.MONTH, Calendar.JANUARY);
		   startSeason1.set(Calendar.DAY_OF_MONTH, 3);
		   startSeason1.set(Calendar.HOUR_OF_DAY, 0);
		   startSeason1.set(Calendar.MINUTE, 0);
		   startSeason1.set(Calendar.SECOND, 0);
		   startSeason1.set(Calendar.MILLISECOND, 0);
		   Calendar endSeason2 = Calendar.getInstance();
		   endSeason2.set(Calendar.YEAR, 2005);
		   endSeason2.set(Calendar.MONTH, Calendar.FEBRUARY);
		   endSeason2.set(Calendar.DAY_OF_MONTH, 12);
		   endSeason2.set(Calendar.HOUR_OF_DAY, 0);
		   endSeason2.set(Calendar.MINUTE, 0);
		   endSeason2.set(Calendar.SECOND, 0);
		   endSeason2.set(Calendar.MILLISECOND, 0);

//			 if (infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getSigla().equals("LEC")) {
//				 startSeason1.set(Calendar.DAY_OF_MONTH, 21);
//				 endSeason2.set(Calendar.DAY_OF_MONTH, 17);
//			 }
//			 if (infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getSigla().equals("LET")) {
//				 startSeason1.set(Calendar.DAY_OF_MONTH, 21);
//				 endSeason2.set(Calendar.DAY_OF_MONTH, 17);
//			 }
//			 if (infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getSigla().equals("LA")) {
//				 startSeason1.set(Calendar.DAY_OF_MONTH, 21);
//				 endSeason2.set(Calendar.DAY_OF_MONTH, 17);
//			 }

		   // Set Exam Season info
		   infoExamsMap.setStartSeason1(startSeason1);
		   infoExamsMap.setEndSeason1(null);
		   infoExamsMap.setStartSeason2(null);
		   infoExamsMap.setEndSeason2(endSeason2);
           List executionDegreeList=new ArrayList(); 
		   // Translate to execute following queries
		   //CLONER
		   //IExecutionDegree executionDegree =
		   //Cloner.copyInfoExecutionDegree2ExecutionDegree(infoExecutionDegree);
		   for (int i = 0; i < infoExecutionDegreeList.size(); i++) {
		   IExecutionDegree executionDegree = InfoExecutionDegreeWithInfoDegreeCurricularPlan
				   .newDomainFromInfo((InfoExecutionDegree)infoExecutionDegreeList.get(i));
			executionDegreeList.add(executionDegree);
		   }	
		   //CLONER
		   //IExecutionPeriod executionPeriod =
		   //Cloner.copyInfoExecutionPeriod2IExecutionPeriod(infoExecutionPeriod);
		   IExecutionPeriod executionPeriod = InfoExecutionPeriod.newDomainFromInfo(infoExecutionPeriod);
		
		   try {
			   ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			   IPersistentEnrollment persistentEnrolment = sp.getIPersistentEnrolment();

			   // List of execution courses
			   List infoExecutionCourses = new ArrayList();

			   // Obtain execution courses and associated information
			   // of the given execution degree for each curricular year specified
			   for (int i = 0; i < curricularYears.size(); i++) {
				   // Obtain list os execution courses
//				   List executionCourses = sp.getIPersistentExecutionCourse()
//						   .readByCurricularYearAndExecutionPeriodAndExecutionDegree(
//								   (Integer) curricularYears.get(i), executionPeriod, executionDegree);
				for (int n = 0; n < executionDegreeList.size(); n++) {
					IExecutionDegree execucaoDegree = (IExecutionDegree)executionDegreeList.get(n);
					List executionCourses = sp.getIPersistentExecutionCourse()
										   .readByCurricularYearAndExecutionPeriodAndExecutionDegree(
												   (Integer) curricularYears.get(i), executionPeriod, execucaoDegree);
				
				   // For each execution course obtain curricular courses and
				   // exams
				   for (int j = 0; j < executionCourses.size(); j++) {
					   //CLONER
					   //InfoExecutionCourse infoExecutionCourse =
					   //(InfoExecutionCourse) Cloner.get((IExecutionCourse)
					   // executionCourses.get(j));
					   InfoExecutionCourse infoExecutionCourse = InfoExecutionCourseWithExecutionPeriodAndExams
							   .newInfoFromDomain((IExecutionCourse) executionCourses.get(j));

					   infoExecutionCourse.setCurricularYear((Integer) curricularYears.get(i));

					   List associatedInfoCurricularCourses = new ArrayList();
					   List associatedCurricularCourses = ((IExecutionCourse) executionCourses.get(j))
							   .getAssociatedCurricularCourses();
					   // Curricular courses
					   for (int k = 0; k < associatedCurricularCourses.size(); k++) {
						   //CLONER
						   //InfoCurricularCourse infoCurricularCourse =
						   //Cloner.copyCurricularCourse2InfoCurricularCourse(
						   //(ICurricularCourse)
						   // associatedCurricularCourses.get(k));
						   InfoCurricularCourse infoCurricularCourse = InfoCurricularCourse
								   .newInfoFromDomain((ICurricularCourse) associatedCurricularCourses
										   .get(k));
                                
						   associatedInfoCurricularCourses.add(infoCurricularCourse);
					   }
					   infoExecutionCourse
							   .setAssociatedInfoCurricularCourses(associatedInfoCurricularCourses);

					   List associatedInfoExams = new ArrayList();
					   List associatedExams = ((IExecutionCourse) executionCourses.get(j))
							   .getAssociatedExams();
					   // Exams
					   for (int k = 0; k < associatedExams.size(); k++) {
						   if (!(associatedExams.get(k) instanceof IExam)) {
							   continue;
						   }
						   //CLONER
						   //InfoExam infoExam = Cloner.copyIExam2InfoExam((IExam)
						   // associatedExams.get(k));
						   InfoExam infoExam = InfoExamWithRoomOccupationsAndScopesWithCurricularCoursesWithDegreeAndSemesterAndYear
								   .newInfoFromDomain((IExam) associatedExams.get(k));
						   int numberOfStudentsForExam = 0;
						   List curricularCourseIDs = new ArrayList();
						   for (int l = 0; l < infoExam.getAssociatedCurricularCourseScope().size(); l++) {
							   InfoCurricularCourseScope scope = (InfoCurricularCourseScope) infoExam
									   .getAssociatedCurricularCourseScope().get(l);
							   InfoCurricularCourse infoCurricularCourse = scope.getInfoCurricularCourse();
							   if (!curricularCourseIDs.contains(infoCurricularCourse.getIdInternal())) {
								   curricularCourseIDs.add(infoCurricularCourse.getIdInternal());
								   int numberEnroledStudentsInCurricularCourse = persistentEnrolment
										   .countEnrolmentsByCurricularCourseAndExecutionPeriod(
												   infoCurricularCourse.getIdInternal(), executionPeriod
														   .getIdInternal());

								   numberOfStudentsForExam += numberEnroledStudentsInCurricularCourse;
							   }
						   }

						   infoExam.setEnrolledStudents(new Integer(numberOfStudentsForExam));

						   List associatedCurricularCourseScope = new ArrayList();
						   associatedCurricularCourseScope = infoExam.getAssociatedCurricularCourseScope();

						   for (int h = 0; h < associatedCurricularCourseScope.size(); h++) {
							   InfoCurricularCourseScope infoCurricularCourseScope = (InfoCurricularCourseScope) associatedCurricularCourseScope
									   .get(h);

							   InfoCurricularYear infoCurricularYear = infoCurricularCourseScope
									   .getInfoCurricularSemester().getInfoCurricularYear();

							   boolean isCurricularYearEqual = infoCurricularYear.getYear().equals(
									   curricularYears.get(i));

							   //obter o curricular plan a partir do curricular
							   // course scope
							   InfoDegreeCurricularPlan degreeCurricularPlanFromScope = infoCurricularCourseScope
									   .getInfoCurricularCourse().getInfoDegreeCurricularPlan();

//							   obter o curricular plan a partir do info degree
							   InfoDegreeCurricularPlan infoDegreeCurricularPlan = infoExecutionDegree
									   .getInfoDegreeCurricularPlan();
//
//							   boolean isCurricularPlanEqual = degreeCurricularPlanFromScope
//									   .equals(infoDegreeCurricularPlan);
							   boolean isCurricularPlanEqual = true;
							   if (isCurricularYearEqual && isCurricularPlanEqual
									   && !associatedInfoExams.contains(infoExam)) {
								   associatedInfoExams.add(infoExam);
								   break;
							   }
						   }
					   }
					   infoExecutionCourse.setAssociatedInfoExams(associatedInfoExams);


					   infoExecutionCourses.add(infoExecutionCourse);
				   }
				   }
			   }
			   infoExamsMap.setExecutionCourses(infoExecutionCourses);
		   } catch (ExcepcaoPersistencia ex) {
			   ex.printStackTrace();
		   }

		   return infoExamsMap;
	   }

}