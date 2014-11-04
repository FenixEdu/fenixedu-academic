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
package org.fenixedu.academic.domain.accounting.events.serviceRequests;

import java.util.Collections;
import java.util.Set;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.serviceRequests.EquivalencePlanRequest;
import org.fenixedu.academic.util.Money;

public class PastEquivalencePlanRequestEvent extends PastEquivalencePlanRequestEvent_Base implements IPastRequestEvent {

    protected PastEquivalencePlanRequestEvent() {
        super();
    }

    public PastEquivalencePlanRequestEvent(final AdministrativeOffice administrativeOffice, final Person person,
            final EquivalencePlanRequest request) {
        this();
        super.init(administrativeOffice, EventType.PAST_EQUIVALENCE_PLAN_REQUEST, person, request);
    }

    @Override
    public Set<EntryType> getPossibleEntryTypesForDeposit() {
        return Collections.singleton(EntryType.EQUIVALENCE_PLAN_REQUEST_FEE);
    }

    @Override
    public void setPastAmount(Money pastAmount) {
        throw new DomainException("error.accounting.events.cannot.modify.pastAmount");
    }

}
