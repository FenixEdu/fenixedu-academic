package net.sourceforge.fenixedu.domain.accounting.events.serviceRequests;

import net.sourceforge.fenixedu.domain.Person;
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
    public void setPastAmount(Money pastAmount) {
	throw new DomainException("error.accounting.events.cannot.modify.pastAmount");
    }

}
