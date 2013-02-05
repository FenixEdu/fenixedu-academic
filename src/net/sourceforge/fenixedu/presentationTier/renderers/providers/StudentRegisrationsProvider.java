package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import net.sourceforge.fenixedu.dataTransferObject.student.ManageStudentStatuteBean;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class StudentRegisrationsProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {

        return ((ManageStudentStatuteBean) source).getStudent().getRegistrations();
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}