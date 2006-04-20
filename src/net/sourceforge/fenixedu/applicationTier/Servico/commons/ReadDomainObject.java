package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadDomainObject<E> extends Service {

    public E run(Class<E> clazz, final Integer domainObjectOID) throws ExcepcaoPersistencia {
        return (E) rootDomainObject.readDomainObjectByOID(clazz, domainObjectOID);
    }

}
