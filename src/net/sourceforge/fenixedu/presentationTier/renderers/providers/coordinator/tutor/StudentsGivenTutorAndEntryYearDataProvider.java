package net.sourceforge.fenixedu.presentationTier.renderers.providers.coordinator.tutor;

import net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor.TutorshipManagementByEntryYearBean;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyArrayConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class StudentsGivenTutorAndEntryYearDataProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
    	TutorshipManagementByEntryYearBean bean = (TutorshipManagementByEntryYearBean) source;
    	
    	Teacher teacher = bean.getTeacher();
    	
    	return teacher.getActiveTutorshipsByStudentsEntryYear(bean.getExecutionYear());
    }

    public Converter getConverter() {
    	return new DomainObjectKeyArrayConverter();
    }
}
