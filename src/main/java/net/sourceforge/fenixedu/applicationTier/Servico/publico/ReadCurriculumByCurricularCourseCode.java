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
package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.Curriculum;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.util.PeriodState;
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
        for (final ExecutionCourse executionCourse : curricularCourse.getAssociatedExecutionCourses()) {
            final ExecutionSemester executionSemester = executionCourse.getExecutionPeriod();
            if (executionSemester.getState().equals(PeriodState.OPEN) || executionSemester.getState().equals(PeriodState.CURRENT)) {
                infoExecutionCourses.add(InfoExecutionCourse.newInfoFromDomain(executionCourse));
            }
        }
        return infoExecutionCourses;
    }

    private static List<InfoCurricularCourseScope> buildActiveScopes(final CurricularCourse curricularCourse) {
        final List<InfoCurricularCourseScope> activeInfoCurricularCourseScopes = new ArrayList<InfoCurricularCourseScope>();
        for (final CurricularCourseScope curricularCourseScope : curricularCourse.getScopes()) {
            if (curricularCourseScope.isActive()) {
                activeInfoCurricularCourseScopes.add(InfoCurricularCourseScope.newInfoFromDomain(curricularCourseScope));
            }
        }
        return activeInfoCurricularCourseScopes;
    }
}