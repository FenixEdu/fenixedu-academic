package net.sourceforge.fenixedu.applicationTier.Servico.manager.executionDegreesManagement;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.predicates.RolePredicates;

import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class EditExecutionDegree {

    @Atomic
    public static void run(String executionDegreeID, String executionYearID, String campusID, Boolean publishedExamMap,
            Date periodLessonsFirstSemesterBegin, Date periodLessonsFirstSemesterEnd, Date periodExamsFirstSemesterBegin,
            Date periodExamsFirstSemesterEnd, Date periodLessonsSecondSemesterBegin, Date periodLessonsSecondSemesterEnd,
            Date periodExamsSecondSemesterBegin, Date periodExamsSecondSemesterEnd, Date periodExamsSpecialSeasonBegin,
            Date periodExamsSpecialSeasonEnd, Date gradeSubmissionNormalSeason1EndDate, Date gradeSubmissionNormalSeason2EndDate,
            Date gradeSubmissionSpecialSeasonEndDate) throws FenixServiceException {
        check(RolePredicates.MANAGER_OR_OPERATOR_PREDICATE);

        final ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeID);
        if (executionDegree == null) {
            throw new FenixServiceException("error.noExecutionDegree");
        }

        final ExecutionYear executionYear = FenixFramework.getDomainObject(executionYearID);
        if (executionYear == null) {
            throw new FenixServiceException("error.noExecutionDegree");
        }

        final Campus campus = (Campus) FenixFramework.getDomainObject(campusID);
        if (campus == null) {
            throw new FenixServiceException("error.noCampus");
        }

        final OccupationPeriod periodLessonsFirstSemester =
                getOccupationPeriod(periodLessonsFirstSemesterBegin, periodLessonsFirstSemesterEnd);
        final OccupationPeriod periodLessonsSecondSemester =
                getOccupationPeriod(periodLessonsSecondSemesterBegin, periodLessonsSecondSemesterEnd);

        final OccupationPeriod periodExamsFirstSemester =
                getOccupationPeriod(periodExamsFirstSemesterBegin, periodExamsFirstSemesterEnd);
        final OccupationPeriod periodExamsSecondSemester =
                getOccupationPeriod(periodExamsSecondSemesterBegin, periodExamsSecondSemesterEnd);
        final OccupationPeriod periodExamsSpecialSeason =
                getOccupationPeriod(periodExamsSpecialSeasonBegin, periodExamsSpecialSeasonEnd);

        final OccupationPeriod gradeSubmissionNormalSeason1 =
                getOccupationPeriod(periodExamsFirstSemesterBegin, gradeSubmissionNormalSeason1EndDate);
        final OccupationPeriod gradeSubmissionNormalSeason2 =
                getOccupationPeriod(periodExamsSecondSemesterBegin, gradeSubmissionNormalSeason2EndDate);
        final OccupationPeriod gradeSubmissionSpecialSeason =
                getOccupationPeriod(periodExamsSpecialSeasonBegin, gradeSubmissionSpecialSeasonEndDate);

        executionDegree.edit(executionYear, campus, publishedExamMap, periodLessonsFirstSemester, periodExamsFirstSemester,
                periodLessonsSecondSemester, periodExamsSecondSemester, periodExamsSpecialSeason, gradeSubmissionNormalSeason1,
                gradeSubmissionNormalSeason2, gradeSubmissionSpecialSeason);
    }

    private static OccupationPeriod getOccupationPeriod(final Date startDate, final Date endDate) {

        OccupationPeriod occupationPeriod =
                OccupationPeriod.readOccupationPeriod(YearMonthDay.fromDateFields(startDate),
                        YearMonthDay.fromDateFields(endDate));
        if (occupationPeriod == null) {
            occupationPeriod = new OccupationPeriod(startDate, endDate);
            occupationPeriod.setNextPeriod(null);
        }
        return occupationPeriod;
    }
}