/*
 * Created on 23/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.DegreeObjectives;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDegreeObjectives;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeObjectives;

import org.apache.ojb.broker.query.Criteria;

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