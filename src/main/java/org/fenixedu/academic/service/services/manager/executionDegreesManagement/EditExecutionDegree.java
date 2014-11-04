/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.service.services.manager.executionDegreesManagement;

import static org.fenixedu.academic.predicate.AccessControl.check;

import java.util.Date;

import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.OccupationPeriod;
import org.fenixedu.academic.predicate.RolePredicates;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.spaces.domain.Space;
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

        final Space campus = (Space) FenixFramework.getDomainObject(campusID);
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