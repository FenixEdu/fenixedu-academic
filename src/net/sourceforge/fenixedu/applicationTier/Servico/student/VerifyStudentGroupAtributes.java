/*
 * Created on 29/Jul/2003
 *
 */

package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.domain.GroupProperties;
import net.sourceforge.fenixedu.domain.IGroupProperties;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidChangeServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

/**
 * @author asnr and scpo
 *  
 */

public class VerifyStudentGroupAtributes implements IServico {

    private static VerifyStudentGroupAtributes service = new VerifyStudentGroupAtributes();

    /**
     * The singleton access method of this class.
     */
    public static VerifyStudentGroupAtributes getService() {
        return service;
    }

    /**
     * The constructor of this class.
     */
    private VerifyStudentGroupAtributes() {
    }

    /**
     * The name of the service
     */
    public final String getNome() {
        return "VerifyStudentGroupAtributes";
    }

    private boolean checkGroupStudentEnrolment(Integer studentGroupCode,
            String username) throws FenixServiceException {

        boolean result = false;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IStudentGroup studentGroup = (IStudentGroup) sp
                    .getIPersistentStudentGroup().readByOID(StudentGroup.class,
                            studentGroupCode);
            
            if (studentGroup == null) {
                throw new FenixServiceException();
            }
            
            IGroupProperties groupProperties = studentGroup.getAttendsSet().getGroupProperties();
           
            IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory
			.getInstance();
            IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory
			.getGroupEnrolmentStrategyInstance(groupProperties);

            
            if(!strategy.checkStudentInAttendsSet(groupProperties,username)){
        		throw new NotAuthorizedException();
        	}
            
            result = strategy.checkAlreadyEnroled(groupProperties, username);
            if (result)
                throw new InvalidSituationServiceException();

            result = strategy.checkPossibleToEnrolInExistingGroup(
                    groupProperties, studentGroup, studentGroup.getShift());
            if (!result)
                throw new InvalidArgumentsServiceException();

        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }
        return true;
    }

    private boolean checkGroupEnrolment(Integer groupPropertiesCode,
            Integer shiftCode, String username) throws FenixServiceException {

        boolean result = false;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IGroupProperties groupProperties = (IGroupProperties) sp
                    .getIPersistentStudentGroup().readByOID(
                            GroupProperties.class, groupPropertiesCode);

            if(groupProperties == null){
            	throw new InvalidChangeServiceException();
            }
            
            IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory
			.getInstance();
            IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory
			.getGroupEnrolmentStrategyInstance(groupProperties);

        	
        	if(!strategy.checkStudentInAttendsSet(groupProperties,username)){
        		throw new NotAuthorizedException();
        	}
        
        	
        		IShift shift = (IShift) sp.getITurnoPersistente().readByOID(Shift.class, shiftCode);
        		result = strategy.checkNumberOfGroups(groupProperties, shift);
            

            if (!result)
                throw new InvalidArgumentsServiceException();

            result = strategy.checkAlreadyEnroled(groupProperties, username);

            if (result)
                throw new InvalidSituationServiceException();
        	
        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }
        return true;
    }

    private boolean checkUnEnrollStudentInGroup(Integer studentGroupCode,
            String username) throws FenixServiceException {

        boolean result = false;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IStudentGroup studentGroup = (IStudentGroup) sp
                    .getIPersistentStudentGroup().readByOID(StudentGroup.class,
                            studentGroupCode);

            
            
            if (studentGroup == null) {
                throw new FenixServiceException();
            }
            IGroupProperties groupProperties = studentGroup.getAttendsSet().getGroupProperties();
            
            GroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory
			.getInstance();
            IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory
			.getGroupEnrolmentStrategyInstance(groupProperties);

            
            if(!strategy.checkStudentInAttendsSet(groupProperties,username)){
            	throw new NotAuthorizedException();
            }
            
            result = strategy.checkNotEnroledInGroup(groupProperties,
                    studentGroup, username);
            if (result)
                throw new InvalidSituationServiceException();

            result = strategy.checkNumberOfGroupElements(groupProperties,
                    studentGroup);
            if (!result)
                throw new InvalidArgumentsServiceException();

        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }
        return true;
    }

    private boolean checkEditStudentGroupShift(Integer studentGroupCode,Integer groupPropertiesCode,
            String username) throws FenixServiceException {

        boolean result = false;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            
            IGroupProperties groupProperties = (IGroupProperties) sp
			.getIPersistentGroupProperties().readByOID(
					GroupProperties.class, groupPropertiesCode);
            
            if(groupProperties == null){
            	throw new ExistingServiceException();
            }
            
            IStudentGroup studentGroup = (IStudentGroup) sp
                    .getIPersistentStudentGroup().readByOID(StudentGroup.class,
                            studentGroupCode);
            if (studentGroup == null) {
                throw new FenixServiceException();
            }
            
            GroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory
			.getInstance();
            IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory
			.getGroupEnrolmentStrategyInstance(groupProperties);

            if(!strategy.checkStudentInAttendsSet(groupProperties,username)){
        		throw new NotAuthorizedException();
        	}
            
            result = strategy.checkNotEnroledInGroup(groupProperties,
                    studentGroup, username);
            if (result)
                throw new InvalidSituationServiceException();
            
            if(groupProperties.getShiftType() == null || studentGroup.getShift() == null){
             	throw new InvalidChangeServiceException();
             }

        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }
        return true;
    }
    
    
    private boolean checkEnrollStudentGroupShift(Integer studentGroupCode,Integer groupPropertiesCode,
            String username) throws FenixServiceException {

        boolean result = false;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            
            IGroupProperties groupProperties = (IGroupProperties) sp
			.getIPersistentGroupProperties().readByOID(
					GroupProperties.class, groupPropertiesCode);
            
            if(groupProperties == null){
            	throw new ExistingServiceException();
            }
            
            IStudentGroup studentGroup = (IStudentGroup) sp
                    .getIPersistentStudentGroup().readByOID(StudentGroup.class,
                            studentGroupCode);
            if (studentGroup == null) {
                throw new FenixServiceException();
            }
            
            GroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory
			.getInstance();
            IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory
			.getGroupEnrolmentStrategyInstance(groupProperties);

            if(!strategy.checkStudentInAttendsSet(groupProperties,username)){
        		throw new NotAuthorizedException();
        	}
            
            result = strategy.checkNotEnroledInGroup(groupProperties,
                    studentGroup, username);
            if (result)
                throw new InvalidSituationServiceException();
            
            
            if(groupProperties.getShiftType() == null || studentGroup.getShift() != null){
             	throw new InvalidChangeServiceException();
             }

        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }
        return true;
    }

    /**
     * Executes the service.
     */

    public boolean run(Integer groupPropertiesCode, Integer shiftCode,
            Integer studentGroupCode, String username, Integer option)
            throws FenixServiceException {

        boolean result = false;

        switch (option.intValue()) {

        case 1:
            result = checkGroupStudentEnrolment(studentGroupCode, username);
            return result;

        case 2:
            result = checkGroupEnrolment(groupPropertiesCode, shiftCode,
                    username);
            return result;

        case 3:
            result = checkUnEnrollStudentInGroup(studentGroupCode, username);
            return result;

        case 4:
            result = checkEditStudentGroupShift(studentGroupCode, groupPropertiesCode, username);
            return result;
            
        case 5:
            result = checkEnrollStudentGroupShift(studentGroupCode, groupPropertiesCode, username);
            return result;
        }

        return result;
    }

}