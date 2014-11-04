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
package net.sourceforge.fenixedu.domain.gratuity;

import java.util.ArrayList;
import java.util.List;

public enum ExemptionGratuityType {

    INSTITUTION,

    INSTITUTION_GRANT_OWNER,

    OTHER_INSTITUTION,

    UNIVERSITY_TEACHER,

    POLYTECHNICAL_TEACHER,

    PALOP_TEACHER,

    STUDENT_TEACH,

    FCT_GRANT_OWNER,

    MILITARY_SON,

    OTHER;

    public static List percentageOfExemption() {
        List percentage = new ArrayList();

        percentage.add(Integer.valueOf(25));
        percentage.add(Integer.valueOf(50));
        percentage.add(Integer.valueOf(75));
        percentage.add(Integer.valueOf(100));

        return percentage;
    }

    public String getName() {
        return name();
    }

}
