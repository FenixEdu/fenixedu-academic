package net.sourceforge.fenixedu.applicationTier.Servico.space;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EditCampus extends Service {

    public void run(final Integer campusID, final Boolean asNewVersion, final String name) throws ExcepcaoPersistencia {
        final Campus campus = (Campus) persistentObject.readByOID(Campus.class, campusID);
        System.out.println("asNewVersion: " + asNewVersion);
        if (asNewVersion.booleanValue()) {
        	campus.edit(name);
        } else {
        	campus.getSpaceInformation().edit(name);
        }
    }

}
