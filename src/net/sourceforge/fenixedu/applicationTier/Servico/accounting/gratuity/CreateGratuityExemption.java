package net.sourceforge.fenixedu.applicationTier.Servico.accounting.gratuity;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.accounting.gratuityExemption.CreateGratuityExemptionBean;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.PercentageGratuityExemption;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.ValueGratuityExemption;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class CreateGratuityExemption extends FenixService {

    @Checked("RolePredicates.ACADEMIC_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    public static void run(final Employee employee, final CreateGratuityExemptionBean createGratuityExemptionBean) {
	if (createGratuityExemptionBean.isPercentageExemption()) {
	    new PercentageGratuityExemption(employee, createGratuityExemptionBean.getGratuityEvent(), createGratuityExemptionBean
		    .getExemptionJustificationType(), createGratuityExemptionBean.getReason(), createGratuityExemptionBean
		    .getDispatchDate(), createGratuityExemptionBean.getSelectedPercentage().divide(BigDecimal.valueOf(100)));
	} else {
	    new ValueGratuityExemption(employee, createGratuityExemptionBean.getGratuityEvent(), createGratuityExemptionBean
		    .getExemptionJustificationType(), createGratuityExemptionBean.getReason(), createGratuityExemptionBean
		    .getDispatchDate(), createGratuityExemptionBean.getAmount());
	}
    }

}