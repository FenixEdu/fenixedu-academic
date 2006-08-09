package net.sourceforge.fenixedu.applicationTier.Servico.space;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EditBuilding extends Service {

    public void run(final Integer buildingInformationID, final Boolean asNewVersion, final String name) throws ExcepcaoPersistencia {
//    	final BuildingInformation buildingInformation = (BuildingInformation) rootDomainObject.readSpaceInformationByOID(buildingInformationID);
//        if (asNewVersion.booleanValue()) {
//        	final Building building = (Building) buildingInformation.getSpace();
//        	building.edit(name);
//        } else {
//        	buildingInformation.edit(name);
//        }
    }

}
