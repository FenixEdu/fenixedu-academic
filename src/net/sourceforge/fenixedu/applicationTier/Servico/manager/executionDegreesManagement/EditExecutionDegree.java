package net.sourceforge.fenixedu.applicationTier.Servico.manager.executionDegreesManagement;

import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Campus;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.OccupationPeriod;

import org.joda.time.YearMonthDay;

public class EditExecutionDegree extends Service {

    public void run(Integer executionDegreeID, Integer executionYearID, Integer campusID,
            Boolean temporaryExamMap, Date periodLessonsFirstSemesterBegin,
            Date periodLessonsFirstSemesterEnd, Date periodExamsFirstSemesterBegin,
            Date periodExamsFirstSemesterEnd, Date periodLessonsSecondSemesterBegin,
            Date periodLessonsSecondSemesterEnd, Date periodExamsSecondSemesterBegin,
            Date periodExamsSecondSemesterEnd, Date periodExamsSpecialSeasonBegin,
            Date periodExamsSpecialSeasonEnd, Date gradeSubmissionNormalSeason1EndDate, 
            Date gradeSubmissionNormalSeason2EndDate, Date gradeSubmissionSpecialSeasonEndDate) throws FenixServiceException {

        final ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeID);
        if (executionDegree == null) {
            throw new FenixServiceException("error.noExecutionDegree");
        }

        final ExecutionYear executionYear = rootDomainObject.readExecutionYearByOID(executionYearID);
        if (executionYear == null) {
            throw new FenixServiceException("error.noExecutionDegree");
        }

        final Campus campus = rootDomainObject.readCampusByOID(campusID);
        if (campus == null) {
            throw new FenixServiceException("error.noCampus");
        }
        
        final OccupationPeriod periodLessonsFirstSemester = getOccupationPeriod(periodLessonsFirstSemesterBegin, periodLessonsFirstSemesterEnd);
        final OccupationPeriod periodLessonsSecondSemester = getOccupationPeriod(periodLessonsSecondSemesterBegin, periodLessonsSecondSemesterEnd);
        
        final OccupationPeriod periodExamsFirstSemester = getOccupationPeriod(periodExamsFirstSemesterBegin, periodExamsFirstSemesterEnd);
        final OccupationPeriod periodExamsSecondSemester = getOccupationPeriod(periodExamsSecondSemesterBegin, periodExamsSecondSemesterEnd);
        final OccupationPeriod periodExamsSpecialSeason = getOccupationPeriod(periodExamsSpecialSeasonBegin, periodExamsSpecialSeasonEnd);
        
        final OccupationPeriod gradeSubmissionNormalSeason1 = getOccupationPeriod(periodExamsFirstSemesterBegin, gradeSubmissionNormalSeason1EndDate);
        final OccupationPeriod gradeSubmissionNormalSeason2 = getOccupationPeriod(periodExamsSecondSemesterBegin, gradeSubmissionNormalSeason2EndDate);
        final OccupationPeriod gradeSubmissionSpecialSeason = getOccupationPeriod(periodExamsSpecialSeasonBegin, gradeSubmissionSpecialSeasonEndDate);

        executionDegree.edit(executionYear, campus, temporaryExamMap, periodLessonsFirstSemester,
                periodExamsFirstSemester,periodLessonsSecondSemester, periodExamsSecondSemester,
                periodExamsSpecialSeason, gradeSubmissionNormalSeason1, gradeSubmissionNormalSeason2,
                gradeSubmissionSpecialSeason);
    }
    
    private OccupationPeriod getOccupationPeriod(final Date startDate, final Date endDate) {
        
        OccupationPeriod occupationPeriod = OccupationPeriod.readFor(YearMonthDay.fromDateFields(startDate), YearMonthDay.fromDateFields(endDate));
        if (occupationPeriod == null) {
            occupationPeriod = new OccupationPeriod(startDate, endDate);
            occupationPeriod.setNextPeriod(null);
        }
        return occupationPeriod;
    }
}
