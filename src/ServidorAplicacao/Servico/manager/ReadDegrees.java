package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.util.Cloner;
import Dominio.ICurso;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */

public class ReadDegrees implements IService {

    /**
     * Executes the service. Returns the current collection of infodegrees .
     */
    public List run() throws FenixServiceException {
        ISuportePersistente sp;
        List allDegrees = null;

        try {
            sp = SuportePersistenteOJB.getInstance();
            allDegrees = sp.getICursoPersistente().readAll();
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }

        if (allDegrees == null || allDegrees.isEmpty())
            return allDegrees;

        // build the result of this service
        Iterator iterator = allDegrees.iterator();
        List result = new ArrayList(allDegrees.size());

        while (iterator.hasNext())
            result.add(Cloner.copyIDegree2InfoDegree((ICurso) iterator.next()));

        return result;
    }
}