/*
 * Created on 7/Abr/2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.precedences.Precedence;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentPrecedence;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author jpvl
 */
public class PrecedenceOJB extends PersistentObjectOJB implements IPersistentPrecedence {

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentPrecedence#readByDegreeCurricularPlan(Dominio.IDegreeCurricularPlan)
     */
    public List readByDegreeCurricularPlan(IDegreeCurricularPlan plan) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("curricularCourse.degreeCurricularPlan.name", plan.getName());
        crit.addEqualTo("curricularCourse.degreeCurricularPlan.degree.sigla", plan.getDegree()
                .getSigla());
        return queryList(Precedence.class, crit);

    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentPrecedence#readByCurricularCourse(Dominio.ICurricularCourse)
     */
    public List readByCurricularCourse(ICurricularCourse curricularCourse) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("curricularCourse.code", curricularCourse.getCode());
        crit.addEqualTo("curricularCourse.name", curricularCourse.getName());
        crit.addEqualTo("curricularCourse.degreeCurricularPlan.name", curricularCourse
                .getDegreeCurricularPlan().getName());
        crit.addEqualTo("curricularCourse.degreeCurricularPlan.degree.sigla", curricularCourse
                .getDegreeCurricularPlan().getDegree().getSigla());
        return queryList(Precedence.class, crit);

    }

}