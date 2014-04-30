package net.sourceforge.fenixedu.domain.accessControl.arguments;

import net.sourceforge.fenixedu.domain.ExecutionCourse;

import org.fenixedu.bennu.core.annotation.GroupArgumentParser;

@GroupArgumentParser
public class ExecutionCourseArgument extends DomainObjectArgumentParser<ExecutionCourse> {
    @Override
    public Class<ExecutionCourse> type() {
        return ExecutionCourse.class;
    }
}
