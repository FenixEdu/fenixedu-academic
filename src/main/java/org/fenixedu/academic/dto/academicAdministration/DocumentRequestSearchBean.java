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
package net.sourceforge.fenixedu.dataTransferObject.academicAdministration;

import java.io.Serializable;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.IDocumentRequestBean;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.AcademicServiceRequestType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;
import net.sourceforge.fenixedu.domain.student.Registration;

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
