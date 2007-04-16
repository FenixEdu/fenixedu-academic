package net.sourceforge.fenixedu.presentationTier.renderers.providers.assiduousness;

import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeJustificationFactory;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.renderers.converters.EnumConverter;

public class JustificationTypeAsProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	if (source instanceof EmployeeJustificationFactory) {
	    EmployeeJustificationFactory employeeJustificationFactory = (EmployeeJustificationFactory) source;
	    if (employeeJustificationFactory.getJustificationDayType().equals(
		    EmployeeJustificationFactory.JustificationDayType.DAY)) {
		return JustificationType.getDayJustificationTypes();
	    } else if (employeeJustificationFactory.getJustificationDayType().equals(
		    EmployeeJustificationFactory.JustificationDayType.HALF_DAY)) {
		return JustificationType.getHalfDayJustificationTypes();
	    }
	    return JustificationType.getHoursJustificationTypes();
	}
	return JustificationType.getJustificationTypesForJustificationMotives();
    }

    public Converter getConverter() {
	return new EnumConverter();
    }
}
