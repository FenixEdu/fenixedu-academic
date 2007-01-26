package net.sourceforge.fenixedu.presentationTier.renderers.providers.payments;

import net.sourceforge.fenixedu.dataTransferObject.accounting.penaltyExemption.CreatePenaltyExemptionBean;
import net.sourceforge.fenixedu.domain.accounting.events.PenaltyExemptionJustificationType;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.renderers.converters.EnumConverter;

public class PenaltyExemptionTypeProviderForCreatePenaltyExemptionBean implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	return PenaltyExemptionJustificationType.getValuesFor(((CreatePenaltyExemptionBean) source).getEvent()
		.getEventType());
    }

    public Converter getConverter() {
	return new EnumConverter();
    }

}
