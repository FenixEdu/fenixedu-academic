
package ServidorPersistente;

import java.util.List;

import Dominio.IExecutionCourse;
import Dominio.IEvaluation;
import Dominio.IEvalutionExecutionCourse;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 *
 * @author  Tânia Pousão
 */
public interface IPersistentEvaluationExecutionCourse extends IPersistentObject {
	public IEvalutionExecutionCourse readBy(IEvaluation evaluation, IExecutionCourse executionCourse) throws ExcepcaoPersistencia;
	public List readAll() throws ExcepcaoPersistencia;
	public void lockWrite(IEvalutionExecutionCourse evalutionExecutionCourseToWrite) throws ExcepcaoPersistencia, ExistingPersistentException;
	public void delete(IEvalutionExecutionCourse evalutionExecutionCourse) throws ExcepcaoPersistencia;
	public void deleteAll() throws ExcepcaoPersistencia;
	public void delete(IEvaluation evaluation) throws ExcepcaoPersistencia;
	public List readByExecutionCourse(IExecutionCourse executionCourse) throws ExcepcaoPersistencia;

}
