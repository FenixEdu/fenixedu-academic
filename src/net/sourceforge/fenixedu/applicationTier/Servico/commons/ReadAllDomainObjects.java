package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadAllDomainObjects<E> extends Service {

    public Collection<E> run(Class<E> clazz) throws ExcepcaoPersistencia {
        return (Collection<E>) persistentObject.readAll(clazz);
    }

}
