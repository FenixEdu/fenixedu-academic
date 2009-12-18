package net.sourceforge.fenixedu.domain.accounting.postingRules.serviceRequests;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

public class ApprovementCertificateRequestPR extends ApprovementCertificateRequestPR_Base {

    private ApprovementCertificateRequestPR() {
	super();
    }

    public ApprovementCertificateRequestPR(DateTime startDate, DateTime endDate,
	    ServiceAgreementTemplate serviceAgreementTemplate, Money baseAmount, Money amountPerUnit, Money amountPerPage,
	    Money maximumAmount) {
	init(EntryType.APPROVEMENT_CERTIFICATE_REQUEST_FEE, EventType.APPROVEMENT_CERTIFICATE_REQUEST, startDate, endDate,
		serviceAgreementTemplate, baseAmount, amountPerUnit, amountPerPage, maximumAmount);
    }

    @Override
    public Money getAmountForUnits(Event event) {
	if (getNumberOfUnits(event) <= 1) {
	    return Money.ZERO;
	}

	Money totalAmountOfUnits = getAmountPerUnit().multiply(new BigDecimal(getNumberOfUnits(event) - 1));

	if (this.getMaximumAmount().greaterThan(Money.ZERO)) {
	    if (totalAmountOfUnits.greaterThan(this.getMaximumAmount())) {
		totalAmountOfUnits = this.getMaximumAmount();
	    }
	}

	return totalAmountOfUnits;
    }

    @Checked("PostingRulePredicates.editPredicate")
    public ApprovementCertificateRequestPR edit(Money baseAmount, Money amountPerUnit, Money amountPerPage, Money maximumAmount) {

	deactivate();

	return new ApprovementCertificateRequestPR(new DateTime().minus(1000), null, getServiceAgreementTemplate(), baseAmount,
		amountPerUnit, amountPerPage, maximumAmount);
    }

}
