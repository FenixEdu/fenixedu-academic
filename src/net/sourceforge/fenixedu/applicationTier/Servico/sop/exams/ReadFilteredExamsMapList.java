/*
 * Created on 2003/10/20
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop.exams;

/**
 * @author Ana & Ricardo
 * 
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExamWithRoomOccupationsAndScopesWithCurricularCoursesWithDegreeAndSemesterAndYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExamsMap;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadFilteredExamsMapList extends Service {

    public InfoExamsMap run(List infoExecutionDegreeList, List curricularYears,
            InfoExecutionPeriod infoExecutionPeriod) throws ExcepcaoPersistencia {
        // Object to be returned
        InfoExamsMap infoExamsMap = new InfoExamsMap();

        // Set Execution Degree
        InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) infoExecutionDegreeList.get(0);
        infoExamsMap.setInfoExecutionDegree(infoExecutionDegree);

        // Set List of Curricular Years
        infoExamsMap.setCurricularYears(curricularYears);

        final ExecutionDegree executionDegreeFromDB = rootDomainObject.readExecutionDegreeByOID(infoExecutionDegree.getIdInternal());

        final Calendar startSeason1;
        final Calendar endSeason2;
        if (infoExecutionPeriod.getSemester().intValue() == 1) {
            startSeason1 = executionDegreeFromDB.getPeriodExamsFirstSemester().getStartDate();
            endSeason2 = executionDegreeFromDB.getPeriodExamsFirstSemester().getEndDate();
        } else {
            startSeason1 = executionDegreeFromDB.getPeriodExamsSecondSemester().getStartDate();
            endSeason2 = executionDegreeFromDB.getPeriodExamsSecondSemester().getEndDate();
        }

        // Set Exam Season info
        infoExamsMap.setStartSeason1(startSeason1);
        infoExamsMap.setEndSeason1(null);
        infoExamsMap.setStartSeason2(null);
        infoExamsMap.setEndSeason2(endSeason2);

        // List of execution courses
        List infoExecutionCourses = new ArrayList();

        // Obtain execution courses and associated information
        // of the given execution degree for each curricular year persistentSupportecified
        for (int i = 0; i < curricularYears.size(); i++) {
            // Obtain list os execution courses
            for (int n = 0; n < infoExecutionDegreeList.size(); n++) {
                InfoExecutionDegree infoExecucaoDegree = (InfoExecutionDegree) infoExecutionDegreeList.get(n);
                DegreeCurricularPlan degreeCurricularPlan = DegreeCurricularPlan.readByNameAndDegreeSigla(infoExecucaoDegree.getInfoDegreeCurricularPlan().getName(), infoExecucaoDegree.getInfoDegreeCurricularPlan().getInfoDegree().getSigla());
                if(degreeCurricularPlan != null) {
                	final ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(infoExecutionPeriod.getIdInternal());
                	List<ExecutionCourse> executionCourses = degreeCurricularPlan.getExecutionCoursesByExecutionPeriodAndSemesterAndYear(executionPeriod, (Integer) curricularYears.get(i), infoExecutionPeriod.getSemester());

	                // For each execution course obtain curricular courses and
	                // exams
	                for (int j = 0; j < executionCourses.size(); j++) {
	                    InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse.newInfoFromDomain((ExecutionCourse) executionCourses.get(j));
	
	                    infoExecutionCourse.setCurricularYear((Integer) curricularYears.get(i));
	
	                    List associatedInfoExams = new ArrayList();
	                    List associatedExams = ((ExecutionCourse) executionCourses.get(j))
	                            .getAssociatedEvaluations();
	                    // Exams
	                    for (int k = 0; k < associatedExams.size(); k++) {
	                        if (!(associatedExams.get(k) instanceof Exam)) {
	                            continue;
	                        }
	
	                        InfoExam infoExam = InfoExamWithRoomOccupationsAndScopesWithCurricularCoursesWithDegreeAndSemesterAndYear
	                                .newInfoFromDomain((Exam) associatedExams.get(k));
	                        int numberOfStudentsForExam = 0;
	                        List curricularCourseIDs = new ArrayList();
	                        for (int l = 0; l < infoExam.getAssociatedCurricularCourseScope().size(); l++) {
	                            InfoCurricularCourseScope scope = (InfoCurricularCourseScope) infoExam
	                                    .getAssociatedCurricularCourseScope().get(l);
	                            InfoCurricularCourse infoCurricularCourse = scope.getInfoCurricularCourse();
	                            if (!curricularCourseIDs.contains(infoCurricularCourse.getIdInternal())) {
	                                curricularCourseIDs.add(infoCurricularCourse.getIdInternal());
	                                CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(infoCurricularCourse.getIdInternal());
	                                int numberEnroledStudentsInCurricularCourse = curricularCourse.countEnrolmentsByExecutionPeriod(executionPeriod);
	
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
	
	                            boolean isCurricularPlanEqual = true;
	                            if (isCurricularYearEqual && isCurricularPlanEqual
	                                    && !associatedInfoExams.contains(infoExam)) {
	                                associatedInfoExams.add(infoExam);
	                                break;
	                            }
	                        }
	                    }
	                    infoExecutionCourse.setFilteredAssociatedInfoExams(associatedInfoExams);
	
	                    infoExecutionCourses.add(infoExecutionCourse);
	                }
                }
            }
        }
        infoExamsMap.setExecutionCourses(infoExecutionCourses);

        return infoExamsMap;
    }

}
