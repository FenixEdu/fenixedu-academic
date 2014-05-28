/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.applicationTier.Servico.accounting.gratuity.paymentPlan.GratuityPaymentPlanManager;
import net.sourceforge.fenixedu.dataTransferObject.accounting.paymentPlan.InstallmentBean;
import net.sourceforge.fenixedu.dataTransferObject.accounting.paymentPlan.PaymentPlanBean;
import net.sourceforge.fenixedu.dataTransferObject.accounting.paymentPlan.StandaloneInstallmentBean;
import net.sourceforge.fenixedu.dataTransferObject.accounting.postingRule.CreateDFAGratuityPostingRuleBean;
import net.sourceforge.fenixedu.dataTransferObject.accounting.postingRule.CreateGratuityPostingRuleBean;
import net.sourceforge.fenixedu.dataTransferObject.accounting.postingRule.CreateSpecializationDegreeGratuityPostingRuleBean;
import net.sourceforge.fenixedu.dataTransferObject.accounting.postingRule.CreateStandaloneEnrolmentGratuityPRBean;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.PaymentPlan;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.postingRules.gratuity.DFAGratuityByAmountPerEctsPR;
import net.sourceforge.fenixedu.domain.accounting.postingRules.gratuity.DFAGratuityByNumberOfEnrolmentsPR;
import net.sourceforge.fenixedu.domain.accounting.postingRules.gratuity.GratuityWithPaymentPlanPR;
import net.sourceforge.fenixedu.domain.accounting.postingRules.gratuity.PastDegreeGratuityPR;
import net.sourceforge.fenixedu.domain.accounting.postingRules.gratuity.SpecializationDegreeGratuityByAmountPerEctsPR;
import net.sourceforge.fenixedu.domain.accounting.postingRules.gratuity.StandaloneEnrolmentGratuityPR;
import net.sourceforge.fenixedu.domain.accounting.serviceAgreementTemplates.DegreeCurricularPlanServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.predicates.AcademicPredicates;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;

public class PostingRulesManager {

    @Atomic
    static public void createGraduationGratuityPostingRule(final CreateGratuityPostingRuleBean bean) {
        check(AcademicPredicates.MANAGE_PAYMENTS);

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
    static public void createStandaloneGraduationGratuityPostingRule(final CreateStandaloneEnrolmentGratuityPRBean bean) {
        check(AcademicPredicates.MANAGE_PAYMENTS);

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

    @Atomic
    static public void createDFAGratuityPostingRule(final CreateDFAGratuityPostingRuleBean bean) {
        check(AcademicPredicates.MANAGE_PAYMENTS);
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
        check(AcademicPredicates.MANAGE_PAYMENTS);
        if (bean.getRule() == SpecializationDegreeGratuityByAmountPerEctsPR.class) {
            new SpecializationDegreeGratuityByAmountPerEctsPR(bean.getStartDate(), null, bean.getServiceAgreementTemplate(),
                    bean.getTotalAmount(), bean.getPartialAcceptedPercentage(), bean.getAmountPerEctsCredit());
        } else {
            throw new RuntimeException("Unexpected rule type for Specialization Degree gratuity posting rule");
        }
    }

    @Atomic
    static public void deleteDEAPostingRule(final PostingRule postingRule) {
        check(AcademicPredicates.MANAGE_PAYMENTS);

        for (PaymentPlan paymentPlan : postingRule.getServiceAgreementTemplate().getPaymentPlansSet()) {
            paymentPlan.delete();
        }

        deletePostingRule(postingRule);
    }

    @Atomic
    static public void deletePostingRule(final PostingRule postingRule) {
        check(AcademicPredicates.MANAGE_PAYMENTS);
        postingRule.delete();
    }

    @Atomic
    public static void createDEAGratuityPostingRule(PaymentPlanBean paymentPlanBean) {
        check(AcademicPredicates.MANAGE_PAYMENTS);
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
        check(AcademicPredicates.MANAGE_PAYMENTS);

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
}
