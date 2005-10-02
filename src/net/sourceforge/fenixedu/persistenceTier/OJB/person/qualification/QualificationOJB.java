package net.sourceforge.fenixedu.persistenceTier.OJB.person.qualification;

import java.util.List;

import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentQualification;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author João Simas
 * @author Nuno Barbosa
 */
public class QualificationOJB extends PersistentObjectOJB implements IPersistentQualification {
    public QualificationOJB() {
    }

    public List readQualificationsByPersonId(Integer personId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("person.idInternal", personId);
        List result = queryList(Qualification.class, criteria);
        return result;
    }

}