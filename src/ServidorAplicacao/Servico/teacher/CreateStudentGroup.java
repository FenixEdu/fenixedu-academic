/*
 * Created on 8/Ago/2003
 */

package ServidorAplicacao.Servico.teacher;

import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.GroupProperties;
import Dominio.IAttendsSet;
import Dominio.IFrequenta;
import Dominio.IGroupProperties;
import Dominio.IStudent;
import Dominio.IStudentGroup;
import Dominio.IStudentGroupAttend;
import Dominio.ITurno;
import Dominio.StudentGroup;
import Dominio.StudentGroupAttend;
import Dominio.Turno;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidSituationServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorAplicacao.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import ServidorAplicacao.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import ServidorAplicacao.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentAttendsSet;
import ServidorPersistente.IPersistentGroupProperties;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPersistentStudentGroup;
import ServidorPersistente.IPersistentStudentGroupAttend;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author ansr & scpo
 */

public class CreateStudentGroup implements IService {

    private IPersistentStudentGroup persistentStudentGroup = null;

    /**
     * The constructor of this class.
     */
    public CreateStudentGroup() {
    }

    private void checkIfStudentGroupExists(Integer groupNumber,
            IGroupProperties groupProperties) throws FenixServiceException {

        IStudentGroup studentGroup = null;

        try {

            ISuportePersistente persistentSupport = SuportePersistenteOJB
                    .getInstance();
            persistentStudentGroup = persistentSupport
                    .getIPersistentStudentGroup();

            studentGroup = persistentStudentGroup
                    .readStudentGroupByAttendsSetAndGroupNumber(
                            groupProperties.getAttendsSet(), groupNumber);
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia.getMessage());
        }

        if (studentGroup != null)
            throw new ExistingServiceException();
    }

    private void checkStudentsForCreation(List studentCodes,IGroupProperties groupProperties) throws FenixServiceException {
    	IPersistentStudent persistentStudent = null;
    	IPersistentStudentGroup persistentStudentGroup = null;
    	IPersistentAttendsSet persistentAttendsSet = null;
    	IFrequentaPersistente persistentAttend = null;
    	IPersistentStudentGroupAttend persistentStudentGroupAttend = null;
    	try {
    		ISuportePersistente persistentSupport = SuportePersistenteOJB
			.getInstance();

    		persistentStudent = persistentSupport.getIPersistentStudent();
    		persistentAttend = persistentSupport.getIFrequentaPersistente();
    		persistentStudentGroupAttend = persistentSupport
			.getIPersistentStudentGroupAttend();
    		
    		List allStudentGroup = groupProperties.getAttendsSet().getStudentGroups();
    		if(allStudentGroup==null)return;
    		
    		Iterator iterGroups = allStudentGroup.iterator();

    		while (iterGroups.hasNext()) {
    			IStudentGroup existingStudentGroup = (IStudentGroup) iterGroups.next();
    			IStudentGroupAttend newStudentGroupAttend = null;
    			Iterator iterator = studentCodes.iterator();

    			while (iterator.hasNext()) {
    				IStudent student = persistentStudent
					.readByUsername((String) iterator.next());

    				IFrequenta attend = groupProperties.getAttendsSet().getStudentAttend(student);
    				
    				if(attend==null){
    					throw new InvalidArgumentsServiceException();
    				}
               
    				newStudentGroupAttend = persistentStudentGroupAttend
					.readBy(existingStudentGroup, attend);

    				if (newStudentGroupAttend != null)
    					throw new InvalidSituationServiceException();
    			}
    			}

    	}
    	catch (ExcepcaoPersistencia excepcaoPersistencia) {
    		throw new FenixServiceException(excepcaoPersistencia.getMessage());
    	}
    }
    
    
    /**
     * Executes the service.
     */

    public boolean run(Integer executionCourseCode, Integer groupNumber,
            Integer groupPropertiesCode, Integer shiftCode, List studentCodes)
            throws FenixServiceException {

    	IPersistentStudentGroupAttend persistentStudentGroupAttend = null;
        IPersistentGroupProperties persistentGroupProperites = null;
        IPersistentStudent persistentStudent = null;
        ITurnoPersistente persistentShift = null;
        IFrequentaPersistente persistentAttend = null;
        IPersistentAttendsSet persistentAttendsSet = null;
        
        try {

            ISuportePersistente persistentSupport = SuportePersistenteOJB
                    .getInstance();

            persistentGroupProperites = persistentSupport
                    .getIPersistentGroupProperties();
            IGroupProperties groupProperties = (IGroupProperties) persistentGroupProperites
                    .readByOID(GroupProperties.class, groupPropertiesCode);

            if(groupProperties == null){
            	throw new NotAuthorizedException();
            }
            
            persistentAttendsSet = persistentSupport.getIPersistentAttendsSet();
            
            checkIfStudentGroupExists(groupNumber, groupProperties);
            checkStudentsForCreation(studentCodes,groupProperties);

            persistentStudentGroup = persistentSupport
                    .getIPersistentStudentGroup();
            IAttendsSet attendsSet = groupProperties.getAttendsSet();
            IStudentGroup newStudentGroup = null;
            
            if(shiftCode != null){
            	persistentShift = persistentSupport.getITurnoPersistente();
                ITurno shift = (ITurno) persistentShift.readByOID(Turno.class,
                        shiftCode);
            newStudentGroup = new StudentGroup(groupNumber,attendsSet, shift);
            }else{
                newStudentGroup = new StudentGroup(groupNumber,attendsSet);
            }
            persistentStudentGroup.simpleLockWrite(newStudentGroup);
            attendsSet.addStudentGroup(newStudentGroup);
            persistentStudent = persistentSupport.getIPersistentStudent();
            persistentAttend = persistentSupport.getIFrequentaPersistente();
            persistentStudentGroupAttend = persistentSupport
                    .getIPersistentStudentGroupAttend();

            List allStudentGroup = groupProperties.getAttendsSet().getStudentGroups();
            
            Iterator iter = studentCodes.iterator();

            while (iter.hasNext()) {

                IStudent student = persistentStudent
                        .readByUsername((String) iter.next());
                IFrequenta attend = groupProperties.getAttendsSet().getStudentAttend(student);
                
                IStudentGroupAttend notExistingSGAttend = new StudentGroupAttend(
                        newStudentGroup, attend);

                persistentStudentGroupAttend
                        .simpleLockWrite(notExistingSGAttend);
                
                
            }

        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia.getMessage());
        }
        return true;

    }
}