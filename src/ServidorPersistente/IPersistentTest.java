/*
 * Created on 28/Jul/2003
 *
 */
package ServidorPersistente;

import java.util.List;

import Dominio.IExecutionCourse;
import Dominio.ITest;

/**
 * @author Susana Fernandes
 */
public interface IPersistentTest extends IPersistentObject {
	public abstract List readByExecutionCourse(IExecutionCourse executionCourse)
		throws ExcepcaoPersistencia;
	public void delete(ITest test) throws ExcepcaoPersistencia;
}