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
package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class EnrolmentDeclarationRequest extends EnrolmentDeclarationRequest_Base {

    private static final int MAX_FREE_DECLARATIONS_PER_EXECUTION_YEAR = 4;

    protected EnrolmentDeclarationRequest() {
        super();
    }

    public EnrolmentDeclarationRequest(final DocumentRequestCreateBean bean) {
        this();
        super.init(bean);

        checkRulesToCreate(bean);
    }

    private void checkRulesToCreate(final DocumentRequestCreateBean bean) {
        final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
        if (!bean.getRegistration().hasAnyEnrolmentsIn(currentExecutionYear)) {
            throw new DomainException("EnrolmentDeclarationRequest.no.enrolments.for.registration.in.current.executionYear");
        }
    }

    @Override
    final public DocumentRequestType getDocumentRequestType() {
        return DocumentRequestType.ENROLMENT_DECLARATION;
    }

    @Override
    final public String getDocumentTemplateKey() {
        return getClass().getName();
    }

    @Override
    final public EventType getEventType() {
        return EventType.ENROLMENT_DECLARATION_REQUEST;
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
