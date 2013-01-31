package net.sourceforge.fenixedu.presentationTier.renderers.providers.payments;

import java.util.Arrays;

import net.sourceforge.fenixedu.domain.accounting.events.InsuranceExemptionJustificationType;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumConverter;

public class InsuranceExemptionJustificationProvider implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {
		return Arrays.asList(InsuranceExemptionJustificationType.values());
	}

	@Override
	public Converter getConverter() {
		return new EnumConverter();
	}

}
