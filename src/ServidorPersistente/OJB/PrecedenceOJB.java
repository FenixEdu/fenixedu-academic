/*
 * Created on 7/Abr/2003 by jpvl
 *  
 */
package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.ICurricularCourse;
import Dominio.IDegreeCurricularPlan;
import Dominio.precedences.Precedence;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentPrecedence;

/**
 * @author jpvl
 */
public class PrecedenceOJB extends ObjectFenixOJB implements IPersistentPrecedence
{

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.IPersistentPrecedence#readByDegreeCurricularPlan(Dominio.IDegreeCurricularPlan)
	 */
    public List readByDegreeCurricularPlan(IDegreeCurricularPlan plan)
        throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("curricularCourse.degreeCurricularPlan.name", plan.getName());
        crit.addEqualTo(
            "curricularCourse.degreeCurricularPlan.degree.sigla",
            plan.getDegree().getSigla());
        return queryList(Precedence.class, crit);

    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.IPersistentPrecedence#readByCurricularCourse(Dominio.ICurricularCourse)
	 */
    public List readByCurricularCourse(ICurricularCourse curricularCourse)
        throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("curricularCourse.code", curricularCourse.getCode());
        crit.addEqualTo("curricularCourse.name", curricularCourse.getName());
        crit.addEqualTo(
            "curricularCourse.degreeCurricularPlan.name",
            curricularCourse.getDegreeCurricularPlan().getName());
        crit.addEqualTo(
            "curricularCourse.degreeCurricularPlan.degree.sigla",
            curricularCourse.getDegreeCurricularPlan().getDegree().getSigla());
        return queryList(Precedence.class, crit);

    }

}
