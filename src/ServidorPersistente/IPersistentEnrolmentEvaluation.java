package ServidorPersistente;

import java.util.List;

import Dominio.IEnrolment;
import Dominio.IEnrolmentEvaluation;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.EnrolmentEvaluationState;
import Util.EnrolmentEvaluationType;

/**
 * @author dcs-rjao
 *
 * 24/Mar/2003
 */

public interface IPersistentEnrolmentEvaluation extends IPersistentObject {
	public void deleteAll() throws ExcepcaoPersistencia;
	public void lockWrite(IEnrolmentEvaluation enrolmentEvaluationToWrite) throws ExcepcaoPersistencia, ExistingPersistentException;
	public void delete(IEnrolmentEvaluation enrolmentEvaluation) throws ExcepcaoPersistencia;
	public List readAll() throws ExcepcaoPersistencia;	
	public IEnrolmentEvaluation readEnrolmentEvaluationByEnrolmentEvaluationTypeAndGrade(IEnrolment enrolment,EnrolmentEvaluationType evaluationType,String grade) throws ExcepcaoPersistencia; 
	public List readEnrolmentEvaluationByEnrolmentEvaluationState(IEnrolment enrolment,EnrolmentEvaluationState evaluationState) throws ExcepcaoPersistencia; 
}