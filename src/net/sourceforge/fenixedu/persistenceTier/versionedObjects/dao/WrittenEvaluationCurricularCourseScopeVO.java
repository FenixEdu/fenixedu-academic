package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

/**
 * 
 * @author Fernanda Quitério
 */
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.IWrittenEvaluationCurricularCourseScope;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentWrittenEvaluationCurricularCourseScope;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

public class WrittenEvaluationCurricularCourseScopeVO extends VersionedObjectsBase implements
        IPersistentWrittenEvaluationCurricularCourseScope {

    public List readByCurricularCourseScope(Integer curricularCourseScopeOID)
            throws ExcepcaoPersistencia {
        ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) readByOID(
                CurricularCourseScope.class, curricularCourseScopeOID);
        return curricularCourseScope.getWrittenEvaluationCurricularCourseScopes();

        /*
         * Criteria crit = new Criteria();
         * crit.addEqualTo("curricularCourseScope.idInternal",
         * curricularCourseScope.getIdInternal()); return
         * queryList(WrittenEvaluationCurricularCourseScope.class, crit);
         */
    }

    public List readByCurricularCourseScopeList(List scopes) throws ExcepcaoPersistencia {
        List<IWrittenEvaluationCurricularCourseScope> result = new ArrayList();
        for (Object scopeIdInternal : scopes) {
            List<IWrittenEvaluationCurricularCourseScope> weccscopes = readByCurricularCourseScope((Integer) scopeIdInternal);
            for (IWrittenEvaluationCurricularCourseScope writtenEvaluationCurricularCourseScope : weccscopes) {
                result.add(writtenEvaluationCurricularCourseScope);
            }
        }
        return result;

        /*
         * Criteria crit = new Criteria();
         * crit.addIn("curricularCourseScope.idInternal", scopes); return
         * queryList(WrittenEvaluationCurricularCourseScope.class, crit);
         */
    }
}