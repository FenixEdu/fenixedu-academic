package net.sourceforge.fenixedu.presentationTier.renderers.providers.assiduousness;

import net.sourceforge.fenixedu.domain.assiduousness.util.ScheduleClockingType;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.renderers.converters.EnumConverter;

public class ScheduleClockingTypeAsProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	return ScheduleClockingType.getValidScheduleClockingTypes();
    }

    public Converter getConverter() {
	return new EnumConverter();
    }
}
