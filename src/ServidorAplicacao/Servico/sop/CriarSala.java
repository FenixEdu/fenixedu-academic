/*
 * CriarSala.java
 *
 * Created on 25 de Outubro de 2002, ??:??
 */

package ServidorAplicacao.Servico.sop;

/**
 * Serviço CriarSala.
 * 
 * @author tfc130
 */
import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoRoom;
import Dominio.ISala;
import Dominio.Sala;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

public class CriarSala implements IService {

    /**
     * The actor of this class.
     */
    public CriarSala() {
    }

    public Object run(InfoRoom infoSala) throws FenixServiceException {

        ISala sala = null;
        boolean result = false;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            sala = new Sala(infoSala.getNome(), infoSala.getEdificio(), infoSala.getPiso(), infoSala
                    .getTipo(), infoSala.getCapacidadeNormal(), infoSala.getCapacidadeExame());
            try {
                sp.getISalaPersistente().simpleLockWrite(sala);
                result = true;
            } catch (ExistingPersistentException ex) {
                throw new ExistingRoomServiceException(ex);
            }
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex.getMessage());
        }

        return new Boolean(result);
    }

    public class ExistingRoomServiceException extends FenixServiceException {

        /**
         *  
         */
        private ExistingRoomServiceException() {
            super();
        }

        /**
         * @param cause
         */
        ExistingRoomServiceException(Throwable cause) {
            super(cause);
        }

    }

}