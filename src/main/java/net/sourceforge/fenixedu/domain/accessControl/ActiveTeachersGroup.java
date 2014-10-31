package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Set;
import java.util.stream.Collectors;

import net.sourceforge.fenixedu.domain.ExecutionSemester;

import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.GroupStrategy;
import org.joda.time.DateTime;

@GroupOperator("activeTeachers")
public class ActiveTeachersGroup extends GroupStrategy {

    private static final long serialVersionUID = -819881356666615761L;

    @Override
    public String getPresentationName() {
        return "Active Teachers";
    }

    @Override
    public Set<User> getMembers() {
        return Bennu
                .getInstance()
                .getTeachersSet()
                .stream()
                .filter(teacher -> teacher.isActive()
                        || teacher.hasTeacherAuthorization(ExecutionSemester.readActualExecutionSemester()))
                .map(teacher -> teacher.getPerson().getUser()).collect(Collectors.toSet());
    }

    @Override
    public Set<User> getMembers(DateTime when) {
        return getMembers();
    }

    @Override
    public boolean isMember(User user) {
        return user != null
                && user.getPerson() != null
                && user.getPerson().getTeacher() != null
                && (user.getPerson().getTeacher().isActive() || user.getPerson().getTeacher()
                        .hasTeacherAuthorization(ExecutionSemester.readActualExecutionSemester()));
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

}
