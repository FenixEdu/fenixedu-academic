/*
 * Created on 23/Abr/2003
 *
 * 
 */
package ServidorPersistente;

import java.util.List;

import Dominio.IDisciplinaExecucao;
import Dominio.IEvaluation;

/**
 * @author João Mota
 *
 *
 */
public interface IPersistentEvaluation extends IPersistentObject {
	
	public IEvaluation readByExecutionCourse(IDisciplinaExecucao executionCourse) throws ExcepcaoPersistencia;
	
	public List readAll() throws ExcepcaoPersistencia;
	
	public void delete(IEvaluation evaluation) throws ExcepcaoPersistencia;
	
	public void deleteAll() throws ExcepcaoPersistencia;
	
	public void lockWrite(IEvaluation evaluation) throws ExcepcaoPersistencia;	
	
	

}
