/*
 * Created on Nov 22, 2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice.enrolment.improvment;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEmployee;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrollment;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrolmentEvaluation;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author nmgo
 */
public class ImprovmentEnrollService implements IService{
    
    public Object run(Integer studentNumber, String employeeUserName, List enrolmentsIds) throws FenixServiceException{
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentStudent persistentStudent = sp.getIPersistentStudent();
            
            IStudent student = persistentStudent.readStudentByNumberAndDegreeType(studentNumber, DegreeType.DEGREE);
            if(student == null) {
                throw new InvalidArgumentsServiceException();
            }
            
            IPessoaPersistente pessoaPersistente = sp.getIPessoaPersistente();
            IPerson pessoa = pessoaPersistente.lerPessoaPorUsername(employeeUserName);
			
	        IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
	        final IExecutionPeriod currentExecutionPeriod = persistentExecutionPeriod.readActualExecutionPeriod();
	        
            
            if(pessoa == null) {
                throw new InvalidArgumentsServiceException();
            }
            
            IPersistentEmployee persistentEmployee = sp.getIPersistentEmployee();
            IEmployee employee = persistentEmployee.readByPerson(pessoa);;
			
            if(employee == null) {
                throw new InvalidArgumentsServiceException();
            }

            IPersistentEnrollment persistentEnrollment = sp.getIPersistentEnrolment();
            IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation = sp.getIPersistentEnrolmentEvaluation();
            
            Iterator iterator = enrolmentsIds.iterator();
            while(iterator.hasNext()) {
                Integer enrolmentId = (Integer) iterator.next();
                IEnrolment enrollment = (IEnrolment) persistentEnrollment.readByOID(Enrolment.class, enrolmentId);
                if(enrollment == null) {
                    throw new InvalidArgumentsServiceException();
                }

				enrollment.createEnrolmentEvaluationForImprovment(employee, currentExecutionPeriod, student);
            }
            
            return new Boolean(true);
            
            
        }catch(ExcepcaoPersistencia ep) {
            throw new FenixServiceException(ep);
        }
    }
}
