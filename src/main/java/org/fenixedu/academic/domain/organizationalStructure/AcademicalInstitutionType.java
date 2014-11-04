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
package org.fenixedu.academic.domain.organizationalStructure;

import java.util.Arrays;
import java.util.List;

public enum AcademicalInstitutionType {

    NATIONAL_PUBLIC_INSTITUTION,

    NATIONAL_PRIVATE_INSTITUTION,

    FOREIGN_INSTITUTION,

    OTHER_INSTITUTION,

    PUBLIC_HIGH_SCHOOL,

    PRIVATE_HIGH_SCHOOL,

    PRIVATE_AND_PUBLIC_HIGH_SCHOOL;

    public String getName() {
        return name();
    }

    static public List<AcademicalInstitutionType> getHighSchoolTypes() {
        return Arrays.asList(PUBLIC_HIGH_SCHOOL, PRIVATE_HIGH_SCHOOL, PRIVATE_AND_PUBLIC_HIGH_SCHOOL);
    }

    static public List<AcademicalInstitutionType> getHigherEducationTypes() {
        return Arrays.asList(NATIONAL_PUBLIC_INSTITUTION, NATIONAL_PRIVATE_INSTITUTION, FOREIGN_INSTITUTION, OTHER_INSTITUTION);
    }

}
