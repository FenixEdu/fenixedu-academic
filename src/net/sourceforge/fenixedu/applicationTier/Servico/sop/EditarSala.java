/*
 * EditarSala.java Created on 27 de Outubro de 2002, 19:43
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Serviço EditarSala
 * 
 * @author tfc130
 */
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.RoomKey;
import net.sourceforge.fenixedu.domain.space.Building;
import net.sourceforge.fenixedu.domain.space.Room;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentBuilding;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;

public class EditarSala implements IService {

	public Object run(RoomKey salaAntiga, InfoRoom salaNova) throws ExistingServiceException,
			ExcepcaoPersistencia {
		Room sala = null;
		boolean result = false;

		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		final IPersistentBuilding persistentBuilding = sp.getIPersistentBuilding();

		sala = sp.getISalaPersistente().readByName(salaAntiga.getNomeSala());

		if (sala != null) {

			if (!sala.getNome().equals(salaNova.getNome())) {
				Room roomWithSameName = sp.getISalaPersistente().readByName(salaNova.getNome());
				if (roomWithSameName != null) {
					throw new ExistingServiceException();
				}
			}

			final Building building = findBuilding(persistentBuilding, salaNova.getEdificio());

			sp.getISalaPersistente().simpleLockWrite(sala);
			sala.setNome(salaNova.getNome());
			sala.setPiso(salaNova.getPiso());
			sala.setCapacidadeNormal(salaNova.getCapacidadeNormal());
			sala.setCapacidadeExame(salaNova.getCapacidadeExame());
			sala.setTipo(salaNova.getTipo());
			sala.setBuilding(building);

			result = true;
		}

		return new Boolean(result);
	}

	protected Building findBuilding(final IPersistentBuilding persistentBuilding, final String edificio)
			throws ExcepcaoPersistencia {
		final List buildings = persistentBuilding.readAll();
		return (Building) CollectionUtils.find(buildings, new Predicate() {
			public boolean evaluate(Object arg0) {
				final Building building = (Building) arg0;
				return building.getName().equalsIgnoreCase(edificio);
			}
		});
	}

}