/*
 * Created on 27/Ago/2003
 *
 */

package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.Iterator;
import java.util.List;

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
import net.sourceforge.fenixedu.domain.StudentGroupAttend;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentGroup;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentGroupAttend;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

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
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
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