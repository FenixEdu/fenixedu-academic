package net.sourceforge.fenixedu.presentationTier.renderers.providers.payments;

import java.util.Arrays;

import net.sourceforge.fenixedu.domain.accounting.events.gratuity.exemption.penalty.PenaltyExemptionType;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.renderers.converters.EnumConverter;

public class PenaltyExemptionTypeProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	return Arrays.asList(PenaltyExemptionType.values());
    }

    public Converter getConverter() {
	return new EnumConverter();
    }

}
