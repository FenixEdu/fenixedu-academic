package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;

import org.fenixedu.bennu.core.annotation.CustomGroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.Group;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

import com.google.common.collect.Sets;

@CustomGroupOperator("teacherResponsibleOfExecutionCourse")
public class PersistentTeacherResponsibleOfExecutionCourseGroup extends PersistentTeacherResponsibleOfExecutionCourseGroup_Base {
    protected PersistentTeacherResponsibleOfExecutionCourseGroup(ExecutionCourse executionCourse) {
        super();
        init(executionCourse);
    }

    @Override
    public Set<User> getMembers() {
        Set<User> users = new HashSet<>();
        for (Professorship professorship : getExecutionCourse().responsibleFors()) {
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
        if (!Sets.intersection(user.getPerson().getProfessorshipsSet(), new HashSet<>(getExecutionCourse().responsibleFors()))
                .isEmpty()) {
            return true;
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

    public static PersistentTeacherResponsibleOfExecutionCourseGroup getInstance(ExecutionCourse executionCourse) {
        PersistentTeacherResponsibleOfExecutionCourseGroup instance =
                select(PersistentTeacherResponsibleOfExecutionCourseGroup.class, executionCourse);
        return instance != null ? instance : create(executionCourse);
    }

    @Atomic(mode = TxMode.WRITE)
    private static PersistentTeacherResponsibleOfExecutionCourseGroup create(ExecutionCourse executionCourse) {
        PersistentTeacherResponsibleOfExecutionCourseGroup instance =
                select(PersistentTeacherResponsibleOfExecutionCourseGroup.class, executionCourse);
        return instance != null ? instance : new PersistentTeacherResponsibleOfExecutionCourseGroup(executionCourse);
    }
}
