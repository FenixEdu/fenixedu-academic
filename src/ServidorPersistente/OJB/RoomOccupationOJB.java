/*
 * Created on 21/Out/2003
 *
 */
package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IPeriod;
import Dominio.IRoomOccupation;
import Dominio.RoomOccupation;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentRoomOccupation;

/**
 * @author Ana e Ricardo
 *  
 */
public class RoomOccupationOJB extends ObjectFenixOJB implements IPersistentRoomOccupation {
    /*
     * public List readBy(Sala room) throws ExcepcaoPersistencia { Criteria
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

    public List readByPeriod(IPeriod period) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyPeriod", period.getIdInternal());
        return queryList(RoomOccupation.class, criteria);

    }

    /*
     * public void deleteAll() throws ExcepcaoPersistencia { String oqlQuery =
     * "select all from " + RoomOccupation.class.getName();
     * super.deleteAll(oqlQuery); }
     */
}