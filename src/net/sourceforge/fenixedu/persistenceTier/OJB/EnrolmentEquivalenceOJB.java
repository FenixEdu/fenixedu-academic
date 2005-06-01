package net.sourceforge.fenixedu.persistenceTier.OJB;

import net.sourceforge.fenixedu.domain.EnrolmentEquivalence;
import net.sourceforge.fenixedu.domain.IEnrolmentEquivalence;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrolmentEquivalence;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author dcs-rjao 24/Mar/2003
 */

public class EnrolmentEquivalenceOJB extends PersistentObjectOJB implements
        IPersistentEnrolmentEquivalence {

    public IEnrolmentEquivalence readByEnrolment(Integer enrolmentId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("enrolment.idInternal", enrolmentId);
        return (IEnrolmentEquivalence) queryObject(EnrolmentEquivalence.class, criteria);
    }
}