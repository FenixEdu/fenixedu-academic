/*
 * Created on 2004/04/19
 *  
 */
package ServidorAplicacao.Servico.student;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.IStudent;
import Dominio.finalDegreeWork.Group;
import Dominio.finalDegreeWork.IGroup;
import Dominio.finalDegreeWork.IGroupStudent;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentFinalDegreeWork;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz
 *  
 */
public class RemoveStudentFromFinalDegreeWorkStudentGroup implements IService
{

    public RemoveStudentFromFinalDegreeWorkStudentGroup()
    {
        super();
    }

    public boolean run(String username, Integer groupOID, Integer studentToRemoveID) throws ExcepcaoPersistencia, FenixServiceException
    {
        ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
        IPersistentFinalDegreeWork persistentFinalDegreeWork = persistentSupport.getIPersistentFinalDegreeWork();
        IPersistentStudent persistentStudent = persistentSupport.getIPersistentStudent();

        IGroup group = (IGroup) persistentFinalDegreeWork.readByOID(Group.class, groupOID);
        IStudent student = persistentStudent.readByUsername(username);
        if (group == null
                || student == null
                || group.getGroupStudents() == null
                || student.getIdInternal().equals(studentToRemoveID))
        {
            return false;
        } else
        {
            if (!group.getGroupProposals().isEmpty())
            {
                throw new GroupProposalCandidaciesExistException();
            }

            persistentFinalDegreeWork.simpleLockWrite(group);
            CollectionUtils.filter(group.getGroupStudents(), new PREDICATE_FILTER_STUDENT_ID(studentToRemoveID));
            return true;
        }
    }

    private class PREDICATE_FILTER_STUDENT_ID implements Predicate
    {
        Integer studentID;

        public boolean evaluate(Object arg0)
        {
            IGroupStudent groupStudent = (IGroupStudent) arg0;
            if (groupStudent != null && groupStudent.getStudent() != null && studentID != null
                    && !studentID.equals(groupStudent.getStudent().getIdInternal()))
            {
                return true;
            } else
            {
                return false;
            }
        }

        public PREDICATE_FILTER_STUDENT_ID(Integer studentID)
        {
            super();
            this.studentID = studentID;
        }
    };

    public class GroupProposalCandidaciesExistException extends FenixServiceException
    {

        public GroupProposalCandidaciesExistException()
        {
            super();
        }

        public GroupProposalCandidaciesExistException(int errorType)
        {
            super(errorType);
        }

        public GroupProposalCandidaciesExistException(String s)
        {
            super(s);
        }

        public GroupProposalCandidaciesExistException(Throwable cause)
        {
            super(cause);
        }

        public GroupProposalCandidaciesExistException(String message, Throwable cause)
        {
            super(message, cause);
        }
    }

}