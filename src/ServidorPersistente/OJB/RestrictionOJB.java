/*
 * Created on 7/Abr/2003 by jpvl
 *  
 */
package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.ICurricularCourse;
import Dominio.precedences.Restriction;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentRestriction;

/**
 * @author jpvl
 */
public class RestrictionOJB extends ObjectFenixOJB implements IPersistentRestriction
{

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.OJB.IPersistentRestriction#readByCurricularCourse(Dominio.ICurricularCourse)
	 */
    public List readByCurricularCourse(ICurricularCourse curricularCourse) throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("precedence.curricularCourse.name", curricularCourse.getName());
        crit.addEqualTo("precedence.curricularCourse.code", curricularCourse.getCode());
        crit.addEqualTo(
            "precedence.curricularCourse.degreeCurricularPlan.name",
            curricularCourse.getDegreeCurricularPlan().getName());
        crit.addEqualTo(
            "precedence.curricularCourse.degreeCurricularPlan.degree.sigla",
            curricularCourse.getDegreeCurricularPlan().getDegree().getSigla());
        return queryList(Restriction.class, crit);

    }

}
