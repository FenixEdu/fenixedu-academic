package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;

import org.fenixedu.bennu.core.annotation.GroupArgument;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.joda.time.DateTime;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;

@GroupOperator("teacherResponsibleOfExecutionCourse")
public class TeacherResponsibleOfExecutionCourseGroup extends FenixGroup {
    private static final long serialVersionUID = -8474941837282629610L;

    @GroupArgument
    private ExecutionCourse executionCourse;

    private TeacherResponsibleOfExecutionCourseGroup() {
        super();
    }

    private TeacherResponsibleOfExecutionCourseGroup(ExecutionCourse executionCourse) {
        this();
        this.executionCourse = executionCourse;
    }

    public static TeacherResponsibleOfExecutionCourseGroup get(ExecutionCourse executionCourse) {
        return new TeacherResponsibleOfExecutionCourseGroup(executionCourse);
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        return new String[] { executionCourse.getNome() };
    }

    @Override
    public Set<User> getMembers() {
        Set<User> users = new HashSet<>();
        for (Professorship professorship : executionCourse.responsibleFors()) {
            users.add(professorship.getPerson().getUser());
        }
        return users;
    }

    @Override
    public Set<User> getMembers(DateTime when) {
        return getMembers();
    }

    @Override
    public boolean isMember(User user) {
        if (user == null) {
            return false;
        }
        if (!Sets.intersection(user.getPerson().getProfessorshipsSet(), new HashSet<>(executionCourse.responsibleFors()))
                .isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

    @Override
    public PersistentGroup toPersistentGroup() {
        return PersistentTeacherResponsibleOfExecutionCourseGroup.getInstance(executionCourse);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof TeacherResponsibleOfExecutionCourseGroup) {
            return Objects.equal(executionCourse, ((TeacherResponsibleOfExecutionCourseGroup) object).executionCourse);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(executionCourse);
    }
}
