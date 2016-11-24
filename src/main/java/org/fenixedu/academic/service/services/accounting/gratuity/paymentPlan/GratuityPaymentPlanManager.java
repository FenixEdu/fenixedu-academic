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
package org.fenixedu.academic.service.services.accounting.gratuity.paymentPlan;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.accounting.Installment;
import org.fenixedu.academic.domain.accounting.PaymentPlan;
import org.fenixedu.academic.domain.accounting.installments.InstallmentForFirstTimeStudents;
import org.fenixedu.academic.domain.accounting.installments.InstallmentWithMonthlyPenalty;
import org.fenixedu.academic.domain.accounting.installments.PartialRegimeInstallment;
import org.fenixedu.academic.domain.accounting.paymentPlans.FullGratuityPaymentPlan;
import org.fenixedu.academic.domain.accounting.paymentPlans.FullGratuityPaymentPlanForAliens;
import org.fenixedu.academic.domain.accounting.paymentPlans.FullGratuityPaymentPlanForFirstTimeInstitutionStudents;
import org.fenixedu.academic.domain.accounting.paymentPlans.FullGratuityPaymentPlanForPartialRegime;
import org.fenixedu.academic.domain.accounting.paymentPlans.GratuityForStudentsInSecondCurricularYear;
import org.fenixedu.academic.domain.accounting.paymentPlans.GratuityForStudentsInSecondCurricularYearForPartialRegime;
import org.fenixedu.academic.domain.accounting.paymentPlans.GratuityPaymentPlanForPartialRegimeEnroledOnlyInSecondSemester;
import org.fenixedu.academic.domain.accounting.paymentPlans.GratuityPaymentPlanForStudentsEnroledOnlyInSecondSemester;
import org.fenixedu.academic.dto.accounting.paymentPlan.InstallmentBean;
import org.fenixedu.academic.dto.accounting.paymentPlan.PaymentPlanBean;
import pt.ist.fenixframework.Atomic;

public class GratuityPaymentPlanManager {

    @Atomic
    public static void create(final PaymentPlanBean paymentPlanBean) {
        for (final DegreeCurricularPlan degreeCurricularPlan : paymentPlanBean.getDegreeCurricularPlans()) {
            createInstallments(makePaymentPlan(paymentPlanBean, degreeCurricularPlan), paymentPlanBean.getInstallments());
        }
    }

    private static PaymentPlan makePaymentPlan(final PaymentPlanBean paymentPlanBean,
            final DegreeCurricularPlan degreeCurricularPlan) {

        if (paymentPlanBean.isForAlien()) {
            return new FullGratuityPaymentPlanForAliens(paymentPlanBean.getExecutionYear(),
                    degreeCurricularPlan.getServiceAgreementTemplate(), paymentPlanBean.isMain());
        }

        if (paymentPlanBean.isForPartialRegime()) {

            if (paymentPlanBean.isForSecondCurricularYear()) {

                return new GratuityForStudentsInSecondCurricularYearForPartialRegime(paymentPlanBean.getExecutionYear(),
                        degreeCurricularPlan.getServiceAgreementTemplate(), paymentPlanBean.isMain());

            } else if (paymentPlanBean.isForStudentEnroledOnSecondSemesterOnly()) {

                return new GratuityPaymentPlanForPartialRegimeEnroledOnlyInSecondSemester(paymentPlanBean.getExecutionYear(),
                        degreeCurricularPlan.getServiceAgreementTemplate(), paymentPlanBean.isMain());
            } else {

                return new FullGratuityPaymentPlanForPartialRegime(paymentPlanBean.getExecutionYear(),
                        degreeCurricularPlan.getServiceAgreementTemplate(), paymentPlanBean.isMain());
            }
        } else {
            if (paymentPlanBean.isForStudentEnroledOnSecondSemesterOnly()) {

                return new GratuityPaymentPlanForStudentsEnroledOnlyInSecondSemester(paymentPlanBean.getExecutionYear(),
                        degreeCurricularPlan.getServiceAgreementTemplate(), paymentPlanBean.isMain());

            } else if (paymentPlanBean.isForFirstTimeInstitutionStudents()) {

                return new FullGratuityPaymentPlanForFirstTimeInstitutionStudents(paymentPlanBean.getExecutionYear(),
                        degreeCurricularPlan.getServiceAgreementTemplate(), paymentPlanBean.isMain());
            } else if (paymentPlanBean.isForSecondCurricularYear()) {

                return new GratuityForStudentsInSecondCurricularYear(paymentPlanBean.getExecutionYear(),
                        degreeCurricularPlan.getServiceAgreementTemplate(), paymentPlanBean.isMain());
            } else {
                return new FullGratuityPaymentPlan(paymentPlanBean.getExecutionYear(),
                        degreeCurricularPlan.getServiceAgreementTemplate(), paymentPlanBean.isMain());
            }
        }
    }

    private static void createInstallments(final PaymentPlan paymentPlan, final List<InstallmentBean> installmentsToCreate) {

        for (final InstallmentBean each : installmentsToCreate) {

            if (paymentPlan.isForPartialRegime()) {

                if (each.isPenaltyAppliable()) {
                    new PartialRegimeInstallment((FullGratuityPaymentPlanForPartialRegime) paymentPlan, each.getAmount(),
                            each.getStartDate(), each.getEndDate(), each.getMontlyPenaltyPercentage(),
                            each.getWhenToStartApplyPenalty(), each.getMaxMonthsToApplyPenalty(), each.getEctsForAmount(),
                            new ArrayList<>(each.getExecutionSemesters()));

                } else {
                    new PartialRegimeInstallment((FullGratuityPaymentPlanForPartialRegime) paymentPlan, each.getAmount(),
                            each.getStartDate(), each.getEndDate(), each.getEctsForAmount(), new ArrayList<>(
                                    each.getExecutionSemesters()));
                }

            } else if (paymentPlan.isForFirstTimeInstitutionStudents() && each.isForFirstTimeInstitutionStudents()) {

                new InstallmentForFirstTimeStudents(paymentPlan, each.getAmount(), each.getStartDate(), each.getEndDate(),
                        each.getMontlyPenaltyPercentage(), each.getMaxMonthsToApplyPenalty(),
                        each.getNumberOfDaysToStartApplyingPenalty());

            } else {

                if (each.isPenaltyAppliable()) {
                    new InstallmentWithMonthlyPenalty(paymentPlan, each.getAmount(), each.getStartDate(), each.getEndDate(),
                            each.getMontlyPenaltyPercentage(), each.getWhenToStartApplyPenalty(),
                            each.getMaxMonthsToApplyPenalty());

                } else {
                    new Installment(paymentPlan, each.getAmount(), each.getStartDate(), each.getEndDate());
                }
            }

        }
    }

    @Atomic
    public static void delete(final PaymentPlan paymentPlan) {
        paymentPlan.delete();
    }

}
