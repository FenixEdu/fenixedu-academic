package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.ExecutionCourse;

import org.fenixedu.bennu.core.annotation.CustomGroupArgument;

import pt.ist.fenixframework.FenixFramework;

import com.google.common.base.Strings;
import com.google.common.collect.FluentIterable;

public abstract class PersistentSpecialCriteriaOverExecutionCourseGroup extends
        PersistentSpecialCriteriaOverExecutionCourseGroup_Base {
    protected PersistentSpecialCriteriaOverExecutionCourseGroup() {
        super();
    }

    protected void init(ExecutionCourse executionCourse) {
        setExecutionCourse(executionCourse);
    }

    @CustomGroupArgument
    public static Argument<ExecutionCourse> executionCourseArgument() {
        return new SimpleArgument<ExecutionCourse, PersistentSpecialCriteriaOverExecutionCourseGroup>() {
            @Override
            public ExecutionCourse parse(String argument) {
                return Strings.isNullOrEmpty(argument) ? null : FenixFramework.<ExecutionCourse> getDomainObject(argument);
            }

            @Override
            public Class<? extends ExecutionCourse> getType() {
                return ExecutionCourse.class;
            }

            @Override
            public String extract(PersistentSpecialCriteriaOverExecutionCourseGroup group) {
                return group.getExecutionCourse() != null ? group.getExecutionCourse().getExternalId() : "";
            }
        };
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        return new String[] { getExecutionCourse().getNome() };
    }

    protected static <T extends PersistentSpecialCriteriaOverExecutionCourseGroup> T select(Class<T> type,
            ExecutionCourse executionCourse) {
        return FluentIterable.from(executionCourse.getSpecialCriteriaOverExecutionCourseGroupSet()).filter(type).first().orNull();
    }
}
