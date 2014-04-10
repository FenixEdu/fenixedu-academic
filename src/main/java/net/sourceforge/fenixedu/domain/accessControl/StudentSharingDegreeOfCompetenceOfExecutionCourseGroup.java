package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.fenixedu.bennu.core.annotation.GroupArgument;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.joda.time.DateTime;

import com.google.common.base.Objects;

@GroupOperator("studentSharingDegreeOfCompetenceOfExecutionCourse")
public class StudentSharingDegreeOfCompetenceOfExecutionCourseGroup extends FenixGroup {
    private static final long serialVersionUID = -16490512164795235L;

    @GroupArgument
    private ExecutionCourse executionCourse;

    private StudentSharingDegreeOfCompetenceOfExecutionCourseGroup() {
        super();
    }

    private StudentSharingDegreeOfCompetenceOfExecutionCourseGroup(ExecutionCourse executionCourse) {
        this();
        this.executionCourse = executionCourse;
    }

    public static StudentSharingDegreeOfCompetenceOfExecutionCourseGroup get(ExecutionCourse executionCourse) {
        return new StudentSharingDegreeOfCompetenceOfExecutionCourseGroup(executionCourse);
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        return new String[] { executionCourse.getNome() };
    }

    @Override
    public Set<User> getMembers() {
        Set<User> users = new HashSet<User>();

        Set<Degree> degrees = new HashSet<>();
        for (CompetenceCourse competenceCourse : executionCourse.getCompetenceCourses()) {
            for (CurricularCourse curricularCourse : competenceCourse.getAssociatedCurricularCoursesSet()) {
                degrees.add(curricularCourse.getDegree());
            }
        }
        // students of any degree sharing the same competence of the given execution course
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
            final Set<CompetenceCourse> competenceCourses = executionCourse.getCompetenceCourses();
            for (Registration registration : user.getPerson().getStudent().getRegistrationsSet()) {
                // students of any degree sharing the same competence of the given execution course 
                for (StudentCurricularPlan studentCurricularPlan : registration.getStudentCurricularPlansSet()) {
                    for (Enrolment enrolment : studentCurricularPlan.getEnrolmentsSet()) {
                        CompetenceCourse competenceCourse = enrolment.getCurricularCourse().getCompetenceCourse();
                        if (competenceCourses.contains(competenceCourse)) {
                            return true;
                        }
                    }
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
        return PersistentStudentSharingDegreeOfCompetenceOfExecutionCourseGroup.getInstance(executionCourse);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof StudentSharingDegreeOfCompetenceOfExecutionCourseGroup) {
            return Objects.equal(executionCourse,
                    ((StudentSharingDegreeOfCompetenceOfExecutionCourseGroup) object).executionCourse);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(executionCourse);
    }
}
