/*
 * Created on 28/Ago/2003
 *
 */
package ServidorAplicacao.Servico.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.GroupProperties;
import Dominio.IFrequenta;
import Dominio.IGroupProperties;
import Dominio.IStudent;
import Dominio.IStudentGroup;
import Dominio.IStudentGroupAttend;
import Dominio.ITurno;
import Dominio.Student;
import Dominio.StudentGroup;
import Dominio.StudentGroupAttend;
import Dominio.Turno;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidSituationServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidStudentNumberServiceException;
import ServidorAplicacao.Servico.exceptions.NoChangeMadeServiceException;
import ServidorAplicacao.Servico.exceptions.NonValidChangeServiceException;
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

            ITurno shift = null;
            
            Integer result= null;
            if(strategy.checkHasShift(groupProperties)){
           	 shift = (ITurno) sp.getITurnoPersistente().readByOID(
                       Turno.class, shiftCode);
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

            IFrequenta userAttend = groupProperties.getAttendsSet().getStudentAttend(userStudent);
           
            checkStudentCodesAndUserAttendInStudentGroup(studentsList,groupProperties,userAttend);

            
            newStudentGroup = new StudentGroup(groupNumber, groupProperties.getAttendsSet(),
                    shift);
            
            persistentStudentGroup.simpleLockWrite(newStudentGroup);
            
            groupProperties.getAttendsSet().addStudentGroup(newStudentGroup);
            
            
            Iterator iter = studentsList.iterator();

            while (iter.hasNext()) {

                IStudent student = (IStudent)iter.next();

                IFrequenta attend = groupProperties.getAttendsSet().getStudentAttend(student);

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

    
    private void checkStudentCodesAndUserAttendInStudentGroup (List studentsList, IGroupProperties groupProperties, IFrequenta userAttend) 
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