package net.sourceforge.fenixedu.domain.accessControl;

import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

import com.google.common.base.Optional;

public class PersistentResponsibleForExecutionCourseGroup extends PersistentResponsibleForExecutionCourseGroup_Base {
    protected PersistentResponsibleForExecutionCourseGroup() {
        super();
    }

    @Override
    public Group toGroup() {
        return ResponsibleForExecutionCourseGroup.get();
    }

    public static PersistentResponsibleForExecutionCourseGroup getInstance() {
        Optional<PersistentResponsibleForExecutionCourseGroup> instance =
                find(PersistentResponsibleForExecutionCourseGroup.class);
        return instance.isPresent() ? instance.get() : create();
    }

    @Atomic(mode = TxMode.WRITE)
    private static PersistentResponsibleForExecutionCourseGroup create() {
        Optional<PersistentResponsibleForExecutionCourseGroup> instance =
                find(PersistentResponsibleForExecutionCourseGroup.class);
        return instance.isPresent() ? instance.get() : new PersistentResponsibleForExecutionCourseGroup();
    }
}
