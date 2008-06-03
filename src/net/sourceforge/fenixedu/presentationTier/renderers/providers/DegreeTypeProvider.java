package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Arrays;

import net.sourceforge.fenixedu.domain.degree.DegreeType;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumConverter;

public class DegreeTypeProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	return Arrays.asList(DegreeType.values());
    }

    public Converter getConverter() {
	return new EnumConverter();
    }

}
