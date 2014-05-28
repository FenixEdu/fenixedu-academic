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
package net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/*
 * 
 * @author Fernanda Quitério 22/Dez/2003
 */
public class ReadExecutionCoursesByExecutionDegreeIdAndExecutionPeriodIdAndCurYear {

    @Atomic
    public static List run(String executionDegreeId, String executionPeriodId, Integer curricularYearInt)
            throws FenixServiceException {

        if (executionPeriodId == null) {
            throw new FenixServiceException("nullExecutionPeriodId");
        }

        final ExecutionSemester executionSemester = FenixFramework.getDomainObject(executionPeriodId);

        final List<ExecutionCourse> executionCourseList;
        if (executionDegreeId == null && curricularYearInt == null) {
            executionCourseList = executionSemester.getExecutionCoursesWithNoCurricularCourses();
        } else {
            final ExecutionDegree executionDegree = findExecutionDegreeByID(executionSemester, executionDegreeId);
            final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
            final CurricularYear curricularYear = CurricularYear.readByYear(curricularYearInt);
            executionCourseList =
                    executionSemester.getExecutionCoursesByDegreeCurricularPlanAndSemesterAndCurricularYearAndName(
                            degreeCurricularPlan, curricularYear, "%");
        }

        final List infoExecutionCourseList = new ArrayList(executionCourseList.size());
        for (final ExecutionCourse executionCourse : executionCourseList) {
            infoExecutionCourseList.add(InfoExecutionCourse.newInfoFromDomain(executionCourse));
        }

        return infoExecutionCourseList;
    }

    private static ExecutionDegree findExecutionDegreeByID(final ExecutionSemester executionSemester,
            final String executionDegreeId) {
        final ExecutionYear executionYear = executionSemester.getExecutionYear();
        for (final ExecutionDegree executionDegree : executionYear.getExecutionDegreesSet()) {
            if (executionDegree.getExternalId().equals(executionDegreeId)) {
                return executionDegree;
            }
        }
        return null;
    }

}