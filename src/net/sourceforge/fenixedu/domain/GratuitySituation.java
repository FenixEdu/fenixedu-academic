/*
 * Created on 5/Jan/2004
 *
 */
package net.sourceforge.fenixedu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.accounting.PaymentCodeType;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.GratuitySituationPaymentCode;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.gratuity.ReimbursementGuideState;
import net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuideEntry;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.domain.transactions.GratuityTransaction;
import net.sourceforge.fenixedu.domain.transactions.PaymentType;
import net.sourceforge.fenixedu.domain.transactions.TransactionType;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.YearMonthDay;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * @author Tânia Pousão
 */
public class GratuitySituation extends GratuitySituation_Base {

    // TODO: this should be configurable
    private static final BigDecimal PENALTY_PERCENTAGE = new BigDecimal("0.01");

    public GratuitySituation() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public GratuitySituation(GratuityValues gratuityValues, StudentCurricularPlan studentCurricularPlan) {

	this();
	setGratuityValues(gratuityValues);
	setStudentCurricularPlan(studentCurricularPlan);
	setWhenDateTime(new DateTime());

	updateValues();
    }

    public void delete() {
	removeRootDomainObject();
	deleteDomainObject();
    }

    public void updateValues() {

	updateTotalValue();
	updateRemainingValue();
    }

    private void updateTotalValue() {
	super.setTotalValue(calculateTotalAmountWithPenalty());
    }

    private double calculateTotalAmountWithPenalty() {
	if (hasPenalty()) {
	    return calculateOriginalTotalValue() + calculatePenalty(calculateOriginalTotalValue());
	} else {
	    return calculateOriginalTotalValue();
	}
    }

    private double calculateOriginalTotalValue() {
	double totalValue = 0.0;

	Specialization specialization = getStudentCurricularPlan().getSpecialization();
	if (specialization.equals(Specialization.STUDENT_CURRICULAR_PLAN_SPECIALIZATION)) {
	    totalValue = this.getGratuityValues().calculateTotalValueForSpecialization(
		    this.getStudentCurricularPlan());
	} else if (specialization.equals(Specialization.STUDENT_CURRICULAR_PLAN_MASTER_DEGREE)) {
	    totalValue = this.getGratuityValues().calculateTotalValueForMasterDegree();
	}

	return totalValue;
    }

    private void updateRemainingValue() {
	super.setRemainingValue(getTotalValue() - calculatePayedValue() - calculateExemptionValue());
    }

    private double calculateExemptionValue() {
	double exemptionValue = 0;
	if (this.getExemptionPercentage() != null) {
	    exemptionValue = getTotalValue() * (this.getExemptionPercentage().doubleValue() / 100.0);
	}

	if (this.getExemptionValue() != null) {
	    exemptionValue += this.getExemptionValue().doubleValue();
	}
	return exemptionValue;
    }

    public double calculatePayedValue() {
	return calculatePayedValue(null);
    }

    public double calculatePayedValue(final YearMonthDay date) {
	double payedValue = 0;
	double reimbursedValue = 0;

	final List<GratuityTransaction> transactions = (date == null ? getTransactionList()
		: getTransactionsUntil(date));
	for (GratuityTransaction gratuityTransaction : transactions) {

	    payedValue += gratuityTransaction.getValue();

	    if (gratuityTransaction.getGuideEntry() != null) {

		List<ReimbursementGuideEntry> reimbursementGuideEntries = gratuityTransaction
			.getGuideEntry().getReimbursementGuideEntries();
		for (ReimbursementGuideEntry reimbursementGuideEntry : reimbursementGuideEntries) {
		    if (reimbursementGuideEntry.getReimbursementGuide()
			    .getActiveReimbursementGuideSituation().getReimbursementGuideState().equals(
				    ReimbursementGuideState.PAYED)) {

			reimbursedValue += reimbursementGuideEntry.getValue();
		    }
		}
	    }
	}

	return payedValue - reimbursedValue;
    }

    private List<GratuityTransaction> getTransactionsUntil(final YearMonthDay date) {
	final List<GratuityTransaction> result = new ArrayList<GratuityTransaction>();

	for (final GratuityTransaction gratuityTransaction : getTransactionList()) {
	    if (gratuityTransaction.getTransactionDateDateTime().toYearMonthDay().compareTo(date) <= 0) {
		result.add(gratuityTransaction);
	    }
	}

	return result;
    }

    public GratuitySituationPaymentCode calculatePaymentCode() {
	updateValues();

	if (!hasPaymentCode()) {
	    createPaymentCode();
	} else {
	    updatePaymentCode();
	}

	return super.getPaymentCode();
    }

    private void updatePaymentCode() {
	super.getPaymentCode().update(new YearMonthDay(), calculatePaymentCodeEndDate(),
		new Money(getRemainingValue()), new Money(getRemainingValue()));
    }

    private void createPaymentCode() {
	GratuitySituationPaymentCode.create(PaymentCodeType.PRE_BOLONHA_MASTER_DEGREE_TOTAL_GRATUITY,
		new YearMonthDay(), calculatePaymentCodeEndDate(), new Money(getRemainingValue()),
		new Money(getRemainingValue()), getStudent(), this);
    }

    private Student getStudent() {
	return getStudentCurricularPlan().getRegistration().getStudent();
    }

    @Override
    public GratuitySituationPaymentCode getPaymentCode() {
	throw new DomainException("error.GratuitySituation.paymentCode.cannot.be.accessed.directly");
    }

    @Override
    public boolean hasPaymentCode() {
	return (super.getPaymentCode() != null);
    }

    private double calculatePenalty(final double remainingValue) {
	if (getGratuityValues().isPenaltyApplicable()) {
	    final YearMonthDay endPaymentDate = getEndPaymentDate();
	    final int extraMonths = endPaymentDate.plusDays(1).getMonthOfYear() == endPaymentDate
		    .getMonthOfYear() ? 1 : 0;
	    final Period period = new Period(endPaymentDate.plusDays(1), new YearMonthDay());
	    final int monthsToChargePenalty = period.getMonths() + extraMonths + 1;
	    return new Money(remainingValue).multiply(PENALTY_PERCENTAGE).multiply(
		    new BigDecimal(monthsToChargePenalty)).getAmount().doubleValue();

	}

	return 0;

    }

    private boolean hasPenalty() {
	if (new YearMonthDay().isAfter(getEndPaymentDate())) {
	    final double payedValue = calculatePayedValue(getEndPaymentDate());
	    return payedValue < (calculateOriginalTotalValue() - calculateExemptionValue());
	}

	return false;

    }

    private YearMonthDay calculatePaymentCodeEndDate() {
	final YearMonthDay now = new YearMonthDay();
	final YearMonthDay endPaymentDate = getEndPaymentDate();
	return now.isAfter(endPaymentDate) ? now.plusMonths(1) : endPaymentDate;
    }

    private boolean isPayedUsingPhases() {
	return getGratuityValues().hasAnyPaymentPhaseList();
    }

    private YearMonthDay getEndPaymentDate() {
	if (isPayedUsingPhases()) {
	    final YearMonthDay lastPaymentPhaseDate = getLastPaymentPhaseDate();
	    if (getGratuityValues().getEndPaymentYearMonthDay() == null) {
		return lastPaymentPhaseDate;
	    }

	    return lastPaymentPhaseDate.isAfter(getGratuityValues().getEndPaymentYearMonthDay()) ? lastPaymentPhaseDate
		    : getGratuityValues().getEndPaymentYearMonthDay();
	}

	if (getGratuityValues().getEndPaymentYearMonthDay() == null) {
	    throw new DomainException(
		    "error.net.sourceforge.fenixedu.domain.GratuitySituation.paymentMode.is.not.correctly.defined");
	}

	return getGratuityValues().getEndPaymentYearMonthDay();
    }

    private YearMonthDay getLastPaymentPhaseDate() {
	final PaymentPhase paymentPhase = getGratuityValues().getLastPaymentPhase();
	return paymentPhase == null ? null : paymentPhase.getEndDateYearMonthDay();
    }

    public boolean isPayed() {
	return getRemainingValue().compareTo(Double.valueOf(0)) == 0;
    }

    public GratuityTransaction processAmount(final Person responsiblePerson, final Money amount,
	    final DateTime whenRegistered, final PaymentType paymentType) {

	final GratuityTransaction transaction = new GratuityTransaction(amount.getAmount(),
		whenRegistered, paymentType, TransactionType.GRATUITY_ADHOC_PAYMENT, responsiblePerson,
		getPersonAccount(), this);

	updateRemainingValue();

	return transaction;

    }

    private PersonAccount getPersonAccount() {
	return getStudent().getPerson().getAssociatedPersonAccount();
    }

    @Override
    public void setTotalValue(Double value) {
	throw new DomainException(
		"error.net.sourceforge.fenixedu.domain.GratuitySituation.cannot.modify.value");
    }

    @Override
    public void setRemainingValue(Double value) {
	throw new DomainException(
		"error.net.sourceforge.fenixedu.domain.GratuitySituation.cannot.modify.value");
    }

}
