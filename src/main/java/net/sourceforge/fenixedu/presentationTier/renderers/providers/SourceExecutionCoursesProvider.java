package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DomainObjectUtil;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.presentationTier.Action.manager.MergeExecutionCourseDispatchionAction.DegreesMergeBean;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class SourceExecutionCoursesProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {

        final DegreesMergeBean degreeBean = (DegreesMergeBean) source;

        Degree sourceDegree = degreeBean.getSourceDegree();

        List<ExecutionCourse> sourceExecutionCourses = sourceDegree.getExecutionCourses(degreeBean.getAcademicInterval());

        Collections.sort(sourceExecutionCourses, DomainObjectUtil.COMPARATOR_BY_ID);

        removeDuplicates(sourceExecutionCourses);

        Collections.sort(sourceExecutionCourses, ExecutionCourse.EXECUTION_COURSE_COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME);

        return sourceExecutionCourses;
    }

    public static void removeDuplicates(final List<ExecutionCourse> beanList) {
        if (beanList.isEmpty()) {
            return;
        }

        final Iterator<ExecutionCourse> iter = beanList.iterator();
        ExecutionCourse prev = iter.next();
        while (iter.hasNext()) {
            final ExecutionCourse curr = iter.next();
            if (curr.equals(prev)) {
                iter.remove();
            }
            prev = curr;
        }
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }
}