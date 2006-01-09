package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeCurricularPlan;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author dcs-rjao 19/Mar/2003
 */

public class DegreeCurricularPlanOJB extends PersistentObjectOJB implements
        IPersistentDegreeCurricularPlan {

    public List readByCurricularStage(CurricularStage curricularStage) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("curricularStage", curricularStage);

        return queryList(DegreeCurricularPlan.class, criteria);
    }

    public List readFromNewDegreeStructure() throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addNotEqualTo("curricularStage", CurricularStage.OLD);

        return queryList(DegreeCurricularPlan.class, criteria);
    }
    
    public List readByDegreeAndState(Integer degreeId, DegreeCurricularPlanState state, CurricularStage curricularStage)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("degree.idInternal", degreeId);
        criteria.addEqualTo("state", state);
        criteria.addEqualTo("curricularStage", curricularStage);

        return queryList(DegreeCurricularPlan.class, criteria);
    }

    public DegreeCurricularPlan readByNameAndDegree(String name, Integer degreeId, CurricularStage curricularStage)
            throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("name", name);
        criteria.addEqualTo("degreeKey", degreeId);
        criteria.addEqualTo("curricularStage", curricularStage);

        return (DegreeCurricularPlan) queryObject(DegreeCurricularPlan.class, criteria);
    }

    public List readByDegreeTypeAndState(DegreeType degreeType, DegreeCurricularPlanState state)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("degree.tipoCurso", degreeType);
        criteria.addEqualTo("state", state);
        criteria.addEqualTo("curricularStage", CurricularStage.OLD);
        
        return queryList(DegreeCurricularPlan.class, criteria);
    }
    
}
