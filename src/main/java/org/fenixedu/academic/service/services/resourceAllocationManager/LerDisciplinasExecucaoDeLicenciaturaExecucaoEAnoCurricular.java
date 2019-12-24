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
package org.fenixedu.academic.service.services.resourceAllocationManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.fenixedu.academic.domain.CurricularYear;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;
import org.fenixedu.academic.dto.InfoExecutionCourse;
import org.fenixedu.academic.dto.InfoExecutionDegree;
import org.fenixedu.academic.dto.InfoExecutionPeriod;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular {

    @Atomic
    public static List run(InfoExecutionDegree infoExecutionDegree, InfoExecutionPeriod infoExecutionPeriod, Integer year) {

        List listInfoDE = new ArrayList();

        CurricularYear curricularYear = CurricularYear.readByYear(year);
        ExecutionInterval executionInterval = FenixFramework.getDomainObject(infoExecutionPeriod.getExternalId());
        DegreeCurricularPlan degreeCurricularPlan =
                FenixFramework.getDomainObject(infoExecutionDegree.getInfoDegreeCurricularPlan().getExternalId());

        if (executionInterval != null) {
            List<ExecutionCourse> listDCDE =
                    ExecutionCourse.getExecutionCoursesByDegreeCurricularPlanAndSemesterAndCurricularYearAndName(
                            executionInterval, degreeCurricularPlan, curricularYear, "%");

            Iterator iterator = listDCDE.iterator();
            listInfoDE = new ArrayList();
            while (iterator.hasNext()) {
                ExecutionCourse elem = (ExecutionCourse) iterator.next();

                listInfoDE.add(InfoExecutionCourse.newInfoFromDomain(elem));

            }
        }
        return listInfoDE;
    }

    @Atomic
    public static List run(InfoExecutionDegree infoExecutionDegree, AcademicInterval academicInterval, Integer year) {

        List listInfoDE = new ArrayList();

        CurricularYear curricularYear = CurricularYear.readByYear(year);

        DegreeCurricularPlan degreeCurricularPlan =
                FenixFramework.getDomainObject(infoExecutionDegree.getInfoDegreeCurricularPlan().getExternalId());

        if (academicInterval != null) {
            List<ExecutionCourse> listDCDE =
                    ExecutionCourse.filterByAcademicIntervalAndDegreeCurricularPlanAndCurricularYearAndName(academicInterval,
                            degreeCurricularPlan, curricularYear, "%");

            Iterator iterator = listDCDE.iterator();
            listInfoDE = new ArrayList();
            while (iterator.hasNext()) {
                ExecutionCourse elem = (ExecutionCourse) iterator.next();

                listInfoDE.add(InfoExecutionCourse.newInfoFromDomain(elem));

            }
        }
        return listInfoDE;
    }
}