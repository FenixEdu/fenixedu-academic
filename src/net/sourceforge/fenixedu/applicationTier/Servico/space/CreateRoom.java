package net.sourceforge.fenixedu.applicationTier.Servico.space;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateRoom extends Service {

    public void run(final Integer suroundingSpaceID, final String name) throws ExcepcaoPersistencia {
        final Space space = rootDomainObject.readSpaceByOID(suroundingSpaceID);
//        new Room(space, name);
    }

}
