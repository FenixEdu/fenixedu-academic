package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse.SearchExecutionCourseLogBean;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ExecutionCourseLogMonthProvider implements DataProvider {

	@Override
	public Object provide(final Object source, final Object currentValue) {
		final SearchExecutionCourseLogBean searchExecutionCourseLogBean = (SearchExecutionCourseLogBean) source;
		return searchExecutionCourseLogBean.getExecutionCourse().getExecutionPeriod().getSemesterMonths();
	}

	@Override
	public Converter getConverter() {
		return null;
	}

}
