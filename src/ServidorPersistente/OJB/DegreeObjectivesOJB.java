/*
 * Created on 23/Jul/2003
 *
 * 
 */
package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.DegreeObjectives;
import Dominio.IDegree;
import Dominio.IDegreeObjectives;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDegreeObjectives;

/**
 * @author João Mota
 * 
 * 23/Jul/2003 fenix-head ServidorPersistente.OJB
 *  
 */
public class DegreeObjectivesOJB extends PersistentObjectOJB implements IPersistentDegreeObjectives {

    public IDegreeObjectives readCurrentByDegree(IDegree degree) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyDegree", degree.getIdInternal());
        criteria.addIsNull("endDate");
        return (IDegreeObjectives) queryObject(DegreeObjectives.class, criteria);

    }

    public List readByDegree(IDegree degree) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyDegree", degree.getIdInternal());

        return queryList(DegreeObjectives.class, criteria);
    }

}