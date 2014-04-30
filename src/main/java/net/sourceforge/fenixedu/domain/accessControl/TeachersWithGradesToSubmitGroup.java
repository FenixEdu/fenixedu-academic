package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Professorship;

import org.fenixedu.bennu.core.annotation.GroupArgument;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.joda.time.DateTime;

import com.google.common.base.Objects;

@GroupOperator("teachersWithGradesToSubmit")
public class TeachersWithGradesToSubmitGroup extends FenixGroup {
    private static final long serialVersionUID = 1231342910537087397L;

    @GroupArgument
    private ExecutionSemester period;

    @GroupArgument
    private DegreeCurricularPlan degreeCurricularPlan;

    private TeachersWithGradesToSubmitGroup() {
        super();
    }

    private TeachersWithGradesToSubmitGroup(ExecutionSemester period, DegreeCurricularPlan degreeCurricularPlan) {
        this();
        this.period = period;
        this.degreeCurricularPlan = degreeCurricularPlan;
    }

    public static TeachersWithGradesToSubmitGroup get(ExecutionSemester period, DegreeCurricularPlan degreeCurricularPlan) {
        return new TeachersWithGradesToSubmitGroup(period, degreeCurricularPlan);
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        return new String[] { degreeCurricularPlan.getPresentationName(), period.getName() };
    }

    @Override
    public Set<User> getMembers() {
        Set<User> users = new HashSet<>();
        for (ExecutionCourse executionCourse : period.getExecutionCoursesWithDegreeGradesToSubmit(degreeCurricularPlan)) {
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

    @Override
    public PersistentGroup toPersistentGroup() {
        return PersistentTeachersWithGradesToSubmitGroup.getInstance(period, degreeCurricularPlan);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof TeachersWithGradesToSubmitGroup) {
            TeachersWithGradesToSubmitGroup other = (TeachersWithGradesToSubmitGroup) object;
            return Objects.equal(period, other.period) && Objects.equal(degreeCurricularPlan, other.degreeCurricularPlan);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(period, degreeCurricularPlan);
    }
}
