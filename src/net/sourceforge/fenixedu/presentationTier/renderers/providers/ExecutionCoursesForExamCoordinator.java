package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.vigilancy.ExamCoordinator;
import net.sourceforge.fenixedu.presentationTier.Action.vigilancy.ConvokeBean;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

import org.apache.commons.beanutils.BeanComparator;

public class ExecutionCoursesForExamCoordinator implements DataProvider {

    public Object provide(Object source, Object currentValue) {
        ConvokeBean bean = (ConvokeBean) source;
        ExamCoordinator coordinator = bean.getExamCoordinator();
        List<ExecutionCourse> courses = coordinator.getAssociatedExecutionCourses();

        Collections.sort(courses, new BeanComparator("nome"));
        return courses;
    }

    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}