package net.sourceforge.fenixedu.applicationTier.Servico.accounting.gratuity;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.accounting.gratuityExemption.CreateGratuityExemptionBean;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.PercentageGratuityExemption;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.ValueGratuityExemption;

public class CreateGratuityExemption extends Service {

    public CreateGratuityExemption() {
	super();
    }

    public void run(final Employee employee,
	    final CreateGratuityExemptionBean createGratuityExemptionBean) {
	if (createGratuityExemptionBean.isPercentageExemption()) {
	    new PercentageGratuityExemption(employee, createGratuityExemptionBean.getGratuityEvent(),
		    createGratuityExemptionBean.getExemptionJustificationType(), createGratuityExemptionBean
			    .getReason(), createGratuityExemptionBean.getDispatchDate(),
		    createGratuityExemptionBean.getSelectedPercentage().divide(BigDecimal.valueOf(100)));
	} else {
	    new ValueGratuityExemption(employee, createGratuityExemptionBean.getGratuityEvent(),
		    createGratuityExemptionBean.getExemptionJustificationType(), createGratuityExemptionBean
			    .getReason(), createGratuityExemptionBean.getDispatchDate(),
		    createGratuityExemptionBean.getAmount());
	}
    }

}
