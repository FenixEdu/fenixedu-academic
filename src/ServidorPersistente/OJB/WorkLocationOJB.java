/*
 * Created on Oct 13, 2003
 *  
 */
package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IWorkLocation;
import Dominio.WorkLocation;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentWorkLocation;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class WorkLocationOJB extends ObjectFenixOJB implements IPersistentWorkLocation
{

    /** Creates a new instance of WorkLocationOJB */
    public WorkLocationOJB()
    {
    }

    public IWorkLocation readByName(String name) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("name", name);

        return (IWorkLocation) queryObject(WorkLocation.class, criteria);
    }

    public IWorkLocation readByOID(Integer oid) throws ExcepcaoPersistencia
    {
        return readByOID(oid);
    }

    public List readAll() throws ExcepcaoPersistencia
    {
        return queryList(WorkLocation.class, new Criteria());
    }
}