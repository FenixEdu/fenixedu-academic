package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.SummariesManagementBean;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.Summary;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class LastSummariesToSummariesManagementProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	SummariesManagementBean bean = (SummariesManagementBean) source;
	ShiftType lessonType = bean.getLessonType();

	if (lessonType != null) {

	    ExecutionCourse executionCourse = bean.getExecutionCourse();
	    List<Summary> summaries = new ArrayList<Summary>();
	    summaries.addAll(executionCourse.getSummariesByShiftType(lessonType));
	    Collections.sort(summaries, Summary.COMPARATOR_BY_DATE_AND_HOUR);

	    List<Summary> result = new ArrayList<Summary>();
	    if (!summaries.isEmpty() && summaries.size() > 4) {
		result = summaries.subList(0, 4);
	    } else {
		result = summaries;
	    }

	    if (bean.getSummary() != null) {
		result.remove(bean.getSummary());
	    }

	    return result;
	}
	return new ArrayList<Summary>();
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
