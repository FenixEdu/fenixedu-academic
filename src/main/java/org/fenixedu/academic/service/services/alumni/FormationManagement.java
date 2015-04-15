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
package org.fenixedu.academic.service.services.alumni;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.organizationalStructure.AcademicalInstitutionType;
import org.fenixedu.academic.domain.organizationalStructure.AcademicalInstitutionUnit;
import org.fenixedu.academic.domain.organizationalStructure.CountryUnit;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.organizationalStructure.UniversityUnit;
import org.fenixedu.academic.dto.alumni.formation.AlumniFormation;
import org.fenixedu.academic.util.MultiLanguageString;
import org.joda.time.YearMonthDay;

public class FormationManagement {

    protected AcademicalInstitutionUnit getFormationInstitution(final AlumniFormation formation) {
        AcademicalInstitutionUnit institutionUnit = formation.getInstitution();

        if (institutionUnit == null
                && formation.getInstitutionType() != null
                && (formation.getInstitutionType().equals(AcademicalInstitutionType.FOREIGN_INSTITUTION) || formation
                        .getInstitutionType().equals(AcademicalInstitutionType.OTHER_INSTITUTION))) {

            if (!StringUtils.isEmpty(formation.getForeignUnit())) {
                institutionUnit =
                        UniversityUnit.createNewUniversityUnit(new MultiLanguageString(formation.getForeignUnit()), null, null,
                                null, new YearMonthDay(), null, getParentUnit(formation), null, null, false, null);
                institutionUnit.setInstitutionType(formation.getInstitutionType());
            }
        }
        return institutionUnit;
    }

    protected Unit getParentUnit(AlumniFormation formation) {
        if (formation.isNationalInstitution() || formation.getCountryUnit() == null) {
            return CountryUnit.getDefault();
        }

        return formation.getCountryUnit();
    }

}
