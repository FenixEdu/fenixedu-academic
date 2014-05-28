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

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.spaces.domain.Space;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class AggregateUnit extends AggregateUnit_Base {

    private AggregateUnit() {
        super();
        super.setType(PartyTypeEnum.AGGREGATE_UNIT);
    }

    public static AggregateUnit createNewAggregateUnit(MultiLanguageString name, String unitNameCard, Integer costCenterCode,
            String acronym, YearMonthDay beginDate, YearMonthDay endDate, Unit parentUnit, AccountabilityType accountabilityType,
            String webAddress, UnitClassification classification, Boolean canBeResponsibleOfSpaces, Space campus) {

        AggregateUnit aggregateUnit = new AggregateUnit();
        aggregateUnit.init(name, unitNameCard, costCenterCode, acronym, beginDate, endDate, webAddress, classification, null,
                canBeResponsibleOfSpaces, campus);
        aggregateUnit.addParentUnit(parentUnit, accountabilityType);

        return aggregateUnit;
    }

    @Override
    public void setType(PartyTypeEnum partyTypeEnum) {
        throw new DomainException("unit.impossible.set.type");
    }

    @Override
    public boolean isAggregateUnit() {
        return true;
    }
}
