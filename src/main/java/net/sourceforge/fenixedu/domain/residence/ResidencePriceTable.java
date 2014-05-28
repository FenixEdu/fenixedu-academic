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
package net.sourceforge.fenixedu.domain.residence;

public class ResidencePriceTable extends ResidencePriceTable_Base {

    public ResidencePriceTable() {
        super();
    }

    public boolean isConfigured() {
        return getDoubleRoomValue() != null && getSingleRoomValue() != null && getPaymentLimitDay() != null;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.organizationalStructure.ResidenceManagementUnit> getUnits() {
        return getUnitsSet();
    }

    @Deprecated
    public boolean hasAnyUnits() {
        return !getUnitsSet().isEmpty();
    }

    @Deprecated
    public boolean hasDoubleRoomValue() {
        return getDoubleRoomValue() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasSingleRoomValue() {
        return getSingleRoomValue() != null;
    }

    @Deprecated
    public boolean hasPaymentLimitDay() {
        return getPaymentLimitDay() != null;
    }

}
