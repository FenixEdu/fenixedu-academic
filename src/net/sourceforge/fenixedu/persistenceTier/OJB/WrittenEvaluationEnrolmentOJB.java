/*
 * Created on 30/Mai/2003
 *
 * 
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.IWrittenEvaluationEnrolment;
import net.sourceforge.fenixedu.domain.WrittenEvaluationEnrolment;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentWrittenEvaluationEnrolment;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author João Mota
 *  
 */
public class WrittenEvaluationEnrolmentOJB extends PersistentObjectOJB implements IPersistentWrittenEvaluationEnrolment {

    public List readByExamOID(Integer examOID) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyWrittenEvaluation", examOID);
        return queryList(WrittenEvaluationEnrolment.class, criteria);

    }

    public List readByStudentOID(Integer studentOID) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyStudent", studentOID);
        return queryList(WrittenEvaluationEnrolment.class, criteria, true);
    }

    public IWrittenEvaluationEnrolment readBy(Integer examOID, Integer studentOID) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyWrittenEvaluation", examOID);
        criteria.addEqualTo("keyStudent", studentOID);
        return (IWrittenEvaluationEnrolment) queryObject(WrittenEvaluationEnrolment.class, criteria);
    }
}