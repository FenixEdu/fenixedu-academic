package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.CertificateRequestEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;

public abstract class CertificateRequest extends CertificateRequest_Base {

    protected CertificateRequest() {
        super();
    }

    @Override
    public void setCertificateRequestEvent(CertificateRequestEvent certificateRequestEvent) {
        throw new DomainException(
                "error.serviceRequests.documentRequests.CertificateRequest.cannot.modify.certificateRequestEvent");
    }

    public Integer getNumberOfUnits() {
        return 0;
    }

    private boolean isPayed() {
        return (hasCertificateRequestEvent() && getCertificateRequestEvent().isClosed());
    }

    public void edit(AcademicServiceRequestSituationType academicServiceRequestSituationType,
            Employee employee, String justification, Integer numberOfPages) {
        if (isPayed() && !getNumberOfPages().equals(numberOfPages)) {
            throw new DomainException(
                    "error.serviceRequests.documentRequests.CertificateRequest.cannot.change.numberOfPages.on.payed.certificates");

        }

        super.edit(academicServiceRequestSituationType, employee, justification, numberOfPages);
    }
}
