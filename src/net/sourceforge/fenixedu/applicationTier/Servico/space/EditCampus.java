package net.sourceforge.fenixedu.applicationTier.Servico.space;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EditCampus extends Service {

    public void run(final Integer campusID, final String name) throws ExcepcaoPersistencia {
        final Campus campus = (Campus) persistentObject.readByOID(Campus.class, campusID);
        campus.getSpaceInformation().setName(name);
    }

}
