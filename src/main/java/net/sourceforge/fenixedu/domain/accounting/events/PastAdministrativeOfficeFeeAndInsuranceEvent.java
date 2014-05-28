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
package net.sourceforge.fenixedu.domain.accounting.events;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.Collections;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import net.sourceforge.fenixedu.util.Money;

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
                    "error.net.sourceforge.fenixedu.domain.accounting.events.PastAdministrativeOfficeFeeAndInsuranceEvent.pastAdministrativeOfficeFeeAndInsuranceAmount.cannot.be.null.and.must.be.greather.than.zero");
        }
    }

    @Override
    public void setPastAdministrativeOfficeFeeAndInsuranceAmount(Money pastAdministrativeOfficeFeeAndInsuranceAmount) {
        check(this, RolePredicates.MANAGER_PREDICATE);
        super.setPastAdministrativeOfficeFeeAndInsuranceAmount(pastAdministrativeOfficeFeeAndInsuranceAmount);
        // throw new DomainException(
        // "error.net.sourceforge.fenixedu.domain.accounting.events.PastAdministrativeOfficeFeeAndInsuranceEvent.cannot.modify.pastAdministrativeOfficeFeeAndInsuranceAmount"
        // );
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

    @Deprecated
    public boolean hasPastAdministrativeOfficeFeeAndInsuranceAmount() {
        return getPastAdministrativeOfficeFeeAndInsuranceAmount() != null;
    }

}
