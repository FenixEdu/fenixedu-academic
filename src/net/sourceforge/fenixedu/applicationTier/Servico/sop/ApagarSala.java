/*
 * ApagarSala.java
 *
 * Created on 25 de Outubro de 2002, 15:36
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Serviço ApagarSala.
 * 
 * @author tfc130
 */
import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.RoomKey;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.exceptions.notAuthorizedPersistentDeleteException;

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

        IRoom sala1 = null;
        boolean result = false;

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
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