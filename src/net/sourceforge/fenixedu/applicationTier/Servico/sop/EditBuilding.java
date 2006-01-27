package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.domain.Campus;
import net.sourceforge.fenixedu.domain.space.OldBuilding;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EditBuilding extends Service {

    public void run(final Integer buildingID, final Integer campusID) throws ExcepcaoPersistencia, ExistingServiceException {
        final OldBuilding building = (OldBuilding) persistentObject.readByOID(OldBuilding.class, buildingID);
        final Campus campus = (Campus) persistentObject.readByOID(Campus.class, campusID);
        building.setCampus(campus);
    }

}