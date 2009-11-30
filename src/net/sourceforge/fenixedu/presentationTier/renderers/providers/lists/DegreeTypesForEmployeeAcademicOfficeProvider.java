package net.sourceforge.fenixedu.presentationTier.renderers.providers.lists;

import net.sourceforge.fenixedu.dataTransferObject.commons.DegreeByExecutionYearBean;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumConverter;

public class DegreeTypesForEmployeeAcademicOfficeProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	return ((DegreeByExecutionYearBean) source).getAdministratedDegreeTypes();
    }

    public Converter getConverter() {
	return new EnumConverter();
    }

}
