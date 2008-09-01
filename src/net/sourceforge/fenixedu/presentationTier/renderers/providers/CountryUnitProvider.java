package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.organizationalStructure.CountryUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class CountryUnitProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	final Set<CountryUnit> countryUnitSet = new TreeSet<CountryUnit>(Party.COMPARATOR_BY_NAME);
	for (Unit unit : CountryUnit.readAllUnits()) {
	    if (unit instanceof CountryUnit) {
		countryUnitSet.add((CountryUnit) unit);
	    }
	}

	return countryUnitSet;
    }

    public Converter getConverter() {

	return new DomainObjectKeyConverter();
    }

}
