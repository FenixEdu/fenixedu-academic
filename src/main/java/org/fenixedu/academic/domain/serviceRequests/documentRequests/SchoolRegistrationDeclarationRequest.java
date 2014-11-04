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
package org.fenixedu.academic.domain.serviceRequests.documentRequests;

import java.util.Set;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.serviceRequests.DocumentRequestCreateBean;

public class SchoolRegistrationDeclarationRequest extends SchoolRegistrationDeclarationRequest_Base {

    private static final int MAX_FREE_DECLARATIONS_PER_EXECUTION_YEAR = 4;

    protected SchoolRegistrationDeclarationRequest() {
        super();
    }

    public SchoolRegistrationDeclarationRequest(final DocumentRequestCreateBean bean) {
        this();
        checkRulesToCreate(bean);
        bean.setExecutionYear(getRequestedExecutionYear(bean));
        super.init(bean);
    }

    private ExecutionYear getRequestedExecutionYear(DocumentRequestCreateBean bean) {
        return bean.getRegistration().hasIndividualCandidacyFor(bean.getExecutionYear().getNextExecutionYear()) ? bean
                .getExecutionYear().getNextExecutionYear() : bean.getExecutionYear();
    }

    protected void checkRulesToCreate(final DocumentRequestCreateBean bean) {
        final ExecutionYear executionYear = bean.getExecutionYear();

        if (!bean.getRegistration().isRegistered(executionYear)
                && !bean.getRegistration().hasIndividualCandidacyFor(executionYear.getNextExecutionYear())) {
            throw new DomainException(
                    "SchoolRegistrationDeclarationRequest.registration.not.in.registered.state.in.current.executionYear");
        }
    }

    @Override
    final public DocumentRequestType getDocumentRequestType() {
        return DocumentRequestType.SCHOOL_REGISTRATION_DECLARATION;
    }

    @Override
    final public String getDocumentTemplateKey() {
        return getClass().getName();
    }

    @Override
    final public EventType getEventType() {
        return EventType.SCHOOL_REGISTRATION_DECLARATION_REQUEST;
    }

    @Override
    public boolean hasPersonalInfo() {
        return true;
    }

    @Override
    protected boolean hasFreeDeclarationRequests() {
        final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

        final Set<DocumentRequest> schoolRegistrationDeclarations =
                getRegistration().getSucessfullyFinishedDocumentRequestsBy(currentExecutionYear,
                        DocumentRequestType.SCHOOL_REGISTRATION_DECLARATION, false);

        final Set<DocumentRequest> enrolmentDeclarations =
                getRegistration().getSucessfullyFinishedDocumentRequestsBy(currentExecutionYear,
                        DocumentRequestType.ENROLMENT_DECLARATION, false);

        return ((schoolRegistrationDeclarations.size() + enrolmentDeclarations.size()) < MAX_FREE_DECLARATIONS_PER_EXECUTION_YEAR);
    }

}
