package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collections;
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

import org.fenixedu.bennu.core.annotation.CustomGroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.Group;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

@CustomGroupOperator("studentSharingDegreeOfCompetenceOfExecutionCourse")
public class PersistentStudentSharingDegreeOfCompetenceOfExecutionCourseGroup extends
        PersistentStudentSharingDegreeOfCompetenceOfExecutionCourseGroup_Base {
    protected PersistentStudentSharingDegreeOfCompetenceOfExecutionCourseGroup(ExecutionCourse executionCourse) {
        super();
        init(executionCourse);
    }

    @Override
    public Set<User> getMembers() {
        Set<User> users = new HashSet<User>();

        Set<Degree> degrees = new HashSet<>();
        for (CompetenceCourse competenceCourse : getExecutionCourse().getCompetenceCourses()) {
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
        for (Attends attends : getExecutionCourse().getAttendsSet()) {
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
            final Set<CompetenceCourse> competenceCourses = getExecutionCourse().getCompetenceCourses();
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
                if (registration.getAttendingExecutionCoursesFor().contains(getExecutionCourse())) {
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

    public static PersistentStudentSharingDegreeOfCompetenceOfExecutionCourseGroup getInstance(ExecutionCourse executionCourse) {
        PersistentStudentSharingDegreeOfCompetenceOfExecutionCourseGroup instance =
                select(PersistentStudentSharingDegreeOfCompetenceOfExecutionCourseGroup.class, executionCourse);
        return instance != null ? instance : create(executionCourse);
    }

    @Atomic(mode = TxMode.WRITE)
    private static PersistentStudentSharingDegreeOfCompetenceOfExecutionCourseGroup create(ExecutionCourse executionCourse) {
        PersistentStudentSharingDegreeOfCompetenceOfExecutionCourseGroup instance =
                select(PersistentStudentSharingDegreeOfCompetenceOfExecutionCourseGroup.class, executionCourse);
        return instance != null ? instance : new PersistentStudentSharingDegreeOfCompetenceOfExecutionCourseGroup(executionCourse);
    }
}
