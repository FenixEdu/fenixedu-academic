package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class DistinctCountriesProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	return Country.readDistinctCountries();
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
