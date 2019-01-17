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

import java.util.Collections;
import java.util.Set;

import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.serviceRequests.DocumentRequestCreateBean;

public class ExternalProgramCertificateRequest extends ExternalProgramCertificateRequest_Base {

    protected ExternalProgramCertificateRequest() {
        super();
    }

    public ExternalProgramCertificateRequest(final DocumentRequestCreateBean bean) {
        this();
        super.init(bean);

        checkParameters(bean);
        super.setNumberOfPrograms(bean.getNumberOfPrograms());
        super.setInstitution(bean.getInstitution());
    }

    @Override
    protected void checkParameters(final DocumentRequestCreateBean bean) {
        if (bean.getNumberOfPrograms() == null || bean.getNumberOfPrograms().intValue() == 0) {
            throw new DomainException("error.ExternalProgramCertificateRequest.invalid.numberOfPrograms");
        }
        if (bean.getInstitution() == null) {
            throw new DomainException("error.ExternalProgramCertificateRequest.invalid.institution");
        }
    }

    @Override
    public Set<Enrolment> getEnrolmentsSet() {
        return Collections.unmodifiableSet(super.getEnrolmentsSet());
    }

    @Override
    public void addEnrolments(Enrolment enrolments) {
        throw new DomainException("error.ExternalProgramCertificateRequest.cannot.add.enrolments");
    }

    @Override
    public void removeEnrolments(Enrolment enrolments) {
        throw new DomainException("error.ExternalProgramCertificateRequest.cannot.remove.enrolments");
    }

    @Override
    public DocumentRequestType getDocumentRequestType() {
        return DocumentRequestType.EXTERNAL_PROGRAM_CERTIFICATE;
    }

    @Override
    protected void disconnect() {
        super.setInstitution(null);
        super.disconnect();
    }

}
