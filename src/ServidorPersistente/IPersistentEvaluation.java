/*
 * Created on 23/Abr/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorPersistente;

import java.util.List;

import Dominio.IDisciplinaExecucao;
import Dominio.IEvaluation;

/**
 * @author jmota
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public interface IPersistentEvaluation extends IPersistentObject {
	
	public IEvaluation readByExecutionCourse(IDisciplinaExecucao executionCourse) throws ExcepcaoPersistencia;
	
	public List readAll() throws ExcepcaoPersistencia;
	
	public void delete(IEvaluation evaluation) throws ExcepcaoPersistencia;
	
	public void deleteAll() throws ExcepcaoPersistencia;
	
	public void lockWrite(IEvaluation evaluation) throws ExcepcaoPersistencia;	
	
	

}
