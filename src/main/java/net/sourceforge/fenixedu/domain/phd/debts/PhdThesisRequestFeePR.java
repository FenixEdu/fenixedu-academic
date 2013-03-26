package net.sourceforge.fenixedu.domain.phd.debts;

import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

public class PhdThesisRequestFeePR extends PhdThesisRequestFeePR_Base {

    private PhdThesisRequestFeePR() {
        super();
    }

    public PhdThesisRequestFeePR(final DateTime startDate, final DateTime endDate,
            final ServiceAgreementTemplate serviceAgreementTemplate, final Money fixedAmount) {
        this();
        super.init(EntryType.PHD_THESIS_REQUEST_FEE, EventType.PHD_THESIS_REQUEST_FEE, startDate, endDate,
                serviceAgreementTemplate, fixedAmount);
    }

    public PhdThesisRequestFeePR edit(final Money fixedAmount, final Money penaltyAmount) {
        deactivate();
        return new PhdThesisRequestFeePR(new DateTime().minus(1000), null, getServiceAgreementTemplate(), fixedAmount);
    }

    @Override
    protected Money subtractFromExemptions(Event event, DateTime when, boolean applyDiscount, Money amountToPay) {
        final PhdThesisRequestFee requestEvent = (PhdThesisRequestFee) event;
        if (requestEvent.hasPhdEventExemption()) {
            amountToPay = amountToPay.subtract(requestEvent.getPhdEventExemption().getValue());
        }

        return amountToPay.isPositive() ? amountToPay : Money.ZERO;
    }
}
