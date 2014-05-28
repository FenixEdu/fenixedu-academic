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
/*
 * Created on Feb 10, 2006
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain.organizationalStructure;

public enum PartyTypeEnum {

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

    SECTION,

    RESEARCH_UNIT,

    PEDAGOGICAL_COUNCIL,

    SCIENTIFIC_COUNCIL,

    MANAGEMENT_COUNCIL,

    PHD_PROGRAM_UNIT;

    public String getName() {
        return name();
    }
}
