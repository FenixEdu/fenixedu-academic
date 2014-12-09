/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.accounting.postingRules.gratuity;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.accounting.Account;
import org.fenixedu.academic.domain.accounting.AccountingTransaction;
import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.ServiceAgreementTemplate;
import org.fenixedu.academic.domain.accounting.events.gratuity.GratuityEvent;
import org.fenixedu.academic.domain.accounting.events.gratuity.GratuityExemption;
import org.fenixedu.academic.domain.accounting.events.gratuity.PercentageGratuityExemption;
import org.fenixedu.academic.domain.accounting.events.gratuity.ValueGratuityExemption;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.exceptions.DomainExceptionWithLabelFormatter;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.dto.accounting.AccountingTransactionDetailDTO;
import org.fenixedu.academic.dto.accounting.EntryDTO;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;

public class StandaloneEnrolmentGratuityPR extends StandaloneEnrolmentGratuityPR_Base {

    protected StandaloneEnrolmentGratuityPR() {
        super();
    }

    public StandaloneEnrolmentGratuityPR(DateTime startDate, DateTime endDate, ServiceAgreementTemplate serviceAgreementTemplate,
            BigDecimal ectsForYear, BigDecimal gratuityFactor, BigDecimal ectsFactor) {
        this();
        init(startDate, endDate, serviceAgreementTemplate, ectsForYear, gratuityFactor, ectsFactor);
    }

    private void init(DateTime startDate, DateTime endDate, ServiceAgreementTemplate serviceAgreementTemplate,
            BigDecimal ectsForYear, BigDecimal gratuityFactor, BigDecimal ectsFactor) {

        checkParameters(ectsForYear, gratuityFactor, ectsFactor);
        checkGratuityPR(serviceAgreementTemplate);

        super.init(EntryType.STANDALONE_ENROLMENT_GRATUITY_FEE, EventType.STANDALONE_ENROLMENT_GRATUITY, startDate, endDate,
                serviceAgreementTemplate);

        super.setEctsForYear(ectsForYear);
        super.setGratuityFactor(gratuityFactor);
        super.setEctsFactor(ectsFactor);

    }

    private void checkParameters(BigDecimal ectsForYear, BigDecimal gratuityFactor, BigDecimal ectsFactor) {

        String[] args = {};
        if (ectsForYear == null) {
            throw new DomainException(
                    "error.accounting.postingRules.gratuity.StandaloneEnrolmentGratuityPR.ectsForYear.cannot.be.null", args);
        }
        String[] args1 = {};

        if (gratuityFactor == null) {
            throw new DomainException(
                    "error.accounting.postingRules.gratuity.StandaloneEnrolmentGratuityPR.gratuity.cannot.be.null", args1);
        }
        String[] args2 = {};

        if (ectsFactor == null) {
            throw new DomainException(
                    "error.accounting.postingRules.gratuity.StandaloneEnrolmentGratuityPR.ectsFactor.cannot.be.null", args2);
        }

    }

    /**
     * Check gratuity pr.
     * Check if a posting rule with gratuity event type already exists.
     * Such posting rule must exists first because standalone PR is calculated
     * based on grauity PR
     * 
     * @param serviceAgreementTemplate the service agreement template
     */
    private void checkGratuityPR(ServiceAgreementTemplate serviceAgreementTemplate) {
        if (!serviceAgreementTemplate.hasActivePostingRuleFor(EventType.GRATUITY)) {
            throw new DomainException("error.accounting.postingRules.gratuity.StandaloneEnrolmentGratuityPR.must.have.gratuityPR");
        }
    }

    @Override
    public List<EntryDTO> calculateEntries(Event event, DateTime when) {
        return Collections.singletonList(new EntryDTO(getEntryType(), event, calculateTotalAmountToPay(event, when), event
                .getPayedAmount(), event.calculateAmountToPay(when), event.getDescriptionForEntryType(getEntryType()), event
                .calculateAmountToPay(when)));
    }

    @Override
    protected Money doCalculationForAmountToPay(Event event, DateTime when, boolean applyDiscount) {
        final GratuityEvent gratuityEvent = (GratuityEvent) event;

        Money result = Money.ZERO;
        for (final Map.Entry<DegreeCurricularPlan, BigDecimal> entry : groupEctsByDegreeCurricularPlan(gratuityEvent).entrySet()) {
            result = result.add(calculateAmountForDegreeCurricularPlan(entry.getKey(), entry.getValue(), gratuityEvent));
        }

        return result;
    }

    @Override
    protected Money subtractFromExemptions(Event event, DateTime when, boolean applyDiscount, Money amountToPay) {
        final GratuityEvent gratuityEvent = (GratuityEvent) event;

        if (!gratuityEvent.hasGratuityExemption()) {
            return amountToPay;
        }

        GratuityExemption gratuityExemption = gratuityEvent.getGratuityExemption();

        if (gratuityExemption.isValueExemption()) {
            amountToPay = amountToPay.subtract(((ValueGratuityExemption) gratuityExemption).getValue());
        } else {
            PercentageGratuityExemption percentageGratuityExemption = (PercentageGratuityExemption) gratuityExemption;
            BigDecimal percentage = percentageGratuityExemption.getPercentage();
            Money toRemove = amountToPay.multiply(percentage);
            amountToPay = amountToPay.subtract(toRemove);
        }

        return amountToPay.isNegative() ? Money.ZERO : amountToPay;
    }

    /**
     * <pre>
     * Formula for students in empty degrees: GratuityFactor x TotalGratuity x (EctsFactor + EnroledEcts / TotalEctsForYear)
     * Formula for students enroled in normal degrees: TotalGratuity x (EnroledEcts / TotalEctsForYear)
     * </pre>
     * 
     * @param degreeCurricularPlan
     * @param enroledEcts
     * @param gratuityEvent
     * @return
     */
    private Money calculateAmountForDegreeCurricularPlan(DegreeCurricularPlan degreeCurricularPlan, BigDecimal enroledEcts,
            GratuityEvent gratuityEvent) {

        final IGratuityPR gratuityPR =
                (IGratuityPR) degreeCurricularPlan.getServiceAgreementTemplate().findPostingRuleBy(EventType.GRATUITY,
                        gratuityEvent.getStartDate(), gratuityEvent.getEndDate());

        final Money degreeGratuityAmount = gratuityPR.getDefaultGratuityAmount(gratuityEvent.getExecutionYear());
        final BigDecimal creditsProporcion = enroledEcts.divide(getEctsForYear());

        if (hasAnyActiveDegreeRegistration(gratuityEvent)) {
            return degreeGratuityAmount.multiply(creditsProporcion);

        } else if (gratuityEvent.getDegree().isEmpty() || gratuityEvent.getDegree().isDEA()) {
            return degreeGratuityAmount.multiply(getGratuityFactor()).multiply(getEctsFactor().add(creditsProporcion));

        } else {
            return degreeGratuityAmount.multiply(creditsProporcion);
        }
    }

    private boolean hasAnyActiveDegreeRegistration(final GratuityEvent gratuityEvent) {

        final Student student = gratuityEvent.getStudentCurricularPlan().getRegistration().getStudent();

        for (final Registration registration : student.getRegistrationsSet()) {

            if (registration.getDegree().isEmpty()) {
                continue;
            }

            if (registration.isDegreeAdministrativeOffice() && registration.hasAnyEnrolmentsIn(gratuityEvent.getExecutionYear())) {
                return true;
            }
        }

        return false;
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

            return gratuityEvent.getStudentCurricularPlan().getStandaloneCurriculumGroup()
                    .getEnrolmentsBy(gratuityEvent.getExecutionYear());
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
    protected Set<AccountingTransaction> internalProcess(User user, Collection<EntryDTO> entryDTOs, Event event,
            Account fromAccount, Account toAccount, AccountingTransactionDetailDTO transactionDetail) {

        if (entryDTOs.size() != 1) {
            throw new DomainException(
                    "error.accounting.postingRules.gratuity.StandaloneEnrolmentGratuityPR.invalid.number.of.entryDTOs");
        }

        checkIfCanAddAmount(entryDTOs.iterator().next().getAmountToPay(), event, transactionDetail.getWhenRegistered());

        return Collections.singleton(makeAccountingTransaction(user, event, fromAccount, toAccount, getEntryType(), entryDTOs
                .iterator().next().getAmountToPay(), transactionDetail));
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
        return MessageFormat.format(super.getFormulaDescription(), getGratuityFactor().toPlainString(), getEctsFactor()
                .toPlainString(), getEctsForYear().toPlainString());
    }

    public StandaloneEnrolmentGratuityPR edit(final BigDecimal ectsForYear, final BigDecimal gratuityFactor,
            final BigDecimal ectsFactor) {
        deactivate();
        return new StandaloneEnrolmentGratuityPR(new DateTime().minus(1000), null, getServiceAgreementTemplate(), ectsForYear,
                gratuityFactor, ectsFactor);
    }

    @Override
    public void setEctsForYear(BigDecimal ectsForYear) {
        throw new DomainException(
                "error.org.fenixedu.academic.domain.accounting.postingRules.gratuity.StandaloneEnrolmentGratuityPR.cannot.modify.ectsForYear");
    }

    @Override
    public void setGratuityFactor(BigDecimal gratuityFactor) {
        throw new DomainException(
                "error.org.fenixedu.academic.domain.accounting.postingRules.gratuity.StandaloneEnrolmentGratuityPR.cannot.modify.gratuityFactor");
    }

    @Override
    public void setEctsFactor(BigDecimal ectsFactor) {
        throw new DomainException(
                "error.org.fenixedu.academic.domain.accounting.postingRules.gratuity.StandaloneEnrolmentGratuityPR.cannot.modify.ectsFactor");
    }

}
