package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class LerSalas extends Service {

    public Object run() throws ExcepcaoPersistencia {
	final List infoSalas = new ArrayList();
	for (final OldRoom oldRoom : OldRoom.getOldRooms()) {
	    infoSalas.add(InfoRoom.newInfoFromDomain(oldRoom));
	}
	return infoSalas;
    }

}