package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IGroupProperties;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.domain.IStudentGroupAttend;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentGroup;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentGroupAttend;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

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
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();

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