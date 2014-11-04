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

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequest;
import net.sourceforge.fenixedu.domain.phd.serviceRequests.documentRequests.certificates.PhdFinalizationCertificateRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

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
