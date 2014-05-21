package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Optional;

import net.sourceforge.fenixedu.domain.Project;

import org.fenixedu.bennu.core.groups.Group;

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
        return singleton(() -> Optional.ofNullable(project.getProjectDepartmentGroup()),
                () -> new PersistentProjectDepartmentGroup(project));
    }
}
