package ServidorPersistente.OJB.person.qualification;

import java.util.Date;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IPessoa;
import Dominio.IQualification;
import Dominio.Qualification;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentQualification;
import ServidorPersistente.OJB.ObjectFenixOJB;

/**
 * @author João Simas
 * @author Nuno Barbosa
 */
public class QualificationOJB extends ObjectFenixOJB implements IPersistentQualification
{
    public QualificationOJB()
    {
    }

    public List readQualificationsByPerson(IPessoa person) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("person.idInternal", person.getIdInternal());
        List result = queryList(Qualification.class, criteria);
        return result;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.IPersistentQualification#readByYearAndSchoolAndDegreeAndPerson(java.lang.String,
	 *      java.lang.String, java.lang.String, Dominio.IPessoa)
	 */
    public IQualification readByDateAndSchoolAndPerson(Date date, String school, IPessoa person)
        throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("date", date);
        criteria.addEqualTo("school", school);
        criteria.addEqualTo("person.idInternal", person.getIdInternal());
        IQualification qualification = (IQualification) queryObject(Qualification.class, criteria);
        return qualification;
    }

}
