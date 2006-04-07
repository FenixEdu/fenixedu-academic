/*
 * Created on 2004/04/19
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Group;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupStudent;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author Luis Cruz
 */
public class AddStudentToFinalDegreeWorkStudentGroup extends Service {

    public boolean run(Integer groupOID, String username) throws ExcepcaoPersistencia,
            FenixServiceException {
        Group group = rootDomainObject.readGroupByOID(groupOID);
        Student student = Student.readByUsername(username);
        if (group == null
                || student == null
                || group.getGroupStudents() == null
                || CollectionUtils.find(group.getGroupStudents(),
                        new PREDICATE_FIND_GROUP_STUDENT_BY_STUDENT(student)) != null) {
            return false;
        }
        Scheduleing scheduleing = group.getExecutionDegree().getScheduling();

        if (scheduleing == null || scheduleing.getMaximumNumberOfStudents() == null) {
            throw new MaximumNumberOfStudentsUndefinedException();
        } else if (scheduleing.getMinimumNumberOfCompletedCourses() == null) {
            throw new MinimumNumberOfCompletedCoursesUndefinedException();
        } else if (scheduleing.getMaximumNumberOfStudents().intValue() <= group.getGroupStudents()
                .size()) {
            throw new MaximumNumberOfStudentsReachedException(scheduleing.getMaximumNumberOfStudents()
                    .toString());
        } else {
            int numberOfCompletedCourses = student
                    .countCompletedCoursesForActiveUndergraduateCurricularPlan();

            if (numberOfCompletedCourses < scheduleing.getMinimumNumberOfCompletedCourses().intValue()) {
                throw new MinimumNumberOfCompletedCoursesNotReachedException(scheduleing
                        .getMinimumNumberOfCompletedCourses().toString());
            }
        }

        GroupStudent groupStudent = DomainFactory.makeGroupStudent();
        groupStudent.setStudent(student);
        groupStudent.setFinalDegreeDegreeWorkGroup(group);
        return true;
    }

    public class MaximumNumberOfStudentsUndefinedException extends FenixServiceException {
        public MaximumNumberOfStudentsUndefinedException() {
            super();
        }

        public MaximumNumberOfStudentsUndefinedException(int errorType) {
            super(errorType);
        }

        public MaximumNumberOfStudentsUndefinedException(String s) {
            super(s);
        }

        public MaximumNumberOfStudentsUndefinedException(Throwable cause) {
            super(cause);
        }

        public MaximumNumberOfStudentsUndefinedException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public class MaximumNumberOfStudentsReachedException extends FenixServiceException {
        public MaximumNumberOfStudentsReachedException() {
            super();
        }

        public MaximumNumberOfStudentsReachedException(int errorType) {
            super(errorType);
        }

        public MaximumNumberOfStudentsReachedException(String s) {
            super(s);
        }

        public MaximumNumberOfStudentsReachedException(Throwable cause) {
            super(cause);
        }

        public MaximumNumberOfStudentsReachedException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public class MinimumNumberOfCompletedCoursesUndefinedException extends FenixServiceException {
        public MinimumNumberOfCompletedCoursesUndefinedException() {
            super();
        }

        public MinimumNumberOfCompletedCoursesUndefinedException(int errorType) {
            super(errorType);
        }

        public MinimumNumberOfCompletedCoursesUndefinedException(String s) {
            super(s);
        }

        public MinimumNumberOfCompletedCoursesUndefinedException(Throwable cause) {
            super(cause);
        }

        public MinimumNumberOfCompletedCoursesUndefinedException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public class MinimumNumberOfCompletedCoursesNotReachedException extends FenixServiceException {
        public MinimumNumberOfCompletedCoursesNotReachedException() {
            super();
        }

        public MinimumNumberOfCompletedCoursesNotReachedException(int errorType) {
            super(errorType);
        }

        public MinimumNumberOfCompletedCoursesNotReachedException(String s) {
            super(s);
        }

        public MinimumNumberOfCompletedCoursesNotReachedException(Throwable cause) {
            super(cause);
        }

        public MinimumNumberOfCompletedCoursesNotReachedException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    private class PREDICATE_FIND_GROUP_STUDENT_BY_STUDENT implements Predicate {
        Student student = null;

        public boolean evaluate(Object arg0) {
            GroupStudent groupStudent = (GroupStudent) arg0;
            return student.getIdInternal().equals(groupStudent.getStudent().getIdInternal());
        }

        public PREDICATE_FIND_GROUP_STUDENT_BY_STUDENT(Student student) {
            super();
            this.student = student;
        }
    }

}
