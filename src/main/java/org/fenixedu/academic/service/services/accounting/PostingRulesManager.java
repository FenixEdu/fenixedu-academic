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
package org.fenixedu.academic.service.services.accounting;

import java.math.BigDecimal;

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.PaymentPlan;
import org.fenixedu.academic.domain.accounting.PostingRule;
import org.fenixedu.academic.domain.accounting.ServiceAgreementTemplate;
import org.fenixedu.academic.domain.accounting.postingRules.gratuity.DFAGratuityByAmountPerEctsPR;
import org.fenixedu.academic.domain.accounting.postingRules.gratuity.DFAGratuityByNumberOfEnrolmentsPR;
import org.fenixedu.academic.domain.accounting.postingRules.gratuity.EnrolmentGratuityPR;
import org.fenixedu.academic.domain.accounting.postingRules.gratuity.GratuityWithPaymentPlanPR;
import org.fenixedu.academic.domain.accounting.postingRules.gratuity.PartialRegimePR;
import org.fenixedu.academic.domain.accounting.postingRules.gratuity.PastDegreeGratuityPR;
import org.fenixedu.academic.domain.accounting.postingRules.gratuity.SpecializationDegreeGratuityByAmountPerEctsPR;
import org.fenixedu.academic.domain.accounting.postingRules.gratuity.StandaloneEnrolmentGratuityPR;
import org.fenixedu.academic.domain.accounting.serviceAgreementTemplates.DegreeCurricularPlanServiceAgreementTemplate;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.accounting.paymentPlan.InstallmentBean;
import org.fenixedu.academic.dto.accounting.paymentPlan.PaymentPlanBean;
import org.fenixedu.academic.dto.accounting.paymentPlan.StandaloneInstallmentBean;
import org.fenixedu.academic.dto.accounting.postingRule.CreateDFAGratuityPostingRuleBean;
import org.fenixedu.academic.dto.accounting.postingRule.CreateEnrolmentGratuityPRBean;
import org.fenixedu.academic.dto.accounting.postingRule.CreateGratuityPostingRuleBean;
import org.fenixedu.academic.dto.accounting.postingRule.CreatePartialRegimePRBean;
import org.fenixedu.academic.dto.accounting.postingRule.CreateSpecializationDegreeGratuityPostingRuleBean;
import org.fenixedu.academic.dto.accounting.postingRule.CreateStandaloneEnrolmentGratuityPRBean;
import org.fenixedu.academic.service.services.accounting.gratuity.paymentPlan.GratuityPaymentPlanManager;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;

public class PostingRulesManager {

    @Atomic
    static public void createGraduationGratuityPostingRule(final CreateGratuityPostingRuleBean bean) {
        if(bean.getStartDate().toLocalDate().isBefore(new LocalDate()) || bean.getStartDate().toLocalDate().isEqual(new LocalDate())) {
            throw new DomainException("createPostingRule.startDate.cannot.be.before.tomorrow");
        }

        if (bean.getRule() == GratuityWithPaymentPlanPR.class) {

            for (final DegreeCurricularPlan dcp : bean.getDegreeCurricularPlans()) {
                if (dcp.isPast()) {
                    continue;
                }
                deactivateExistingPostingRule(EventType.GRATUITY, bean.getStartDate(), dcp.getServiceAgreementTemplate());
                new GratuityWithPaymentPlanPR(bean.getStartDate(), null, dcp.getServiceAgreementTemplate());
            }

        } else if (bean.getRule() == PastDegreeGratuityPR.class) {

            for (final DegreeCurricularPlan dcp : bean.getDegreeCurricularPlans()) {
                if (!dcp.isPast()) {
                    continue;
                }
                deactivateExistingPostingRule(EventType.GRATUITY, bean.getStartDate(), dcp.getServiceAgreementTemplate());
                new PastDegreeGratuityPR(bean.getStartDate(), null, dcp.getServiceAgreementTemplate());
            }

        } else {
            throw new RuntimeException("Unexpected rule type for gratuity posting rule");
        }

    }


    @Atomic
    static public void createEnrolmentGratuityPR(final CreateEnrolmentGratuityPRBean bean) {
        if(bean.getStartDate().toLocalDate().isBefore(new LocalDate()) || bean.getStartDate().toLocalDate().isEqual(new LocalDate())) {
            throw new DomainException("createPostingRule.startDate.cannot.be.before.tomorrow");
        }

        if (bean.getRule() == EnrolmentGratuityPR.class) {
            for (final DegreeCurricularPlan degreeCurricularPlan : bean.getDegreeCurricularPlans()) {
                final ServiceAgreementTemplate serviceAgreementTemplate = degreeCurricularPlan.getServiceAgreementTemplate();
                deactivateExistingEnrolmentGratuityPR(bean.getEventType(), bean.getStartDate(), serviceAgreementTemplate, bean
                        .isForAliens());
                new EnrolmentGratuityPR(bean.getStartDate(), null, bean.getEventType(), serviceAgreementTemplate, bean
                        .getAmountPerEcts(), bean.getNumberOfDaysToStartApplyingInterest(), bean.isForAliens());
            }
        } else {
            throw new RuntimeException("Unexpected rule type for gratuity posting rule");
        }
    }

    @Atomic
    static public void createEnrolmentGratuityPR(final CreateStandaloneEnrolmentGratuityPRBean bean) {
        if(bean.getStartDate().toLocalDate().isBefore(new LocalDate()) || bean.getStartDate().toLocalDate().isEqual(new LocalDate())) {
            throw new DomainException("createPostingRule.startDate.cannot.be.before.tomorrow");
        }

        if (bean.getRule() == StandaloneEnrolmentGratuityPR.class) {
            for (final DegreeCurricularPlan degreeCurricularPlan : bean.getDegreeCurricularPlans()) {
                final ServiceAgreementTemplate serviceAgreementTemplate = degreeCurricularPlan.getServiceAgreementTemplate();
                deactivateExistingPostingRule(EventType.STANDALONE_ENROLMENT_GRATUITY, bean.getStartDate(),
                        serviceAgreementTemplate);
                new StandaloneEnrolmentGratuityPR(bean.getStartDate(), null, serviceAgreementTemplate, bean.getEctsForYear(),
                        bean.getGratuityFactor(), bean.getEctsFactor());
            }
        } else {
            throw new RuntimeException("Unexpected rule type for gratuity posting rule");
        }

    }

    private static void deactivateExistingPostingRule(final EventType eventType, final DateTime when,
            final ServiceAgreementTemplate serviceAgreementTemplate) {
        if (!serviceAgreementTemplate.hasPostingRuleFor(eventType, when)) {
            return;
        }

        final PostingRule existingPostingRule = serviceAgreementTemplate.findPostingRuleByEventTypeAndDate(eventType, when);

        if (existingPostingRule != null) {
            existingPostingRule.deactivate(when);
        }
    }

    private static void deactivateExistingEnrolmentGratuityPR(final EventType eventType, final DateTime when,
            final ServiceAgreementTemplate serviceAgreementTemplate, boolean forAliens) {

        for (final PostingRule postingRule : serviceAgreementTemplate.getPostingRulesSet()) {
            if (postingRule.getEventType() == eventType && postingRule.isActiveForDate(when)
                    && ((EnrolmentGratuityPR)postingRule).isForAliens() == forAliens) {
                postingRule.deactivate(when);
            }
        }
    }

    private static void deactivateExistingPartialRegimePR(final EventType eventType, final DateTime when,
            final ServiceAgreementTemplate serviceAgreementTemplate, boolean forAliens) {

        for (final PostingRule postingRule : serviceAgreementTemplate.getPostingRulesSet()) {
            if (postingRule.getEventType() == eventType && postingRule.isActiveForDate(when)
                    && ((PartialRegimePR)postingRule).isForAliens() == forAliens) {
                postingRule.deactivate(when);
            }
        }
    }

    @Atomic
    static public void createDFAGratuityPostingRule(final CreateDFAGratuityPostingRuleBean bean) {
        if (bean.getRule() == DFAGratuityByAmountPerEctsPR.class) {
            new DFAGratuityByAmountPerEctsPR(bean.getStartDate(), null, bean.getServiceAgreementTemplate(),
                    bean.getTotalAmount(), bean.getPartialAcceptedPercentage(), bean.getAmountPerEctsCredit());
        } else if (bean.getRule() == DFAGratuityByNumberOfEnrolmentsPR.class) {
            new DFAGratuityByNumberOfEnrolmentsPR(bean.getStartDate(), null, bean.getServiceAgreementTemplate(),
                    bean.getTotalAmount(), bean.getPartialAcceptedPercentage());
        } else {
            throw new RuntimeException("Unexpected rule type for DFA gratuity posting rule");
        }
    }

    @Atomic
    static public void createSpecializationDegreeGratuityPostingRule(final CreateSpecializationDegreeGratuityPostingRuleBean bean) {
        if (bean.getRule() == SpecializationDegreeGratuityByAmountPerEctsPR.class) {
            new SpecializationDegreeGratuityByAmountPerEctsPR(bean.getStartDate(), null, bean.getServiceAgreementTemplate(),
                    bean.getTotalAmount(), bean.getPartialAcceptedPercentage(), bean.getAmountPerEctsCredit());
        } else {
            throw new RuntimeException("Unexpected rule type for Specialization Degree gratuity posting rule");
        }
    }

    @Atomic
    static public void deleteDEAPostingRule(final PostingRule postingRule) {
        for (PaymentPlan paymentPlan : postingRule.getServiceAgreementTemplate().getPaymentPlansSet()) {
            paymentPlan.delete();
        }

        deletePostingRule(postingRule);
    }

    @Atomic
    static public void deletePostingRule(final PostingRule postingRule) {
        postingRule.delete();
    }

    @Atomic
    public static void createDEAGratuityPostingRule(PaymentPlanBean paymentPlanBean) {
        CreateGratuityPostingRuleBean createGratuityPostingRuleBean = new CreateGratuityPostingRuleBean();
        createGratuityPostingRuleBean.setExecutionYear(paymentPlanBean.getExecutionYear());
        createGratuityPostingRuleBean.setDegreeCurricularPlans(paymentPlanBean.getDegreeCurricularPlans());

        DateTime minStartDate = null;
        for (InstallmentBean installmentBean : paymentPlanBean.getInstallments()) {
            if (minStartDate == null) {
                minStartDate = installmentBean.getStartDate().toDateMidnight().toDateTime();
                continue;
            }

            if (installmentBean.getStartDate().toDateMidnight().toDateTime().isBefore(minStartDate)) {
                minStartDate = installmentBean.getStartDate().toDateMidnight().toDateTime();
            }
        }

        createGratuityPostingRuleBean.setStartDate(minStartDate);
        createGratuityPostingRuleBean.setRule(GratuityWithPaymentPlanPR.class);

        createGraduationGratuityPostingRule(createGratuityPostingRuleBean);
        GratuityPaymentPlanManager.create(paymentPlanBean);
    }

    @Atomic
    public static void createDEAStandaloneGratuityPostingRule(StandaloneInstallmentBean bean,
            DegreeCurricularPlan degreeCurricularPlan) {
        DegreeCurricularPlanServiceAgreementTemplate dcpSAT = degreeCurricularPlan.getServiceAgreementTemplate();
        if (dcpSAT != null) {
            YearMonthDay startDate = bean.getStartDate();

            LocalDate startLocalDate = new LocalDate(startDate.getYear(), startDate.getMonthOfYear(), startDate.getDayOfMonth());
            BigDecimal ectsForYear = bean.getEctsForYear();
            BigDecimal gratuityFactor = bean.getGratuityFactor();
            BigDecimal ectsFactor = bean.getEctsFactor();

            new StandaloneEnrolmentGratuityPR(startLocalDate.toDateTimeAtStartOfDay(), null, dcpSAT, ectsForYear, gratuityFactor,
                    ectsFactor);
        } else {
            throw new DomainException("StandaloneEnrolmentGratuityPR.DegreeCurricularPlanServiceAgreementTemplate.cannot.be.null");
        }
    }

    @Atomic
    public static void createPartialRegimePR(CreatePartialRegimePRBean bean) {
        if(bean.getStartDate().toLocalDate().isBefore(new LocalDate()) || bean.getStartDate().toLocalDate().isEqual(new LocalDate())) {
            throw new DomainException("createPostingRule.startDate.cannot.be.before.tomorrow");
        }

        if (bean.getRule() == PartialRegimePR.class) {
            for (final DegreeCurricularPlan degreeCurricularPlan : bean.getDegreeCurricularPlans()) {
                final ServiceAgreementTemplate serviceAgreementTemplate = degreeCurricularPlan.getServiceAgreementTemplate();
                deactivateExistingPartialRegimePR(EventType.PARTIAL_REGIME_GRATUITY, bean.getStartDate(),
                        serviceAgreementTemplate, bean.isForAliens());
                new PartialRegimePR(bean.getStartDate(), null, serviceAgreementTemplate, bean
                        .getAmount(), bean.getNumberOfDaysToStartApplyingInterest(), bean.isForAliens());
            }
        } else {
            throw new RuntimeException("Unexpected rule type for gratuity posting rule");
        }
    }
}
