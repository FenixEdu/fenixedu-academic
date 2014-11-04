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
package org.fenixedu.academic.service.services.publico;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.CurricularCourseScope;
import org.fenixedu.academic.domain.Curriculum;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.dto.InfoCurricularCourse;
import org.fenixedu.academic.dto.InfoCurricularCourseScope;
import org.fenixedu.academic.dto.InfoCurriculum;
import org.fenixedu.academic.dto.InfoExecutionCourse;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NonExistingServiceException;
import org.fenixedu.academic.util.PeriodState;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ReadCurriculumByCurricularCourseCode {

    @Atomic
    public static InfoCurriculum run(final String curricularCourseCode) throws FenixServiceException {

        if (curricularCourseCode == null) {
            throw new FenixServiceException("nullCurricularCourse");
        }

        final CurricularCourse curricularCourse = (CurricularCourse) FenixFramework.getDomainObject(curricularCourseCode);
        if (curricularCourse == null) {
            throw new NonExistingServiceException();
        }

        final Curriculum curriculum = curricularCourse.findLatestCurriculum();
        final InfoCurriculum infoCurriculum =
                (curriculum != null) ? InfoCurriculum.newInfoFromDomain(curriculum) : new InfoCurriculum();
        infoCurriculum.setInfoCurricularCourse(InfoCurricularCourse.newInfoFromDomain(curricularCourse));

        List infoExecutionCourses = buildExecutionCourses(curricularCourse);
        infoCurriculum.getInfoCurricularCourse().setInfoAssociatedExecutionCourses(infoExecutionCourses);

        List activeInfoScopes = buildActiveScopes(curricularCourse);
        infoCurriculum.getInfoCurricularCourse().setInfoScopes(activeInfoScopes);

        return infoCurriculum;
    }

    private static List buildExecutionCourses(final CurricularCourse curricularCourse) {
        final List<InfoExecutionCourse> infoExecutionCourses = new ArrayList<InfoExecutionCourse>();
        for (final ExecutionCourse executionCourse : curricularCourse.getAssociatedExecutionCoursesSet()) {
            final ExecutionSemester executionSemester = executionCourse.getExecutionPeriod();
            if (executionSemester.getState().equals(PeriodState.OPEN) || executionSemester.getState().equals(PeriodState.CURRENT)) {
                infoExecutionCourses.add(InfoExecutionCourse.newInfoFromDomain(executionCourse));
            }
        }
        return infoExecutionCourses;
    }

    private static List<InfoCurricularCourseScope> buildActiveScopes(final CurricularCourse curricularCourse) {
        final List<InfoCurricularCourseScope> activeInfoCurricularCourseScopes = new ArrayList<InfoCurricularCourseScope>();
        for (final CurricularCourseScope curricularCourseScope : curricularCourse.getScopesSet()) {
            if (curricularCourseScope.isActive()) {
                activeInfoCurricularCourseScopes.add(InfoCurricularCourseScope.newInfoFromDomain(curricularCourseScope));
            }
        }
        return activeInfoCurricularCourseScopes;
    }
}