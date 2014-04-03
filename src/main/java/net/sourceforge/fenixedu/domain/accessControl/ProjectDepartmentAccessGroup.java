package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Project;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.OidOperator;

public class ProjectDepartmentAccessGroup extends DomainBackedGroup<Project> {

    public ProjectDepartmentAccessGroup(final Project project) {
        super(project);
    }

    private static final long serialVersionUID = 1L;

    @Override
    public Set<Person> getElements() {
        final Set<Person> people = new HashSet<Person>();
        final Project project = getObject();
        for (final Department department : project.getDeparmentsSet()) {
            for (final Teacher teacher : department.getAllCurrentTeachers()) {
                people.add(teacher.getPerson());
            }
        }
        return people;
    }

    @Override
    public boolean isMember(final Person person) {
        if (person != null && person.hasTeacher()) {
            final Teacher teacher = person.getTeacher();
            final Department department = teacher.getCurrentWorkingDepartment();
            if (department != null) {
                final Project project = getObject();
                return project.getDeparmentsSet().contains(department);
            }
        }
        return false;
    }

    @Override
    protected Argument[] getExpressionArguments() {
        return new Argument[] { new OidOperator(getObject()) };
    }

    public static class Builder implements GroupBuilder {

        @Override
        public Group build(Object[] arguments) {
            try {
                return new ProjectDepartmentAccessGroup((Project) arguments[0]);
            } catch (final ClassCastException e) {
                throw new Error(e);
            }
        }

        @Override
        public int getMinArguments() {
            return 1;
        }

        @Override
        public int getMaxArguments() {
            return 1;
        }

    }

    @Override
    public PersistentProjectDepartmentGroup convert() {
        return PersistentProjectDepartmentGroup.getInstance(getObject());
    }
}
