package ServidorPersistente.OJB;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IUniversity;
import Dominio.University;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentUniversity;

/**
 * @author David Santos 28/Out/2003
 */

public class UniversityOJB extends PersistentObjectOJB implements IPersistentUniversity {

    public UniversityOJB() {
    }

    public IUniversity readByNameAndCode(String name, String code) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("name", name);
        criteria.addEqualTo("code", code);
        return (IUniversity) queryObject(University.class, criteria);
    }

}