package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.renderers.converters.EnumConverter;

public class BolonhaDegreeTypesProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	return DegreeType.getBolonhaDegreeTypes();
    }

    public Converter getConverter() {
	return new EnumConverter();
    }

}
