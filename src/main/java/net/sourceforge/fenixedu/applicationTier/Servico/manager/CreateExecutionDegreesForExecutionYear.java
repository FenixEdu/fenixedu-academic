package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.predicates.RolePredicates;

import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class CreateExecutionDegreesForExecutionYear {

    @Atomic
    public static List<DegreeCurricularPlan> run(final String[] degreeCurricularPlansIDs,
            final String[] bolonhaDegreeCurricularPlansIDs, final String executionYearID, final String campusName,
            final Boolean publishedExamMap, final Calendar lessonSeason1BeginDate, final Calendar lessonSeason1EndDate,
            final Calendar lessonSeason2BeginDate, final Calendar lessonSeason2EndDate,
            final Calendar lessonSeason2BeginDatePart2, final Calendar lessonSeason2EndDatePart2,
            final Calendar examsSeason1BeginDate, final Calendar examsSeason1EndDate, final Calendar examsSeason2BeginDate,
            final Calendar examsSeason2EndDate, final Calendar examsSpecialSeasonBeginDate,
            final Calendar examsSpecialSeasonEndDate, final Calendar gradeSubmissionNormalSeason1EndDate,
            final Calendar gradeSubmissionNormalSeason2EndDate, final Calendar gradeSubmissionSpecialSeasonEndDate) {
        check(RolePredicates.MANAGER_OR_OPERATOR_PREDICATE);

        final ExecutionYear executionYear = FenixFramework.getDomainObject(executionYearID);
        final Campus campus = readCampusByName(campusName);

        final OccupationPeriod lessonSeason1 = getOccupationPeriod(lessonSeason1BeginDate, lessonSeason1EndDate);
        final OccupationPeriod lessonSeason2 =
                OccupationPeriod.getOccupationPeriod(lessonSeason2BeginDate, lessonSeason2EndDate, lessonSeason2BeginDatePart2,
                        lessonSeason2EndDatePart2);
        final OccupationPeriod examsSeason1 = getOccupationPeriod(examsSeason1BeginDate, examsSeason1EndDate);
        final OccupationPeriod examsSeason2 = getOccupationPeriod(examsSeason2BeginDate, examsSeason2EndDate);
        final OccupationPeriod examsSpecialSeason = getOccupationPeriod(examsSpecialSeasonBeginDate, examsSpecialSeasonEndDate);
        final OccupationPeriod gradeSubmissionNormalSeason1 =
                getOccupationPeriod(examsSeason1BeginDate, gradeSubmissionNormalSeason1EndDate);
        final OccupationPeriod gradeSubmissionNormalSeason2 =
                getOccupationPeriod(examsSeason2BeginDate, gradeSubmissionNormalSeason2EndDate);
        final OccupationPeriod gradeSubmissionSpecialSeason =
                getOccupationPeriod(examsSpecialSeasonBeginDate, gradeSubmissionSpecialSeasonEndDate);

        final Set<String> allDegreeCurricularPlanIDs = new HashSet<String>();
        if (degreeCurricularPlansIDs != null && degreeCurricularPlansIDs.length > 0) {
            allDegreeCurricularPlanIDs.addAll(Arrays.asList(degreeCurricularPlansIDs));
        }
        if (bolonhaDegreeCurricularPlansIDs != null && bolonhaDegreeCurricularPlansIDs.length > 0) {
            allDegreeCurricularPlanIDs.addAll(Arrays.asList(bolonhaDegreeCurricularPlansIDs));
        }

        final List<DegreeCurricularPlan> created = new ArrayList<DegreeCurricularPlan>();
        for (final String degreeCurricularPlanID : allDegreeCurricularPlanIDs) {
            final DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanID);
            if (degreeCurricularPlan == null) {
                continue;
            }

            final ExecutionDegree executionDegree =
                    degreeCurricularPlan.createExecutionDegree(executionYear, campus, publishedExamMap);
            setPeriods(executionDegree, examsSeason1, examsSeason2, examsSpecialSeason, lessonSeason1, lessonSeason2,
                    gradeSubmissionNormalSeason1, gradeSubmissionNormalSeason2, gradeSubmissionSpecialSeason);
            created.add(degreeCurricularPlan);
        }

        return created;
    }

    private static Campus readCampusByName(String campusName) {
        for (Campus campus : Campus.getAllActiveCampus()) {
            if (campus.getName().equalsIgnoreCase(campusName)) {
                return campus;
            }
        }
        return null;
    }

    private static OccupationPeriod getOccupationPeriod(final Calendar startDate, final Calendar endDate) {
        OccupationPeriod occupationPeriod =
                OccupationPeriod.readOccupationPeriod(YearMonthDay.fromCalendarFields(startDate),
                        YearMonthDay.fromCalendarFields(endDate));
        if (occupationPeriod == null) {
            occupationPeriod = new OccupationPeriod(startDate.getTime(), endDate.getTime());
            occupationPeriod.setNextPeriod(null);
        }
        return occupationPeriod;
    }

    protected static void setPeriods(ExecutionDegree executionDegree, OccupationPeriod periodExamsSeason1,
            OccupationPeriod periodExamsSeason2, OccupationPeriod periodExamsSpecialSeason, OccupationPeriod periodLessonSeason1,
            OccupationPeriod periodLessonSeason2, OccupationPeriod gradeSubmissionNormalSeason1,
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