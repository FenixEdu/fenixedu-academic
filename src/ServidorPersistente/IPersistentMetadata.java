/*
 * Created on 23/Jul/2003
 *
 */

package ServidorPersistente;

import java.util.List;

import Dominio.IExecutionCourse;
import Dominio.IMetadata;

/**
 * @author Susana Fernandes
 */

public interface IPersistentMetadata extends IPersistentObject {
	public abstract List readByExecutionCourse(IExecutionCourse executionCourse)
		throws ExcepcaoPersistencia;
	public abstract List readByExecutionCourseAndVisibility(IExecutionCourse executionCourse)
		throws ExcepcaoPersistencia;
	public void delete(IMetadata metadata) throws ExcepcaoPersistencia;
}