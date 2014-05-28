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
package net.sourceforge.fenixedu.applicationTier.factoryExecutors;

import net.sourceforge.fenixedu.applicationTier.Servico.serviceRequests.documentRequests.CreatePastDiplomaRequest;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.Under23TransportsDeclarationRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.CertificateRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DeclarationRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DiplomaRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DiplomaSupplementRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.RegistryDiplomaRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
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
