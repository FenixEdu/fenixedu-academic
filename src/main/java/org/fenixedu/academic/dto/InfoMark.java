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
 * InfoStudent.java
 *
 * Created on 13 de Dezembro de 2002, 16:04
 */

package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.Mark;

/**
 * 
 * @author tfc130
 */

public class InfoMark extends InfoObject {

    private final Mark markDomainReference;

    public InfoMark(final Mark mark) {
        markDomainReference = mark;
    }

    public static InfoMark newInfoFromDomain(Mark mark) {
        return mark == null ? null : new InfoMark(mark);
    }

    private Mark getMarkObject() {
        return markDomainReference;
    }

    @Override
    public String getExternalId() {
        return getMarkObject().getExternalId();
    }

    @Override
    public void setExternalId(String integer) {
        throw new Error("Method should not be called!");
    }

    /**
     * @return
     */
    public String getMark() {
        return getMarkObject().getMark();
    }

    /**
     * @return
     */
    public String getPublishedMark() {
        return getMarkObject().getPublishedMark();
    }

    /**
     * @return
     */
    public InfoEvaluation getInfoEvaluation() {
        return InfoEvaluation.newInfoFromDomain(getMarkObject().getEvaluation());
    }

    /**
     * @return
     */
    public InfoFrequenta getInfoFrequenta() {
        return InfoFrequenta.newInfoFromDomain(getMarkObject().getAttend());
    }

    @Override
    public String toString() {
        return getMarkObject().toString();
    }

}
