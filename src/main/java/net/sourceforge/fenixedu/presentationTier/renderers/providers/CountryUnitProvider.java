package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.organizationalStructure.CountryUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class CountryUnitProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        final Set<CountryUnit> countryUnitSet = new TreeSet<CountryUnit>(Party.COMPARATOR_BY_NAME);
        countryUnitSet.addAll(CountryUnit.readAllCountryUnits());
        return countryUnitSet;
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
