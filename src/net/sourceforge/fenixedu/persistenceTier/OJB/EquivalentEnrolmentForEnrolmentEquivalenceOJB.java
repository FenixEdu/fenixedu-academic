package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.EquivalentEnrolmentForEnrolmentEquivalence;
import net.sourceforge.fenixedu.domain.IEnrollment;
import net.sourceforge.fenixedu.domain.IEnrolmentEquivalence;
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

    public void delete(IEquivalentEnrolmentForEnrolmentEquivalence enrolmentEquivalenceRestriction)
            throws ExcepcaoPersistencia {
        try {
            super.delete(enrolmentEquivalenceRestriction);
        } catch (ExcepcaoPersistencia ex) {
            throw ex;
        }
    }

    public IEquivalentEnrolmentForEnrolmentEquivalence readByEnrolmentEquivalenceAndEquivalentEnrolment(
            IEnrolmentEquivalence enrolmentEquivalence, IEnrollment equivalentEnrolment)
            throws ExcepcaoPersistencia {
        try {
            Criteria criteria = new Criteria();
            criteria.addEqualTo("enrolmentEquivalence.idInternal", enrolmentEquivalence.getIdInternal());
            criteria.addEqualTo("equivalentEnrolment.idInternal", equivalentEnrolment.getIdInternal());
            return (IEquivalentEnrolmentForEnrolmentEquivalence) queryObject(
                    EquivalentEnrolmentForEnrolmentEquivalence.class, criteria);
        } catch (ExcepcaoPersistencia e) {
            throw e;
        }
    }

    public List readByEquivalentEnrolment(IEnrollment equivalentEnrolment) throws ExcepcaoPersistencia {
        try {
            Criteria criteria = new Criteria();
            criteria.addEqualTo("equivalentEnrolment.idInternal", equivalentEnrolment.getIdInternal());
            return queryList(EquivalentEnrolmentForEnrolmentEquivalence.class, criteria);
        } catch (ExcepcaoPersistencia e) {
            throw e;
        }
    }

    public List readAll() throws ExcepcaoPersistencia {

        return queryList(EquivalentEnrolmentForEnrolmentEquivalence.class, new Criteria());
    }

    public List readByEnrolmentEquivalence(IEnrolmentEquivalence enrolmentEquivalence)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("enrolmentEquivalence.idInternal", enrolmentEquivalence.getIdInternal());
        return queryList(EquivalentEnrolmentForEnrolmentEquivalence.class, criteria);
    }

}