/*
 * Created on 07/Set/2003
 *
 */
package ServidorAplicacao.Servico.student;

import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.GroupProperties;
import Dominio.IGroupProperties;
import Dominio.IStudent;
import Dominio.IStudentGroup;
import Dominio.IStudentGroupAttend;
import Dominio.ITurno;
import Dominio.StudentGroup;
import Dominio.Turno;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidChangeServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidSituationServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidStudentNumberServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorAplicacao.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import ServidorAplicacao.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import ServidorAplicacao.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentGroupProperties;
import ServidorPersistente.IPersistentStudentGroup;
import ServidorPersistente.IPersistentStudentGroupAttend;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author asnr and scpo
 *  
 */

public class EditGroupShift implements IService {

    /**
     * The constructor of this class.
     */
    public EditGroupShift() {
    }

    /**
     * Executes the service.
     */


    public boolean run(Integer studentGroupCode, Integer groupPropertiesCode,Integer newShiftCode,
            String username) throws FenixServiceException {

        ITurnoPersistente persistentShift = null;
        IPersistentStudentGroup persistentStudentGroup = null;
        IPersistentGroupProperties persistentGroupProperties = null;
        
        try {
            ISuportePersistente persistentSupport = SuportePersistenteOJB
                    .getInstance();
            IPersistentStudentGroupAttend persistentStudentGroupAttend = persistentSupport
                    .getIPersistentStudentGroupAttend();
            
            persistentGroupProperties = persistentSupport.getIPersistentGroupProperties();
            
            IGroupProperties groupProperties = (IGroupProperties)persistentGroupProperties.readByOID(
            		GroupProperties.class, groupPropertiesCode);
            
            if(groupProperties == null){
            	throw new ExistingServiceException();
            }

            persistentStudentGroup = persistentSupport
                    .getIPersistentStudentGroup();
            IStudentGroup studentGroup = (IStudentGroup) persistentStudentGroup
                    .readByOID(StudentGroup.class, studentGroupCode);

            if (studentGroup == null)
                throw new InvalidArgumentsServiceException();
            
            persistentShift = persistentSupport.getITurnoPersistente();
            ITurno shift = (ITurno) persistentShift.readByOID(Turno.class,
                    newShiftCode);
            
            if(groupProperties.getShiftType() == null || 
             	   (groupProperties.getShiftType().getTipo().intValue() != shift.getTipo().getTipo().intValue())){
             	throw new InvalidStudentNumberServiceException();
             }
            
            IStudent student = persistentSupport.getIPersistentStudent()
            .readByUsername(username);
            
            IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory
			.getInstance();
            IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory
			.getGroupEnrolmentStrategyInstance(groupProperties);
    
            if(!strategy.checkStudentInAttendsSet(groupProperties,username)){
            	throw new NotAuthorizedException();
            }
            
            if(!checkStudentInStudentGroup(student,studentGroup)){
                throw new InvalidSituationServiceException();
            }
            
            boolean result = strategy.checkNumberOfGroups(groupProperties,shift);
            if (!result) {
                throw new InvalidChangeServiceException();
            }
            persistentStudentGroup.simpleLockWrite(studentGroup);
            studentGroup.setShift(shift);
            
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia.getMessage());
        }

        return true;
    }
    
    private boolean checkStudentInStudentGroup(IStudent student,IStudentGroup studentGroup)throws FenixServiceException{
    	boolean found = false;
    	try {
    	ISuportePersistente persistentSupport = SuportePersistenteOJB
		.getInstance();
    	IPersistentStudentGroupAttend persistentStudentGroupAttend = persistentSupport
		.getIPersistentStudentGroupAttend();
    	List studentGroupAttends = persistentStudentGroupAttend.readAllByStudentGroup(studentGroup);
    	IStudentGroupAttend studentGroupAttend = null;
    	Iterator iterStudentGroupAttends = studentGroupAttends.iterator();
    	while(iterStudentGroupAttends.hasNext() && !found){
    		studentGroupAttend = ((IStudentGroupAttend)iterStudentGroupAttends.next());
    		if(studentGroupAttend.getAttend().getAluno().equals(student)){
    			found = true;
    		}
    	}
    	} catch (ExcepcaoPersistencia excepcaoPersistencia) {
    		throw new FenixServiceException(excepcaoPersistencia.getMessage());
    	}
    	return found;
    }
    
    
}