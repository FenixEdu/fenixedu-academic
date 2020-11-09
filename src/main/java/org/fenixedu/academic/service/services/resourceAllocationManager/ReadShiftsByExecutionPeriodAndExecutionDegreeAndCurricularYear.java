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
/*
 * ReadShiftsByExecutionDegreeAndCurricularYear.java
 * 
 * Created on 2003/08/09
 */

package org.fenixedu.academic.service.services.resourceAllocationManager;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.CurricularYear;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;
import org.fenixedu.academic.dto.InfoCurricularYear;
import org.fenixedu.academic.dto.InfoExecutionDegree;
import org.fenixedu.academic.dto.InfoShift;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ReadShiftsByExecutionPeriodAndExecutionDegreeAndCurricularYear {

    @Atomic
    public static List<InfoShift> run(AcademicInterval academicInterval, InfoExecutionDegree infoExecutionDegree,
            InfoCurricularYear infoCurricularYear) {

        final ExecutionDegree executionDegree = FenixFramework.getDomainObject(infoExecutionDegree.getExternalId());
        final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
        final CurricularYear curricularYear = FenixFramework.getDomainObject(infoCurricularYear.getExternalId());
        final List<InfoShift> infoShifts = new ArrayList<InfoShift>();
        final List<ExecutionCourse> executionCourses =
                SearchExecutionCourses.filterByAcademicIntervalAndDegreeCurricularPlanAndCurricularYearAndName(academicInterval,
                        degreeCurricularPlan, curricularYear, "%");
        for (final ExecutionCourse executionCourse : executionCourses) {
            for (final Shift shift : executionCourse.getAssociatedShifts()) {
                final InfoShift infoShift = new InfoShift(shift);
                infoShifts.add(infoShift);
            }
        }

        return infoShifts;
    }
}