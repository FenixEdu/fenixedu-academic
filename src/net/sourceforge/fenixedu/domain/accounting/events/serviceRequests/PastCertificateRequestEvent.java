package net.sourceforge.fenixedu.domain.accounting.events.serviceRequests;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.CertificateRequest;
import net.sourceforge.fenixedu.util.Money;

public class PastCertificateRequestEvent extends PastCertificateRequestEvent_Base implements IPastRequestEvent {

    protected PastCertificateRequestEvent() {
        super();
    }

    public PastCertificateRequestEvent(final AdministrativeOffice administrativeOffice, final EventType eventType,
            final Person person, final CertificateRequest certificateRequest) {
        this();
        super.init(administrativeOffice, eventType, person, certificateRequest);
    }

    @Override
    public Set<EntryType> getPossibleEntryTypesForDeposit() {
        final Set<EntryType> result = new HashSet<EntryType>();

        result.add(EntryType.APPROVEMENT_CERTIFICATE_REQUEST_FEE);
        result.add(EntryType.DEGREE_FINALIZATION_CERTIFICATE_REQUEST_FEE);
        result.add(EntryType.ENROLMENT_CERTIFICATE_REQUEST_FEE);
        result.add(EntryType.SCHOOL_REGISTRATION_CERTIFICATE_REQUEST_FEE);

        return result;
    }

    @Override
    public void setPastAmount(Money pastAmount) {
        throw new DomainException("error.accounting.events.cannot.modify.pastAmount");
    }

}
