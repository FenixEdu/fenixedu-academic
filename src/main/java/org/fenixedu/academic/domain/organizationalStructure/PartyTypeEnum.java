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
 * Created on Feb 10, 2006
 *	by mrsp
 */
package org.fenixedu.academic.domain.organizationalStructure;

import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

public enum PartyTypeEnum {

    @Deprecated
    ADMINISTRATIVE_OFFICE_UNIT,

    SCIENTIFIC_AREA,

    COMPETENCE_COURSE_GROUP,

    AGGREGATE_UNIT,

    PLANET,

    COUNTRY,

    UNIVERSITY,

    SCHOOL,

    DEPARTMENT,

    DEGREE_UNIT,

    @Deprecated
    SECTION,

    @Deprecated
    RESEARCH_UNIT,

    @Deprecated
    PEDAGOGICAL_COUNCIL,

    @Deprecated
    SCIENTIFIC_COUNCIL,

    @Deprecated
    MANAGEMENT_COUNCIL,

    @Deprecated
    PHD_PROGRAM_UNIT,

    PERSON;

    public String getName() {
        return name();
    }

    public String getLocalizedName() {
        return BundleUtil.getString(Bundle.ENUMERATION, name());
    }

}
