package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import net.sourceforge.fenixedu.domain.Country;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DistinctCountriesProvider implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {
		return Country.readDistinctCountries();
	}

	@Override
	public Converter getConverter() {
		return new DomainObjectKeyConverter();
	}

}
