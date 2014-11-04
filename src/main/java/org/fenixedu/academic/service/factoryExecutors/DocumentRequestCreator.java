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
package org.fenixedu.academic.service.factoryExecutors;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.serviceRequests.Under23TransportsDeclarationRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.CertificateRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DeclarationRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DiplomaRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DiplomaSupplementRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentRequestType;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.RegistryDiplomaRequest;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.dto.serviceRequests.DocumentRequestCreateBean;
import org.fenixedu.academic.service.services.commons.FactoryExecutor;
import org.fenixedu.academic.service.services.serviceRequests.documentRequests.CreatePastDiplomaRequest;

import pt.ist.fenixframework.Atomic;

final public class DocumentRequestCreator extends DocumentRequestCreateBean implements FactoryExecutor {

    static private final long serialVersionUID = 1L;

    public DocumentRequestCreator(Registration registration) {
        super(registration);
    }

    @Override
    @Atomic
    public Object execute() {

        if (getChosenDocumentRequestType().isCertificate()) {
            return CertificateRequest.create(this);

        } else if (getChosenDocumentRequestType().isDeclaration()) {
            if (this.getExecutionYear() == null) {
                this.setExecutionYear(ExecutionYear.readCurrentExecutionYear());
            }
            return DeclarationRequest.create(this);

        } else if (getChosenDocumentRequestType().isDiploma()) {
            return new DiplomaRequest(this);

        } else if (getChosenDocumentRequestType().isRegistryDiploma()) {
            return new RegistryDiplomaRequest(this);

        } else if (getChosenDocumentRequestType().isPastDiploma()) {
            return CreatePastDiplomaRequest.create(this);

        } else if (getChosenDocumentRequestType().isDiplomaSupplement()) {
            return new DiplomaSupplementRequest(this);

        } else if (getChosenDocumentRequestType() == DocumentRequestType.UNDER_23_TRANSPORTS_REQUEST) {
            return new Under23TransportsDeclarationRequest(this);
        }

        throw new DomainException("error.DocumentRequestCreator.unexpected.document.request.type");
    }

}
