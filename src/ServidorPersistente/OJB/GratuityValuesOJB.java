/*
 * Created on 9/Jan/2004
 *
 */
package ServidorPersistente.OJB;

import org.apache.ojb.broker.query.Criteria;

import Dominio.GratuityValues;
import Dominio.IExecutionDegree;
import Dominio.IGratuityValues;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentGratuityValues;

/**
 * @author Tânia Pousão
 *  
 */
public class GratuityValuesOJB extends PersistentObjectOJB implements IPersistentGratuityValues {

    public IGratuityValues readGratuityValuesByExecutionDegree(IExecutionDegree executionDegree)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("executionDegree.idInternal", executionDegree.getIdInternal());

        return (IGratuityValues) queryObject(GratuityValues.class, criteria);
    }

}