/*
 * Created on 23/Abr/2003
 *
 * 
 */
package ServidorPersistente;

import java.util.List;

import Dominio.ICurricularCourse;
import Dominio.IDisciplinaExecucao;
import Dominio.IEvaluationMethod;

/**
 * @author João Mota
 *
 *
 */
public interface IPersistentEvaluationMethod extends IPersistentObject {
	
	public IEvaluationMethod readByExecutionCourse(IDisciplinaExecucao executionCourse) throws ExcepcaoPersistencia;
	
	public List readAll() throws ExcepcaoPersistencia;
	
	public void delete(IEvaluationMethod evaluation) throws ExcepcaoPersistencia;
	
	public void deleteAll() throws ExcepcaoPersistencia;
	
	public void lockWrite(IEvaluationMethod evaluation) throws ExcepcaoPersistencia;	
	
	public IEvaluationMethod readByCurricularCourse(ICurricularCourse curricularCourse)
				throws ExcepcaoPersistencia;
	
	

}
