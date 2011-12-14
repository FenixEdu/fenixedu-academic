package net.sourceforge.fenixedu.presentationTier.renderers.providers.teacher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.StudentsByTutorBean;
import net.sourceforge.fenixedu.domain.Tutorship;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyArrayConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class StudentsGivenTutorDataProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	StudentsByTutorBean bean = (StudentsByTutorBean) source;

	List<Tutorship> tutorships = new ArrayList<Tutorship>(bean.getActiveTutorshipsMatchingEntryYear());
	Collections.sort(tutorships, Tutorship.TUTORSHIP_COMPARATOR_BY_STUDENT_NUMBER);

	return tutorships;
    }

    public Converter getConverter() {
	return new DomainObjectKeyArrayConverter();
    }
}
