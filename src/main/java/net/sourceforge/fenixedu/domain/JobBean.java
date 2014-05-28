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
package net.sourceforge.fenixedu.domain;

import java.io.Serializable;

import org.joda.time.LocalDate;

public class JobBean implements Serializable {

    static private final long serialVersionUID = 5885003369040710968L;

    private BusinessArea parentBusinessArea;
    private BusinessArea childBusinessArea;

    private String city;
    private String employerName;
    private String position;

    private LocalDate beginDate;
    private LocalDate endDate;

    private Country country;

    public JobBean() {
    }

    public BusinessArea getParentBusinessArea() {
        return this.parentBusinessArea;
    }

    public void setParentBusinessArea(BusinessArea parentBusinessArea) {
        this.parentBusinessArea = parentBusinessArea;
    }

    public boolean hasParentBusinessArea() {
        return getParentBusinessArea() != null;
    }

    public BusinessArea getChildBusinessArea() {
        return this.childBusinessArea;
    }

    public void setChildBusinessArea(BusinessArea childBusinessArea) {
        this.childBusinessArea = childBusinessArea;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmployerName() {
        return employerName;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(LocalDate beginDate) {
        this.beginDate = beginDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Country getCountry() {
        return this.country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

}
