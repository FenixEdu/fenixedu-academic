/*
 * Created on 23/Abr/2003 
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import net.sourceforge.fenixedu.domain.EvaluationMethod;
import net.sourceforge.fenixedu.domain.IEvaluationMethod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEvaluationMethod;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author João Mota
 */
public class EvaluationMethodOJB extends PersistentObjectOJB implements IPersistentEvaluationMethod {

    public IEvaluationMethod readByIdExecutionCourse(Integer executionCourseOID)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyExecutionCourse", executionCourseOID);
        return (IEvaluationMethod) queryObject(EvaluationMethod.class, criteria);
    }
}