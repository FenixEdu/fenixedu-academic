/*
 * Created on Oct 13, 2003
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.IInstitution;
import net.sourceforge.fenixedu.domain.Institution;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentInstitution;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class InstitutionOJB extends PersistentObjectOJB implements IPersistentInstitution {

    public IInstitution readByName(String name) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("name", name);

        return (IInstitution) queryObject(Institution.class, criteria);
    }

    public List readAll() throws ExcepcaoPersistencia {
        return queryList(Institution.class, new Criteria());
    }

}