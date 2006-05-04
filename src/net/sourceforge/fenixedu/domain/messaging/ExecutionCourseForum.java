package net.sourceforge.fenixedu.domain.messaging;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;

public class ExecutionCourseForum extends ExecutionCourseForum_Base {

    public ExecutionCourseForum() {
        super();
    }

    public ExecutionCourseForum(Person owner, String name, String description, Group readersGroup,
            Group writersGroup) {
        super();
        init(owner, name, description, readersGroup, writersGroup);
    }
}
