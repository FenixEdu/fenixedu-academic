/*
 * Created on 19/Ago/2003
 *
 */
package ServidorPersistente;

import java.util.List;

import Dominio.IExecutionCourse;
import Dominio.IDistributedTest;

/**
 * @author Susana Fernandes
 */
public interface IPersistentDistributedTest extends IPersistentObject {
	public List readByExecutionCourse(IExecutionCourse executionCourse)
		throws ExcepcaoPersistencia;
	public void delete(IDistributedTest distributedTest)
		throws ExcepcaoPersistencia;
}