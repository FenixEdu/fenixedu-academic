package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import org.joda.time.YearMonthDay;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Campus;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CreateExecutionDegreesForExecutionYear extends Service {

    public void run(final Integer[] degreeCurricularPlansIDs,
            final Integer[] bolonhaDegreeCurricularPlansIDs, final Integer executionYearID,
            final String campusName, final Boolean temporaryExamMap,
            final Calendar lessonSeason1BeginDate, final Calendar lessonSeason1EndDate,
            final Calendar lessonSeason2BeginDate, final Calendar lessonSeason2EndDate,
            final Calendar examsSeason1BeginDate, final Calendar examsSeason1EndDate,
            final Calendar examsSeason2BeginDate, final Calendar examsSeason2EndDate,
            final Calendar examsSpecialSeasonBeginDate, final Calendar examsSpecialSeasonEndDate,
            final Calendar gradeSubmissionNormalSeason1EndDate, final Calendar gradeSubmissionNormalSeason2EndDate,
            final Calendar gradeSubmissionSpecialSeasonEndDate) throws ExcepcaoPersistencia {
        
        final ExecutionYear executionYear = rootDomainObject.readExecutionYearByOID(executionYearID);
        final Campus campus = readCampusByName(campusName);

        final OccupationPeriod lessonSeason1 = getOccupationPeriod(lessonSeason1BeginDate, lessonSeason1EndDate);
        final OccupationPeriod lessonSeason2 = getOccupationPeriod(lessonSeason2BeginDate, lessonSeason2EndDate);
        final OccupationPeriod examsSeason1 = getOccupationPeriod(examsSeason1BeginDate, examsSeason1EndDate);
        final OccupationPeriod examsSeason2 = getOccupationPeriod(examsSeason2BeginDate, examsSeason2EndDate);
        final OccupationPeriod examsSpecialSeason = getOccupationPeriod(examsSpecialSeasonBeginDate, examsSpecialSeasonEndDate);
        final OccupationPeriod gradeSubmissionNormalSeason1 = getOccupationPeriod(examsSeason1BeginDate, gradeSubmissionNormalSeason1EndDate);
        final OccupationPeriod gradeSubmissionNormalSeason2 = getOccupationPeriod(examsSeason2BeginDate, gradeSubmissionNormalSeason2EndDate);
        final OccupationPeriod gradeSubmissionSpecialSeason = getOccupationPeriod(examsSpecialSeasonBeginDate, gradeSubmissionSpecialSeasonEndDate);

        final Set<Integer> allDegreeCurricularPlanIDs = new HashSet<Integer>();
        allDegreeCurricularPlanIDs.addAll(Arrays.asList(degreeCurricularPlansIDs));
        allDegreeCurricularPlanIDs.addAll(Arrays.asList(bolonhaDegreeCurricularPlansIDs));
        
        for (final Integer degreeCurricularPlanID : allDegreeCurricularPlanIDs) {
            final DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanID);
            if (degreeCurricularPlan == null || degreeCurricularPlan.hasAnyExecutionDegreeFor(executionYear)) {
                continue;
            }
            createExecutionDegree(executionYear, campus, degreeCurricularPlan, temporaryExamMap,
                    examsSeason1, examsSeason2, examsSpecialSeason, lessonSeason1, lessonSeason2,
                    gradeSubmissionNormalSeason1, gradeSubmissionNormalSeason2, gradeSubmissionSpecialSeason);
        }
    }

    private Campus readCampusByName(String campusName) throws ExcepcaoPersistencia {
        for (Campus campus : rootDomainObject.getCampuss()) {
            if (campus.getName().equalsIgnoreCase(campusName)) {
                return campus;
            }
        }
        return null;
    }

    private OccupationPeriod getOccupationPeriod(final Calendar startDate, final Calendar endDate) {
        
        OccupationPeriod occupationPeriod = OccupationPeriod.readFor(YearMonthDay.fromCalendarFields(startDate), YearMonthDay.fromCalendarFields(endDate));
        if (occupationPeriod == null) {
            occupationPeriod = DomainFactory.makeOccupationPeriod(startDate.getTime(), endDate.getTime());
            occupationPeriod.setNextPeriod(null);
        }
        return occupationPeriod;
    }

    protected void createExecutionDegree(

    final ExecutionYear executionYear, Campus campus, DegreeCurricularPlan degreeCurricularPlan,
            Boolean temporaryExamMap, OccupationPeriod periodExamsSeason1,
            OccupationPeriod periodExamsSeason2, OccupationPeriod periodExamsSpecialSeason,
            OccupationPeriod periodLessonSeason1, OccupationPeriod periodLessonSeason2,
            OccupationPeriod gradeSubmissionNormalSeason1, OccupationPeriod gradeSubmissionNormalSeason2,
            OccupationPeriod gradeSubmissionSpecialSeason) {

        final ExecutionDegree executionDegree = DomainFactory.makeExecutionDegree();
        executionDegree.setCampus(campus);
        executionDegree.setDegreeCurricularPlan(degreeCurricularPlan);
        executionDegree.setExecutionYear(executionYear);
        executionDegree.setPeriodExamsFirstSemester(periodExamsSeason1);
        executionDegree.setPeriodExamsSecondSemester(periodExamsSeason2);
        executionDegree.setPeriodExamsSpecialSeason(periodExamsSpecialSeason);
        executionDegree.setPeriodLessonsFirstSemester(periodLessonSeason1);
        executionDegree.setPeriodLessonsSecondSemester(periodLessonSeason2);
        executionDegree.setPeriodGradeSubmissionNormalSeasonFirstSemester(gradeSubmissionNormalSeason1);
        executionDegree.setPeriodGradeSubmissionNormalSeasonSecondSemester(gradeSubmissionNormalSeason2);
        executionDegree.setPeriodGradeSubmissionSpecialSeason(gradeSubmissionSpecialSeason);
        executionDegree.setScheduling(null);
        executionDegree.setTemporaryExamMap(temporaryExamMap);
    }

}
