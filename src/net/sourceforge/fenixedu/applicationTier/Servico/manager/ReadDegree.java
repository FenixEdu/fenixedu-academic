package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author lmac1
 */

public class ReadDegree implements IService {

    /**
     * Executes the service. Returns the current infodegree.
     */
    public InfoDegree run(Integer idInternal) throws FenixServiceException {
        ISuportePersistente sp;
        InfoDegree infoDegree = null;
        IDegree degree = null;

        try {
            sp = SuportePersistenteOJB.getInstance();
            degree = sp.getICursoPersistente().readByIdInternal(idInternal);

        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }

        if (degree == null) {
            throw new NonExistingServiceException();
        }

        infoDegree = Cloner.copyIDegree2InfoDegree(degree);
        return infoDegree;
    }
}