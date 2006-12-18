package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

import org.apache.commons.beanutils.BeanComparator;

public class CountryProvider implements DataProvider {

	public Object provide(Object source, Object currentValue) {
		
		Set<Country> countrySet = new TreeSet<Country>(new BeanComparator("name"));
		countrySet.addAll(RootDomainObject.readAllDomainObjects(Country.class));
		return countrySet;
	}

	public Converter getConverter() {
		
		return new DomainObjectKeyConverter();
	}
	
}
