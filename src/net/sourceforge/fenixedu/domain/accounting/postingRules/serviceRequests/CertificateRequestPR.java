package net.sourceforge.fenixedu.domain.accounting.postingRules.serviceRequests;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.CertificateRequestEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

public class CertificateRequestPR extends CertificateRequestPR_Base {

    protected CertificateRequestPR() {
        super();
    }

    public CertificateRequestPR(EntryType entryType, EventType eventType, DateTime startDate,
            DateTime endDate, ServiceAgreementTemplate serviceAgreementTemplate, BigDecimal baseAmount,
            BigDecimal amountPerUnit, BigDecimal amountPerPage) {
        init(entryType, eventType, startDate, endDate, serviceAgreementTemplate, baseAmount,
                amountPerUnit, amountPerPage);
    }

    protected void init(EntryType entryType, EventType eventType, DateTime startDate, DateTime endDate,
            ServiceAgreementTemplate serviceAgreementTemplate, BigDecimal baseAmount,
            BigDecimal amountPerUnit, BigDecimal amountPerPage) {
        super.init(entryType, eventType, startDate, endDate, serviceAgreementTemplate, baseAmount,
                amountPerUnit);
        checkParameters(amountPerPage);
        super.setAmountPerPage(amountPerPage);
    }

    private void checkParameters(BigDecimal amountPerPage) {
        if (amountPerPage == null) {
            throw new DomainException(
                    "error.accounting.postingRules.serviceRequests.CertificateRequestPR.amountPerPage.cannot.be.null");
        }

    }

    @Override
    public void setAmountPerPage(BigDecimal amountPerPage) {
        throw new DomainException(
                "error.accounting.postingRules.serviceRequests.CertificateRequestPR.cannot.modify.amountPerPage");
    }

    @Override
    protected Integer getNumberOfUnits(Event event) {
        return ((CertificateRequestEvent) event).getNumberOfUnits();
    }

    protected boolean isUrgent(CertificateRequestEvent certificateRequestEvent) {
        return certificateRequestEvent.isUrgentRequest();
    }

    //FIXME: this method should be in superclass. subclasses should only reimplement variable part...
    @Override
    public BigDecimal calculateTotalAmountToPay(Event event, DateTime when) {
        final CertificateRequestEvent certificateRequestEvent = (CertificateRequestEvent) event;
        final BigDecimal totalAmountToPay = isUrgent(certificateRequestEvent) ? super
                .calculateTotalAmountToPay(certificateRequestEvent, when)
                .multiply(BigDecimal.valueOf(2)) : super.calculateTotalAmountToPay(event, when);

        return totalAmountToPay.add(calculateAmountToPayForPages(certificateRequestEvent)).subtract(event.calculatePayedAmount());
    }

    private BigDecimal calculateAmountToPayForPages(CertificateRequestEvent event) {
        return getAmountPerPage().multiply(BigDecimal.valueOf(event.getNumberOfPages()));
    }

}
