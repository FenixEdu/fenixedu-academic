package ServidorPersistente.OJB.person.qualification;

import java.util.Date;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IPerson;
import Dominio.IQualification;
import Dominio.Qualification;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentQualification;
import ServidorPersistente.OJB.PersistentObjectOJB;

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