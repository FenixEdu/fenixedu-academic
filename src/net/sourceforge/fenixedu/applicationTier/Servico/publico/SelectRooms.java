/*
 * SelectRooms.java
 *
 * Created on January 12th, 2003, 01:25
 */

package net.sourceforge.fenixedu.applicationTier.Servico.publico;

/**
 * Service SelectRooms.
 * 
 * @author tfc130
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class SelectRooms implements IService {

	public Object run(InfoRoom infoRoom) throws ExcepcaoPersistencia {
		List salas = null;

		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		Integer tipo = infoRoom.getTipo() != null ? infoRoom.getTipo().getTipo() : null;

		salas = sp.getISalaPersistente().readSalas(infoRoom.getNome(), infoRoom.getEdificio(),
				infoRoom.getPiso(), tipo, infoRoom.getCapacidadeNormal(), infoRoom.getCapacidadeExame());

		if (salas == null)
			return new ArrayList();

		Iterator iter = salas.iterator();
		List salasView = new ArrayList();
		IRoom sala;

		while (iter.hasNext()) {
			sala = (IRoom) iter.next();
			salasView.add(InfoRoom.newInfoFromDomain(sala));
		}

		return salasView;
	}

}