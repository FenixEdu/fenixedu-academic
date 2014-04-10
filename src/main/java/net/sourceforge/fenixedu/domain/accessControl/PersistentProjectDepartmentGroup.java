package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.Project;

import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

public class PersistentProjectDepartmentGroup extends PersistentProjectDepartmentGroup_Base {
    protected PersistentProjectDepartmentGroup(Project project) {
        super();
        setProject(project);
    }

    @Override
    public Group toGroup() {
        return ProjectDepartmentGroup.get(getProject());
    }

    @Override
    protected void gc() {
        setProject(null);
        super.gc();
    }

    public static PersistentProjectDepartmentGroup getInstance(final Project project) {
        PersistentProjectDepartmentGroup instance = project.getProjectDepartmentGroup();
        return instance != null ? instance : create(project);
    }

    @Atomic(mode = TxMode.WRITE)
    private static PersistentProjectDepartmentGroup create(final Project project) {
        PersistentProjectDepartmentGroup instance = project.getProjectDepartmentGroup();
        return instance != null ? instance : new PersistentProjectDepartmentGroup(project);
    }
}
