/*
 * Created on 8/Ago/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.GroupProperties;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IAttendsSet;
import net.sourceforge.fenixedu.domain.IGroupProperties;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.domain.IStudentGroupAttend;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.StudentGroupAttend;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGroupProperties;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentGroup;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentGroupAttend;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

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

            ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
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
    	IPersistentStudentGroupAttend persistentStudentGroupAttend = null;
    	try {
    		ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();

    		persistentStudent = persistentSupport.getIPersistentStudent();
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

    				IAttends attend = groupProperties.getAttendsSet().getStudentAttend(student);
    				
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
        
        try {

            ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();

            persistentGroupProperites = persistentSupport
                    .getIPersistentGroupProperties();
            IGroupProperties groupProperties = (IGroupProperties) persistentGroupProperites
                    .readByOID(GroupProperties.class, groupPropertiesCode);

            if(groupProperties == null){
            	throw new NotAuthorizedException();
            }
            
            checkIfStudentGroupExists(groupNumber, groupProperties);
            checkStudentsForCreation(studentCodes,groupProperties);

            persistentStudentGroup = persistentSupport
                    .getIPersistentStudentGroup();
            IAttendsSet attendsSet = groupProperties.getAttendsSet();
            IStudentGroup newStudentGroup = null;
            
            if(shiftCode != null){
            	persistentShift = persistentSupport.getITurnoPersistente();
                IShift shift = (IShift) persistentShift.readByOID(Shift.class,
                        shiftCode);
            newStudentGroup = new StudentGroup(groupNumber,attendsSet, shift);
            }else{
                newStudentGroup = new StudentGroup(groupNumber,attendsSet);
            }
            persistentStudentGroup.simpleLockWrite(newStudentGroup);
            attendsSet.addStudentGroup(newStudentGroup);
            persistentStudent = persistentSupport.getIPersistentStudent();
            persistentStudentGroupAttend = persistentSupport
                    .getIPersistentStudentGroupAttend();

            Iterator iter = studentCodes.iterator();

            while (iter.hasNext()) {

                IStudent student = persistentStudent
                        .readByUsername((String) iter.next());
                IAttends attend = groupProperties.getAttendsSet().getStudentAttend(student);
                
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