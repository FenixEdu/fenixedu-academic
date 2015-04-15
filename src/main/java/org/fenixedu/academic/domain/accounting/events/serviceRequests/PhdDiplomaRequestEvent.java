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
import org.fenixedu.academic.domain.phd.serviceRequests.documentRequests.PhdDiplomaRequest;
import org.fenixedu.academic.domain.serviceRequests.AcademicServiceRequest;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.LabelFormatter;

public class PhdDiplomaRequestEvent extends PhdDiplomaRequestEvent_Base {

    protected PhdDiplomaRequestEvent() {
        super();
    }

    protected PhdDiplomaRequestEvent(final AdministrativeOffice administrativeOffice, final EventType eventType,
            final Person person, final AcademicServiceRequest academicServiceRequest) {
        this();

        if (!academicServiceRequest.isDiploma()) {
            throw new DomainException("error.PhdRegistryDiplomaRequestEvent.request.is.not.diploma");
        }

        if (!EventType.BOLONHA_PHD_DIPLOMA_REQUEST.equals(eventType)) {
            throw new DomainException("error.PhdRegistryDiplomaRequestEvent.eventType.is.not.phd.diploma");
        }

        super.init(administrativeOffice, eventType, person, academicServiceRequest);
    }

    final static public PhdDiplomaRequestEvent create(final AdministrativeOffice administrativeOffice, final Person person,
            final PhdDiplomaRequest phdDiplomaRequest) {
        return new PhdDiplomaRequestEvent(administrativeOffice, phdDiplomaRequest.getEventType(), person, phdDiplomaRequest);
    }

    @Override
    public LabelFormatter getDescription() {
        final LabelFormatter result = super.getDescription();
        fillDescription(result);
        return result;
    }

    @Override
    final public LabelFormatter getDescriptionForEntryType(final EntryType entryType) {
        final LabelFormatter labelFormatter = new LabelFormatter();
        labelFormatter.appendLabel(entryType.name(), Bundle.ENUMERATION);
        fillDescription(labelFormatter);

        return labelFormatter;
    }

    private void fillDescription(final LabelFormatter labelFormatter) {
        labelFormatter.appendLabel(" (");
        final PhdDiplomaRequest request = (PhdDiplomaRequest) getAcademicServiceRequest();
        labelFormatter.appendLabel(request.getPhdIndividualProgramProcess().getPhdProgram().getName().getPreferedContent());
        labelFormatter.appendLabel(")");
    }

}
