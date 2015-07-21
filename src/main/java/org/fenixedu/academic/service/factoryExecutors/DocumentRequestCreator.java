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
import org.fenixedu.academic.domain.serviceRequests.AcademicServiceRequest;
import org.fenixedu.academic.domain.serviceRequests.CustomServiceRequestRequest;
import org.fenixedu.academic.domain.serviceRequests.Under23TransportsDeclarationRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.CertificateRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DeclarationRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DiplomaRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DiplomaSupplementRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentRequestType;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentSigner;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.RegistryDiplomaRequest;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.treasury.ITreasuryBridgeAPI;
import org.fenixedu.academic.dto.serviceRequests.DocumentRequestCreateBean;
import org.fenixedu.academic.service.services.commons.FactoryExecutor;
import org.fenixedu.academic.service.services.serviceRequests.documentRequests.CreatePastDiplomaRequest;
import org.fenixedu.bennu.signals.DomainObjectEvent;
import org.fenixedu.bennu.signals.Signal;

import pt.ist.fenixframework.Atomic;

final public class DocumentRequestCreator extends DocumentRequestCreateBean implements FactoryExecutor {

    static private final long serialVersionUID = 1L;

    public DocumentRequestCreator(Registration registration) {
        super(registration);
    }

    @Override
    @Atomic
    public Object execute() {

        AcademicServiceRequest academicServiceRequest = null;
        if (!getChosenServiceRequestType().isLegacy()) {
            academicServiceRequest = CustomServiceRequestRequest.create(this);
        } else {
            final DocumentRequestType requestType = getChosenServiceRequestType().getDocumentRequestType();
            if (requestType.isCertificate()) {
                academicServiceRequest = CertificateRequest.create(this);
            } else if (requestType.isDeclaration()) {
                if (this.getExecutionYear() == null) {
                    this.setExecutionYear(ExecutionYear.readCurrentExecutionYear());
                }

                academicServiceRequest = DeclarationRequest.create(this);
            } else if (requestType.isDiploma()) {
                academicServiceRequest = new DiplomaRequest(this);
            } else if (requestType.isRegistryDiploma()) {
                academicServiceRequest = new RegistryDiplomaRequest(this);
            } else if (requestType.isPastDiploma()) {
                academicServiceRequest = CreatePastDiplomaRequest.create(this);
            } else if (requestType.isDiplomaSupplement()) {
                academicServiceRequest = new DiplomaSupplementRequest(this);
            } else if (requestType == DocumentRequestType.UNDER_23_TRANSPORTS_REQUEST) {
                academicServiceRequest = new Under23TransportsDeclarationRequest(this);
            }
        }

        if (academicServiceRequest == null) {
            throw new DomainException("error.DocumentRequestCreator.unexpected.document.request.type");
        }

        Signal.emit(ITreasuryBridgeAPI.ACADEMIC_SERVICE_REQUEST_NEW_SITUATION_EVENT,
                new DomainObjectEvent<AcademicServiceRequest>(academicServiceRequest));

        academicServiceRequest.setDocumentSigner(DocumentSigner.findDefaultDocumentSignature());
        return academicServiceRequest;
    }

}
