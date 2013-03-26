package net.sourceforge.fenixedu.domain.accounting.postingRules.serviceRequests;

import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

public class StandaloneEnrolmentCertificateRequestPR extends StandaloneEnrolmentCertificateRequestPR_Base {

    public StandaloneEnrolmentCertificateRequestPR() {
        super();
    }

    public StandaloneEnrolmentCertificateRequestPR(EntryType entryType, EventType eventType, DateTime startDate,
            DateTime endDate, ServiceAgreementTemplate serviceAgreementTemplate, Money baseAmount, Money amountPerUnit,
            Money amountPerPage) {
        init(entryType, eventType, startDate, endDate, serviceAgreementTemplate, baseAmount, amountPerUnit, amountPerPage);
    }
}
