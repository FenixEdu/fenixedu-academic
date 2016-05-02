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
package org.fenixedu.academic.domain;

import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

public enum ProfessionalSituationConditionType {

    //TODO: RAIDES Provider and beans exclude this value.
    //This enum should be refactored to contain an "isActive"
    UNKNOWN,

    WORKS_FOR_OTHERS,

    EMPLOYEER,

    INDEPENDENT_WORKER,

    WORKS_FOR_FAMILY_WITHOUT_PAYMENT,

    RETIRED,

    UNEMPLOYED,

    HOUSEWIFE,

    STUDENT,

    INTERN,

    GRANT_HOLDER,

    //TODO: RAIDES Provider and beans exclude this value.
    //This enum should be refactored to contain an "isActive"
    MILITARY_SERVICE,

    OTHER;

    public String getName() {
        return name();
    }

    public String getQualifiedName() {
        return ProfessionalSituationConditionType.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
        return ProfessionalSituationConditionType.class.getName() + "." + name();
    }

    public String getLocalizedName() {
        return BundleUtil.getString(Bundle.ENUMERATION, getQualifiedName());
    }

}
