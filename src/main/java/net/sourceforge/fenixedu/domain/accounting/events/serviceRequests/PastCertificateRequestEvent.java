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
package net.sourceforge.fenixedu.domain.accounting.events.serviceRequests;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.CertificateRequest;
import net.sourceforge.fenixedu.util.Money;

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

    @Deprecated
    public boolean hasPastAmount() {
        return getPastAmount() != null;
    }

}
