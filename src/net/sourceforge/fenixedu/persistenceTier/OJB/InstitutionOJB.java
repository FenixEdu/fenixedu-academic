/*
 * Created on Apr 28, 2005
 *
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.Institution;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentInstitution;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author Ricardo Rodrigues
 *
 */

public class InstitutionOJB extends PersistentObjectOJB implements IPersistentInstitution{

    public InstitutionOJB() {
    }
    
    public List readByName(String name) throws ExcepcaoPersistencia{
        Criteria criteria = new Criteria();
        criteria.addLike("name", name);
        return queryList(Institution.class, criteria);
    }
    
    public List readAll() throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        return queryList(Institution.class, criteria);
    }

}
