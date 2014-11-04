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
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.RegistryDiplomaRequest;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

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
        labelFormatter.appendLabel(entryType.name(), LabelFormatter.ENUMERATION_RESOURCES);
        fillDescription(labelFormatter);

        return labelFormatter;
    }

    private void fillDescription(final LabelFormatter labelFormatter) {
        labelFormatter.appendLabel(" (");
        final RegistryDiplomaRequest request = (RegistryDiplomaRequest) getAcademicServiceRequest();
        if (request.getRequestedCycle() != null) {
            labelFormatter.appendLabel(request.getRequestedCycle().getQualifiedName(), LabelFormatter.ENUMERATION_RESOURCES)
                    .appendLabel(" ").appendLabel("label.of", LabelFormatter.APPLICATION_RESOURCES).appendLabel(" ");
        }

        labelFormatter.appendLabel(getDegree().getDegreeType().name(), LabelFormatter.ENUMERATION_RESOURCES);
        labelFormatter.appendLabel(" ");
        labelFormatter.appendLabel("label.in", LabelFormatter.APPLICATION_RESOURCES);
        labelFormatter.appendLabel(" ");
        labelFormatter.appendLabel(getDegree().getNameFor(getExecutionYear()).getContent());
        labelFormatter.appendLabel(")");
    }
}
