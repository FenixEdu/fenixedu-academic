/*
 * Created on 23/Ago/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidChangeServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.domain.GroupProperties;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IAttendsSet;
import net.sourceforge.fenixedu.domain.IGroupProperties;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.domain.IStudentGroupAttend;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGroupProperties;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentGroup;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentGroupAttend;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

/**
 * @author asnr and scpo
 * @author joaosa and rmalo 31/Ago/2004
 */

public class DeleteStudentGroupMembers implements IServico {

    private static DeleteStudentGroupMembers service = new DeleteStudentGroupMembers();

    /**
     * The singleton access method of this class.
     */
    public static DeleteStudentGroupMembers getService() {
        return service;
    }

    /**
     * The constructor of this class.
     */
    private DeleteStudentGroupMembers() {
    }

    /**
     * The name of the service
     */
    public final String getNome() {
        return "DeleteStudentGroupMembers";
    }
    

    /**
     * Executes the service.
     */

    public boolean run(Integer executionCourseCode, Integer studentGroupCode, Integer groupPropertiesCode,
            List studentUsernames) throws FenixServiceException {


        IPersistentStudentGroup persistentStudentGroup = null;
        IPersistentStudent persistentStudent = null;
        IPersistentStudentGroupAttend persistentStudentGroupAttend = null;
        IPersistentGroupProperties persistentGroupProperties = null;

        try {

            ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();

            persistentStudent = persistentSupport.getIPersistentStudent();
            persistentStudentGroup = persistentSupport
                    .getIPersistentStudentGroup();
            persistentStudentGroupAttend = persistentSupport
                    .getIPersistentStudentGroupAttend();
            persistentGroupProperties = persistentSupport
				.getIPersistentGroupProperties();
            
            IGroupProperties groupProperties = (IGroupProperties) persistentGroupProperties
                    .readByOID(GroupProperties.class, groupPropertiesCode);

           if(groupProperties==null){
           	throw new  InvalidChangeServiceException();
           }
                       
            IStudentGroup studentGroup = (IStudentGroup) persistentStudentGroup
                    .readByOID(StudentGroup.class, studentGroupCode);

            if (studentGroup == null) {
                throw new ExistingServiceException();
            }
            
            
            IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory
            .getInstance();
            IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory
            .getGroupEnrolmentStrategyInstance(groupProperties);
            
            if(!strategy.checkStudentsUserNamesInAttendsSet(studentUsernames,groupProperties)){
                throw new InvalidArgumentsServiceException();
            }
            
           if(!checkStudentsInStudentGroup( studentUsernames, studentGroup)){
            throw new InvalidSituationServiceException();
           }
            
            Iterator iterator = studentUsernames.iterator();

            IAttendsSet attendsSet = studentGroup.getAttendsSet();
            while (iterator.hasNext()) {

                IStudent student = persistentStudent.readByUsername(iterator
                        .next().toString());
                IAttends attend = attendsSet.getStudentAttend(student);
                
                 IStudentGroupAttend oldStudentGroupAttend = persistentStudentGroupAttend
                        .readBy(studentGroup, attend);
                 persistentStudentGroupAttend.delete(oldStudentGroupAttend);
            }

        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia.getMessage());
        }
        return true;
    }
    
    
    
    private boolean checkStudentsInStudentGroup(List studentsUserNames, IStudentGroup studentGroup) throws ExcepcaoPersistencia{
    	IPersistentStudentGroupAttend persistentStudentGroupAttend = null;
    	IPersistentStudent persistentStudent = null;
    	ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
    	persistentStudent = persistentSupport.getIPersistentStudent();
    	persistentStudentGroupAttend = persistentSupport
                    .getIPersistentStudentGroupAttend();
            
    	Iterator iterator = studentsUserNames.iterator();
    	IAttendsSet attendsSet = studentGroup.getAttendsSet();
    	while (iterator.hasNext()) {

    		IStudent student = persistentStudent.readByUsername(iterator
    				.next().toString());
    		IAttends attend = attendsSet.getStudentAttend(student);
    		IStudentGroupAttend oldStudentGroupAttend = persistentStudentGroupAttend
                    .readBy(studentGroup, attend);
    		if (oldStudentGroupAttend == null) {
    			return false;
    		}
    	}
    	return true;
    	 
    }
}