package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.fenixedu.bennu.core.annotation.GroupArgument;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.joda.time.DateTime;

import com.google.common.base.Objects;

@GroupOperator("studentSharingDegreeOfExecutionCourse")
public class StudentSharingDegreeOfExecutionCourseGroup extends FenixGroup {
    private static final long serialVersionUID = -5847181312665627041L;

    @GroupArgument
    private ExecutionCourse executionCourse;

    private StudentSharingDegreeOfExecutionCourseGroup() {
        super();
    }

    private StudentSharingDegreeOfExecutionCourseGroup(ExecutionCourse executionCourse) {
        this();
        this.executionCourse = executionCourse;
    }

    public static StudentSharingDegreeOfExecutionCourseGroup get(ExecutionCourse executionCourse) {
        return new StudentSharingDegreeOfExecutionCourseGroup(executionCourse);
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        return new String[] { executionCourse.getNome() };
    }

    @Override
    public Set<User> getMembers() {
        Set<User> users = new HashSet<>();

        Set<Degree> degrees = new HashSet<>();
        for (CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
            degrees.add(curricularCourse.getDegree());
        }
        // students of any degree containing the given execution course 
        for (Degree degree : degrees) {
            for (Registration registration : degree.getActiveRegistrations()) {
                User user = registration.getPerson().getUser();
                if (user != null) {
                    users.add(user);
                }
            }
        }
        // students attending the given execution course (most will be in the previous case but some may not)
        for (Attends attends : executionCourse.getAttendsSet()) {
            User user = attends.getRegistration().getPerson().getUser();
            if (user != null) {
                users.add(user);
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
        if (user == null) {
            return false;
        }
        if (user.getPerson().getStudent() != null) {
            Set<Degree> degrees = new HashSet<>();
            for (CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
                degrees.add(curricularCourse.getDegree());
            }
            for (Registration registration : user.getPerson().getStudent().getRegistrationsSet()) {
                // students of any degree containing the given execution course 
                if (degrees.contains(registration.getDegree())) {
                    return true;
                }
                // students attending the given execution course (most will be in the previous case but some may not)
                if (registration.getAttendingExecutionCoursesFor().contains(executionCourse)) {
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

    @Override
    public PersistentGroup toPersistentGroup() {
        return PersistentStudentSharingDegreeOfExecutionCourseGroup.getInstance(executionCourse);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof StudentSharingDegreeOfExecutionCourseGroup) {
            return Objects.equal(executionCourse, ((StudentSharingDegreeOfExecutionCourseGroup) object).executionCourse);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(executionCourse);
    }
}
