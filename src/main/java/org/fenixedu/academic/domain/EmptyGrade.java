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

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.curriculum.grade.GradeScale;

public class EmptyGrade extends Grade {

    protected EmptyGrade() {
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public String getValue() {
        return null;
    }

    @Override
    public Integer getIntegerValue() {
        return null;
    }

    @Override
    public GradeScale getGradeScale() {
        return null;
    }

    @Override
    public int compareTo(Grade otherGrade) {
        return otherGrade.isEmpty() ? 0 : -1;
    }

    static protected boolean qualifiesAsEmpty(String value) {
        if (value != null) {
            value = value.trim();
        }
        return StringUtils.isEmpty(value) || value.equals("null");
    }

    @Override
    public String exportAsString() {
        return StringUtils.EMPTY;
    }

    @Override
    public boolean isNumeric() {
        return false;
    }

    @Override
    public boolean isApproved() {
        return false;
    }

    @Override
    public boolean isNotApproved() {
        return true;
    }

}
