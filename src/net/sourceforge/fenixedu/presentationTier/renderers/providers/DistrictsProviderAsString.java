package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.District;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DistrictsProviderAsString implements DataProvider {

    public Converter getConverter() {
	return null;
    }

    @Override
    public Object provide(Object source, Object currentValue) {
	final SortedSet<String> result = new TreeSet<String>();

	for (final District each : RootDomainObject.getInstance().getDistricts()) {
	    result.add(each.getName());
	}

	return result;
    }

}
