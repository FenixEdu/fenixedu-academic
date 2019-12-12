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
package org.fenixedu.academic.util;

import java.io.Serializable;

/**
 * @author dcs-rjao
 * 
 *         25/Mar/2003
 */
public class CurricularCourseExecutionScope implements Serializable {

    public static final int SEMESTRIAL = 1;

    public static final int ANUAL = 2;

    public static final CurricularCourseExecutionScope SEMESTRIAL_OBJ = new CurricularCourseExecutionScope(
            CurricularCourseExecutionScope.SEMESTRIAL);

    public static final CurricularCourseExecutionScope ANUAL_OBJ = new CurricularCourseExecutionScope(
            CurricularCourseExecutionScope.ANUAL);

    private final Integer type;

    public CurricularCourseExecutionScope(int state) {
        this.type = new Integer(state);
    }

    public CurricularCourseExecutionScope(Integer state) {
        this.type = state;
    }

    public java.lang.Integer getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof CurricularCourseExecutionScope) {
            CurricularCourseExecutionScope aux = (CurricularCourseExecutionScope) o;
            return this.type.equals(aux.getType());
        }
        return false;

    }

    @Override
    public String toString() {

        int value = this.type.intValue();
        String valueS = null;

        switch (value) {
        case SEMESTRIAL:
            valueS = "SEMESTRIAL";
            break;
        case ANUAL:
            valueS = "ANUAL";
            break;
        default:
            break;
        }

        return "[" + this.getClass().getName() + ": " + valueS + "]";
    }
}