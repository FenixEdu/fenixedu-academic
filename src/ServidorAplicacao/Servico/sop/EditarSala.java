/*
 * EditarSala.java Created on 27 de Outubro de 2002, 19:43
 */

package ServidorAplicacao.Servico.sop;

/**
 * Serviço EditarSala
 * 
 * @author tfc130
 */
import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoRoom;
import DataBeans.RoomKey;
import Dominio.ISala;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class EditarSala implements IService {

    /**
     * The actor of this class.
     */
    public EditarSala() {
    }

    public Object run(RoomKey salaAntiga, InfoRoom salaNova) throws ExistingServiceException {

        ISala sala = null;
        boolean result = false;

        try {

            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            sala = sp.getISalaPersistente().readByName(salaAntiga.getNomeSala());

            if (sala != null) {

                if (!sala.getNome().equals(salaNova.getNome())) {
                    ISala roomWithSameName = sp.getISalaPersistente().readByName(salaNova.getNome());
                    if (roomWithSameName != null) {
                        throw new ExistingServiceException();
                    }
                }

                sp.getISalaPersistente().simpleLockWrite(sala);
                sala.setNome(salaNova.getNome());
                sala.setEdificio(salaNova.getEdificio());
                sala.setPiso(salaNova.getPiso());
                sala.setCapacidadeNormal(salaNova.getCapacidadeNormal());
                sala.setCapacidadeExame(salaNova.getCapacidadeExame());
                sala.setTipo(salaNova.getTipo());
                result = true;
            }
        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }

        return new Boolean(result);
    }

}