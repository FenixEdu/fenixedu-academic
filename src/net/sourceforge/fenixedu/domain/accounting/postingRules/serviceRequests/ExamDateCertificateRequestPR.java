package net.sourceforge.fenixedu.domain.accounting.postingRules.serviceRequests;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.CertificateRequestEvent;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

public class ExamDateCertificateRequestPR extends ExamDateCertificateRequestPR_Base {

    protected ExamDateCertificateRequestPR() {
	super();
    }

    public ExamDateCertificateRequestPR(final ServiceAgreementTemplate serviceAgreementTemplate, final DateTime startDate,
	    final DateTime endDate, final Money baseAmount, final Money amountPerPage) {
	this();
	super.init(EntryType.EXAM_DATE_CERTIFICATE_REQUEST_FEE, EventType.EXAM_DATE_CERTIFICATE_REQUEST, startDate, endDate,
		serviceAgreementTemplate, baseAmount, amountPerPage);
    }

    @Checked("PostingRulePredicates.editPredicate")
    public ExamDateCertificateRequestPR edit(final Money baseAmount, final Money amountPerUnit) {
	deactivate();
	return new ExamDateCertificateRequestPR(getServiceAgreementTemplate(), new DateTime().minus(1000), null, baseAmount,
		amountPerUnit);
    }

    @Override
    protected Money getAmountForPages(final Event event) {
	final CertificateRequestEvent requestEvent = (CertificateRequestEvent) event;
	final int extraPages = requestEvent.getNumberOfPages().intValue() - 1;
	return getAmountPerPage().multiply(BigDecimal.valueOf(extraPages < 0 ? 0 : extraPages));
    }

    @Override
    protected boolean isUrgent(final Event event) {
	return ((CertificateRequestEvent) event).isUrgentRequest();
    }
}
