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

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.documents.GeneratedDocument;
import net.sourceforge.fenixedu.domain.serviceRequests.IAcademicServiceRequest;
import net.sourceforge.fenixedu.domain.student.Student;

import org.joda.time.DateTime;

public interface IDocumentRequest extends IAcademicServiceRequest {
    public DocumentRequestType getDocumentRequestType();

    public String getDocumentTemplateKey();

    public ExecutionYear getExecutionYear();

    public boolean hasExecutionYear();

    public boolean isDiploma();

    public boolean isCertificate();

    public AdministrativeOffice getAdministrativeOffice();

    public EventType getEventType();

    public DateTime getRequestDate();

    public DateTime getProcessingDate();

    public boolean isToShowCredits();

    public Student getStudent();

    public GeneratedDocument getLastGeneratedDocument();

    public byte[] generateDocument();

    public String getReportFileName();
}
