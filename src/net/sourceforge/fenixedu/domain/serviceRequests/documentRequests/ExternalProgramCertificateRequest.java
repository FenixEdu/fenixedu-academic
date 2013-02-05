package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

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
    public List<Enrolment> getEnrolments() {
        return Collections.unmodifiableList(super.getEnrolments());
    }

    @Override
    public Set<Enrolment> getEnrolmentsSet() {
        return Collections.unmodifiableSet(super.getEnrolmentsSet());
    }

    @Override
    public Iterator<Enrolment> getEnrolmentsIterator() {
        return getEnrolmentsSet().iterator();
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
    public EventType getEventType() {
        return EventType.EXTERNAL_PROGRAM_CERTIFICATE_REQUEST;
    }

    @Override
    protected void disconnect() {
        super.setInstitution(null);
        super.disconnect();
    }

}
