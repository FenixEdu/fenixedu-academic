package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.ICurso;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */

public class DeleteDegrees implements IService {

    // delete a set of degrees
    public List run(List degreesInternalIds) throws FenixServiceException {

        try {

            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            ICursoPersistente persistentDegree = sp.getICursoPersistente();
            IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = sp
                    .getIPersistentDegreeCurricularPlan();

            Iterator iter = degreesInternalIds.iterator();
            List degreeCurricularPlans;

            List undeletedDegreesNames = new ArrayList();

            while (iter.hasNext()) {

                Integer internalId = (Integer) iter.next();
                ICurso degree = persistentDegree.readByIdInternal(internalId);
                if (degree != null) {
                    degreeCurricularPlans = persistentDegreeCurricularPlan.readByDegree(degree);
                    if (degreeCurricularPlans.isEmpty())
                        persistentDegree.delete(degree);
                    else
                        undeletedDegreesNames.add(degree.getNome());
                }
            }

            return undeletedDegreesNames;

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

    }

}