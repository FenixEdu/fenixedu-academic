package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.CertificateRequestEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

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

}
