package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.RoomKey;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class LerSala extends Service {

    public Object run(RoomKey keySala) throws ExcepcaoPersistencia {
	final OldRoom sala = OldRoom.findOldRoomByName(keySala.getNomeSala());
	return sala == null ? null : InfoRoom.newInfoFromDomain(sala);
    }

}