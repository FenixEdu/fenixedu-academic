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
 * Created on Dec 9, 2005
 */
package org.fenixedu.academic.service.services.bolonhaManager;

import org.fenixedu.academic.domain.CompetenceCourse;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.degreeStructure.CurricularStage;
import org.fenixedu.academic.domain.degreeStructure.OptionalCurricularCourse;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;

import pt.ist.fenixframework.Atomic;

public class EditCurricularCourse {

    @Atomic
    public static void run(CurricularCourse curricularCourse, Double weight, CompetenceCourse competenceCourse)
            throws FenixServiceException {
        curricularCourse.setWeigth(weight);
        curricularCourse.setCompetenceCourse(competenceCourse);
    }

    /**
     * @deprecated If CurricularCourse is not an OptionalCurricularCourse, this method edits fields that are no
     *             longer in use as they were moved to the
     *             {@link org.fenixedu.academic.domain.degreeStructure.CompetenceCourseInformation CompetenceCourseInformation}
     *             class
     */
    @Deprecated
    @Atomic
    public static void run(CurricularCourse curricularCourse, String name, String nameEn) throws FenixServiceException {
        curricularCourse.setName(name);
        curricularCourse.setNameEn(nameEn);
    }

    @Atomic
    public static void run(OptionalCurricularCourse curricularCourse, String name, String nameEn) throws FenixServiceException {
        curricularCourse.setName(name);
        curricularCourse.setNameEn(nameEn);
    }
}
