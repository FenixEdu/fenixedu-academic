/*
 * Created on 21/Out/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

/**
 * @author Ana e Ricardo
 *  
 */
public interface IPersistentRoomOccupation extends IPersistentObject {
    
    public List readAll() throws ExcepcaoPersistencia;

}