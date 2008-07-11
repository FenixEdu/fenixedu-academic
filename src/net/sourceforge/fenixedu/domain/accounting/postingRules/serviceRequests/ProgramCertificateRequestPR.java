package net.sourceforge.fenixedu.domain.accounting.postingRules.serviceRequests;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.CertificateRequestEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

public class ProgramCertificateRequestPR extends ProgramCertificateRequestPR_Base {

    protected ProgramCertificateRequestPR() {
	super();
    }

    public ProgramCertificateRequestPR(final ServiceAgreementTemplate serviceAgreementTemplate, final DateTime startDate,
	    final DateTime endDate, final Money certificateAmount, final Money amountFirstPage, final Money amountPerPage) {
	this();
	super.init(EntryType.PROGRAM_CERTIFICATE_REQUEST_FEE, EventType.PROGRAM_CERTIFICATE_REQUEST, startDate, endDate,
		serviceAgreementTemplate, certificateAmount, amountPerPage);
	checkParameters(amountFirstPage);
	super.setAmountFirstPage(amountFirstPage);
    }

    protected void checkParameters(final Money amountFirstPage) {
	if (amountFirstPage == null) {
	    throw new DomainException("error.accounting.postingRules.ProgramCertificateRequestPR.amountFirstPage.cannot.be.null");
	}
    }

    @Checked("PostingRulePredicates.editPredicate")
    public ProgramCertificateRequestPR edit(final Money certificateAmount, final Money amountFirstPage, final Money amountPerPage) {
	deactivate();
	return new ProgramCertificateRequestPR(getServiceAgreementTemplate(), new DateTime().minus(1000), null,
		certificateAmount, amountFirstPage, amountPerPage);
    }

    @Override
    protected Money getAmountForPages(final Event event) {
	final CertificateRequestEvent requestEvent = (CertificateRequestEvent) event;
	// remove certificate page number
	int extraPages = requestEvent.getNumberOfPages().intValue() - 1;
	return (extraPages <= 0) ? Money.ZERO : getAmountFirstPage().add(
		getAmountPerPage().multiply(BigDecimal.valueOf(--extraPages)));
    }

    @Override
    protected boolean isUrgent(final Event event) {
	return ((CertificateRequestEvent) event).isUrgentRequest();
    }
}
