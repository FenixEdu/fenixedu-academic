package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Professorship;

import org.fenixedu.bennu.core.annotation.CustomGroupOperator;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.Group;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

import com.google.common.base.Optional;

@CustomGroupOperator("responsibleForExecutionCourse")
public class PersistentResponsibleForExecutionCourseGroup extends PersistentResponsibleForExecutionCourseGroup_Base {
    protected PersistentResponsibleForExecutionCourseGroup() {
        super();
    }

    @Override
    public Set<User> getMembers() {
        Set<User> users = new HashSet<>();
        for (final ExecutionYear executionYear : Bennu.getInstance().getExecutionYearsSet()) {
            if (executionYear.isCurrent()) {
                for (final ExecutionSemester executionSemester : executionYear.getExecutionPeriodsSet()) {
                    for (final ExecutionCourse executionCourse : executionSemester.getAssociatedExecutionCoursesSet()) {
                        for (final Professorship professorship : executionCourse.getProfessorshipsSet()) {
                            if (professorship.getResponsibleFor().booleanValue()) {
                                User user = professorship.getPerson().getUser();
                                if (user != null) {
                                    users.add(user);
                                }
                            }
                        }
                    }
                }
                break;
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
        if (user.getPerson().getTeacher() != null) {
            for (final Professorship professorship : user.getPerson().getTeacher()
                    .getProfessorships(ExecutionYear.readCurrentExecutionYear())) {
                if (professorship.isResponsibleFor()) {
                    return true;
                }
            }
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
