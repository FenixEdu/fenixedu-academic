package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Optional;
import java.util.function.Supplier;

import net.sourceforge.fenixedu.domain.ExecutionCourse;

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

    protected static <T extends PersistentSpecialCriteriaOverExecutionCourseGroup> T singleton(Class<T> type,
            ExecutionCourse executionCourse, Supplier<T> creator) {
        return singleton(
                () -> (Optional<T>) executionCourse.getSpecialCriteriaOverExecutionCourseGroupSet().stream()
                        .filter(group -> group.getClass() == type).findAny(), creator);
    }
}
