/*
 * EditarSala.java Created on 27 de Outubro de 2002, 19:43
 */

package ServidorAplicacao.Servico.sop;

/**
 * Serviço EditarSala
 * 
 * @author tfc130
 */
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoRoom;
import DataBeans.RoomKey;
import Dominio.IBuilding;
import Dominio.ISala;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentBuilding;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class EditarSala implements IService {

    public Object run(RoomKey salaAntiga, InfoRoom salaNova) throws ExistingServiceException {

        ISala sala = null;
        boolean result = false;

        try {

            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            final IPersistentBuilding persistentBuilding = sp.getIPersistentBuilding();

            sala = sp.getISalaPersistente().readByName(salaAntiga.getNomeSala());

            if (sala != null) {

                if (!sala.getNome().equals(salaNova.getNome())) {
                    ISala roomWithSameName = sp.getISalaPersistente().readByName(salaNova.getNome());
                    if (roomWithSameName != null) {
                        throw new ExistingServiceException();
                    }
                }

                final IBuilding building = findBuilding(persistentBuilding, salaNova.getEdificio());

                sp.getISalaPersistente().simpleLockWrite(sala);
                sala.setNome(salaNova.getNome());
                sala.setEdificio(salaNova.getEdificio());
                sala.setPiso(salaNova.getPiso());
                sala.setCapacidadeNormal(salaNova.getCapacidadeNormal());
                sala.setCapacidadeExame(salaNova.getCapacidadeExame());
                sala.setTipo(salaNova.getTipo());
                sala.setBuilding(building);

                result = true;
            }
        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }

        return new Boolean(result);
    }

    protected IBuilding findBuilding(final IPersistentBuilding persistentBuilding, final String edificio)
            throws ExcepcaoPersistencia {
        final List buildings = persistentBuilding.readAll();
        return (IBuilding) CollectionUtils.find(buildings, new Predicate() {
            public boolean evaluate(Object arg0) {
                final IBuilding building = (IBuilding) arg0;
                return building.getName().equalsIgnoreCase(edificio);
            }
        });
    }

}