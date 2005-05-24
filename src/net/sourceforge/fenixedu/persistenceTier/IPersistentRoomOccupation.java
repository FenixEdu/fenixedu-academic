/*
 * Created on 21/Out/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.IRoomOccupation;

/**
 * @author Ana e Ricardo
 *  
 */
public interface IPersistentRoomOccupation extends IPersistentObject {
    //public List readBy(Room room) throws ExcepcaoPersistencia;
    public List readAll() throws ExcepcaoPersistencia;

    public void delete(IRoomOccupation roomOccupation) throws ExcepcaoPersistencia;
}