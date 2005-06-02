package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.IWorkLocation;
import net.sourceforge.fenixedu.domain.WorkLocation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentWorkLocation;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class WorkLocationVO extends VersionedObjectsBase implements IPersistentWorkLocation {

    public IWorkLocation readByName(String name) throws ExcepcaoPersistencia {

        Collection<IWorkLocation> workLocations = readAll(WorkLocation.class);

        for (IWorkLocation workLocation : workLocations) {
            if (workLocation.getName().equals(name)) {
                return workLocation;
            }
        }

        return null;
    }

    public List readAll() throws ExcepcaoPersistencia {

        return (List) readAll(WorkLocation.class);
    }
}