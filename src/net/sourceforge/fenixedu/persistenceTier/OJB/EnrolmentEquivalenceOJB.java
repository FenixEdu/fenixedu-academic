package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.EnrolmentEquivalence;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IEnrolmentEquivalence;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrolmentEquivalence;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author dcs-rjao 24/Mar/2003
 */

public class EnrolmentEquivalenceOJB extends PersistentObjectOJB implements
        IPersistentEnrolmentEquivalence {

    public void delete(IEnrolmentEquivalence enrolment) throws ExcepcaoPersistencia {
        try {
            super.delete(enrolment);
        } catch (ExcepcaoPersistencia ex) {
            throw ex;
        }
    }

    public List readAll() throws ExcepcaoPersistencia {

        return queryList(EnrolmentEquivalence.class, new Criteria());
    }

    public IEnrolmentEquivalence readByEnrolment(IEnrolment enrolment) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("enrolment.idInternal", enrolment.getIdInternal());
        return (IEnrolmentEquivalence) queryObject(EnrolmentEquivalence.class, criteria);
    }
}