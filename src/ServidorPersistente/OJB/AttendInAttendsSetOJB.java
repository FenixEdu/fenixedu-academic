/*
 * Created on 17/Ago/2004
 *
 */
package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.AttendInAttendsSet;
import Dominio.IAttendInAttendsSet;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentAttendInAttendsSet;

/**
 * @author joaosa & rmalo
 */

public class AttendInAttendsSetOJB extends ObjectFenixOJB 
				implements IPersistentAttendInAttendsSet
				{


    
        public List readAll() throws ExcepcaoPersistencia
    {
        return queryList(AttendInAttendsSet.class, new Criteria());

    }

    
    public void delete(IAttendInAttendsSet attendInAttendsSet) throws ExcepcaoPersistencia
    {
            super.delete(attendInAttendsSet);
    }       
}
