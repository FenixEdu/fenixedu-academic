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
package org.fenixedu.academic.domain.degreeStructure;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumLine;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;

public class NoEctsComparabilityTableFound extends DomainException {
    public NoEctsComparabilityTableFound(CurriculumLine curriculumLine) {
        super("error.no.ects.course.comparability.found", curriculumLine.getName().getContent());
    }

    public NoEctsComparabilityTableFound(AcademicInterval year, CycleType cycle) {
        super("error.no.ects.graduation.comparability.found", year.getPathName(), cycle.getDescription());
    }

    public NoEctsComparabilityTableFound(AcademicInterval year) {
        super("error.no.ects.any.comparability.found", year.getPathName());
    }
}
