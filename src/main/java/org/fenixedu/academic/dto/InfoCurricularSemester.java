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
package org.fenixedu.academic.dto;

import org.fenixedu.academic.domain.CurricularSemester;

/**
 * @author dcs-rjao
 * 
 *         19/Mar/2003
 */

public class InfoCurricularSemester extends InfoObject {

    private final CurricularSemester curricularSemester;

    public InfoCurricularSemester(final CurricularSemester curricularSemester) {
        this.curricularSemester = curricularSemester;
    }

    public CurricularSemester getCurricularSemester() {
        return curricularSemester;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof InfoCurricularSemester
                && getCurricularSemester() == ((InfoCurricularSemester) obj).getCurricularSemester();
    }

    @Override
    public String toString() {
        return getCurricularSemester().toString();
    }

    public InfoCurricularYear getInfoCurricularYear() {
        return InfoCurricularYear.newInfoFromDomain(getCurricularSemester().getCurricularYear());
    }

    public Integer getSemester() {
        return getCurricularSemester().getSemester();
    }

    public static InfoCurricularSemester newInfoFromDomain(final CurricularSemester curricularSemester) {
        return curricularSemester == null ? null : new InfoCurricularSemester(curricularSemester);
    }

    @Override
    public String getExternalId() {
        return getCurricularSemester().getExternalId();
    }

    @Override
    public void setExternalId(String integer) {
        throw new Error("Method should not be called!");
    }

}
