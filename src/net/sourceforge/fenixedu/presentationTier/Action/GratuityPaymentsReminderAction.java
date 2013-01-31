package net.sourceforge.fenixedu.presentationTier.Action;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.PaymentCode;
import net.sourceforge.fenixedu.domain.accounting.PaymentPlan;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEvent;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.AccountingEventPaymentCode;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.InstallmentPaymentCode;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.Money;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/gratuityPaymentsReminder")
@Forwards({

@Forward(name = "showGratuityPaymentsReminder", path = "/showGratuityPaymentsReminder.jsp", useTile = false)

})
public class GratuityPaymentsReminderAction extends FenixDispatchAction {

	public ActionForward showReminder(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		Person person = AccessControl.getPerson();

		InstallmentPaymentCode debtPaymentCode = readDebtPaymentCodeInThisExecutionYear(person);
		if (debtPaymentCode != null) {
			PaymentPlan paymentPlan = debtPaymentCode.getInstallment().getPaymentPlan();

			if (paymentPlan.isForPartialRegime() && debtPaymentCode.getInstallment().getOrder() == 1) {
				request.setAttribute("remnantGratuity", false);
				request.setAttribute("remainingPaymentEndDate", "15 de Dezembro de 2011");
				request.setAttribute("remainingPaymentDebt", debtPaymentCode.getMinAmount().toString());
				request.setAttribute("remainingPaymentCode", debtPaymentCode.getCode());
			} else if (debtPaymentCode.getInstallment().getOrder() == 3) {
				request.setAttribute("remnantGratuity", false);
				request.setAttribute("remainingPaymentEndDate", "31 de Maio de 2012");
				request.setAttribute("remainingPaymentDebt", debtPaymentCode.getMinAmount().toString());
				request.setAttribute("remainingPaymentCode", debtPaymentCode.getCode());
			}
		} else {
			request.setAttribute("remnantGratuity", false);
		}

		return mapping.findForward("showGratuityPaymentsReminder");
	}

	final Money THRESHOLD = new Money("13.00");

	private InstallmentPaymentCode readDebtPaymentCodeInThisExecutionYear(Person person) {
		ExecutionYear currentExecutionYear = ExecutionYear.readExecutionYearByName("2011/2012");

		Set<GratuityEvent> gratuityEvents = person.getGratuityEvents();

		for (GratuityEvent gratuityEvent : gratuityEvents) {

			if (gratuityEvent.getExecutionYear() != currentExecutionYear) {
				continue;
			}

			if (gratuityEvent.getAmountToPay().lessThan(THRESHOLD)) {
				PaymentCode activePaymentCode = getActivePaymentCode(gratuityEvent);

				if (activePaymentCode == null) {
					continue;

				}

				if (!(activePaymentCode instanceof InstallmentPaymentCode)) {
					continue;
				}

				return (InstallmentPaymentCode) activePaymentCode;
			}
		}

		return null;
	}

	private PaymentCode getActivePaymentCode(GratuityEvent gratuityEvent) {
		List<AccountingEventPaymentCode> paymentCodesSet = gratuityEvent.getAllPaymentCodes();

		for (AccountingEventPaymentCode accountingEventPaymentCode : paymentCodesSet) {
			if (accountingEventPaymentCode.isNew()) {
				return accountingEventPaymentCode;
			}
		}

		return null;
	}

}