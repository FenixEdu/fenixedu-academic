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

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.phd.serviceRequests.PhdAcademicServiceRequest;
import org.fenixedu.academic.domain.phd.serviceRequests.documentRequests.certificates.PhdFinalizationCertificateRequest;
import org.fenixedu.academic.domain.serviceRequests.AcademicServiceRequest;
import org.fenixedu.academic.util.LabelFormatter;

public class PhdFinalizationCertificateRequestEvent extends PhdFinalizationCertificateRequestEvent_Base {

    protected PhdFinalizationCertificateRequestEvent() {
        super();
    }

    protected PhdFinalizationCertificateRequestEvent(AdministrativeOffice administrativeOffice, EventType eventType,
            Person person, PhdFinalizationCertificateRequest academicServiceRequest) {
        this();

        init(administrativeOffice, eventType, person, academicServiceRequest);
    }

    @Override
    protected void init(AdministrativeOffice administrativeOffice, EventType eventType, Person person) {
        throw new DomainException("invoke init(AdministrativeOffice, EventType, Person, PhdFinalizationCertificateRequest)");
    }

    @Override
    protected void init(AdministrativeOffice administrativeOffice, EventType eventType, Person person,
            AcademicServiceRequest academicServiceRequest) {
        throw new DomainException("invoke init(AdministrativeOffice, EventType, Person, PhdFinalizationCertificateRequest)");
    }

    protected void init(AdministrativeOffice administrativeOffice, EventType eventType, Person person,
            PhdFinalizationCertificateRequest academicServiceRequest) {
        super.init(administrativeOffice, eventType, person, academicServiceRequest);
    }

    public static PhdFinalizationCertificateRequestEvent create(AdministrativeOffice administrativeOffice, Person person,
            PhdFinalizationCertificateRequest academicServiceRequest) {
        return new PhdFinalizationCertificateRequestEvent(administrativeOffice, EventType.PHD_FINALIZATION_CERTIFICATE_REQUEST,
                person, academicServiceRequest);
    }

    @Override
    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
        final LabelFormatter result = super.getDescription();
        fillDescription(result);
        return result;

    }

    private void fillDescription(final LabelFormatter labelFormatter) {
        labelFormatter.appendLabel(" (");
        final PhdAcademicServiceRequest request = (PhdAcademicServiceRequest) getAcademicServiceRequest();
        labelFormatter.appendLabel(request.getPhdIndividualProgramProcess().getPhdProgram().getName().getPreferedContent());
        labelFormatter.appendLabel(")");
    }
}
