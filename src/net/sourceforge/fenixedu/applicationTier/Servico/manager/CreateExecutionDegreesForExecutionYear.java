package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Campus;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.OccupationPeriod;

import org.joda.time.YearMonthDay;

public class CreateExecutionDegreesForExecutionYear extends Service {

    public List<DegreeCurricularPlan> run(
            final Integer[] degreeCurricularPlansIDs, final Integer[] bolonhaDegreeCurricularPlansIDs, final Integer executionYearID,
            final String campusName, final Boolean temporaryExamMap,
            final Calendar lessonSeason1BeginDate, final Calendar lessonSeason1EndDate,
            final Calendar lessonSeason2BeginDate, final Calendar lessonSeason2EndDate,
            final Calendar examsSeason1BeginDate, final Calendar examsSeason1EndDate,
            final Calendar examsSeason2BeginDate, final Calendar examsSeason2EndDate,
            final Calendar examsSpecialSeasonBeginDate, final Calendar examsSpecialSeasonEndDate,
            final Calendar gradeSubmissionNormalSeason1EndDate,
            final Calendar gradeSubmissionNormalSeason2EndDate,
            final Calendar gradeSubmissionSpecialSeasonEndDate) {

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
        if (degreeCurricularPlansIDs != null && degreeCurricularPlansIDs.length > 0) {
        	allDegreeCurricularPlanIDs.addAll(Arrays.asList(degreeCurricularPlansIDs));
        }
        if (bolonhaDegreeCurricularPlansIDs != null && bolonhaDegreeCurricularPlansIDs.length > 0) {
        	allDegreeCurricularPlanIDs.addAll(Arrays.asList(bolonhaDegreeCurricularPlansIDs));
        }

        final List<DegreeCurricularPlan> created = new ArrayList<DegreeCurricularPlan>();
        for (final Integer degreeCurricularPlanID : allDegreeCurricularPlanIDs) {
            final DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanID);
            if (degreeCurricularPlan == null) {
                continue;
            }
            
            final ExecutionDegree executionDegree = degreeCurricularPlan.createExecutionDegree(executionYear, campus, temporaryExamMap);
            setPeriods(executionDegree, examsSeason1, examsSeason2, examsSpecialSeason, lessonSeason1, lessonSeason2, gradeSubmissionNormalSeason1, gradeSubmissionNormalSeason2,gradeSubmissionSpecialSeason);
            created.add(degreeCurricularPlan);
        }
        
        return created;
    }

    private Campus readCampusByName(String campusName) {
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
            occupationPeriod = new OccupationPeriod(startDate.getTime(), endDate.getTime());
            occupationPeriod.setNextPeriod(null);
        }
        return occupationPeriod;
    }

    protected void setPeriods(ExecutionDegree executionDegree, OccupationPeriod periodExamsSeason1, OccupationPeriod periodExamsSeason2, OccupationPeriod periodExamsSpecialSeason,
            OccupationPeriod periodLessonSeason1, OccupationPeriod periodLessonSeason2, OccupationPeriod gradeSubmissionNormalSeason1,
            OccupationPeriod gradeSubmissionNormalSeason2, OccupationPeriod gradeSubmissionSpecialSeason) {

        executionDegree.setPeriodExamsFirstSemester(periodExamsSeason1);
        executionDegree.setPeriodExamsSecondSemester(periodExamsSeason2);
        executionDegree.setPeriodExamsSpecialSeason(periodExamsSpecialSeason);
        executionDegree.setPeriodLessonsFirstSemester(periodLessonSeason1);
        executionDegree.setPeriodLessonsSecondSemester(periodLessonSeason2);
        executionDegree.setPeriodGradeSubmissionNormalSeasonFirstSemester(gradeSubmissionNormalSeason1);
        executionDegree.setPeriodGradeSubmissionNormalSeasonSecondSemester(gradeSubmissionNormalSeason2);
        executionDegree.setPeriodGradeSubmissionSpecialSeason(gradeSubmissionSpecialSeason);
    }

}
