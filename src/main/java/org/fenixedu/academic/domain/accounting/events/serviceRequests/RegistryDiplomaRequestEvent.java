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
import org.fenixedu.academic.domain.serviceRequests.documentRequests.RegistryDiplomaRequest;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.LabelFormatter;

public class RegistryDiplomaRequestEvent extends RegistryDiplomaRequestEvent_Base {

    protected RegistryDiplomaRequestEvent() {
        super();
    }

    protected RegistryDiplomaRequestEvent(final AdministrativeOffice administrativeOffice, final EventType eventType,
            final Person person, final RegistryDiplomaRequest registryDiplomaRequest) {
        this();
        super.init(administrativeOffice, eventType, person, registryDiplomaRequest);
    }

    final static public RegistryDiplomaRequestEvent create(final AdministrativeOffice administrativeOffice, final Person person,
            final RegistryDiplomaRequest registryDiplomaRequest) {
        switch (registryDiplomaRequest.getEventType()) {
        case BOLONHA_DEGREE_REGISTRY_DIPLOMA_REQUEST:
        case BOLONHA_MASTER_DEGREE_REGISTRY_DIPLOMA_REQUEST:
        case BOLONHA_ADVANCED_FORMATION_REGISTRY_DIPLOMA_REQUEST:
            return new RegistryDiplomaRequestEvent(administrativeOffice, registryDiplomaRequest.getEventType(), person,
                    registryDiplomaRequest);
        default:
            throw new DomainException("error.registryDiplomaRequestEvent.invalid.event.type.in.creation");
        }
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
        final RegistryDiplomaRequest request = (RegistryDiplomaRequest) getAcademicServiceRequest();
        if (request.getProgramConclusion() != null) {
            labelFormatter.appendLabel(request.getProgramConclusion().getName().getContent()).appendLabel(" ")
                    .appendLabel("label.of", Bundle.APPLICATION).appendLabel(" ");
        }

        labelFormatter.appendLabel(getDegree().getDegreeType().getName().getContent());
        labelFormatter.appendLabel(" ");
        labelFormatter.appendLabel("label.in", Bundle.APPLICATION);
        labelFormatter.appendLabel(" ");
        labelFormatter.appendLabel(getDegree().getNameFor(getExecutionYear()).getContent());
        labelFormatter.appendLabel(")");
    }
}
