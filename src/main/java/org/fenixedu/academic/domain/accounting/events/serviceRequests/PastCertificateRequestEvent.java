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

import java.util.HashSet;
import java.util.Set;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.CertificateRequest;
import org.fenixedu.academic.util.Money;

public class PastCertificateRequestEvent extends PastCertificateRequestEvent_Base implements IPastRequestEvent {

    protected PastCertificateRequestEvent() {
        super();
    }

    public PastCertificateRequestEvent(final AdministrativeOffice administrativeOffice, final EventType eventType,
            final Person person, final CertificateRequest certificateRequest) {
        this();
        super.init(administrativeOffice, eventType, person, certificateRequest);
    }

    @Override
    public Set<EntryType> getPossibleEntryTypesForDeposit() {
        final Set<EntryType> result = new HashSet<EntryType>();

        result.add(EntryType.APPROVEMENT_CERTIFICATE_REQUEST_FEE);
        result.add(EntryType.DEGREE_FINALIZATION_CERTIFICATE_REQUEST_FEE);
        result.add(EntryType.ENROLMENT_CERTIFICATE_REQUEST_FEE);
        result.add(EntryType.SCHOOL_REGISTRATION_CERTIFICATE_REQUEST_FEE);

        return result;
    }

    @Override
    public void setPastAmount(Money pastAmount) {
        throw new DomainException("error.accounting.events.cannot.modify.pastAmount");
    }

}
