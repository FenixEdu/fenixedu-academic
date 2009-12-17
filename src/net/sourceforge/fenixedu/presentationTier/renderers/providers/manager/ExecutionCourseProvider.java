package net.sourceforge.fenixedu.presentationTier.renderers.providers.manager;

import java.util.Collections;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.presentationTier.Action.manager.executionCourseManagement.ExecutionCourseBean;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ExecutionCourseProvider implements DataProvider {

    @Override
    public Converter getConverter() {
	return null;
    }

    @Override
    public Object provide(Object arg0, Object arg1) {
	ExecutionCourseBean bean = (ExecutionCourseBean) arg0;
	ExecutionSemester executionSemester = bean.getExecutionSemester();
	return executionSemester == null ? Collections.EMPTY_LIST : executionSemester.getAssociatedExecutionCourses();
    }

}
