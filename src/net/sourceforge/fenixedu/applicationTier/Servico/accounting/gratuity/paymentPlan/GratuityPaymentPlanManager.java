package net.sourceforge.fenixedu.applicationTier.Servico.accounting.gratuity.paymentPlan;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.accounting.paymentPlan.InstallmentBean;
import net.sourceforge.fenixedu.dataTransferObject.accounting.paymentPlan.PaymentPlanBean;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.Installment;
import net.sourceforge.fenixedu.domain.accounting.PaymentPlan;
import net.sourceforge.fenixedu.domain.accounting.installments.InstallmentForFirstTimeStudents;
import net.sourceforge.fenixedu.domain.accounting.installments.InstallmentWithMonthlyPenalty;
import net.sourceforge.fenixedu.domain.accounting.installments.PartialRegimeInstallment;
import net.sourceforge.fenixedu.domain.accounting.paymentPlans.FullGratuityPaymentPlan;
import net.sourceforge.fenixedu.domain.accounting.paymentPlans.FullGratuityPaymentPlanForFirstTimeInstitutionStudents;
import net.sourceforge.fenixedu.domain.accounting.paymentPlans.FullGratuityPaymentPlanForPartialRegime;
import net.sourceforge.fenixedu.domain.accounting.paymentPlans.GratuityForStudentsInSecondCurricularYear;
import net.sourceforge.fenixedu.domain.accounting.paymentPlans.GratuityForStudentsInSecondCurricularYearForPartialRegime;
import net.sourceforge.fenixedu.domain.accounting.paymentPlans.GratuityPaymentPlanForPartialRegimeEnroledOnlyInSecondSemester;
import net.sourceforge.fenixedu.domain.accounting.paymentPlans.GratuityPaymentPlanForStudentsEnroledOnlyInSecondSemester;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class GratuityPaymentPlanManager {

	@Checked("RolePredicates.MANAGER_PREDICATE")
	@Service
	public static void create(final PaymentPlanBean paymentPlanBean) {
		for (final DegreeCurricularPlan degreeCurricularPlan : paymentPlanBean.getDegreeCurricularPlans()) {
			createInstallments(makePaymentPlan(paymentPlanBean, degreeCurricularPlan), paymentPlanBean.getInstallments());
		}
	}

	private static PaymentPlan makePaymentPlan(final PaymentPlanBean paymentPlanBean,
			final DegreeCurricularPlan degreeCurricularPlan) {

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
							each.getExecutionSemesters());

				} else {
					new PartialRegimeInstallment((FullGratuityPaymentPlanForPartialRegime) paymentPlan, each.getAmount(),
							each.getStartDate(), each.getEndDate(), each.getEctsForAmount(), each.getExecutionSemesters());
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

	@Checked("RolePredicates.MANAGER_PREDICATE")
	@Service
	public static void delete(final PaymentPlan paymentPlan) {
		paymentPlan.delete();
	}

}
