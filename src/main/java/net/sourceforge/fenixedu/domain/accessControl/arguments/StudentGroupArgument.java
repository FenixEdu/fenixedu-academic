package net.sourceforge.fenixedu.domain.accessControl.arguments;

import net.sourceforge.fenixedu.domain.StudentGroup;

import org.fenixedu.bennu.core.annotation.GroupArgumentParser;

@GroupArgumentParser
public class StudentGroupArgument extends DomainObjectArgumentParser<StudentGroup> {
    @Override
    public Class<StudentGroup> type() {
        return StudentGroup.class;
    }
}
