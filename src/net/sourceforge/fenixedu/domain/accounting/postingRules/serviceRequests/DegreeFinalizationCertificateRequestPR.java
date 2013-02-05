package net.sourceforge.fenixedu.domain.accounting.postingRules.serviceRequests;

import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.DegreeFinalizationCertificateRequestEvent;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

public class DegreeFinalizationCertificateRequestPR extends DegreeFinalizationCertificateRequestPR_Base {

    protected DegreeFinalizationCertificateRequestPR() {
        super();
    }

    public DegreeFinalizationCertificateRequestPR(final DateTime startDate, DateTime endDate,
            final ServiceAgreementTemplate serviceAgreementTemplate, final Money baseAmount, final Money amountPerUnit,
            final Money amountPerPage, final Money maximumAmount) {
        this();
        init(EntryType.DEGREE_FINALIZATION_CERTIFICATE_REQUEST_FEE, EventType.DEGREE_FINALIZATION_CERTIFICATE_REQUEST, startDate,
                endDate, serviceAgreementTemplate, baseAmount, amountPerUnit, amountPerPage, maximumAmount);
    }

    @Override
    public DegreeFinalizationCertificateRequestPR edit(Money baseAmount, Money amountPerUnit, Money amountPerPage,
            Money maximumAmount) {

        deactivate();

        return new DegreeFinalizationCertificateRequestPR(new DateTime().minus(1000), null, getServiceAgreementTemplate(),
                baseAmount, amountPerUnit, amountPerPage, maximumAmount);
    }

    @Override
    protected Money subtractFromExemptions(Event event, DateTime when, boolean applyDiscount, Money amountToPay) {
        final DegreeFinalizationCertificateRequestEvent requestEvent = (DegreeFinalizationCertificateRequestEvent) event;

        if (requestEvent.hasAcademicEventExemption()) {
            return amountToPay.subtract(requestEvent.getAcademicEventExemption().getValue());
        }

        if (amountToPay.isNegative()) {
            return Money.ZERO;
        }

        return amountToPay;
    }
}
