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
package org.fenixedu.academic.dto.administrativeOffice.externalUnits;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.organizationalStructure.PartyTypeEnum;
import org.fenixedu.academic.domain.organizationalStructure.Unit;

public class ExternalUnitsSearchBean implements Serializable {

    private String unitName;
    private PartyTypeEnum unitType;
    private Unit earthUnit;
    private List<AbstractExternalUnitResultBean> results;

    public ExternalUnitsSearchBean() {
    }

    public ExternalUnitsSearchBean(final Unit earthUnit) {
        setEarthUnit(earthUnit);
    }

    public PartyTypeEnum getUnitType() {
        return unitType;
    }

    public void setUnitType(PartyTypeEnum type) {
        this.unitType = type;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Unit getEarthUnit() {
        return this.earthUnit;
    }

    public void setEarthUnit(Unit earthUnit) {
        this.earthUnit = earthUnit;
    }

    public List<AbstractExternalUnitResultBean> getResults() {
        return results;
    }

    public void setResults(List<AbstractExternalUnitResultBean> results) {
        this.results = results;
    }

    public boolean add(AbstractExternalUnitResultBean resultBean) {
        return this.results.add(resultBean);
    }

    public void clearResults() {
        if (this.results == null) {
            this.results = new ArrayList<AbstractExternalUnitResultBean>();
        } else {
            this.results.clear();
        }
    }

    public List<PartyTypeEnum> getValidPartyTypes() {
        final List<PartyTypeEnum> result = new ArrayList<PartyTypeEnum>(5);
        result.add(PartyTypeEnum.COUNTRY);
        result.add(PartyTypeEnum.UNIVERSITY);
        result.add(PartyTypeEnum.SCHOOL);
        result.add(PartyTypeEnum.DEPARTMENT);
        return result;
    }
}
