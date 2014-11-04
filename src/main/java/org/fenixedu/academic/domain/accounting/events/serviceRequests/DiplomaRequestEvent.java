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
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DiplomaRequest;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

abstract public class DiplomaRequestEvent extends DiplomaRequestEvent_Base {

    protected DiplomaRequestEvent() {
        super();
    }

    protected DiplomaRequestEvent(final AdministrativeOffice administrativeOffice, final EventType eventType,
            final Person person, final DiplomaRequest diplomaRequest) {
        this();
        super.init(administrativeOffice, eventType, person, diplomaRequest);
    }

    final static public DiplomaRequestEvent create(final AdministrativeOffice administrativeOffice, final Person person,
            final DiplomaRequest diplomaRequest) {
        switch (diplomaRequest.getEventType()) {
        case BOLONHA_DEGREE_DIPLOMA_REQUEST:
            return new BolonhaDegreeDiplomaRequestEvent(administrativeOffice, diplomaRequest.getEventType(), person,
                    diplomaRequest);
        case BOLONHA_MASTER_DEGREE_DIPLOMA_REQUEST:
            return new BolonhaMasterDegreeDiplomaRequestEvent(administrativeOffice, diplomaRequest.getEventType(), person,
                    diplomaRequest);
        case BOLONHA_ADVANCED_FORMATION_DIPLOMA_REQUEST:
            return new BolonhaAdvancedFormationDiplomaRequestEvent(administrativeOffice, diplomaRequest.getEventType(), person,
                    diplomaRequest);
        case BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA_REQUEST:
            return new BolonhaAdvancedSpecializationDiplomaRequestEvent(administrativeOffice, diplomaRequest.getEventType(),
                    person, diplomaRequest);
        default:
            throw new DomainException("DiplomaRequestEvent.invalid.event.type.in.creation");
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
        final DiplomaRequest request = (DiplomaRequest) getAcademicServiceRequest();
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

    @Override
    public PostingRule getPostingRule() {
        return getAdministrativeOffice().getServiceAgreementTemplate().findPostingRuleByEventTypeAndDate(getEventType(),
                getAcademicServiceRequest().getRequestDate());
    }

    @Override
    public boolean isExemptionAppliable() {
        return true;
    }

}
