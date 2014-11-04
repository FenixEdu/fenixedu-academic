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
/**
 * 
 */
package net.sourceforge.fenixedu.domain.curricularPeriod;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public enum CurricularPeriodType {

    FIVE_YEAR(5), THREE_YEAR(3), TWO_YEAR(2), YEAR(1), SEMESTER(0.5f), TRIMESTER(0.25f);

    private float weight;

    private CurricularPeriodType(float weight) {
        this.weight = weight;
    }

    public float getWeight() {
        return weight;
    }

    public String getAbbreviatedName() {
        return name() + ".ABBREVIATION";
    }

}
