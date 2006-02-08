package net.sourceforge.fenixedu.applicationTier.Servico.space;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.space.Building;
import net.sourceforge.fenixedu.domain.space.Room;
import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateRoom extends Service {

    public void run(final Integer suroundingSpaceID, final String name) throws ExcepcaoPersistencia {
        final Space space = (Space) persistentObject.readByOID(Space.class, suroundingSpaceID);
        final Room room = DomainFactory.makeRoom(space, name);
    }

}
