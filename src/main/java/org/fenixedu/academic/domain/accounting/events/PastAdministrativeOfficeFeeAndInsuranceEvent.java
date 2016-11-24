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
package org.fenixedu.academic.domain.accounting.events;

import java.util.Collections;
import java.util.Set;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Money;

public class PastAdministrativeOfficeFeeAndInsuranceEvent extends PastAdministrativeOfficeFeeAndInsuranceEvent_Base {

    protected PastAdministrativeOfficeFeeAndInsuranceEvent() {
        super();
    }

    public PastAdministrativeOfficeFeeAndInsuranceEvent(AdministrativeOffice administrativeOffice, Person person,
            ExecutionYear executionYear, final Money pastAdministrativeOfficeFeeAndInsuranceAmount) {
        this();
        init(administrativeOffice, person, executionYear, pastAdministrativeOfficeFeeAndInsuranceAmount);
    }

    private void init(AdministrativeOffice administrativeOffice, Person person, ExecutionYear executionYear,
            Money pastAdministrativeOfficeFeeAndInsuranceAmount) {
        super.init(administrativeOffice, EventType.ADMINISTRATIVE_OFFICE_FEE_INSURANCE, person, executionYear);
        checkParameters(pastAdministrativeOfficeFeeAndInsuranceAmount);
        super.setPastAdministrativeOfficeFeeAndInsuranceAmount(pastAdministrativeOfficeFeeAndInsuranceAmount);
    }

    private void checkParameters(Money pastAdministrativeOfficeFeeAndInsuranceAmount) {
        if (pastAdministrativeOfficeFeeAndInsuranceAmount == null || pastAdministrativeOfficeFeeAndInsuranceAmount.isZero()) {
            throw new DomainException(
                    "error.org.fenixedu.academic.domain.accounting.events.PastAdministrativeOfficeFeeAndInsuranceEvent.pastAdministrativeOfficeFeeAndInsuranceAmount.cannot.be.null.and.must.be.greather.than.zero");
        }
    }

    @Override
    public boolean isExemptionAppliable() {
        return true;
    }

    @Override
    public Set<EntryType> getPossibleEntryTypesForDeposit() {
        return Collections.singleton(EntryType.ADMINISTRATIVE_OFFICE_FEE_INSURANCE);
    }

    @Override
    public boolean isInDebt() {
        return isOpen();
    }

}
