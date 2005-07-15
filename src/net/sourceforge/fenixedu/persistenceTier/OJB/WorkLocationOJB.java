/*
 * Created on Oct 13, 2003
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.IWorkLocation;
import net.sourceforge.fenixedu.domain.WorkLocation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentWorkLocation;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class WorkLocationOJB extends PersistentObjectOJB implements IPersistentWorkLocation {

    public IWorkLocation readByName(String name) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("name", name);

        return (IWorkLocation) queryObject(WorkLocation.class, criteria);
    }

    public List readAll() throws ExcepcaoPersistencia {
        return queryList(WorkLocation.class, new Criteria());
    }

}