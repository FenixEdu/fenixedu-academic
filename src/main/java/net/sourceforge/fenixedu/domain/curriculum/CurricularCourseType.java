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
package net.sourceforge.fenixedu.domain.curriculum;

/**
 * @author dcs-rjao
 * 
 *         19/Mar/2003
 */

public enum CurricularCourseType {

    NORMAL_COURSE,

    OPTIONAL_COURSE,

    PROJECT_COURSE,

    TFC_COURSE,

    TRAINING_COURSE,

    LABORATORY_COURSE,

    M_TYPE_COURSE,

    P_TYPE_COURSE,

    DM_TYPE_COURSE,

    A_TYPE_COURSE,

    ML_TYPE_COURSE;

    public String getName() {
        return name();
    }

}
