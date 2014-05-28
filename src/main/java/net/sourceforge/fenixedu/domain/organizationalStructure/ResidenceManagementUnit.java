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

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.residence.ResidenceMonth;
import net.sourceforge.fenixedu.util.Money;

public class ResidenceManagementUnit extends ResidenceManagementUnit_Base {

    public ResidenceManagementUnit() {
        super();
    }

    public boolean isPaymentEventAvailable(Person person, ResidenceMonth month) {
        return month.isEventPresent(person);
    }

    public Integer getCurrentPaymentLimitDay() {
        return getResidencePriceTable().getPaymentLimitDay();
    }

    public Money getCurrentSingleRoomValue() {
        return getResidencePriceTable().getSingleRoomValue();
    }

    public Money getCurrentDoubleRoomValue() {
        return getResidencePriceTable().getDoubleRoomValue();
    }

    public boolean isResidencePriceTableConfigured() {
        return getResidencePriceTable().isConfigured();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.residence.ResidenceYear> getResidenceYears() {
        return getResidenceYearsSet();
    }

    @Deprecated
    public boolean hasAnyResidenceYears() {
        return !getResidenceYearsSet().isEmpty();
    }

    @Deprecated
    public boolean hasResidencePriceTable() {
        return getResidencePriceTable() != null;
    }

    @Deprecated
    public boolean hasBennuForResidenceUnit() {
        return getRootDomainObjectForResidenceUnit() != null;
    }

}
