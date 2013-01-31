package net.sourceforge.fenixedu.presentationTier.Action.phd;

import net.sourceforge.fenixedu.domain.phd.debts.PhdEventExemptionJustificationType;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumConverter;

public class ExternalScholarshipProvidersProvider implements DataProvider {

	@Override
	public Converter getConverter() {
		return new EnumConverter();
	}

	@Override
	public Object provide(Object arg0, Object arg1) {
		return PhdEventExemptionJustificationType.values();
	}
}
