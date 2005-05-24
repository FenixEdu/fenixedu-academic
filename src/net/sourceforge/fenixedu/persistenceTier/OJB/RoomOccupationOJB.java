/*
 * Created on 21/Out/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.IRoomOccupation;
import net.sourceforge.fenixedu.domain.RoomOccupation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentRoomOccupation;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author Ana e Ricardo
 *  
 */
public class RoomOccupationOJB extends ObjectFenixOJB implements IPersistentRoomOccupation {
    /*
     * public List readBy(Room room) throws ExcepcaoPersistencia { Criteria
     * criteria = new Criteria(); criteria.addEqualTo("keyRoom",
     * room.getIdInternal()); return queryList(RoomOccupation.class, criteria); }
     */
    public List readAll() throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        return queryList(RoomOccupation.class, criteria);
    }

    /*
     * public List readAll() throws ExcepcaoPersistencia { try { String oqlQuery =
     * "select all from " + RoomOccupation.class.getName(); // oqlQuery += "
     * order by season asc"; query.create(oqlQuery); List result = (List)
     * query.execute(); lockRead(result); return result; } catch (QueryException
     * ex) { throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex); } }
     */
    public void delete(IRoomOccupation roomOccupation) throws ExcepcaoPersistencia {
        // TO DO falta apagar as ligações a outras tabelas
        super.delete(roomOccupation);

    }
}