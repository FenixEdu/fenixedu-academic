/*
 * Created on Nov 22, 2004
 */
package ServidorAplicacao.Servico.degreeAdministrativeOffice.enrolment.improvment;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.Enrolment;
import Dominio.EnrolmentEvaluation;
import Dominio.Attends;
import Dominio.IEmployee;
import Dominio.IEnrollment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.IAttends;
import Dominio.IPerson;
import Dominio.IStudent;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentEmployee;
import ServidorPersistente.IPersistentEnrollment;
import ServidorPersistente.IPersistentEnrolmentEvaluation;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrolmentEvaluationState;
import Util.EnrolmentEvaluationType;
import Util.TipoCurso;

/**
 * @author nmgo
 */
public class ImprovmentEnrollService implements IService{
    
    public Object run(Integer studentNumber, String employeeUserName, List enrolmentsIds) throws FenixServiceException{
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentStudent persistentStudent = sp.getIPersistentStudent();
            
            IStudent student = persistentStudent.readStudentByNumberAndDegreeType(studentNumber, TipoCurso.LICENCIATURA_OBJ);
            if(student == null) {
                throw new InvalidArgumentsServiceException();
            }
            
            IPessoaPersistente pessoaPersistente = sp.getIPessoaPersistente();
            IPerson pessoa = pessoaPersistente.lerPessoaPorUsername(employeeUserName);
            
            if(pessoa == null) {
                throw new InvalidArgumentsServiceException();
            }
            
            IPersistentEmployee persistentEmployee = sp.getIPersistentEmployee();
            IEmployee employee = persistentEmployee.readByPerson(pessoa);
            
            if(employee == null) {
                throw new InvalidArgumentsServiceException();
            }

            IPersistentEnrollment persistentEnrollment = sp.getIPersistentEnrolment();
            IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation = sp.getIPersistentEnrolmentEvaluation();
            
            Iterator iterator = enrolmentsIds.iterator();
            while(iterator.hasNext()) {
                Integer enrolmentId = (Integer) iterator.next();
                IEnrollment enrollment = (IEnrollment) persistentEnrollment.readByOID(Enrolment.class, enrolmentId);
                if(enrollment == null) {
                    throw new InvalidArgumentsServiceException();
                }
                
                //create new Improvment EnrolmentEvaluation
                IEnrolmentEvaluation enrolmentEvaluation = new EnrolmentEvaluation();
                persistentEnrolmentEvaluation.simpleLockWrite(enrolmentEvaluation);
                enrolmentEvaluation.setEmployee(employee);
                enrolmentEvaluation.setWhen(new Date());
                enrolmentEvaluation.setEnrolment(enrollment);
                enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ);
                enrolmentEvaluation.setEnrolmentEvaluationType(EnrolmentEvaluationType.IMPROVEMENT_OBJ);
                                
                
                setAttend(sp, enrollment, student);
            }
            
            return new Boolean(true);
            
            
        }catch(ExcepcaoPersistencia ep) {
            throw new FenixServiceException(ep);
        }
    }

    /**
     * @param sp
     * @param enrollment
     * @param student
     */
    private void setAttend(ISuportePersistente sp, IEnrollment enrollment, final IStudent student) throws ExcepcaoPersistencia{
        
        IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
        final IExecutionPeriod currentExecutionPeriod = persistentExecutionPeriod.readActualExecutionPeriod();
        
        IFrequentaPersistente frequentaPersistente = sp.getIFrequentaPersistente();
        
        List executionCourses = enrollment.getCurricularCourse().getAssociatedExecutionCourses();
        IExecutionCourse currentExecutionCourse = (IExecutionCourse) CollectionUtils.find(executionCourses, new Predicate() {

            public boolean evaluate(Object arg0) {
                IExecutionCourse executionCourse = (IExecutionCourse) arg0;
                if(executionCourse.getExecutionPeriod().equals(currentExecutionPeriod))
                	return true;
                return false;
            }
            
        });
        
        if(currentExecutionCourse != null) {
            List attends = currentExecutionCourse.getAttends();
            IAttends attend = (IAttends) CollectionUtils.find(attends, new Predicate() {

                public boolean evaluate(Object arg0) {
                    IAttends frequenta = (IAttends) arg0;
                    if(frequenta.getAluno().equals(student))
                    	return true;
                    return false;
                }
                
            });
            
            if(attend != null) {
                frequentaPersistente.simpleLockWrite(attend);
                attend.setEnrolment(enrollment);
            }
            else {
                attend = new Attends();
                frequentaPersistente.simpleLockWrite(attend);
                attend.setDisciplinaExecucao(currentExecutionCourse);
                attend.setAluno(student);
                attend.setEnrolment(enrollment);
                
            }
            
        }
        
    }

}
