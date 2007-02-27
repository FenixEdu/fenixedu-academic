package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Arrays;

import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.renderers.converters.EnumConverter;

public class DegreeTypesProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	return Arrays.asList(DegreeType.values());
    }

    public Converter getConverter() {
	return new EnumConverter();
    }

}
