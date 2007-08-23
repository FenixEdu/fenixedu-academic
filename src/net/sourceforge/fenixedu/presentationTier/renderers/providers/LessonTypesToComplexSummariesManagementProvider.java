package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;

import net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse.NextPossibleSummaryLessonsAndDatesBean;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.renderers.converters.EnumConverter;

public class LessonTypesToComplexSummariesManagementProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	NextPossibleSummaryLessonsAndDatesBean bean = (NextPossibleSummaryLessonsAndDatesBean) source;
	if(bean.getLesson() != null) {
	    return bean.getLesson().getShift().getTypes();
	}
	return new ArrayList<ShiftType>();
    }

    public Converter getConverter() {
	return new EnumConverter();
    }
}
