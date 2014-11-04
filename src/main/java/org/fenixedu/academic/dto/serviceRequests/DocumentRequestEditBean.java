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
package org.fenixedu.academic.dto.serviceRequests;

import java.io.Serializable;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.serviceRequests.AcademicServiceRequestSituationType;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.CertificateRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentRequest;

public class DocumentRequestEditBean implements Serializable {
    private Person responsible;

    private DocumentRequest documentRequest;

    private AcademicServiceRequestSituationType academicServiceRequestSituationType;

    private Integer numberOfPages;

    private String justification;

    public DocumentRequestEditBean() {

    }

    public DocumentRequestEditBean(DocumentRequest documentRequest, Person responsible) {
        setDocumentRequest(documentRequest);
        setResponsible(responsible);
        setAcademicServiceRequestSituationType(documentRequest.getAcademicServiceRequestSituationType());
        setJustification(documentRequest.getActiveSituation().getJustification());

        if (documentRequest.isCertificate()) {
            setNumberOfPages(((CertificateRequest) documentRequest).getNumberOfPages());
        }
    }

    public DocumentRequest getDocumentRequest() {
        return this.documentRequest;
    }

    public void setDocumentRequest(DocumentRequest documentRequest) {
        this.documentRequest = documentRequest;
    }

    public Person getResponsible() {
        return responsible;
    }

    public void setResponsible(Person responsible) {
        this.responsible = responsible;
    }

    public AcademicServiceRequestSituationType getAcademicServiceRequestSituationType() {
        return academicServiceRequestSituationType;
    }

    public void setAcademicServiceRequestSituationType(AcademicServiceRequestSituationType academicServiceRequestSituationType) {
        this.academicServiceRequestSituationType = academicServiceRequestSituationType;
    }

    public Integer getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(Integer numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public String getJustification() {
        return justification;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }

}
