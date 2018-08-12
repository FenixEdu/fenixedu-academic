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
package org.fenixedu.academic.domain.phd.serviceRequests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.serviceRequests.documentRequests.PhdDiplomaRequest;
import org.fenixedu.academic.domain.phd.serviceRequests.documentRequests.PhdDocumentRequest;
import org.fenixedu.academic.domain.phd.serviceRequests.documentRequests.PhdRegistryDiplomaRequest;
import org.fenixedu.academic.domain.phd.serviceRequests.documentRequests.certificates.PhdFinalizationCertificateRequest;
import org.fenixedu.academic.domain.serviceRequests.RegistryCode;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.AcademicServiceRequestType;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentRequestType;

import pt.ist.fenixframework.Atomic;

public class PhdDocumentRequestCreateBean extends PhdAcademicServiceRequestCreateBean {
    private static final long serialVersionUID = 1L;

    private String givenNames;
    private String familyNames;

    private RegistryCode registryCode;
    private List<RegistryCode> associateCodes;

    private DocumentRequestType documentRequestType;

    public PhdDocumentRequestCreateBean(PhdIndividualProgramProcess phdIndividualProgramProcess) {
        super(phdIndividualProgramProcess);
        setRequestType(AcademicServiceRequestType.DOCUMENT);
        setGivenAndFamilyNames();
    }

    private void setGivenAndFamilyNames() {
        final Person person = getPhdIndividualProgramProcess().getPerson();

        if (StringUtils.isEmpty(person.getGivenNames())) {
            String[] parts = person.getName().split("\\s+");
            int split = parts.length > 3 ? 2 : 1;
            setGivenNames(StringUtils.join(Arrays.copyOfRange(parts, 0, split), " "));
            setFamilyNames(StringUtils.join(Arrays.copyOfRange(parts, split, parts.length), " "));
        } else {
            setGivenNames(person.getGivenNames());
            setFamilyNames(person.getFamilyNames());
        }
    }

    public String getGivenNames() {
        return givenNames;
    }

    public void setGivenNames(String givenNames) {
        this.givenNames = givenNames;
    }

    public String getFamilyNames() {
        return familyNames;
    }

    public void setFamilyNames(String familyNames) {
        this.familyNames = familyNames;
    }

    public RegistryCode getRegistryCode() {
        return registryCode;
    }

    public void setRegistryCode(RegistryCode registryCode) {
        this.registryCode = registryCode;
    }

    public List<RegistryCode> getAssociateCodes() {
        return associateCodes != null ? new ArrayList<>(associateCodes) : new ArrayList<>();
    }

    public void setAssociateCodes(List<RegistryCode> associateCodes) {
        this.associateCodes =  associateCodes != null ? new ArrayList<>(associateCodes) : new ArrayList<>();
    }

    public DocumentRequestType getDocumentRequestType() {
        return documentRequestType;
    }

    public void setDocumentRequestType(DocumentRequestType documentRequestType) {
        this.documentRequestType = documentRequestType;
    }

    @Atomic
    @Override
    public PhdDocumentRequest createNewRequest() {
        switch (getDocumentRequestType()) {
        case DIPLOMA_REQUEST:
            return PhdDiplomaRequest.create(this);
        case REGISTRY_DIPLOMA_REQUEST:
            return PhdRegistryDiplomaRequest.create(this);
        case PHD_FINALIZATION_CERTIFICATE:
            return PhdFinalizationCertificateRequest.create(this);
        default:
            throw new DomainException("error.PhdAcademicServiceRequest.create.document.request.type.unknown");
        }
    }
}
