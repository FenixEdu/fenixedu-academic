package net.sourceforge.fenixedu.applicationTier.Servico.space;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.space.Floor;
import net.sourceforge.fenixedu.domain.space.FloorInformation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EditFloor extends Service {

    public void run(final Integer floorInformationID, final Boolean asNewVersion, final Integer level) throws ExcepcaoPersistencia {
    	final FloorInformation floorInformation = (FloorInformation) rootDomainObject.readSpaceInformationByOID(floorInformationID);
        if (asNewVersion) {
        	final Floor floor = (Floor) floorInformation.getSpace();
//        	floor.edit(level);
//        } else {
//        	floorInformation.edit(level);
        }
    }

}
