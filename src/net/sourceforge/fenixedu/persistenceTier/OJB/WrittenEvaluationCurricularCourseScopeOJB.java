package net.sourceforge.fenixedu.persistenceTier.OJB;

/**
 * 
 * @author Fernanda Quitério
 */
import java.util.List;

import net.sourceforge.fenixedu.domain.WrittenEvaluationCurricularCourseScope;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentWrittenEvaluationCurricularCourseScope;

import org.apache.ojb.broker.query.Criteria;

public class WrittenEvaluationCurricularCourseScopeOJB extends PersistentObjectOJB implements
        IPersistentWrittenEvaluationCurricularCourseScope {

    public List readByCurricularCourseScope(Integer curricularCourseScopeOID)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("curricularCourseScope.idInternal", curricularCourseScopeOID);
        return queryList(WrittenEvaluationCurricularCourseScope.class, crit);
    }

    public List readByCurricularCourseScopeList(List scopes) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addIn("curricularCourseScope.idInternal", scopes);
        return queryList(WrittenEvaluationCurricularCourseScope.class, crit);
    }
}