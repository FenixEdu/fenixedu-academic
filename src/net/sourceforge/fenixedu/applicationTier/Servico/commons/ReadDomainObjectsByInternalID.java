package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadDomainObjectsByInternalID<E> extends Service {

    public Collection<E> run(final Class<E> clazz, final Integer[] domainObjectOIDs)
            throws ExcepcaoPersistencia {
        final Collection<E> domainObjects = new ArrayList<E>(domainObjectOIDs.length);
        for (final Integer domainObjectOID : domainObjectOIDs) {
            domainObjects.add((E) persistentObject.readByOID(clazz, domainObjectOID));
        }

        return domainObjects;
    }

}
