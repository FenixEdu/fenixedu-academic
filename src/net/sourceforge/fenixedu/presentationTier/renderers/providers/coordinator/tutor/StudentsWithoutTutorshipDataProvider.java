package net.sourceforge.fenixedu.presentationTier.renderers.providers.coordinator.tutor;

import net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor.StudentsByEntryYearBean;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyArrayConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class StudentsWithoutTutorshipDataProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	StudentsByEntryYearBean bean = (StudentsByEntryYearBean) source;

	return bean.getStudentsList();
    }

    public Converter getConverter() {
	return new DomainObjectKeyArrayConverter();
    }
}
