/*
 * Created on 9/Jan/2004
 *
 */
package ServidorPersistente;

import Dominio.ICursoExecucao;
import Dominio.IGratuityValues;

/**
 * @author Tânia Pousão
 *
 */
public interface IPersistentGratuityValues extends IPersistentObject
{
	public IGratuityValues readGratuityValuesByExecutionDegree(ICursoExecucao executionDegree) throws ExcepcaoPersistencia;
}
