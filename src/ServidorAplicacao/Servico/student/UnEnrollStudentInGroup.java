package ServidorAplicacao.Servico.student;

import Dominio.IAttends;
import Dominio.IGroupProperties;
import Dominio.IStudent;
import Dominio.IStudentGroup;
import Dominio.IStudentGroupAttend;
import Dominio.StudentGroup;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidSituationServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorAplicacao.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import ServidorAplicacao.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import ServidorAplicacao.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPersistentStudentGroup;
import ServidorPersistente.IPersistentStudentGroupAttend;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author asnr and scpo
 *  
 */

public class UnEnrollStudentInGroup implements IServico {

    private static UnEnrollStudentInGroup _servico = new UnEnrollStudentInGroup();

    /**
     * The singleton access method of this class.
     */

    public static UnEnrollStudentInGroup getService() {
        return _servico;
    }

    /**
     * The actor of this class.
     */
    private UnEnrollStudentInGroup() {
    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome() {
        return "UnEnrollStudentInGroup";
    }

    public Boolean run(String userName, Integer studentGroupCode)
            throws FenixServiceException {

        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB
                    .getInstance();

            IPersistentStudentGroup persistentStudentGroup = persistentSuport
                    .getIPersistentStudentGroup();
            IPersistentStudentGroupAttend persistentStudentGroupAttend = persistentSuport
                    .getIPersistentStudentGroupAttend();
            IPersistentStudent persistentStudent = persistentSuport
                    .getIPersistentStudent();
            

            IStudentGroup studentGroup = (IStudentGroup) persistentStudentGroup
                    .readByOID(StudentGroup.class, studentGroupCode);

            if (studentGroup == null) {
            	throw new InvalidSituationServiceException();
            }
            
            
            IStudent student = persistentStudent.readByUsername(userName);
            
            IGroupProperties groupProperties = studentGroup.getAttendsSet().getGroupProperties();
            
            IAttends attend = studentGroup.getAttendsSet().getStudentAttend(student);
            
            if(attend == null){
            	throw new NotAuthorizedException();
            }
            
            IStudentGroupAttend studentGroupAttendToDelete = persistentStudentGroupAttend
                    .readBy(studentGroup, attend);

            if (studentGroupAttendToDelete == null){
            	throw new InvalidArgumentsServiceException();
                }

            IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory
			.getInstance();
            IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory
			.getGroupEnrolmentStrategyInstance(groupProperties);

            boolean resultEmpty = strategy.checkIfStudentGroupIsEmpty(
                    studentGroupAttendToDelete, studentGroup);

            persistentStudentGroupAttend.delete(studentGroupAttendToDelete);

            if (resultEmpty) {
                persistentStudentGroup.delete(studentGroup);
                groupProperties.getAttendsSet().removeStudentGroup(studentGroup);
                return new Boolean(false);
            }

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        return new Boolean(true);
    }

}