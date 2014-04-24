package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Professorship;

import org.fenixedu.bennu.core.annotation.CustomGroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.Group;
import org.joda.time.DateTime;

import com.google.common.base.Supplier;

@CustomGroupOperator("teachersWithGradesToSubmit")
public class PersistentTeachersWithGradesToSubmitGroup extends PersistentTeachersWithGradesToSubmitGroup_Base {

    public PersistentTeachersWithGradesToSubmitGroup(ExecutionSemester period, DegreeCurricularPlan degreeCurricularPlan) {
        super();
        init(period, degreeCurricularPlan);
    }

    @Override
    public Set<User> getMembers() {
        Set<User> users = new HashSet<>();
        for (ExecutionCourse executionCourse : getPeriod().getExecutionCoursesWithDegreeGradesToSubmit(getDegreeCurricularPlan())) {
            for (Professorship professorship : executionCourse.getProfessorshipsSet()) {
                if (professorship.getResponsibleFor()) {
                    if (professorship.getPerson() != null) {
                        User user = professorship.getPerson().getUser();
                        if (user != null) {
                            users.add(user);
                        }
                    }
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
        return user != null && getMembers().contains(user);
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

    public static Set<Group> groupsForUser(User user) {
        return Collections.emptySet();
    }

    public static PersistentTeachersWithGradesToSubmitGroup getInstance(final ExecutionSemester period,
            final DegreeCurricularPlan degreeCurricularPlan) {
        return getInstance(PersistentTeachersWithGradesToSubmitGroup.class, period, degreeCurricularPlan,
                new Supplier<PersistentTeachersWithGradesToSubmitGroup>() {
                    @Override
                    public PersistentTeachersWithGradesToSubmitGroup get() {
                        return new PersistentTeachersWithGradesToSubmitGroup(period, degreeCurricularPlan);
                    }
                });
    }
}
