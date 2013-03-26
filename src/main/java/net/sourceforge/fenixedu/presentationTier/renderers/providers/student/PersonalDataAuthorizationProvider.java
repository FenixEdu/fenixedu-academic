package net.sourceforge.fenixedu.presentationTier.renderers.providers.student;

import net.sourceforge.fenixedu.util.StudentPersonalDataAuthorizationChoice;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumConverter;

public class PersonalDataAuthorizationProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        return StudentPersonalDataAuthorizationChoice.getGeneralPersonalDataAuthorizationsTypes();
    }

    @Override
    public Converter getConverter() {
        return new EnumConverter();
    }
}