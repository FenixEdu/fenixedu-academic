/*
 * Created on 23/Jul/2003
 *
 */

package ServidorPersistente;

import java.util.List;

import Dominio.IDisciplinaExecucao;

/**
 * @author Susana Fernandes
 */

public interface IPersistentMetadata extends IPersistentObject {
	public abstract List readByExecutionCourse(IDisciplinaExecucao executionCourse)
		throws ExcepcaoPersistencia;
}