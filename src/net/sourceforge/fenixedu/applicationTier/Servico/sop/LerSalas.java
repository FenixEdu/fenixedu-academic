package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Servi�o LerSalas
 * 
 * @author tfc130
 * @version
 */

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class LerSalas extends Service {

	public Object run() throws ExcepcaoPersistencia {
		List infoSalas = new ArrayList();
		for (final OldRoom oldRoom : rootDomainObject.getOldRoomsSet()) {
			infoSalas.add(new InfoRoom(oldRoom.getNome(), oldRoom.getBuilding().getName(), oldRoom.getPiso(),
					oldRoom.getTipo(), oldRoom.getCapacidadeNormal(), oldRoom.getCapacidadeExame()));
		}
		return infoSalas;
	}

}