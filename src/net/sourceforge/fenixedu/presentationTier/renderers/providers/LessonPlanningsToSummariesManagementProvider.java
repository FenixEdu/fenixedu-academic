package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;

import net.sourceforge.fenixedu.dataTransferObject.SummariesManagementBean;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.LessonPlanning;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class LessonPlanningsToSummariesManagementProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	SummariesManagementBean bean = (SummariesManagementBean) source;
	ShiftType lessonType = bean.getLessonType();
	if (lessonType != null) {
	    ExecutionCourse executionCourse = bean.getExecutionCourse();
	    return executionCourse.getLessonPlanningsOrderedByOrder(lessonType);
	}
	return new ArrayList<LessonPlanning>();
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
