package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ICursoPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

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
                IDegree degree = persistentDegree.readByIdInternal(internalId);
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