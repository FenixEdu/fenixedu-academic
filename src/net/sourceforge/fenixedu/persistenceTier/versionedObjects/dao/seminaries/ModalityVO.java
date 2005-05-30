/*
 * Created on Jul 23, 2003
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.seminaries;

import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.Seminaries.IModality;
import net.sourceforge.fenixedu.domain.Seminaries.Modality;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryModality;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at Jul 23, 2003, 9:49:19 AM
 *  
 */
public class ModalityVO extends VersionedObjectsBase implements IPersistentSeminaryModality {

    public List readAll() {
        return (List) readAll(Modality.class);
    }

    public IModality readByName(final String name) {
        final Collection<IModality> modalities = readAll(Modality.class);
        for (final IModality modality : modalities) {
            if (modality.getName().equals(name)) {
                return modality;
            }
        }
        return null;
    }


}