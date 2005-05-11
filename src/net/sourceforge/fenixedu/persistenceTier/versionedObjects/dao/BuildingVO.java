package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.List;

import net.sourceforge.fenixedu.domain.Building;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentBuilding;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author Luis Cruz
 * 
 */
public class BuildingVO extends VersionedObjectsBase implements IPersistentBuilding {

    public List readAll() throws ExcepcaoPersistencia {
        return (List) readAll(Building.class);
    }
}