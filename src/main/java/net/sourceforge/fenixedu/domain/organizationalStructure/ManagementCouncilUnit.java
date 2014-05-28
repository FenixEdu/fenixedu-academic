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
package net.sourceforge.fenixedu.domain.organizationalStructure;

import net.sourceforge.fenixedu.domain.ManagementCouncilSite;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ManagementCouncilUnit extends ManagementCouncilUnit_Base {

    private ManagementCouncilUnit() {
        super();
        super.setType(PartyTypeEnum.MANAGEMENT_COUNCIL);
        new ManagementCouncilSite(this);
    }

    public static ManagementCouncilUnit createManagementCouncilUnit(MultiLanguageString name, String unitNameCard,
            Integer costCenterCode, String acronym, YearMonthDay beginDate, YearMonthDay endDate, Unit parentUnit,
            AccountabilityType accountabilityType, String webAddress, UnitClassification classification,
            Boolean canBeResponsibleOfSpaces, Space campus) {

        if (!PartyType.getPartiesSet(PartyTypeEnum.MANAGEMENT_COUNCIL).isEmpty()) {
            throw new DomainException("error.can.only.exist.one.managementCouncilUnit");
        }
        ManagementCouncilUnit unit = new ManagementCouncilUnit();
        unit.init(name, unitNameCard, costCenterCode, acronym, beginDate, endDate, webAddress, classification, null,
                canBeResponsibleOfSpaces, campus);
        unit.addParentUnit(parentUnit, accountabilityType);

        return unit;
    }

    @Override
    public void setAcronym(String acronym) {
        if (StringUtils.isEmpty(acronym)) {
            throw new DomainException("acronym.cannot.be.null");
        }
        super.setAcronym(acronym);
    }

    @Override
    public void setType(PartyTypeEnum partyTypeEnum) {
        throw new DomainException("unit.impossible.set.type");
    }
}
