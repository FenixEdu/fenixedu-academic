package net.sourceforge.fenixedu.presentationTier.renderers.providers.coordinator.tutor;

import net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor.StudentsByEntryYearBean;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyArrayConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class StudentsWithoutTutorshipDataProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        StudentsByEntryYearBean bean = (StudentsByEntryYearBean) source;

        return bean.getStudentsList();
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyArrayConverter();
    }
}
