/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular {

    @Atomic
    public static List run(InfoExecutionDegree infoExecutionDegree, InfoExecutionPeriod infoExecutionPeriod, Integer year) {
        check(RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE);

        List listInfoDE = new ArrayList();

        CurricularYear curricularYear = CurricularYear.readByYear(year);
        ExecutionSemester executionSemester = FenixFramework.getDomainObject(infoExecutionPeriod.getExternalId());
        DegreeCurricularPlan degreeCurricularPlan =
                FenixFramework.getDomainObject(infoExecutionDegree.getInfoDegreeCurricularPlan().getExternalId());

        if (executionSemester != null) {
            List<ExecutionCourse> listDCDE =
                    executionSemester.getExecutionCoursesByDegreeCurricularPlanAndSemesterAndCurricularYearAndName(
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

    @Atomic
    public static List run(InfoExecutionDegree infoExecutionDegree, AcademicInterval academicInterval, Integer year) {
        check(RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE);

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