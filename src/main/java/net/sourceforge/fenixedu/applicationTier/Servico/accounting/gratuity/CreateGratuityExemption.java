package net.sourceforge.fenixedu.applicationTier.Servico.accounting.gratuity;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.dataTransferObject.accounting.gratuityExemption.CreateGratuityExemptionBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.PercentageGratuityExemption;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.ValueGratuityExemption;
import net.sourceforge.fenixedu.predicates.AcademicPredicates;
import pt.ist.fenixframework.Atomic;

public class CreateGratuityExemption {

    @Atomic
    public static void run(final Person responsible, final CreateGratuityExemptionBean createGratuityExemptionBean) {
        check(AcademicPredicates.MANAGE_STUDENT_PAYMENTS);
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