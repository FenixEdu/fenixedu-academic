/*
 * Created on 2003/07/30
 *
 *
 */
package ServidorAplicacao.Servico.sop;

import DataBeans.InfoRoom;
import DataBeans.util.Cloner;
import Dominio.IRoom;
import Dominio.Room;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISalaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
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