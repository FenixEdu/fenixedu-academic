/*
 * Created on 23/Ago/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.Iterator;
import java.util.List;

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
import net.sourceforge.fenixedu.domain.IGroupProperties;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.domain.IStudentGroupAttend;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.StudentGroupAttend;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGroupProperties;
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

public class InsertStudentGroupMembers implements IService {

    /**
     * The constructor of this class.
     */
    public InsertStudentGroupMembers() {
    }

    /**
     * Executes the service.
     */

    public Boolean run(Integer executionCourseCode, Integer studentGroupCode,Integer groupPropertiesCode,
            List studentCodes) throws FenixServiceException {

        IPersistentStudentGroupAttend persistentStudentGroupAttend = null;
        IPersistentStudentGroup persistentStudentGroup = null;
        IPersistentStudent persistentStudent = null;
        IPersistentGroupProperties persistentGroupProperties = null;
        
        try {

            ISuportePersistente persistentSupport = SuportePersistenteOJB
                    .getInstance();

            persistentStudentGroup = persistentSupport
                    .getIPersistentStudentGroup();
            persistentStudent = persistentSupport.getIPersistentStudent();
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
            
            if(!strategy.checkStudentsInAttendsSet(studentCodes,groupProperties)){
            	throw new InvalidArgumentsServiceException();
            }
            
            List allStudentGroup = groupProperties.getAttendsSet().getStudentGroups();

            Iterator iterGroups = allStudentGroup.iterator();

            while (iterGroups.hasNext()) {
                IStudentGroup existingStudentGroup = (IStudentGroup) iterGroups
                        .next();
                IStudentGroupAttend newStudentGroupAttend = null;
                Iterator iterator = studentCodes.iterator();

                while (iterator.hasNext()) {
                    IStudent student = (IStudent) persistentStudent.readByOID(
                            Student.class, (Integer) iterator.next());
                    IAttends attend = groupProperties.getAttendsSet().getStudentAttend(student);
                    
                    newStudentGroupAttend = persistentStudentGroupAttend
                            .readBy(existingStudentGroup, attend);

                    if (newStudentGroupAttend != null)
                        throw new InvalidSituationServiceException();

                }
            }

            Iterator iter = studentCodes.iterator();

            while (iter.hasNext()) {

                IStudent student = (IStudent) persistentStudent.readByOID(
                        Student.class, (Integer) iter.next());

                IAttends attend = groupProperties.getAttendsSet().getStudentAttend(student);


                IStudentGroupAttend notExistingSGAttend = new StudentGroupAttend(
                        studentGroup, attend);

                persistentStudentGroupAttend
                        .simpleLockWrite(notExistingSGAttend);
            }

        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia.getMessage());
        }

        return new Boolean(true);
    }
}