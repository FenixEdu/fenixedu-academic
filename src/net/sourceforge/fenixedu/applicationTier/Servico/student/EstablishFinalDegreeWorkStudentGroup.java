/*
 * Created on 2004/04/15
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Group;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupStudent;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Luis Cruz
 * 
 */
public class EstablishFinalDegreeWorkStudentGroup extends Service {

    public boolean run(Person person, Integer executionDegreeOID) throws ExcepcaoPersistencia,
            FenixServiceException {
    	Registration student = person.getStudentByType(DegreeType.DEGREE);
    	if (student == null) {
            throw new FenixServiceException("Error reading student to place in final degree work group.");
    	}
        Group group = student.findFinalDegreeWorkGroupForCurrentExecutionYear();
        if (group == null) {
            group = new Group();
                GroupStudent groupStudent = new GroupStudent();
                groupStudent.setStudent(student);
                groupStudent.setFinalDegreeDegreeWorkGroup(group);
        } else {
            if (!group.getGroupProposals().isEmpty()) {
                throw new GroupProposalCandidaciesExistException();
            }
            if (group.getGroupStudents().size() > 1) {
                throw new GroupStudentCandidaciesExistException();
            }
        }

        if (group.getExecutionDegree() == null
                || !group.getExecutionDegree().getIdInternal().equals(executionDegreeOID)) {
            ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeOID);
            if (executionDegree != null) {
                group.setExecutionDegree(executionDegree);
            }
        }

        return true;
    }

    public class GroupStudentCandidaciesExistException extends FenixServiceException {

        public GroupStudentCandidaciesExistException() {
            super();
        }

        public GroupStudentCandidaciesExistException(int errorType) {
            super(errorType);
        }

        public GroupStudentCandidaciesExistException(String s) {
            super(s);
        }

        public GroupStudentCandidaciesExistException(Throwable cause) {
            super(cause);
        }

        public GroupStudentCandidaciesExistException(String message, Throwable cause) {
            super(message, cause);
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