package net.sourceforge.fenixedu.domain.accounting.report.events;

import java.math.BigDecimal;
import java.util.Map;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.accounting.Installment;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEventWithPaymentPlan;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

public class GratuityEventInstallmentWrapper implements InstallmentWrapper {

    private GratuityEventWithPaymentPlan event;
    private Installment installment;

    public GratuityEventInstallmentWrapper(final GratuityEventWithPaymentPlan event, final Installment installment) {
        this.event = event;
        this.installment = installment;
    }

    @Override
    public String getExpirationDateLabel() {
        ResourceBundle bundle = ResourceBundle.getBundle("resources.AcademicAdminOffice");
        String label =
                bundle.getString("label.net.sourceforge.fenixedu.domain.accounting.report.events.EventReportQueueJob.installment.expiration.date");
        return String.format(label, this.installment.getOrder());
    }

    @Override
    public String getAmountToPayLabel() {
        ResourceBundle bundle = ResourceBundle.getBundle("resources.AcademicAdminOffice");
        String label =
                bundle.getString("label.net.sourceforge.fenixedu.domain.accounting.report.events.EventReportQueueJob.installment.expiration.amountToPay");
        return String.format(label, this.installment.getOrder());
    }

    @Override
    public String getRemainingAmountLabel() {
        ResourceBundle bundle = ResourceBundle.getBundle("resources.AcademicAdminOffice");
        String label =
                bundle.getString("label.net.sourceforge.fenixedu.domain.accounting.report.events.EventReportQueueJob.installment.expiration.remainingAmount");
        return String.format(label, this.installment.getOrder());
    }

    @Override
    public String getExpirationDate() {
        return installment.getEndDate().toString("dd/MM/yyyy");
    }

    @Override
    public String getAmountToPay() {
        return installment.calculateAmount(event, installment.getStartDate().toLocalDate().toDateTimeAtStartOfDay(),
                BigDecimal.ZERO, event.getGratuityPaymentPlan().isToApplyPenalty(event, this.installment)).toPlainString();
    }

    @Override
    public String getRemainingAmount() {
        Map<Installment, Money> calculateInstallmentRemainingAmounts =
                event.getGratuityPaymentPlan().calculateInstallmentRemainingAmounts(event, new DateTime(),
                        event.getPostingRule().getDiscountPercentage(event));

        for (Map.Entry<Installment, Money> entry : calculateInstallmentRemainingAmounts.entrySet()) {
            if (entry.getKey() == this.installment) {
                return entry.getValue().toPlainString();
            }
        }

        return Money.ZERO.toPlainString();
    }
}
