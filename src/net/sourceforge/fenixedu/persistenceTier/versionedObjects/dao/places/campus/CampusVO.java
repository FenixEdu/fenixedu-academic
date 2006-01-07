package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.places.campus;

import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.Campus;
import net.sourceforge.fenixedu.domain.Campus;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.places.campus.IPersistentCampus;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author jpvl
 */
public class CampusVO extends VersionedObjectsBase implements IPersistentCampus {

    public List readAll() throws ExcepcaoPersistencia {
        return (List) readAll(Campus.class);
    }

    public Campus readByName(final String campusName) throws ExcepcaoPersistencia {

        Collection campusList = readAll(Campus.class);

        return (Campus) CollectionUtils.find(campusList, new Predicate() {

            public boolean evaluate(Object arg0) {
                Campus campus = (Campus) arg0;
                if (campus.getName().equals(campusName)) {
                    return true;
                }
                return false;
            }

        });

    }

}