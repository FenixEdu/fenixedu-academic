/*
 * Created on 28/Ago/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.GroupProperties;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IGroupProperties;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.domain.IStudentGroupAttend;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.StudentGroupAttend;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidStudentNumberServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NoChangeMadeServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonValidChangeServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentGroup;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentGroupAttend;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author asnr and scpo
 *  
 */
public class GroupEnrolment implements IService {

    /**
     * The actor of this class.
     */
    public GroupEnrolment() {
    }

    public boolean run(Integer groupPropertiesCode, Integer shiftCode,
            Integer groupNumber, List studentCodes, String username)
            throws FenixServiceException {

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentStudentGroupAttend persistentStudentGroupAttend = sp
            .getIPersistentStudentGroupAttend();
            IPersistentStudentGroup persistentStudentGroup = sp
            .getIPersistentStudentGroup();
            IPersistentStudent persistentStudent = sp.getIPersistentStudent();

            IGroupProperties groupProperties = (IGroupProperties) sp
                    .getIPersistentGroupProperties().readByOID(
                            GroupProperties.class, groupPropertiesCode);

            if(groupProperties == null){
            	throw new NonExistingServiceException();
            }
            
            IStudent userStudent = sp.getIPersistentStudent().readByUsername(
                    username);
       
            List studentsList = new ArrayList();
            Iterator iterator = studentCodes.iterator();
    		while (iterator.hasNext()) {
    			IStudent student = (IStudent) persistentStudent.readByOID(
    					Student.class, (Integer) iterator.next());
    			studentsList.add(student);
    		}
            
            
    		IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory
			.getInstance();
    		IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory
			.getGroupEnrolmentStrategyInstance(groupProperties);
 
            if(!strategy.checkStudentInAttendsSet(groupProperties,userStudent.getPerson().getUsername()))
            {
            	throw new NoChangeMadeServiceException();
            }

            IShift shift = null;
            
            Integer result= null;
            if(shiftCode != null){
           	 shift = (IShift) sp.getITurnoPersistente().readByOID(
                       Shift.class, shiftCode);
            }
             result = strategy.enrolmentPolicyNewGroup(groupProperties,
                    studentCodes.size() + 1, shift);
            
            if (result.equals(new Integer(-1))) {
                throw new InvalidArgumentsServiceException();
            }
            if (result.equals(new Integer(-2))) {
                throw new NonValidChangeServiceException();
            }
            if (result.equals(new Integer(-3))) {
                throw new NotAuthorizedException();
            }
            

            IStudentGroup newStudentGroup = persistentStudentGroup
                    .readStudentGroupByAttendsSetAndGroupNumber(
                            groupProperties.getAttendsSet(), groupNumber);

            if (newStudentGroup != null) {
                throw new FenixServiceException();
            }
            
            if(!strategy.checkStudentsInAttendsSet(studentCodes,groupProperties)){
            	throw new InvalidStudentNumberServiceException();
            }

            IAttends userAttend = groupProperties.getAttendsSet().getStudentAttend(userStudent);
           
            checkStudentCodesAndUserAttendInStudentGroup(studentsList,groupProperties,userAttend);

            
            newStudentGroup = new StudentGroup(groupNumber, groupProperties.getAttendsSet(),
                    shift);
            
            persistentStudentGroup.simpleLockWrite(newStudentGroup);
            
            groupProperties.getAttendsSet().addStudentGroup(newStudentGroup);
            
            
            Iterator iter = studentsList.iterator();

            while (iter.hasNext()) {

                IStudent student = (IStudent)iter.next();

                IAttends attend = groupProperties.getAttendsSet().getStudentAttend(student);

                IStudentGroupAttend notExistingSGAttend = new StudentGroupAttend(
                        newStudentGroup, attend);

                persistentStudentGroupAttend
                        .simpleLockWrite(notExistingSGAttend);
            }
            IStudentGroupAttend notExistingUserSGAttend = new StudentGroupAttend(
                    newStudentGroup, userAttend);

            persistentStudentGroupAttend
                    .simpleLockWrite(notExistingUserSGAttend);

        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }
        return true;
    }

    
    private void checkStudentCodesAndUserAttendInStudentGroup (List studentsList, IGroupProperties groupProperties, IAttends userAttend) 
    throws FenixServiceException {
    	
    	try {
    		ISuportePersistente sp = SuportePersistenteOJB.getInstance();
    		IPersistentStudent persistentStudent = sp.getIPersistentStudent();
        
    		IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory
			.getInstance();
    		IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory
			.getGroupEnrolmentStrategyInstance(groupProperties);
            
    		Iterator iterator = studentsList.iterator();
    		while (iterator.hasNext()) {
    			IStudent student = (IStudent) iterator.next();
    			if(strategy.checkAlreadyEnroled(groupProperties,student.getPerson().getUsername())){
    				throw new ExistingServiceException();
    			}
    		}
    		if(strategy.checkAlreadyEnroled(groupProperties,userAttend.getAluno().getPerson().getUsername())){
    			throw new InvalidSituationServiceException();
    		}
    	} catch (ExcepcaoPersistencia ex) {
    		ex.printStackTrace();
    	}
    }
	}