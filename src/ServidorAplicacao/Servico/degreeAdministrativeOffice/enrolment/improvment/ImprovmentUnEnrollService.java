/*
 * Created on Nov 22, 2004
 */
package ServidorAplicacao.Servico.degreeAdministrativeOffice.enrolment.improvment;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.Enrolment;
import Dominio.IEnrollment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.IAttends;
import Dominio.IStudent;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentEnrollment;
import ServidorPersistente.IPersistentEnrolmentEvaluation;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrolmentEvaluationState;
import Util.EnrolmentEvaluationType;
import Util.TipoCurso;

/**
 * @author nmgo
 */
public class ImprovmentUnEnrollService implements IService {
    
    public Object run(Integer studentNumber, List enrolmentsIds) throws FenixServiceException{
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentEnrollment persistentEnrollment = sp.getIPersistentEnrolment();
            IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation = sp.getIPersistentEnrolmentEvaluation();
            
            IPersistentStudent persistentStudent = sp.getIPersistentStudent();
            IStudent student = persistentStudent.readStudentByNumberAndDegreeType(studentNumber, TipoCurso.LICENCIATURA_OBJ);
            if(student == null) {
                throw new InvalidArgumentsServiceException();
            }
            
            Iterator iterator = enrolmentsIds.iterator();
            while(iterator.hasNext()) {
                Integer enrolmentId = (Integer) iterator.next();
                IEnrollment enrollment = (IEnrollment) persistentEnrollment.readByOID(Enrolment.class, enrolmentId);
                if(enrollment == null) {
                    throw new InvalidArgumentsServiceException();
                }
                IEnrolmentEvaluation improvmentEnrolmentEvaluation = (IEnrolmentEvaluation) CollectionUtils.find(enrollment.getEvaluations(), new Predicate() {

                    public boolean evaluate(Object arg0) {
                        IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) arg0;
                        if(enrolmentEvaluation.getEnrolmentEvaluationType().equals(EnrolmentEvaluationType.IMPROVEMENT_OBJ)
                                && enrolmentEvaluation.getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.TEMPORARY_OBJ))
                            return true;
                        return false;
                    }
                    
                });
                
                if(improvmentEnrolmentEvaluation != null) {
                    persistentEnrolmentEvaluation.delete(improvmentEnrolmentEvaluation);
                }
                
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
                attend.setEnrolment(null);
            }
        }
        
    }

}
