/*
 * Created on 21/Out/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.List;

import net.sourceforge.fenixedu.domain.RoomOccupation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentRoomOccupation;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author Ana e Ricardo
 * 
 */
public class RoomOccupationVO extends VersionedObjectsBase implements IPersistentRoomOccupation {

    public List readAll() throws ExcepcaoPersistencia {
        return (List) readAll(RoomOccupation.class);
    }

}