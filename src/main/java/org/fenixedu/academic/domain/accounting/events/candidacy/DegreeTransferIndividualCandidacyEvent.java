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
package org.fenixedu.academic.domain.accounting.events.candidacy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOfficeType;
import org.fenixedu.academic.domain.candidacyProcess.degreeTransfer.DegreeTransferIndividualCandidacy;
import org.fenixedu.academic.domain.exceptions.DomainException;

public class DegreeTransferIndividualCandidacyEvent extends DegreeTransferIndividualCandidacyEvent_Base {

    private static final List<EventType> COMPATIBLE_TYPES = Arrays.asList(

    EventType.DEGREE_TRANSFER_INDIVIDUAL_CANDIDACY,

    EventType.DEGREE_CHANGE_INDIVIDUAL_CANDIDACY);

    private DegreeTransferIndividualCandidacyEvent() {
        super();
    }

    public DegreeTransferIndividualCandidacyEvent(final DegreeTransferIndividualCandidacy candidacy, final Person person) {
        this();
        super.init(candidacy, EventType.DEGREE_TRANSFER_INDIVIDUAL_CANDIDACY, person);

        attachAvailablePaymentCode(person);
    }

    @Override
    protected AdministrativeOffice readAdministrativeOffice() {
        return AdministrativeOffice.readByAdministrativeOfficeType(AdministrativeOfficeType.DEGREE);
    }

    @Override
    protected void checkConditionsToTransferPaymentsAndCancel(Event targetEvent) {

        if (!COMPATIBLE_TYPES.contains(targetEvent.getEventType())) {
            throw new DomainException("error.accounting.Event.events.must.be.compatible");
        }

        if (isCancelled()) {
            throw new DomainException("error.accounting.Event.cannot.transfer.payments.from.cancelled.events");
        }

        if (this == targetEvent) {
            throw new DomainException(
                    "error.org.fenixedu.academic.domain.accounting.Event.target.event.must.be.different.from.source");
        }
    }

    @Override
    public Set<EntryType> getPossibleEntryTypesForDeposit() {
        return Collections.singleton(EntryType.DEGREE_TRANSFER_INDIVIDUAL_CANDIDACY_FEE);
    }

    @Override
    public DegreeTransferIndividualCandidacy getIndividualCandidacy() {
        return (DegreeTransferIndividualCandidacy) super.getIndividualCandidacy();
    }

    public Degree getCandidacyDegree() {
        return getIndividualCandidacy().getSelectedDegree();
    }

    @Override
    protected EntryType getEntryType() {
        return EntryType.DEGREE_TRANSFER_INDIVIDUAL_CANDIDACY_FEE;
    }

}
