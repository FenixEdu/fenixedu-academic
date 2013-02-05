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
