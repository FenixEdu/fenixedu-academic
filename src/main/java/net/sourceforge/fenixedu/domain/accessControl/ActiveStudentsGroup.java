package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Set;
import java.util.stream.Collectors;

import net.sourceforge.fenixedu.domain.student.Student;

import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.GroupStrategy;
import org.joda.time.DateTime;

@GroupOperator("activeStudents")
public class ActiveStudentsGroup extends GroupStrategy {

    private static final long serialVersionUID = 2139482012047494196L;

    @Override
    public String getPresentationName() {
        return "Active Students";
    }

    @Override
    public Set<User> getMembers() {
        return Bennu.getInstance().getStudentsSet().stream().filter(Student::hasActiveRegistrations)
                .map(student -> student.getPerson().getUser()).collect(Collectors.toSet());
    }

    @Override
    public Set<User> getMembers(DateTime when) {
        return getMembers();
    }

    @Override
    public boolean isMember(User user) {
        return user != null && user.getPerson() != null && user.getPerson().getStudent() != null
                && user.getPerson().getStudent().hasActiveRegistrations();
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

}
