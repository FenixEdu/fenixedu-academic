package net.sourceforge.fenixedu.domain.accounting.postingRules.serviceRequests;

import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

public class ExternalProgramCertificateRequestPR extends ExternalProgramCertificateRequestPR_Base {

    private ExternalProgramCertificateRequestPR() {
	super();
    }

    public ExternalProgramCertificateRequestPR(final ServiceAgreementTemplate serviceAgreementTemplate, final DateTime startDate,
	    final DateTime endDate, final Money certificateAmount, final Money amountFirstPage, final Money amountPerPage) {
	this();
	super.init(EntryType.EXTERNAL_PROGRAM_CERTIFICATE_REQUEST_FEE, EventType.EXTERNAL_PROGRAM_CERTIFICATE_REQUEST, startDate,
		endDate, serviceAgreementTemplate, certificateAmount, amountPerPage);
	checkParameters(amountFirstPage);
	super.setAmountFirstPage(amountFirstPage);
    }

    @Checked("PostingRulePredicates.editPredicate")
    public ExternalProgramCertificateRequestPR edit(final Money certificateAmount, final Money amountFirstPage,
	    final Money amountPerPage) {
	deactivate();
	return new ExternalProgramCertificateRequestPR(getServiceAgreementTemplate(), new DateTime().minus(1000), null,
		certificateAmount, amountFirstPage, amountPerPage);
    }
}
