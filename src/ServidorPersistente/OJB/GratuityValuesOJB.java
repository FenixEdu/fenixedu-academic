/*
 * Created on 9/Jan/2004
 *
 */
package ServidorPersistente.OJB;

import org.apache.ojb.broker.query.Criteria;

import Dominio.ICursoExecucao;
import Dominio.IGratuityValues;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentGratuityValues;

/**
 * @author Tânia Pousão
 *
 */
public class GratuityValuesOJB extends ObjectFenixOJB implements IPersistentGratuityValues
{

	public IGratuityValues readGratuityValuesByExecutionDegree(ICursoExecucao executionDegree) throws ExcepcaoPersistencia{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("executionDegree.idInternal", executionDegree.getIdInternal());
		
		return (IGratuityValues) queryObject(IGratuityValues.class, criteria);
	}

}
