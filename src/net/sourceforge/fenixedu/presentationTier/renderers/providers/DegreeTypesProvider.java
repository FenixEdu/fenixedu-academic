package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.fenixedu.domain.degree.DegreeType;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DegreeTypesProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	return Arrays.asList(DegreeType.values());
    }

    public Converter getConverter() {
	return new Converter() {
	    @Override
	    public Object convert(Class type, Object value) {
		final List<DegreeType> degreeTypes = new ArrayList<DegreeType>();
		for (final String o : (String[]) value) {
		    degreeTypes.add(DegreeType.valueOf(o));
		}
		return degreeTypes;
	    }
	};
    }

}
