package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.Institution;
import net.sourceforge.fenixedu.domain.Institution;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentInstitution;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class InstitutionVO extends VersionedObjectsBase implements IPersistentInstitution {

    public Institution readByName(String name) throws ExcepcaoPersistencia {

        Collection<Institution> institutions = readAll(Institution.class);

        for (Institution institution : institutions) {
            if (institution.getName().equals(name)) {
                return institution;
            }
        }

        return null;
    }

    public List readAll() throws ExcepcaoPersistencia {

        return (List) readAll(Institution.class);
    }
}