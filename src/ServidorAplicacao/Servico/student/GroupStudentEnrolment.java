/*
 * Created on 27/Ago/2003
 *
 */

package ServidorAplicacao.Servico.student;

import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.IAttends;
import Dominio.IGroupProperties;
import Dominio.IStudent;
import Dominio.IStudentGroup;
import Dominio.IStudentGroupAttend;
import Dominio.StudentGroup;
import Dominio.StudentGroupAttend;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidSituationServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorAplicacao.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import ServidorAplicacao.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import ServidorAplicacao.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudentGroup;
import ServidorPersistente.IPersistentStudentGroupAttend;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author asnr and scpo
 *  
 */
public class GroupStudentEnrolment implements IService {

    /**
     * The actor of this class.
     */
    public GroupStudentEnrolment() {
    }

    public Boolean run(Integer studentGroupCode, String username)
            throws FenixServiceException {

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentStudentGroupAttend persistentStudentGroupAttend = sp
                    .getIPersistentStudentGroupAttend();
            IPersistentStudentGroup persistentStudentGroup = sp
                    .getIPersistentStudentGroup();
           
            IStudentGroup studentGroup = (IStudentGroup) persistentStudentGroup
                    .readByOID(StudentGroup.class, studentGroupCode);
            IStudent student = sp.getIPersistentStudent().readByUsername(
                    username);

            if (studentGroup == null) {
                throw new FenixServiceException();
            }
            
            IGroupProperties groupProperties = studentGroup.getAttendsSet().getGroupProperties();
            
            IAttends attend = groupProperties.getAttendsSet().getStudentAttend(student);
 
            if(attend == null){
            	throw new NotAuthorizedException();
            }
            
            IStudentGroupAttend studentGroupAttend = persistentStudentGroupAttend
                    .readBy(studentGroup, attend);

            if (studentGroupAttend != null)
                throw new InvalidSituationServiceException();

            
            IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory
			.getInstance();
            IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory
			.getGroupEnrolmentStrategyInstance(groupProperties);
        	
            boolean result = strategy.checkPossibleToEnrolInExistingGroup(
                    groupProperties, studentGroup, studentGroup.getShift());
            if (!result) {
                throw new InvalidArgumentsServiceException();
            }
            IStudentGroupAttend newStudentGroupAttend = new StudentGroupAttend(
                    studentGroup, attend);
            
            List allStudentGroup = groupProperties.getAttendsSet().getStudentGroups();
            
            Iterator iter = allStudentGroup.iterator();
            IStudentGroup group = null;
            IStudentGroupAttend existingStudentAttend = null;
            while (iter.hasNext()) {
                group = (IStudentGroup) iter.next();
                existingStudentAttend = persistentStudentGroupAttend.readBy(
                        group, attend);
                if (existingStudentAttend != null) {
                    throw new InvalidSituationServiceException();
                }
            }
            persistentStudentGroupAttend.simpleLockWrite(newStudentGroupAttend);

        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }
        return new Boolean(true);
    }
}