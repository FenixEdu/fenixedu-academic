package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import net.sourceforge.fenixedu.domain.degree.DegreeType;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumConverter;

public class BolonhaDegreeTypesProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	return DegreeType.getBolonhaDegreeTypes();
    }

    public Converter getConverter() {
	return new EnumConverter();
    }

}
