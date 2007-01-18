package net.sourceforge.fenixedu.presentationTier.renderers.providers.payments;

import net.sourceforge.fenixedu.dataTransferObject.accounting.penaltyExemption.CreateInstallmentPenaltyExemptionBean;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyArrayConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class InstallmentsForCreateInstallmentPenaltyExemptionBeanProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	return ((CreateInstallmentPenaltyExemptionBean) source).getGratuityEventWithPaymentPlan()
		.getGratuityPaymentPlan().getInstallmentsSortedByEndDate();
    }

    public Converter getConverter() {
	return new DomainObjectKeyArrayConverter();
    }

}
