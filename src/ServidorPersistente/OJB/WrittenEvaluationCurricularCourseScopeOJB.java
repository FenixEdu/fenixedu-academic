package ServidorPersistente.OJB;

/**
 * 
 * @author Fernanda Quitério
 */
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.ICurricularCourseScope;
import Dominio.WrittenEvaluationCurricularCourseScope;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentWrittenEvaluationCurricularCourseScope;

public class WrittenEvaluationCurricularCourseScopeOJB extends PersistentObjectOJB implements
        IPersistentWrittenEvaluationCurricularCourseScope {

    public List readByCurricularCourseScope(ICurricularCourseScope curricularCourseScope)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("curricularCourseScope.idInternal", curricularCourseScope.getIdInternal());
        return queryList(WrittenEvaluationCurricularCourseScope.class, crit);
    }

    public List readByCurricularCourseScopeList(List scopes) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addIn("curricularCourseScope.idInternal", scopes);
        return queryList(WrittenEvaluationCurricularCourseScope.class, crit);
    }
}