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
package net.sourceforge.fenixedu.domain.organizationalStructure;

public enum UnitClassification {

    CENTRAL_ORG, DIRECTIVE_COUNCIL, EXECUTIVE_DIRECTION, FINANCIER_DIRECTION, TECHNIQUE_DIRECTION, ACADEMIC_SERVICES_SUPERVISION,
    OUT_RELATION_SERVICES_SUPERVISION, SCIENTIFIC_PEDAGOGICAL_MANAGEMENT_RESOURCES_SERVICES_SUPERVISION, DEGREE_COORDINATION,
    SCIENCE_INFRASTRUCTURE, INFORMATION_TECHNOLOGY_LABORATORY, ASSOCIATED_LABORATORY, EXTRA_CURRICULAR_ACTIVITIES,
    PARTICIPATED_IST_INSTITUTION, DIVERSE_CONSTRUCTION_BUILDING, RESEARCH_UNIT, OUT;

    public String getName() {
        return name();
    }
}
