/*
 * Created on 28/Ago/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student.onlineTests;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

/**
 * @author Susana Fernandes
 */
public class ReadExecutionCoursesByStudentTests {

    @Atomic
    public static Set<ExecutionCourse> run(final Student student, final ExecutionYear executionYear) {
        check(RolePredicates.STUDENT_PREDICATE);
        final Set<ExecutionCourse> executionCourses = new HashSet<ExecutionCourse>();
        for (final Registration registration : student.getRegistrationsSet()) {
            if (registration.isActive()) {
                for (Attends attend : registration.getAssociatedAttendsSet()) {
                    final ExecutionCourse executionCourse = attend.getExecutionCourse();
                    if (executionCourse.getExecutionYear().equals(executionYear)
                            && student.countDistributedTestsByExecutionCourse(executionCourse) != 0) {
                        executionCourses.add(executionCourse);
                    }
                }
            }
        }
        return executionCourses;
    }

}