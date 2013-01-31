package net.sourceforge.fenixedu.presentationTier.renderers.providers.payments;

import net.sourceforge.fenixedu.dataTransferObject.accounting.penaltyExemption.CreateInstallmentPenaltyExemptionBean;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyArrayConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class InstallmentsForCreateInstallmentPenaltyExemptionBeanProvider implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {
		return ((CreateInstallmentPenaltyExemptionBean) source).getGratuityEventWithPaymentPlan().getGratuityPaymentPlan()
				.getInstallmentsSortedByEndDate();
	}

	@Override
	public Converter getConverter() {
		return new DomainObjectKeyArrayConverter();
	}

}
