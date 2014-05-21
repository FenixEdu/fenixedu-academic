package net.sourceforge.fenixedu.domain.accessControl;

import org.fenixedu.bennu.core.groups.Group;

public class PersistentResponsibleForExecutionCourseGroup extends PersistentResponsibleForExecutionCourseGroup_Base {
    protected PersistentResponsibleForExecutionCourseGroup() {
        super();
    }

    @Override
    public Group toGroup() {
        return ResponsibleForExecutionCourseGroup.get();
    }

    public static PersistentResponsibleForExecutionCourseGroup getInstance() {
        return singleton(() -> find(PersistentResponsibleForExecutionCourseGroup.class),
                () -> new PersistentResponsibleForExecutionCourseGroup());
    }
}
