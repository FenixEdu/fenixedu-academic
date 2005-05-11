package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeCurricularPlan;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author dcs-rjao 19/Mar/2003
 */

public class DegreeCurricularPlanOJB extends PersistentObjectOJB implements
        IPersistentDegreeCurricularPlan {

    public DegreeCurricularPlanOJB() {
    }

    public List readAll() throws ExcepcaoPersistencia {
        return queryList(DegreeCurricularPlan.class, new Criteria());
    }

    public void deleteDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan)
            throws ExcepcaoPersistencia {

        super.delete(degreeCurricularPlan);

    }

    public List readByDegree(IDegree degree) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("degree.nome", degree.getNome());
        criteria.addEqualTo("degree.sigla", degree.getSigla());
        criteria.addEqualTo("degree.tipoCurso", degree.getTipoCurso());
        return queryList(DegreeCurricularPlan.class, criteria);

    }

    public List readByDegreeAndState(IDegree degree, DegreeCurricularPlanState state)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("degree.nome", degree.getNome());
        criteria.addEqualTo("degree.sigla", degree.getSigla());
        criteria.addEqualTo("degree.tipoCurso", degree.getTipoCurso());
        criteria.addEqualTo("state", state);

        return queryList(DegreeCurricularPlan.class, criteria);
    }

    public IDegreeCurricularPlan readByNameAndDegree(String name, IDegree degree)
            throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("name", name);
        criteria.addEqualTo("degreeKey", degree.getIdInternal());

        return (IDegreeCurricularPlan) queryObject(DegreeCurricularPlan.class, criteria);
    }

    public List readByDegreeTypeAndState(DegreeType degreeType, DegreeCurricularPlanState state)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("degree.tipoCurso", degreeType);
        criteria.addEqualTo("state", state);
        return queryList(DegreeCurricularPlan.class, criteria);
    }

}