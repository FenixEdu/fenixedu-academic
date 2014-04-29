package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Project;
import net.sourceforge.fenixedu.domain.Teacher;

import org.fenixedu.bennu.core.annotation.CustomGroupArgument;
import org.fenixedu.bennu.core.annotation.CustomGroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.Group;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

import com.google.common.base.Strings;

@CustomGroupOperator("project")
public class PersistentProjectDepartmentGroup extends PersistentProjectDepartmentGroup_Base {
    protected PersistentProjectDepartmentGroup(Project project) {
        super();
        setProject(project);
    }

    @CustomGroupArgument
    public static Argument<Project> projectArgument() {
        return new SimpleArgument<Project, PersistentProjectDepartmentGroup>() {
            private static final long serialVersionUID = -4731635395938942581L;

            @Override
            public Project parse(String argument) {
                return Strings.isNullOrEmpty(argument) ? null : FenixFramework.<Project> getDomainObject(argument);
            }

            @Override
            public Class<? extends Project> getType() {
                return Project.class;
            }

            @Override
            public String extract(PersistentProjectDepartmentGroup group) {
                return group.getProject() != null ? group.getProject().getExternalId() : "";
            }
        };
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        return new String[] { getProject().getName() };
    }

    @Override
    public Set<User> getMembers() {
        Set<User> users = new HashSet<>();
        for (Department department : getProject().getDeparmentsSet()) {
            for (Teacher teacher : department.getAllCurrentTeachers()) {
                User user = teacher.getPerson().getUser();
                if (user != null) {
                    users.add(user);
                }
            }
        }
        return users;
    }

    @Override
    public Set<User> getMembers(DateTime when) {
        return getMembers();
    }

    @Override
    public boolean isMember(User user) {
        if (user == null || user.getPerson().getTeacher() == null) {
            return false;
        }
        final Teacher teacher = user.getPerson().getTeacher();
        final Department department = teacher.getCurrentWorkingDepartment();
        if (department != null) {
            return getProject().getDeparmentsSet().contains(department);
        }
        return false;
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

    public static Set<Group> groupsForUser(User user) {
        return Collections.emptySet();
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
