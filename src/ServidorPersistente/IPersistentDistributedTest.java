/*
 * Created on 19/Ago/2003
 *
 */
package ServidorPersistente;

import java.util.List;

import Dominio.IDisciplinaExecucao;
import Dominio.IDistributedTest;
import Dominio.ITest;

/**
 * @author Susana Fernandes
 */
public interface IPersistentDistributedTest extends IPersistentObject {
	public List readByExecutionCourse(IDisciplinaExecucao executionCourse)
		throws ExcepcaoPersistencia;
	public List readByTest(ITest test) throws ExcepcaoPersistencia;
	public void deleteByTest(ITest test) throws ExcepcaoPersistencia;
	public void delete(IDistributedTest distributedTest)
		throws ExcepcaoPersistencia;
}