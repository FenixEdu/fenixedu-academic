package net.sourceforge.fenixedu.persistenceTier.OJB;

import net.sourceforge.fenixedu.domain.University;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentUniversity;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author David Santos 28/Out/2003
 */

public class UniversityOJB extends PersistentObjectOJB implements IPersistentUniversity {

    public UniversityOJB() {
    }

    public University readByNameAndCode(String name, String code) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("name", name);
        criteria.addEqualTo("code", code);
        return (University) queryObject(University.class, criteria);
    }

}