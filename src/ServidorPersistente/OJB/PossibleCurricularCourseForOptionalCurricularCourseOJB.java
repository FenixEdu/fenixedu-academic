package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.ICurricularCourse;
import Dominio.IDegreeCurricularPlan;
import Dominio.PossibleCurricularCourseForOptionalCurricularCourse;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentPossibleCurricularCourseForOptionalCurricularCourse;

/**
 * @author dcs-rjao
 * 
 * 24/Mar/2003
 */

public class PossibleCurricularCourseForOptionalCurricularCourseOJB
    extends ObjectFenixOJB
    implements IPersistentPossibleCurricularCourseForOptionalCurricularCourse
{

    public List readAllByDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan)
        throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo(
            "optionalCurricularCourse.degreeCurricularPlan.name",
            degreeCurricularPlan.getName());
        crit.addEqualTo(
            "optionalCurricularCourse.degreeCurricularPlan.state",
            degreeCurricularPlan.getState());
        crit.addEqualTo(
            "optionalCurricularCourse.degreeCurricularPlan.degree.nome",
            degreeCurricularPlan.getDegree().getNome());
        crit.addEqualTo(
            "optionalCurricularCourse.degreeCurricularPlan.degree.sigla",
            degreeCurricularPlan.getDegree().getSigla());
        crit.addEqualTo(
            "optionalCurricularCourse.degreeCurricularPlan.degree.tipoCurso",
            degreeCurricularPlan.getDegree().getTipoCurso());
        return queryList(PossibleCurricularCourseForOptionalCurricularCourse.class, crit);
    }

    public List readAllByOptionalCurricularCourse(ICurricularCourse curricularCourse)
        throws ExcepcaoPersistencia
    {
        IDegreeCurricularPlan degreeCurricularPlan = curricularCourse.getDegreeCurricularPlan();
        Criteria crit = new Criteria();
        crit.addEqualTo("optionalCurricularCourse.code",curricularCourse.getCode());
        crit.addEqualTo("optionalCurricularCourse.name",curricularCourse.getName());
        crit.addEqualTo("optionalCurricularCourse.degreeCurricularPlan.name",degreeCurricularPlan.getName());
        crit.addEqualTo("optionalCurricularCourse.degreeCurricularPlan.state",degreeCurricularPlan.getState());
        crit.addEqualTo("optionalCurricularCourse.degreeCurricularPlan.degree.nome",degreeCurricularPlan.getDegree().getNome());
        crit.addEqualTo("optionalCurricularCourse.degreeCurricularPlan.degree.sigla",degreeCurricularPlan.getDegree().getSigla());
        crit.addEqualTo("optionalCurricularCourse.degreeCurricularPlan.degree.tipoCurso",degreeCurricularPlan.getDegree().getTipoCurso());
        return queryList(PossibleCurricularCourseForOptionalCurricularCourse.class,crit);
     
    }

}