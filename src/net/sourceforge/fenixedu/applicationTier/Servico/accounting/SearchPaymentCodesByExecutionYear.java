package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.AutoCompleteSearchService;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.PaymentCode;
import net.sourceforge.fenixedu.domain.accounting.events.AnnualEvent;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.AccountingEventPaymentCode;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class SearchPaymentCodesByExecutionYear implements AutoCompleteSearchService, IService {

    @Override
    public Collection run(final Class type, final String value, int limit, final Map<String, String> arguments) {
	final ExecutionYear executionYear = getExecutionYear(arguments.get("executionYearOid"));

	final Collection<PaymentCode> result = new ArrayList<PaymentCode>();
	for (final AnnualEvent event : executionYear.getAnnualEventsSet()) {
	    for (final AccountingEventPaymentCode code : event.getAllPaymentCodes()) {
		if (code.getCode().startsWith(value)) {
		    result.add(code);
		}
	    }
	}
	return result;
    }

    private ExecutionYear getExecutionYear(final String oid) {
	return (ExecutionYear) DomainObject.fromOID(Long.valueOf(oid).longValue());
    }

}
