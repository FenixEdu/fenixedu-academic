package net.sourceforge.fenixedu.persistenceTier.OJB.person.qualification;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IQualification;
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

    public List readQualificationsByPerson(IPerson person) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("person.idInternal", person.getIdInternal());
        List result = queryList(Qualification.class, criteria);
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentQualification#readByYearAndSchoolAndDegreeAndPerson(java.lang.String,
     *      java.lang.String, java.lang.String, Dominio.IPerson)
     */
    public IQualification readByDateAndSchoolAndPerson(Date date, String school, IPerson person)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("date", date);
        criteria.addEqualTo("school", school);
        criteria.addEqualTo("person.idInternal", person.getIdInternal());
        IQualification qualification = (IQualification) queryObject(Qualification.class, criteria);
        return qualification;
    }

}