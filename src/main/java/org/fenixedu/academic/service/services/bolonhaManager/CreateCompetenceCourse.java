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

import java.util.Objects;

import org.fenixedu.academic.domain.CompetenceCourse;
import org.fenixedu.academic.domain.CompetenceCourseType;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.degreeStructure.CompetenceCourseLevel;
import org.fenixedu.academic.domain.degreeStructure.CurricularStage;
import org.fenixedu.academic.domain.degreeStructure.RegimeType;
import org.fenixedu.academic.domain.organizationalStructure.CompetenceCourseGroupUnit;
import org.fenixedu.academic.service.services.exceptions.ExistingCompetenceCourseInformationException;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.util.StringFormatter;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class CreateCompetenceCourse {

    @Atomic
    public static CompetenceCourse run(String name, String nameEn, String acronym, Boolean basic, RegimeType regimeType,
            CompetenceCourseLevel competenceCourseLevel, CompetenceCourseType type, String unitID,
            ExecutionSemester startSemester, String code) throws FenixServiceException {
        final CompetenceCourseGroupUnit unit = (CompetenceCourseGroupUnit) FenixFramework.getDomainObject(unitID);
        if (unit == null) {
            throw new FenixServiceException("error.invalidUnit");
        }
        checkIfCanCreateCompetenceCourse(name.trim(), nameEn.trim(), code);
        final CompetenceCourse competenceCourse = new CompetenceCourse(name, nameEn, basic, regimeType.convertToAcademicPeriod(),
                competenceCourseLevel, type, CurricularStage.DRAFT, unit, startSemester);
        competenceCourse.setCode(code);

        return competenceCourse;
    }

    private static void checkIfCanCreateCompetenceCourse(final String name, final String nameEn, String code)
            throws FenixServiceException {

        final String normalizedName = StringFormatter.normalize(name);
        final String normalizedNameEn = StringFormatter.normalize(nameEn);

        for (final CompetenceCourse competenceCourse : CompetenceCourse.readBolonhaCompetenceCourses()) {

            final boolean sameCode = Objects.equals(code, competenceCourse.getCode());

            if (StringFormatter.normalize(competenceCourse.getName()) != null) {
                if (StringFormatter.normalize(competenceCourse.getName()).equals(normalizedName) && sameCode) {
                    throw new ExistingCompetenceCourseInformationException("error.existingCompetenceCourseWithSameName",
                            competenceCourse.getDepartmentUnit().getName());
                }
            }
            if (StringFormatter.normalize(competenceCourse.getNameEn()) != null) {
                if (StringFormatter.normalize(competenceCourse.getNameEn()).equals(normalizedNameEn) && sameCode) {
                    throw new ExistingCompetenceCourseInformationException("error.existingCompetenceCourseWithSameNameEn",
                            competenceCourse.getDepartmentUnit().getName());
                }
            }
        }
    }
}