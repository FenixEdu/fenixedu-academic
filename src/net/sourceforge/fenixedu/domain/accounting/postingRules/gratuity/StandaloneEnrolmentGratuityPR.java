package net.sourceforge.fenixedu.domain.accounting.postingRules.gratuity;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.AccountingTransactionDetailDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountingTransaction;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

public class StandaloneEnrolmentGratuityPR extends StandaloneEnrolmentGratuityPR_Base {

    protected StandaloneEnrolmentGratuityPR() {
	super();
    }

    public StandaloneEnrolmentGratuityPR(DateTime startDate, DateTime endDate, ServiceAgreementTemplate serviceAgreementTemplate,
	    BigDecimal ectsForYear) {
	this();
	init(startDate, endDate, serviceAgreementTemplate, ectsForYear);
    }

    private void init(DateTime startDate, DateTime endDate, ServiceAgreementTemplate serviceAgreementTemplate,
	    BigDecimal ectsForYear) {

	super.init(EntryType.STANDALONE_ENROLMENT_GRATUITY_FEE, EventType.STANDALONE_ENROLMENT_GRATUITY, startDate, endDate,
		serviceAgreementTemplate);

	checkParameters(ectsForYear);

	super.setEctsForYear(ectsForYear);

    }

    private void checkParameters(BigDecimal ectsForYear) {
	if (ectsForYear == null) {
	    throw new DomainException(
		    "error.accounting.postingRules.gratuity.StandaloneEnrolmentGratuityPR.ectsForYear.cannot.be.null");
	}

    }

    @Override
    public List<EntryDTO> calculateEntries(Event event, DateTime when) {
	return Collections.singletonList(new EntryDTO(getEntryType(), event, calculateTotalAmountToPay(event, when), event
		.getPayedAmount(), event.calculateAmountToPay(when), event.getDescriptionForEntryType(getEntryType()), event
		.calculateAmountToPay(when)));
    }

    @Override
    public Money calculateTotalAmountToPay(Event event, DateTime when, boolean applyDiscount) {
	final GratuityEvent gratuityEvent = (GratuityEvent) event;

	Money result = Money.ZERO;

	for (final Map.Entry<DegreeCurricularPlan, BigDecimal> entry : groupEctsByDegreeCurricularPlan(gratuityEvent).entrySet()) {
	    result = result.add(calculateAmountForDegreeCurricularPlan(entry.getKey(), entry.getValue(), gratuityEvent));
	}

	return result;
    }

    /**
     * Formula: 0.5 x TotalGratuity x (1 + EnroledEcts / TotalEctsForYear)
     * 
     * @param degreeCurricularPlan
     * @param totalEcts
     * @param gratuityEvent
     * @return
     */
    private Money calculateAmountForDegreeCurricularPlan(DegreeCurricularPlan degreeCurricularPlan, BigDecimal totalEcts,
	    GratuityEvent gratuityEvent) {

	final IGratuityPR gratuityPR = (IGratuityPR) degreeCurricularPlan.getServiceAgreementTemplate().findPostingRuleBy(
		EventType.GRATUITY, gratuityEvent.getStartDate(), gratuityEvent.getEndDate());

	final Money degreeGratuityAmount = gratuityPR.getDefaultGratuityAmount(gratuityEvent.getExecutionYear());

	final BigDecimal proporcionFactor = BigDecimal.ONE.add(totalEcts.divide(getEctsForYear()));

	return degreeGratuityAmount.multiply(new BigDecimal("0.5")).multiply(proporcionFactor);

    }

    private Map<DegreeCurricularPlan, BigDecimal> groupEctsByDegreeCurricularPlan(GratuityEvent gratuityEvent) {

	final Map<DegreeCurricularPlan, BigDecimal> result = new HashMap<DegreeCurricularPlan, BigDecimal>();

	for (final Enrolment enrolment : getEnrolmentsToCalculateGratuity(gratuityEvent)) {
	    addEctsToDegree(result, enrolment.getDegreeCurricularPlanOfDegreeModule(), enrolment.getEctsCreditsForCurriculum());
	}

	return result;

    }

    private Set<Enrolment> getEnrolmentsToCalculateGratuity(GratuityEvent gratuityEvent) {

	if (!gratuityEvent.getDegree().isEmpty()) {
	    if (!gratuityEvent.getStudentCurricularPlan().hasStandaloneCurriculumGroup()) {
		return Collections.emptySet();
	    }

	    return gratuityEvent.getStudentCurricularPlan().getStandaloneCurriculumGroup().getEnrolmentsBy(
		    gratuityEvent.getExecutionYear());
	} else {
	    return gratuityEvent.getStudentCurricularPlan().getRoot().getEnrolmentsBy(gratuityEvent.getExecutionYear());
	}
    }

    private void addEctsToDegree(final Map<DegreeCurricularPlan, BigDecimal> result, DegreeCurricularPlan degree, BigDecimal ects) {
	if (result.containsKey(degree)) {
	    result.put(degree, result.get(degree).add(ects));
	} else {
	    result.put(degree, ects);
	}
    }

    @Override
    protected Set<AccountingTransaction> internalProcess(User user, List<EntryDTO> entryDTOs, Event event, Account fromAccount,
	    Account toAccount, AccountingTransactionDetailDTO transactionDetail) {

	if (entryDTOs.size() != 1) {
	    throw new DomainException(
		    "error.accounting.postingRules.gratuity.StandaloneEnrolmentGratuityPR.invalid.number.of.entryDTOs");
	}

	checkIfCanAddAmount(entryDTOs.iterator().next().getAmountToPay(), event, transactionDetail.getWhenRegistered());

	return Collections.singleton(makeAccountingTransaction(user, event, fromAccount, toAccount, getEntryType(), entryDTOs
		.get(0).getAmountToPay(), transactionDetail));
    }

    private void checkIfCanAddAmount(Money amountToPay, Event event, DateTime whenRegistered) {
	final Money totalFinalAmount = event.getPayedAmount().add(amountToPay);

	if (totalFinalAmount.lessThan(calculateTotalAmountToPay(event, whenRegistered))) {
	    throw new DomainExceptionWithLabelFormatter(
		    "error.accounting.postingRules.gratuity.StandaloneEnrolmentGratuityPR.amount.being.payed.must.be.equal.to.amount.in.debt",
		    event.getDescriptionForEntryType(getEntryType()));

	}

    }

    @Override
    public String getFormulaDescription() {
	return MessageFormat.format(super.getFormulaDescription(), getEctsForYear());
    }

    @Checked("PostingRulePredicates.editPredicate")
    public StandaloneEnrolmentGratuityPR edit(final BigDecimal ectsForYear) {
	deactivate();
	return new StandaloneEnrolmentGratuityPR(new DateTime().minus(1000), null, getServiceAgreementTemplate(), ectsForYear);
    }
}
