/*
 * Created on 23/Ago/2003
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.GroupProperties;
import Dominio.IAttends;
import Dominio.IGroupProperties;
import Dominio.IStudent;
import Dominio.IStudentGroup;
import Dominio.IStudentGroupAttend;
import Dominio.Student;
import Dominio.StudentGroup;
import Dominio.StudentGroupAttend;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidChangeServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidSituationServiceException;
import ServidorAplicacao.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import ServidorAplicacao.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import ServidorAplicacao.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentGroupProperties;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPersistentStudentGroup;
import ServidorPersistente.IPersistentStudentGroupAttend;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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
        IFrequentaPersistente persistentAttend = null;
        IPersistentStudent persistentStudent = null;
        IPersistentGroupProperties persistentGroupProperties = null;
        
        try {

            ISuportePersistente persistentSupport = SuportePersistenteOJB
                    .getInstance();

            persistentStudentGroup = persistentSupport
                    .getIPersistentStudentGroup();
            persistentStudent = persistentSupport.getIPersistentStudent();
            persistentAttend = persistentSupport.getIFrequentaPersistente();
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