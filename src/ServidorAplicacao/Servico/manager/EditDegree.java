/*
 * Created on 29/Jul/2003
 */
package ServidorAplicacao.Servico.manager;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoDegree;
import Dominio.IDegree;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author lmac1
 */
public class EditDegree implements IService {

    /**
     * The constructor of this class.
     */
    public EditDegree() {
    }

    /**
     * Executes the service.
     */

    public void run(InfoDegree newInfoDegree) throws FenixServiceException {

        ISuportePersistente persistentSuport = null;
        ICursoPersistente persistentDegree = null;
        IDegree oldDegree = null;

        try {

            persistentSuport = SuportePersistenteOJB.getInstance();
            persistentDegree = persistentSuport.getICursoPersistente();
            oldDegree = persistentDegree.readByIdInternal(newInfoDegree.getIdInternal());

            if (oldDegree == null) {
                throw new NonExistingServiceException();
            }
            persistentDegree.simpleLockWrite(oldDegree);
            oldDegree.setNome(newInfoDegree.getNome());
            oldDegree.setSigla(newInfoDegree.getSigla());
            oldDegree.setTipoCurso(newInfoDegree.getTipoCurso());

        } catch (ExistingPersistentException ex) {
            throw new ExistingServiceException(ex);
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }
    }
}