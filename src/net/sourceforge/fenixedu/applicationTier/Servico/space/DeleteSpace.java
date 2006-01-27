package net.sourceforge.fenixedu.applicationTier.Servico.space;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteSpace extends Service {

    public void run(final Integer spaceID) throws ExcepcaoPersistencia {
        final Space space = (Space) persistentObject.readByOID(Space.class, spaceID);
        space.delete();
    }

}
