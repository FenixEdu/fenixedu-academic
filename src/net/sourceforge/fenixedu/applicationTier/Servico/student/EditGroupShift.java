/*
 * Created on 07/Set/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidChangeServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidStudentNumberServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.domain.GroupProperties;
import net.sourceforge.fenixedu.domain.IGroupProperties;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.domain.IStudentGroupAttend;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGroupProperties;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentGroup;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentGroupAttend;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

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
            ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();

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
            IShift shift = (IShift) persistentShift.readByOID(Shift.class,
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
    	ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
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