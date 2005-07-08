/*
 * Created on 2004/04/19
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Group;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupStudent;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IGroup;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IGroupStudent;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentFinalDegreeWork;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz
 *  
 */
public class RemoveStudentFromFinalDegreeWorkStudentGroup implements IService {

    public RemoveStudentFromFinalDegreeWorkStudentGroup() {
        super();
    }

    public boolean run(String username, Integer groupOID, Integer studentToRemoveID)
            throws ExcepcaoPersistencia, FenixServiceException {
        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentFinalDegreeWork persistentFinalDegreeWork = persistentSupport
                .getIPersistentFinalDegreeWork();
        IPersistentStudent persistentStudent = persistentSupport.getIPersistentStudent();

        IGroup group = (IGroup) persistentFinalDegreeWork.readByOID(Group.class, groupOID);
        IStudent student = persistentStudent.readByUsername(username);
        if (group == null || student == null || group.getGroupStudents() == null
                || student.getIdInternal().equals(studentToRemoveID)) {
            return false;
        }
        if (!group.getGroupProposals().isEmpty()) {
            throw new GroupProposalCandidaciesExistException();
        }

        PREDICATE_FILTER_STUDENT_ID predicate = new PREDICATE_FILTER_STUDENT_ID(studentToRemoveID);
        for (int i = 0; i < group.getGroupStudents().size(); i++) {
            IGroupStudent groupStudent = group.getGroupStudents().get(i);
            if (!predicate.evaluate(groupStudent)) {
                persistentFinalDegreeWork.deleteByOID(GroupStudent.class, groupStudent.getIdInternal());
            }
        }
        return true;

    }

    private class PREDICATE_FILTER_STUDENT_ID implements Predicate {
        Integer studentID;

        public boolean evaluate(Object arg0) {
            IGroupStudent groupStudent = (IGroupStudent) arg0;
            if (groupStudent != null && groupStudent.getStudent() != null && studentID != null
                    && !studentID.equals(groupStudent.getStudent().getIdInternal())) {
                return true;
            }
            return false;

        }

        public PREDICATE_FILTER_STUDENT_ID(Integer studentID) {
            super();
            this.studentID = studentID;
        }
    }

    public class GroupProposalCandidaciesExistException extends FenixServiceException {

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