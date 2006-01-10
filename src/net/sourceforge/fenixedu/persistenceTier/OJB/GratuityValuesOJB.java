/*
 * Created on 9/Jan/2004
 *
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import net.sourceforge.fenixedu.domain.GratuityValues;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGratuityValues;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author Tânia Pousão
 *  
 */
public class GratuityValuesOJB extends PersistentObjectOJB implements IPersistentGratuityValues {

    public GratuityValues readGratuityValuesByExecutionDegree(Integer executionDegreeID)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("executionDegree.idInternal", executionDegreeID);

        return (GratuityValues) queryObject(GratuityValues.class, criteria);
    }

}