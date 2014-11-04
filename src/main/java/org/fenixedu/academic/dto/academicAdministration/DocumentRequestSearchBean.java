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
package org.fenixedu.academic.dto.academicAdministration;

import java.io.Serializable;

import org.fenixedu.academic.domain.serviceRequests.AcademicServiceRequestSituationType;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.AcademicServiceRequestType;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentRequestType;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.dto.serviceRequests.IDocumentRequestBean;

public class DocumentRequestSearchBean implements Serializable, IDocumentRequestBean {

    private DocumentRequestType chosenDocumentRequestType;

    private Registration registration;

    private AcademicServiceRequestType academicServiceRequestType;

    private AcademicServiceRequestSituationType academicServiceRequestSituationType;

    private boolean urgentRequest;

    public DocumentRequestSearchBean() {
    }

    @Override
    public Registration getRegistration() {
        return registration;
    }

    public void setRegistration(Registration registration) {
        this.registration = registration;
    }

    public DocumentRequestType getChosenDocumentRequestType() {
        return chosenDocumentRequestType;
    }

    public void setChosenDocumentRequestType(DocumentRequestType chosenDocumentRequestType) {
        this.chosenDocumentRequestType = chosenDocumentRequestType;
    }

    @Override
    public boolean hasRegistration() {
        return getRegistration() != null;
    }

    public AcademicServiceRequestType getAcademicServiceRequestType() {
        return academicServiceRequestType;
    }

    public void setAcademicServiceRequestType(AcademicServiceRequestType academicServiceRequestType) {
        this.academicServiceRequestType = academicServiceRequestType;
    }

    public AcademicServiceRequestSituationType getAcademicServiceRequestSituationType() {
        return academicServiceRequestSituationType;
    }

    public void setAcademicServiceRequestSituationType(AcademicServiceRequestSituationType academicServiceRequestSituationType) {
        this.academicServiceRequestSituationType = academicServiceRequestSituationType;
    }

    public boolean getUrgentRequest() {
        return urgentRequest;
    }

    public void setUrgentRequest(boolean urgentRequest) {
        this.urgentRequest = urgentRequest;
    }

    public boolean isUrgentRequest() {
        return urgentRequest;
    }
}
