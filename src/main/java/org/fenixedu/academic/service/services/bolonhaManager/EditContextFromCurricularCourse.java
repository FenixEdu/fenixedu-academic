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
package org.fenixedu.academic.service.services.bolonhaManager;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.curricularPeriod.CurricularPeriod;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicPeriod;
import org.fenixedu.academic.dto.CurricularPeriodInfoDTO;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class EditContextFromCurricularCourse {

    @Atomic
    public static void run(CurricularCourse curricularCourse, Context context, CourseGroup courseGroup, Integer year,
            Integer semester, Integer term, String beginExecutionPeriodID, String endExecutionPeriodID) {

        final CurricularPeriod degreeCurricularPeriod =
                context.getParentCourseGroup().getParentDegreeCurricularPlan().getDegreeStructure();

        // ********************************************************
        /*
         * TODO: Important - change this code (must be generic to support
         * several curricularPeriodInfoDTOs, instead of year and semester)
         */
        CurricularPeriod curricularPeriod = null;
        CurricularPeriodInfoDTO curricularPeriodInfoYear = null;
        if (courseGroup.getParentDegreeCurricularPlan().getDurationInYears() > 1) {
            curricularPeriodInfoYear = new CurricularPeriodInfoDTO(year, AcademicPeriod.YEAR);
        }
        final CurricularPeriodInfoDTO curricularPeriodInfoSemester =
                new CurricularPeriodInfoDTO(semester, AcademicPeriod.SEMESTER);

        if (curricularPeriodInfoYear != null) {
            curricularPeriod = degreeCurricularPeriod.getCurricularPeriod(curricularPeriodInfoYear, curricularPeriodInfoSemester);
            if (curricularPeriod == null) {
                curricularPeriod =
                        degreeCurricularPeriod.addCurricularPeriod(curricularPeriodInfoYear, curricularPeriodInfoSemester);
            }
        } else {
            curricularPeriod = degreeCurricularPeriod.getCurricularPeriod(curricularPeriodInfoSemester);
            if (curricularPeriod == null) {
                curricularPeriod = degreeCurricularPeriod.addCurricularPeriod(curricularPeriodInfoSemester);
            }
        }

        // ********************************************************

        context.edit(courseGroup, curricularPeriod, term, getBeginExecutionPeriod(beginExecutionPeriodID),
                getEndExecutionPeriod(endExecutionPeriodID));
    }

    private static ExecutionSemester getBeginExecutionPeriod(final String beginExecutionPeriodID) {
        if (beginExecutionPeriodID == null) {
            return ExecutionSemester.readActualExecutionSemester();
        } else {
            return FenixFramework.getDomainObject(beginExecutionPeriodID);
        }
    }

    private static ExecutionSemester getEndExecutionPeriod(String endExecutionPeriodID) {
        final ExecutionSemester endExecutionPeriod =
                (endExecutionPeriodID == null) ? null : FenixFramework.<ExecutionSemester> getDomainObject(endExecutionPeriodID);
        return endExecutionPeriod;
    }
}
