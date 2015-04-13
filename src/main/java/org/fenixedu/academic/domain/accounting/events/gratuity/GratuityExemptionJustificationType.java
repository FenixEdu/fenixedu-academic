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
package org.fenixedu.academic.domain.accounting.events.gratuity;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.I18N;

public enum GratuityExemptionJustificationType {

    // Staff and Agents from institution
    INSTITUTION,

    // Institution or Institution Partners Grant Owners
    INSTITUTION_GRANT_OWNER,

    // Staff from Institution Partners
    OTHER_INSTITUTION,

    // PALOP Teachers
    PALOP_TEACHER,

    SON_OF_DECORATED_MILITARY,

    SOCIAL_SHARE_GRANT_OWNER,

    // Students teaching classes
    STUDENT_TEACH,

    // Directive council authorization dispatch
    DIRECTIVE_COUNCIL_AUTHORIZATION,

    // Used to mark exemptions created by separation cycles
    SEPARATION_CYCLES_AUTHORIZATION;

    public String getName() {
        return name();
    }

    public String getQualifiedName() {
        return GratuityExemptionJustificationType.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
        return GratuityExemptionJustificationType.class.getName() + "." + name();
    }

    public static List<GratuityExemptionJustificationType> getTypesFor(final DegreeType degreeType) {
        if (degreeType.isEmpty()) {
            return Arrays.asList(new GratuityExemptionJustificationType[] { INSTITUTION, SON_OF_DECORATED_MILITARY,
                    SOCIAL_SHARE_GRANT_OWNER, DIRECTIVE_COUNCIL_AUTHORIZATION });
        }
        if (degreeType.isAdvancedSpecializationDiploma()) {
            return Arrays.asList(new GratuityExemptionJustificationType[] { INSTITUTION, OTHER_INSTITUTION, PALOP_TEACHER });
        }
        if (degreeType.isAdvancedFormationDiploma()) {
            return Arrays.asList(new GratuityExemptionJustificationType[] { INSTITUTION, INSTITUTION_GRANT_OWNER,
                    OTHER_INSTITUTION, PALOP_TEACHER, STUDENT_TEACH, DIRECTIVE_COUNCIL_AUTHORIZATION });
        }
        if (degreeType.isPreBolonhaDegree() || degreeType.isBolonhaMasterDegree() || degreeType.isBolonhaDegree()
                || degreeType.isIntegratedMasterDegree()) {
            return Arrays.asList(new GratuityExemptionJustificationType[] { SON_OF_DECORATED_MILITARY, SOCIAL_SHARE_GRANT_OWNER,
                    DIRECTIVE_COUNCIL_AUTHORIZATION, SEPARATION_CYCLES_AUTHORIZATION });
        }
        throw new RuntimeException("Unknown degree type");
    }

    public String localizedName(Locale locale) {
        return BundleUtil.getString(Bundle.ENUMERATION, getQualifiedName());
    }

    public String getLocalizedName() {
        return localizedName(I18N.getLocale());
    }

}
