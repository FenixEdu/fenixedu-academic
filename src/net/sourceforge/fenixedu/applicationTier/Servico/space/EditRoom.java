package net.sourceforge.fenixedu.applicationTier.Servico.space;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.space.Room;
import net.sourceforge.fenixedu.domain.space.RoomInformation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EditRoom extends Service {

    public void run(final Integer roomInformationID, final Boolean asNewVersion, final String name) throws ExcepcaoPersistencia {
    	final RoomInformation roomInformation = (RoomInformation) rootDomainObject.readSpaceInformationByOID(roomInformationID);
        if (asNewVersion.booleanValue()) {
        	final Room room = (Room) roomInformation.getSpace();
//        	room.edit(name);
        } else {
        	roomInformation.edit(name);
        }
    }

}
