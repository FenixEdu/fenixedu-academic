package net.sourceforge.fenixedu.presentationTier.renderers.providers.lists;

import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumConverter;

public class DegreeTypesForEmployeeAcademicOfficeProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	return AccessControl.getPerson().getEmployee().getAdministrativeOffice().getAdministratedDegreeTypes();
    }

    public Converter getConverter() {
	return new EnumConverter();
    }

}
