package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.ExecutionCourse;

import com.google.common.collect.FluentIterable;

public abstract class PersistentSpecialCriteriaOverExecutionCourseGroup extends
        PersistentSpecialCriteriaOverExecutionCourseGroup_Base {
    protected PersistentSpecialCriteriaOverExecutionCourseGroup() {
        super();
    }

    protected void init(ExecutionCourse executionCourse) {
        setExecutionCourse(executionCourse);
    }

    @Override
    protected void gc() {
        setExecutionCourse(null);
        super.gc();
    }

    protected static <T extends PersistentSpecialCriteriaOverExecutionCourseGroup> T select(Class<T> type,
            ExecutionCourse executionCourse) {
        return FluentIterable.from(executionCourse.getSpecialCriteriaOverExecutionCourseGroupSet()).filter(type).first().orNull();
    }
}
