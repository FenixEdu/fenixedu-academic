package net.sourceforge.fenixedu.domain.phd.serviceRequests.documentRequests.certificates;

public abstract class PhdCertificateRequest extends PhdCertificateRequest_Base {

	protected PhdCertificateRequest() {
		super();
	}

	@Override
	public boolean isCertificate() {
		return true;
	}

}
