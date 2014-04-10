package net.sourceforge.fenixedu.domain.accessControl.arguments;

import net.sourceforge.fenixedu.domain.Project;

import org.fenixedu.bennu.core.annotation.GroupArgumentParser;

@GroupArgumentParser
public class ProjectArgument extends DomainObjectArgumentParser<Project> {
    @Override
    public Class<Project> type() {
        return Project.class;
    }
}
