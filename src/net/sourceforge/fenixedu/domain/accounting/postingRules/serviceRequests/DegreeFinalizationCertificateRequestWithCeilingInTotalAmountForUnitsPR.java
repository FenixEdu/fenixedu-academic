package net.sourceforge.fenixedu.domain.accounting.postingRules.serviceRequests;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

public class DegreeFinalizationCertificateRequestWithCeilingInTotalAmountForUnitsPR extends
	DegreeFinalizationCertificateRequestWithCeilingInTotalAmountForUnitsPR_Base {

    private DegreeFinalizationCertificateRequestWithCeilingInTotalAmountForUnitsPR() {
	super();
    }

    public DegreeFinalizationCertificateRequestWithCeilingInTotalAmountForUnitsPR(final DateTime startDate, DateTime endDate,
	    final ServiceAgreementTemplate serviceAgreementTemplate, final Money baseAmount, final Money amountPerUnit,
	    final Money amountPerPage, final Money maximumAmount) {
	this();
	init(EntryType.DEGREE_FINALIZATION_CERTIFICATE_REQUEST_FEE, EventType.DEGREE_FINALIZATION_CERTIFICATE_REQUEST, startDate,
		endDate, serviceAgreementTemplate, baseAmount, amountPerUnit, amountPerPage, maximumAmount);
    }

    @Override
    public Money getAmountForUnits(Integer numberOfUnits) {
	if (numberOfUnits <= 1) {
	    return Money.ZERO;
	}

	Money totalAmountOfUnits = getAmountPerUnit().multiply(new BigDecimal(numberOfUnits - 1));

	if (this.getMaximumAmount().greaterThan(Money.ZERO)) {
	    if (totalAmountOfUnits.greaterThan(this.getMaximumAmount())) {
		totalAmountOfUnits = this.getMaximumAmount();
	    }
	}

	return totalAmountOfUnits;

    }

    @Override
    public DegreeFinalizationCertificateRequestWithCeilingInTotalAmountForUnitsPR edit(Money baseAmount, Money amountPerUnit,
	    Money amountPerPage, Money maximumAmount) {

	deactivate();

	return new DegreeFinalizationCertificateRequestWithCeilingInTotalAmountForUnitsPR(new DateTime().minus(1000), null,
		getServiceAgreementTemplate(), baseAmount, amountPerUnit, amountPerPage, maximumAmount);
    }

}
