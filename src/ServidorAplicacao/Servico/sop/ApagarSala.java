/*
 * ApagarSala.java
 *
 * Created on 25 de Outubro de 2002, 15:36
 */

package ServidorAplicacao.Servico.sop;

/**
 * Serviço ApagarSala.
 * 
 * @author tfc130
 */
import DataBeans.RoomKey;
import Dominio.ISala;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.notAuthorizedPersistentDeleteException;

public class ApagarSala implements IServico {

    private static ApagarSala _servico = new ApagarSala();

    /**
     * The singleton access method of this class.
     */
    public static ApagarSala getService() {
        return _servico;
    }

    /**
     * The actor of this class.
     */
    private ApagarSala() {
    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome() {
        return "ApagarSala";
    }

    public Object run(RoomKey keySala) throws FenixServiceException {

        ISala sala1 = null;
        boolean result = false;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            sala1 = sp.getISalaPersistente().readByName(keySala.getNomeSala());
            if (sala1 != null) {
                sp.getISalaPersistente().delete(sala1);
                result = true;
            }
        } catch (notAuthorizedPersistentDeleteException ex) {
            throw new NotAuthorizedServiceDeleteRoomException(ex);
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }

        return new Boolean(result);
    }

    public class NotAuthorizedServiceDeleteRoomException extends FenixServiceException {

        /**
         *  
         */
        private NotAuthorizedServiceDeleteRoomException() {
            super();
        }

        /**
         * @param cause
         */
        NotAuthorizedServiceDeleteRoomException(Throwable cause) {
            super(cause);
        }

    }

}