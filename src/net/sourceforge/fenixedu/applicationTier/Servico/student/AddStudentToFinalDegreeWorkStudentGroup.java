/*
 * Created on 2004/04/19
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Group;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupStudent;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IGroup;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IGroupStudent;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IScheduleing;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrollment;
import net.sourceforge.fenixedu.persistenceTier.IPersistentFinalDegreeWork;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz
 */
public class AddStudentToFinalDegreeWorkStudentGroup implements IService {

    public AddStudentToFinalDegreeWorkStudentGroup() {
        super();
    }

    public boolean run(Integer groupOID, String username) throws ExcepcaoPersistencia,
            FenixServiceException {
        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentFinalDegreeWork persistentFinalDegreeWork = persistentSupport
                .getIPersistentFinalDegreeWork();
        IPersistentStudent persistentStudent = persistentSupport.getIPersistentStudent();
        //        IStudentCurricularPlanPersistente studentCurricularPlanPersistente =
        // persistentSupport
        //                .getIStudentCurricularPlanPersistente();
        IPersistentEnrollment persistentEnrolment = persistentSupport.getIPersistentEnrolment();

        IGroup group = (IGroup) persistentFinalDegreeWork.readByOID(Group.class, groupOID);
        IStudent student = persistentStudent.readByUsername(username);
        if (group == null
                || student == null
                || group.getGroupStudents() == null
                || CollectionUtils.find(group.getGroupStudents(),
                        new PREDICATE_FIND_GROUP_STUDENT_BY_STUDENT(student)) != null) {
            return false;
        }
        IScheduleing scheduleing = persistentFinalDegreeWork.readFinalDegreeWorkScheduleing(group
                .getExecutionDegree().getIdInternal());

        if (scheduleing == null || scheduleing.getMaximumNumberOfStudents() == null) {
            throw new MaximumNumberOfStudentsUndefinedException();
        } else if (scheduleing.getMinimumNumberOfCompletedCourses() == null) {
            throw new MinimumNumberOfCompletedCoursesUndefinedException();
        } else if (scheduleing.getMaximumNumberOfStudents().intValue() <= group.getGroupStudents()
                .size()) {
            throw new MaximumNumberOfStudentsReachedException(scheduleing.getMaximumNumberOfStudents()
                    .toString());
        } else {
            int numberOfCompletedCourses = persistentEnrolment
                    .countCompletedCoursesForStudentForActiveUndergraduateCurricularPlan(student);

            if (numberOfCompletedCourses < scheduleing.getMinimumNumberOfCompletedCourses().intValue()) {
                throw new MinimumNumberOfCompletedCoursesNotReachedException(scheduleing
                        .getMinimumNumberOfCompletedCourses().toString());
            }
        }

        IGroupStudent groupStudent = new GroupStudent();
        persistentFinalDegreeWork.simpleLockWrite(groupStudent);
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

        IStudent student = null;

        public boolean evaluate(Object arg0) {
            IGroupStudent groupStudent = (IGroupStudent) arg0;
            return student.getIdInternal().equals(groupStudent.getStudent().getIdInternal());
        }

        public PREDICATE_FIND_GROUP_STUDENT_BY_STUDENT(IStudent student) {
            super();
            this.student = student;
        }
    }

}