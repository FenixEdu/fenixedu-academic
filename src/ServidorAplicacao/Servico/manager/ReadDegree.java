package ServidorAplicacao.Servico.manager;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoDegree;
import DataBeans.util.Cloner;
import Dominio.IDegree;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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