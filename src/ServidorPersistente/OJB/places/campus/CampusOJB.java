/*
 * Created on Dec 5, 2003 by jpvl
 *
 */
package ServidorPersistente.OJB.places.campus;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.Campus;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.PersistentObjectOJB;
import ServidorPersistente.places.campus.IPersistentCampus;

/**
 * @author jpvl
 */
public class CampusOJB extends PersistentObjectOJB implements IPersistentCampus {

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.places.campus.IPersistentCampus#readAll()
     */
    public List readAll() throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        return queryList(Campus.class, criteria);
    }

}