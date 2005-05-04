package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.IAdvisory;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.persistenceTier.IPersistentAdvisory;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * 
 * @author Luis Cruz
 */
public class AdvisoryVO extends VersionedObjectsBase implements IPersistentAdvisory {

    public void write(final IAdvisory advisory, final List group) {
        for (final Iterator iterator = group.iterator(); iterator.hasNext();) {
            final IPerson person = (IPerson) iterator.next();
            person.getAdvisories().add(advisory);
            advisory.getPeople().add(person);
        }
    }

}