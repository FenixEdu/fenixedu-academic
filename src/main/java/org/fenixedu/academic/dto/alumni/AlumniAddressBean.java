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
package net.sourceforge.fenixedu.dataTransferObject.alumni;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Alumni;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;

public class AlumniAddressBean implements Serializable {

    private Alumni alumni;
    private String address;
    private String areaCode;
    private String areaOfAreaCode;
    private Country country;

    public AlumniAddressBean(Alumni alumni) {
        setAlumni(alumni);
    }

    public AlumniAddressBean(Alumni alumni, PhysicalAddress address) {
        setAlumni(alumni);
        setAddress(address.getAddress());
        setAreaCode(address.getAreaCode());
        setAreaOfAreaCode(address.getAreaOfAreaCode());
        setCountry(address.getCountryOfResidence());
    }

    public void setAlumni(Alumni alumni) {
        this.alumni = alumni;
    }

    public Alumni getAlumni() {
        return this.alumni;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaOfAreaCode() {
        return areaOfAreaCode;
    }

    public void setAreaOfAreaCode(String areaOfAreaCode) {
        this.areaOfAreaCode = areaOfAreaCode;
    }

    public Country getCountry() {
        return this.country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

}
