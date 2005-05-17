/*
 * Created on May 11, 2005
 *
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IEvaluationMethod;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEvaluationMethod;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author jdnf
 *  
 */
public class EvaluationMethodVO extends VersionedObjectsBase implements IPersistentEvaluationMethod {

    public IEvaluationMethod readByIdExecutionCourse(Integer executionCourseOID)
            throws ExcepcaoPersistencia {

        IEvaluationMethod evaluationMethod = null;
        final IExecutionCourse executionCourse = (IExecutionCourse) readByOID(ExecutionCourse.class,
                executionCourseOID);

        if (executionCourse != null)
            evaluationMethod = executionCourse.getEvaluationMethod();

        return evaluationMethod;
    }

}
