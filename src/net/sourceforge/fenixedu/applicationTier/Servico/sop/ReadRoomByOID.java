/*
 * Created on 2003/07/30
 *
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.domain.Room;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISalaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 *  
 */
public class ReadRoomByOID implements IServico {

    private static ReadRoomByOID service = new ReadRoomByOID();

    /**
     * The singleton access method of this class.
     */
    public static ReadRoomByOID getService() {
        return service;
    }

    /**
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome() {
        return "ReadRoomByOID";
    }

    public InfoRoom run(Integer oid) throws FenixServiceException {

        InfoRoom result = null;
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            ISalaPersistente roomDAO = sp.getISalaPersistente();
            IRoom room = (IRoom) roomDAO.readByOID(Room.class, oid);
            if (room != null) {
                result = Cloner.copyRoom2InfoRoom(room);
            }
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }

        return result;
    }
}