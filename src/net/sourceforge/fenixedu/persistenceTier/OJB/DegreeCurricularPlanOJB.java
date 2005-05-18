package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeCurricularPlan;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author dcs-rjao 19/Mar/2003
 */

public class DegreeCurricularPlanOJB extends PersistentObjectOJB implements
        IPersistentDegreeCurricularPlan {

    public List readAll() throws ExcepcaoPersistencia {
        return queryList(DegreeCurricularPlan.class, new Criteria());
    }

    public List readByDegreeAndState(Integer degreeId, DegreeCurricularPlanState state)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("degree.idInternal", degreeId);
        criteria.addEqualTo("state", state);

        return queryList(DegreeCurricularPlan.class, criteria);
    }
}