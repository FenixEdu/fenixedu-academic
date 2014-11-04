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
package org.fenixedu.academic.service.services.masterDegree.administrativeOffice;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.dto.InfoCurricularCourse;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NonExistingServiceException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Fernanda Quitério 01/07/2003
 * 
 */
public class ReadCurricularCoursesByDegree {

    @Atomic
    public static List run(String executionYearString, String degreeName) throws FenixServiceException {
        ExecutionYear executionYear = ExecutionYear.readExecutionYearByName(executionYearString);

        // Read degree
        ExecutionDegree executionDegree =
                ExecutionDegree.getByDegreeCurricularPlanNameAndExecutionYear(degreeName, executionYear);

        if (executionDegree == null || executionDegree.getDegreeCurricularPlan() == null
                || executionDegree.getDegreeCurricularPlan().getCurricularCoursesSet() == null
                || executionDegree.getDegreeCurricularPlan().getCurricularCoursesSet().isEmpty()) {
            throw new NonExistingServiceException();
        }

        List<InfoCurricularCourse> infoCurricularCourses = new ArrayList<InfoCurricularCourse>();
        for (CurricularCourse curricularCourse : executionDegree.getDegreeCurricularPlan().getCurricularCoursesSet()) {
            infoCurricularCourses.add(InfoCurricularCourse.newInfoFromDomain(curricularCourse));
        }

        return infoCurricularCourses;

    }

    @Atomic
    public static List run(String degreeCurricularPlanID) {
        DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanID);

        List<InfoCurricularCourse> infoCurricularCourses = new ArrayList<InfoCurricularCourse>();
        for (CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCoursesSet()) {
            infoCurricularCourses.add(InfoCurricularCourse.newInfoFromDomain(curricularCourse));
        }

        return infoCurricularCourses;
    }

}