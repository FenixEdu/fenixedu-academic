package net.sourceforge.fenixedu.presentationTier.renderers.providers.assiduousness;

import net.sourceforge.fenixedu.domain.assiduousness.util.ScheduleClockingType;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumConverter;

public class ScheduleClockingTypeAsProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	return ScheduleClockingType.getValidScheduleClockingTypes();
    }

    public Converter getConverter() {
	return new EnumConverter();
    }
}
