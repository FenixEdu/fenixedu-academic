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
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.ServiceAgreementTemplate;
import org.fenixedu.academic.domain.accounting.events.gratuity.GratuityEvent;
import org.fenixedu.academic.domain.accounting.events.gratuity.StandaloneEnrolmentGratuityEvent;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.signals.DomainObjectEvent;
import org.fenixedu.bennu.core.signals.Signal;
import org.joda.time.DateTime;

import pt.ist.fenixframework.FenixFramework;

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
     * Check if a posting rule with gratuity dueDate type already exists.
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

    static {
        Signal.register(Enrolment.SIGNAL_CREATED, (DomainObjectEvent<Enrolment> wrapper) -> {
            Enrolment enrolment = wrapper.getInstance();
            if (enrolment.getStudentCurricularPlan().getGratuityEventsSet()
                    .stream()
                    .anyMatch(e -> e instanceof StandaloneEnrolmentGratuityEvent && e.getExecutionYear().equals(enrolment.getExecutionYear()))) {
                throw new DomainException("Can't enroll since exists standalone gratuity event");
            }
        });

        FenixFramework.getDomainModel().registerDeletionBlockerListener(Enrolment.class, ((enrolment, collection) -> {
            // the first scp check must be done since this is invoked for the curriculum module deletion blockers
            if (enrolment.getStudentCurricularPlan() != null && enrolment.getStudentCurricularPlan().getGratuityEventsSet()
                    .stream()
                    .anyMatch(e -> e instanceof StandaloneEnrolmentGratuityEvent && e.getExecutionYear().equals(enrolment.getExecutionYear()))) {
                collection.add("Can't delete enrolment since exists standalone gratuity event");
            }
        }));
    }


    @Override
    protected Money doCalculationForAmountToPay(Event event) {
        final GratuityEvent gratuityEvent = (GratuityEvent) event;

        Money result = Money.ZERO;
        for (final Map.Entry<DegreeCurricularPlan, BigDecimal> entry : groupEctsByDegreeCurricularPlan(gratuityEvent).entrySet()) {
            result = result.add(calculateAmountForDegreeCurricularPlan(entry.getKey(), entry.getValue(), gratuityEvent));
        }

        return result;
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

        if (gratuityPR == null) {
            throw new DomainException("error.accounting.agreement.ServiceAgreementTemplate.cannot.find.postingRule.for.eventType.and.date.desc",
                    gratuityEvent.getWhenOccured().toString("dd/MM/yyyy"), BundleUtil.getString(Bundle.ENUMERATION,gratuityEvent.getEventType().getQualifiedName()));
        }

        final Money degreeGratuityAmount = gratuityPR.getDefaultGratuityAmount(gratuityEvent.getExecutionYear());
        final BigDecimal creditsProporcion = enroledEcts.divide(getEctsForYear(), 10, RoundingMode.HALF_EVEN);

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
