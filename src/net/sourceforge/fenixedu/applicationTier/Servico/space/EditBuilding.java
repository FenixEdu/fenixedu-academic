package net.sourceforge.fenixedu.applicationTier.Servico.space;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.space.Building;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EditBuilding extends Service {

    public void run(final Integer buildingID, final Boolean asNewVersion, final String name) throws ExcepcaoPersistencia {
        final Building building = (Building) persistentObject.readByOID(Building.class, buildingID);
        System.out.println("asNewVersion: " + asNewVersion);
        if (asNewVersion.booleanValue()) {
        	building.edit(name);
        } else {
        	building.getSpaceInformation().edit(name);
        }
    }

}
