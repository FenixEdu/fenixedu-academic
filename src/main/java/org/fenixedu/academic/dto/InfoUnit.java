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
 * Created on Sep 22, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class InfoUnit extends InfoObject {

    private String name = "";

    private String costCenterCode = "";

    private String superiorUnitsNames = "";

    private String presentationName = "";

    public String getPresentationName() {
        return presentationName;
    }

    public void setPresentationName(String presentationName) {
        this.presentationName = presentationName;
    }

    public String getCostCenterCode() {
        return costCenterCode;
    }

    public void setCostCenterCode(String costCenterCode) {
        this.costCenterCode = costCenterCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSuperiorUnitsNames() {
        return superiorUnitsNames;
    }

    public void setSuperiorUnitsNames(String superiorUnitsNames) {
        this.superiorUnitsNames = superiorUnitsNames;
    }

    public void copyFromDomain(Unit unit) {
        super.copyFromDomain(unit);
        if (unit != null) {
            setCostCenterCode(unit.getCostCenterCode().toString());
            setName(unit.getName());
            setSuperiorUnitsNames(unit.getParentUnitsPresentationNameWithBreakLine());
            setPresentationName(unit.getPresentationName());
        }
    }

    public static InfoUnit newInfoFromDomain(Unit unit) {
        InfoUnit infoUnit = null;
        if (unit != null) {
            infoUnit = new InfoUnit();
            infoUnit.copyFromDomain(unit);
        }
        return infoUnit;
    }
}
