package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.AutoCompleteSearchService;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.PaymentCode;
import net.sourceforge.fenixedu.domain.accounting.events.AnnualEvent;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.AccountingEventPaymentCode;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.degreeChange.DegreeChangeCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.degreeTransfer.DegreeTransferCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.graduatedPerson.DegreeCandidacyForGraduatedPersonProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle.SecondCycleCandidacyProcess;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class SearchPaymentCodesByExecutionYear implements AutoCompleteSearchService, IService {

    @Override
    public Collection run(final Class type, final String value, int limit, final Map<String, String> arguments) {
	final ExecutionYear executionYear = getExecutionYear(arguments.get("executionYearOid"));

	final Collection<PaymentCode> result = findAnnualEventsPaymentCodeFor(value, executionYear);

	result.addAll(findIndividualCandidaciesEventPaymentCodesFor(DegreeTransferCandidacyProcess.class, value, executionYear));
	result.addAll(findIndividualCandidaciesEventPaymentCodesFor(DegreeChangeCandidacyProcess.class, value, executionYear));
	result.addAll(findIndividualCandidaciesEventPaymentCodesFor(SecondCycleCandidacyProcess.class, value, executionYear));
	result.addAll(findIndividualCandidaciesEventPaymentCodesFor(DegreeCandidacyForGraduatedPersonProcess.class, value,
		executionYear));
	result.addAll(findIndividualCandidaciesEventPaymentCodesFor(DegreeTransferCandidacyProcess.class, value, executionYear));

	return result;
    }

    private Collection<PaymentCode> findIndividualCandidaciesEventPaymentCodesFor(
	    final Class<? extends CandidacyProcess> candidacyProcessClazz, final String paymentCodeValue,
	    final ExecutionYear executionYear) {
	CandidacyProcess candidacyProcessByExecutionInterval = CandidacyProcess.getCandidacyProcessByExecutionInterval(
		candidacyProcessClazz, executionYear);

	final Collection<PaymentCode> result = new ArrayList<PaymentCode>();
	if (candidacyProcessByExecutionInterval == null) {
	    return result;
	}

	List<IndividualCandidacyProcess> childProcesses = candidacyProcessByExecutionInterval.getChildProcesses();
	for (IndividualCandidacyProcess individualCandidacyProcess : childProcesses) {
	    if (!individualCandidacyProcess.getCandidacy().hasEvent()) {
		continue;
	    }
	    if (individualCandidacyProcess.getCandidacy().getEvent().getAllPaymentCodes().isEmpty()) {
		continue;
	    }
	    for (PaymentCode paymentCode : individualCandidacyProcess.getCandidacy().getEvent().getAllPaymentCodes()) {
		
		if (paymentCode.getCode().startsWith(paymentCodeValue)) {
		    result.add(paymentCode);
		}
	    }
	}

	return result;
    }

    private Collection<PaymentCode> findAnnualEventsPaymentCodeFor(final String paymentCodeValue,
	    final ExecutionYear executionYear) {
	final Collection<PaymentCode> result = new ArrayList<PaymentCode>();
	for (final AnnualEvent event : executionYear.getAnnualEventsSet()) {
	    for (final AccountingEventPaymentCode code : event.getAllPaymentCodes()) {
		if (code.getCode().startsWith(paymentCodeValue)) {
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
