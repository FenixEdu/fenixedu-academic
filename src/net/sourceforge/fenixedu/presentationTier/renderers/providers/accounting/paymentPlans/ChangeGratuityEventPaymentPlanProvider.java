package net.sourceforge.fenixedu.presentationTier.renderers.providers.accounting.paymentPlans;

import java.util.ArrayList;

import net.sourceforge.fenixedu.domain.accounting.PaymentPlan;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEventWithPaymentPlan;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ChangeGratuityEventPaymentPlanProvider implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {
		final GratuityEventWithPaymentPlan event = (GratuityEventWithPaymentPlan) source;
		return new ArrayList<PaymentPlan>(event.getDegreeCurricularPlanServiceAgreement().getServiceAgreementTemplate()
				.getGratuityPaymentPlansFor(event.getExecutionYear()));
	}

	@Override
	public Converter getConverter() {
		return new DomainObjectKeyConverter();
	}

}
