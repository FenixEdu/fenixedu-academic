package net.sourceforge.fenixedu.applicationTier.Servico.sop.exams;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExamWithRoomOccupationsAndScopesWithCurricularCoursesWithDegreeAndSemesterAndYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExamsMap;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourseWithExecutionPeriodAndExams;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadFilteredExamsMap extends Service {

    public class ExamsPeriodUndefined extends FenixServiceException {
        private static final long serialVersionUID = 1L;
    }

    public InfoExamsMap run(InfoExecutionDegree infoExecutionDegree, List<Integer> curricularYears,
            InfoExecutionPeriod infoExecutionPeriod) throws FenixServiceException, ExcepcaoPersistencia {

        InfoExamsMap result = new InfoExamsMap();
        result.setInfoExecutionDegree(infoExecutionDegree);
        result.setInfoExecutionPeriod(infoExecutionPeriod);
        result.setCurricularYears(curricularYears);

        ExecutionDegree executionDegree = RootDomainObject.getInstance().readExecutionDegreeByOID(infoExecutionDegree.getIdInternal());

        this.obtainExamSeasonInfo(result, infoExecutionPeriod.getSemester(), executionDegree);

        // Obtain execution courses and associated information of the given execution degree for each curricular year specified
        List<InfoExecutionCourse> infoExecutionCourses = this.obtainInfoExecutionCourses(curricularYears, infoExecutionPeriod, executionDegree);
        result.setExecutionCourses(infoExecutionCourses);

        return result;
    }

    private void obtainExamSeasonInfo(InfoExamsMap result, Integer wantedSemester, ExecutionDegree executionDegree) throws ExamsPeriodUndefined {
        OccupationPeriod period = null;
        if (wantedSemester.equals(Integer.valueOf(1))) {
            period = executionDegree.getPeriodExamsFirstSemester();
        } else {
            period = executionDegree.getPeriodExamsSecondSemester();
        }

        if (period == null) {
            throw new ExamsPeriodUndefined();
        }
        
        Calendar startSeason1 = period.getStartDate();
        if (startSeason1.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            // The calendar must start at a monday
            int shiftDays = Calendar.MONDAY - startSeason1.get(Calendar.DAY_OF_WEEK);
            startSeason1.add(Calendar.DATE, shiftDays);
        }
        result.setStartSeason1(startSeason1);
        result.setEndSeason1(null);
        result.setStartSeason2(null);
        
        Calendar endSeason2 = period.getEndDateOfComposite();
        result.setEndSeason2(endSeason2);
    }

    private List<InfoExecutionCourse> obtainInfoExecutionCourses(List<Integer> curricularYears, InfoExecutionPeriod infoExecutionPeriod, ExecutionDegree executionDegree) throws ExcepcaoPersistencia {
        List<InfoExecutionCourse> result = new ArrayList<InfoExecutionCourse>();
        ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(infoExecutionPeriod.getIdInternal());
        for (Integer curricularYear : curricularYears) {
            // Obtain list of execution courses
        	List<ExecutionCourse> executionCourses = executionDegree.getDegreeCurricularPlan().getExecutionCoursesByExecutionPeriodAndSemesterAndYear(executionPeriod, curricularYear, infoExecutionPeriod.getSemester());
        	
            // For each execution course obtain curricular courses and exams
            for (ExecutionCourse executionCourse : executionCourses) {
                InfoExecutionCourse infoExecutionCourse = InfoExecutionCourseWithExecutionPeriodAndExams.newInfoFromDomain(executionCourse);
                infoExecutionCourse.setCurricularYear(curricularYear);

                List<InfoCurricularCourse> associatedInfoCurricularCourses = new ArrayList<InfoCurricularCourse>();
                for (CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCourses()) {
                    associatedInfoCurricularCourses.add(InfoCurricularCourse.newInfoFromDomain(curricularCourse));
                }
                infoExecutionCourse.setAssociatedInfoCurricularCourses(associatedInfoCurricularCourses);

                List<InfoExam> associatedInfoExams = this.obtainInfoExams(executionDegree, infoExecutionPeriod.getIdInternal(), curricularYear, executionCourse);
                infoExecutionCourse.setAssociatedInfoExams(associatedInfoExams);

                result.add(infoExecutionCourse);
            }
        }
        return result;
    }

    private List<InfoExam> obtainInfoExams(ExecutionDegree executionDegree, Integer executionPeriodId, Integer wantedCurricularYear, ExecutionCourse executionCourse) throws ExcepcaoPersistencia {
        List<InfoExam> result = new ArrayList<InfoExam>();
        for (Exam exam : executionCourse.getAssociatedExams()) {
            InfoExam infoExam = InfoExamWithRoomOccupationsAndScopesWithCurricularCoursesWithDegreeAndSemesterAndYear.newInfoFromDomain(exam);
            
            int numberOfStudentsForExam = 0;
            Set<CurricularCourse> checkedCurricularCourses = new HashSet<CurricularCourse>();
            for (CurricularCourseScope curricularCourseScope : exam.getAssociatedCurricularCourseScope()) {
                CurricularCourse curricularCourse = curricularCourseScope.getCurricularCourse();
                if (!checkedCurricularCourses.contains(curricularCourse)) {
                    checkedCurricularCourses.add(curricularCourse);
                    ExecutionPeriod executionPeriod = (ExecutionPeriod) persistentObject.readByOID(ExecutionPeriod.class, executionPeriodId);
                    int numberEnroledStudentsInCurricularCourse = curricularCourse.countEnrolmentsByExecutionPeriod(executionPeriod);
                    
                    numberOfStudentsForExam += numberEnroledStudentsInCurricularCourse;
                }
                
                CurricularYear curricularYearFromScope = curricularCourseScope.getCurricularSemester().getCurricularYear();
                boolean isCurricularYearEqual = curricularYearFromScope.getYear().equals(wantedCurricularYear);

                DegreeCurricularPlan degreeCurricularPlanFromScope = curricularCourseScope.getCurricularCourse().getDegreeCurricularPlan();
                DegreeCurricularPlan degreeCurricularPlanFromExecutionDegree = executionDegree.getDegreeCurricularPlan();
                boolean isCurricularPlanEqual = degreeCurricularPlanFromScope.equals(degreeCurricularPlanFromExecutionDegree);

                if (isCurricularYearEqual && isCurricularPlanEqual && !result.contains(infoExam)) {
                    result.add(infoExam);
                    break;
                }
            }
            infoExam.setEnrolledStudents(Integer.valueOf(numberOfStudentsForExam));
        }
        return result;
    }

}
