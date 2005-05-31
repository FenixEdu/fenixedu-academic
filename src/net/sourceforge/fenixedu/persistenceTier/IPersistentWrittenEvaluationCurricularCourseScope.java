package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

/**
 * @author Fernanda Quitério Created on 16/Jun/2004
 *  
 */
public interface IPersistentWrittenEvaluationCurricularCourseScope extends IPersistentObject {

    public List readByCurricularCourseScope(Integer curricularCourseScopeOID)
            throws ExcepcaoPersistencia;

    public List readByCurricularCourseScopeList(List scopes) throws ExcepcaoPersistencia;
}