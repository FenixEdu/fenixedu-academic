/*
 * Created on 21/Out/2003
 *
 */
package ServidorPersistente;

import java.util.List;

import Dominio.IPeriod;
import Dominio.IRoomOccupation;

/**
 * @author Ana e Ricardo
 *  
 */
public interface IPersistentRoomOccupation extends IPersistentObject {
    //public List readBy(Room room) throws ExcepcaoPersistencia;
    public List readAll() throws ExcepcaoPersistencia;

    public void delete(IRoomOccupation roomOccupation) throws ExcepcaoPersistencia;

    public List readByPeriod(IPeriod period) throws ExcepcaoPersistencia;
    //public void deleteAll() throws ExcepcaoPersistencia;
}