/*
 * Created on 23/Abr/2003
 *
 * 
 */
package ServidorPersistente;

import java.util.List;

import Dominio.IExecutionCourse;
import Dominio.IEvaluationMethod;

/**
 * @author João Mota
 *
 *
 */
public interface IPersistentEvaluationMethod extends IPersistentObject {
	
	public IEvaluationMethod readByExecutionCourse(IExecutionCourse executionCourse) throws ExcepcaoPersistencia;
	
	public IEvaluationMethod readByIdExecutionCourse(IExecutionCourse executionCourse) throws ExcepcaoPersistencia;
		
	public List readAll() throws ExcepcaoPersistencia;
	
	public void delete(IEvaluationMethod evaluation) throws ExcepcaoPersistencia;
	
	
	
	public void lockWrite(IEvaluationMethod evaluation) throws ExcepcaoPersistencia;	
	
	

}
