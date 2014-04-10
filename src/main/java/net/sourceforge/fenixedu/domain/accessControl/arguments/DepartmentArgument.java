package net.sourceforge.fenixedu.domain.accessControl.arguments;

import net.sourceforge.fenixedu.domain.Department;

import org.fenixedu.bennu.core.annotation.GroupArgumentParser;

@GroupArgumentParser
public class DepartmentArgument extends DomainObjectArgumentParser<Department> {
    @Override
    public Class<Department> type() {
        return Department.class;
    }
}
