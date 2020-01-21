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

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.documents.GeneratedDocument;
import org.fenixedu.academic.domain.serviceRequests.IAcademicServiceRequest;
import org.fenixedu.academic.domain.student.Student;
import org.joda.time.DateTime;

public interface IDocumentRequest extends IAcademicServiceRequest {
    DocumentRequestType getDocumentRequestType();

    String getDocumentTemplateKey();

    ExecutionYear getExecutionYear();

    boolean hasExecutionYear();

    boolean isDiploma();

    boolean isCertificate();

    AdministrativeOffice getAdministrativeOffice();

    EventType getEventType();

    DateTime getRequestDate();

    DateTime getProcessingDate();

    boolean isToShowCredits();

    Student getStudent();

    GeneratedDocument getLastGeneratedDocument();

    byte[] generateDocument();

    String getReportFileName();
}
