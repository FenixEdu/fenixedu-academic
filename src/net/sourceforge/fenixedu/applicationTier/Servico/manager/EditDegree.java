/*
 * Created on 29/Jul/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ICursoPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTier.exceptions.ExistingPersistentException;
import pt.utl.ist.berserk.logic.serviceManager.IService;

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