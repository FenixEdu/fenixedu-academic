package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.places.campus;

import java.util.List;

import net.sourceforge.fenixedu.domain.Campus;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.places.campus.IPersistentCampus;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author jpvl
 */
public class CampusVO extends VersionedObjectsBase implements IPersistentCampus {

    public List readAll() throws ExcepcaoPersistencia {
        return (List) readAll(Campus.class);
    }

}