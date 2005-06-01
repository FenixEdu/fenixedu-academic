package net.sourceforge.fenixedu.persistenceTier.OJB;

import net.sourceforge.fenixedu.domain.EquivalentEnrolmentForEnrolmentEquivalence;
import net.sourceforge.fenixedu.domain.IEquivalentEnrolmentForEnrolmentEquivalence;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEquivalentEnrolmentForEnrolmentEquivalence;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author dcs-rjao
 * 
 * 24/Mar/2003
 */

public class EquivalentEnrolmentForEnrolmentEquivalenceOJB extends PersistentObjectOJB implements
        IPersistentEquivalentEnrolmentForEnrolmentEquivalence {

    public IEquivalentEnrolmentForEnrolmentEquivalence readByEnrolmentEquivalenceAndEquivalentEnrolment(
            Integer enrolmentEquivalenceId, Integer equivalentEnrolmentId)
            throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("enrolmentEquivalence.idInternal", enrolmentEquivalenceId);
        criteria.addEqualTo("equivalentEnrolment.idInternal", equivalentEnrolmentId);
        return (IEquivalentEnrolmentForEnrolmentEquivalence) queryObject(
                EquivalentEnrolmentForEnrolmentEquivalence.class, criteria);
    }
}