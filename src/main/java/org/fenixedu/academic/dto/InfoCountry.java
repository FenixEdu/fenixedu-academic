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
 * InfoCountry.java
 * 
 * Created on 13 de Dezembro de 2002, 16:28
 */

package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.Country;

/**
 * @author tfc130
 */
public class InfoCountry extends InfoObject {

    private final Country country;

    public InfoCountry(final Country country) {
        this.country = country;
    }

    public Country getCountry() {
        return country;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof InfoCountry && getCountry() == ((InfoCountry) obj).getCountry();
    }

    public String getCode() {
        return getCountry().getCode();
    }

    public String getName() {
        return getCountry().getName();
    }

    public String getNationality() {
        return getCountry().getNationality();
    }

    public static InfoCountry newInfoFromDomain(final Country country) {
        return country == null ? null : new InfoCountry(country);
    }

    public String getNameAndNationality() {
        return getCountry().getName() + " - " + getCountry().getNationality();
    }

    @Override
    public String getExternalId() {
        return getCountry().getExternalId();
    }

    @Override
    public void setExternalId(String integer) {
        throw new Error("Method should not be called!");
    }

}
