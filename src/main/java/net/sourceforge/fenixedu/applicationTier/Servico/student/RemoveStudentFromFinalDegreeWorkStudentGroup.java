/*
 * Created on 2004/04/19
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.finalDegreeWork.FinalDegreeWorkGroup;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupStudent;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.apache.commons.collections.Predicate;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Luis Cruz
 * 
 */
public class RemoveStudentFromFinalDegreeWorkStudentGroup {

    public RemoveStudentFromFinalDegreeWorkStudentGroup() {
        super();
    }

    @Checked("RolePredicates.STUDENT_PREDICATE")
    @Service
    public static Boolean run(String username, String groupOID, String studentToRemoveID) throws FenixServiceException {
        FinalDegreeWorkGroup group = FenixFramework.getDomainObject(groupOID);
        Registration registration = Registration.readByUsername(username);

        if (group == null || registration == null || group.getGroupStudents() == null
                || registration.getExternalId().equals(studentToRemoveID)) {
            return false;
        }

        if (!group.getGroupProposals().isEmpty()) {
            throw new GroupProposalCandidaciesExistException();
        }

        PREDICATE_FILTER_STUDENT_ID predicate = new PREDICATE_FILTER_STUDENT_ID(studentToRemoveID);
        for (GroupStudent groupStudent : group.getGroupStudents()) {
            if (!predicate.evaluate(groupStudent)) {
                groupStudent.delete();
            }
        }
        return true;
    }

    private static class PREDICATE_FILTER_STUDENT_ID implements Predicate {
        String studentID;

        @Override
        public boolean evaluate(Object arg0) {
            GroupStudent groupStudent = (GroupStudent) arg0;
            if (groupStudent != null && groupStudent.getRegistration() != null && studentID != null
                    && !studentID.equals(groupStudent.getRegistration().getExternalId())) {
                return true;
            }
            return false;

        }

        public PREDICATE_FILTER_STUDENT_ID(String studentID) {
            super();
            this.studentID = studentID;
        }
    }

    public static class GroupProposalCandidaciesExistException extends FenixServiceException {

        public GroupProposalCandidaciesExistException() {
            super();
        }

        public GroupProposalCandidaciesExistException(int errorType) {
            super(errorType);
        }

        public GroupProposalCandidaciesExistException(String s) {
            super(s);
        }

        public GroupProposalCandidaciesExistException(Throwable cause) {
            super(cause);
        }

        public GroupProposalCandidaciesExistException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}