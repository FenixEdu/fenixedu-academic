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
 * @author Pedro Santos e Rita Carvalho
 */
import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoRoom;
import DataBeans.util.Cloner;
import Dominio.ISala;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISalaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class CriarSala implements IService {

    public Object run(InfoRoom infoSala) throws FenixServiceException, ExcepcaoPersistencia {

        final ISuportePersistente sp = SuportePersistenteOJB.getInstance();

        final ISala room = Cloner.copyInfoRoom2Room(infoSala);

        final ISalaPersistente roomDAO = sp.getISalaPersistente();
        final ISala existingRoom = roomDAO.readByName(infoSala.getNome());

        if (existingRoom != null) {
            throw new ExistingServiceException("Duplicate Entry: " + infoSala.getNome());
        }

        roomDAO.simpleLockWrite(room);

        return Cloner.copyRoom2InfoRoom(room);

    }

}