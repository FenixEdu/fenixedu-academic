package net.sourceforge.fenixedu.applicationTier.Servico.accounting.gratuity;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.dataTransferObject.accounting.gratuityExemption.CreateGratuityExemptionBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.PercentageGratuityExemption;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.ValueGratuityExemption;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;

public class CreateGratuityExemption {

    @Checked("AcademicPredicates.MANAGE_STUDENT_PAYMENTS")
    @Atomic
    public static void run(final Person responsible, final CreateGratuityExemptionBean createGratuityExemptionBean) {
        if (createGratuityExemptionBean.isPercentageExemption()) {
            new PercentageGratuityExemption(responsible, createGratuityExemptionBean.getGratuityEvent(),
                    createGratuityExemptionBean.getExemptionJustificationType(), createGratuityExemptionBean.getReason(),
                    createGratuityExemptionBean.getDispatchDate(), createGratuityExemptionBean.getSelectedPercentage().divide(
                            BigDecimal.valueOf(100)));
        } else {
            new ValueGratuityExemption(responsible, createGratuityExemptionBean.getGratuityEvent(),
                    createGratuityExemptionBean.getExemptionJustificationType(), createGratuityExemptionBean.getReason(),
                    createGratuityExemptionBean.getDispatchDate(), createGratuityExemptionBean.getAmount());
        }
    }

}