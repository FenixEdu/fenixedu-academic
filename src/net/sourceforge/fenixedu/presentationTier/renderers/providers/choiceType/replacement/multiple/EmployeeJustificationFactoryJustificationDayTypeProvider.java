package net.sourceforge.fenixedu.presentationTier.renderers.providers.choiceType.replacement.multiple;

import java.util.Arrays;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumArrayConverter;

public class EmployeeJustificationFactoryJustificationDayTypeProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
	return Arrays
		.asList(net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeJustificationFactory.JustificationDayType
			.values());
    }

    @Override
    public Converter getConverter() {
	return new EnumArrayConverter(
		net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeJustificationFactory.JustificationDayType.class);
    }

}
