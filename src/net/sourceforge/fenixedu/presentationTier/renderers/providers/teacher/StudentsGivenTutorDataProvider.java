package net.sourceforge.fenixedu.presentationTier.renderers.providers.teacher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.StudentsByTutorBean;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyArrayConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class StudentsGivenTutorDataProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
    	StudentsByTutorBean bean = (StudentsByTutorBean) source;
    	
    	final Teacher teacher = bean.getTeacher();
    	
    	List<Tutorship> tutorships = new ArrayList<Tutorship>(teacher.getActiveTutorships());
    	Collections.sort(tutorships, Tutorship.TUTORSHIP_COMPARATOR_BY_STUDENT_NUMBER);
    	
    	return tutorships;
    }

    public Converter getConverter() {
    	return new DomainObjectKeyArrayConverter();
    }
}
