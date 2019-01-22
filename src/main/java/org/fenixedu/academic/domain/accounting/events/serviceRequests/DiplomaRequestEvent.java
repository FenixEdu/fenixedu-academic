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

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.PostingRule;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.serviceRequests.RegistrationAcademicServiceRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DiplomaRequest;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.LabelFormatter;

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
    protected LabelFormatter getDescriptionForEntryType(final EntryType entryType) {
        final LabelFormatter labelFormatter = new LabelFormatter();
        labelFormatter.appendLabel(entryType.name(), Bundle.ENUMERATION);
        fillDescription(labelFormatter);

        return labelFormatter;
    }

    private void fillDescription(final LabelFormatter labelFormatter) {
        final RegistrationAcademicServiceRequest request = getAcademicServiceRequest();
        final ExecutionYear executionYear = getExecutionYear();

        labelFormatter.appendLabel(" (");

        if (request instanceof DiplomaRequest) {
            if (((DiplomaRequest)request).getRequestedCycle() != null) {
                labelFormatter.appendLabel(((DiplomaRequest)request).getRequestedCycle().getQualifiedName(), Bundle.ENUMERATION).appendLabel(" ")
                        .appendLabel("label.of", Bundle.APPLICATION).appendLabel(" ");
            }
        } else {
            labelFormatter.appendLabel(" ");
        }

        labelFormatter.appendLabel(getDegree().getDegreeType().getName().getContent());
        labelFormatter.appendLabel(" ");
        labelFormatter.appendLabel("label.in", Bundle.APPLICATION);
        labelFormatter.appendLabel(" ");
        labelFormatter.appendLabel(getDegree().getNameFor(executionYear).getContent());
        labelFormatter.appendLabel(")");

        if(executionYear != null) {
            labelFormatter.appendLabel(" - ").appendLabel(executionYear.getYear());
        }

        labelFormatter.appendLabel(" - ").appendLabel(request.getServiceRequestNumberYear());

    }

    @Override
    public PostingRule getPostingRule() {
        return getAdministrativeOffice().getServiceAgreementTemplate().findPostingRuleByEventTypeAndDate(getEventType(),
                getAcademicServiceRequest().getRequestDate());
    }

    @Override public EntryType getEntryType() {
        return EntryType.DIPLOMA_REQUEST_FEE;
    }

}
