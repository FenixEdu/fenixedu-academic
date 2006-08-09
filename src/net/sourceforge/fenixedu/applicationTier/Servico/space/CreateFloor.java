package net.sourceforge.fenixedu.applicationTier.Servico.space;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateFloor extends Service {

    public void run(final Integer suroundingSpaceID, final Integer level) throws ExcepcaoPersistencia {
        final Space space = rootDomainObject.readSpaceByOID(suroundingSpaceID);
//        new Floor(space, level);
    }

}
