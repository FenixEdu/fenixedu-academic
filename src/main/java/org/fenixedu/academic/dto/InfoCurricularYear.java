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
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.CurricularYear;

/**
 * @author dcs-rjao
 * 
 *         21/Mar/2003
 */

public class InfoCurricularYear extends InfoObject {

    private final CurricularYear curricularYear;

    public InfoCurricularYear(final CurricularYear curricularYear) {
        this.curricularYear = curricularYear;
    }

    public CurricularYear getCurricularYear() {
        return curricularYear;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof InfoCurricularYear && curricularYear == ((InfoCurricularYear) obj).curricularYear;
    }

    @Override
    public String toString() {
        return curricularYear == null ? null : curricularYear.toString();
    }

    public Integer getYear() {
        return curricularYear == null ? null : curricularYear.getYear();
    }

    public static InfoCurricularYear newInfoFromDomain(final CurricularYear curricularYear) {
        return curricularYear == null ? null : new InfoCurricularYear(curricularYear);
    }

    @Override
    public String getExternalId() {
        return curricularYear == null ? null : curricularYear.getExternalId();
    }

    @Override
    public void setExternalId(String integer) {
        throw new Error("Method should not be called!");
    }

}
