/*
 * Created on 29/Jul/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author lmac1
 */

public class ReadDegreeCurricularPlansByDegree implements IService {

    /**
     * Executes the service. Returns the current collection of
     * infoDegreeCurricularPlans.
     */
    public List run(Integer idDegree) throws FenixServiceException {
        ISuportePersistente sp;
        List allDegreeCurricularPlans = null;
        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IDegree degree = (IDegree)sp.getICursoPersistente().readByOID(Degree.class,idDegree);
            allDegreeCurricularPlans = degree.getDegreeCurricularPlans();
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }

        if (allDegreeCurricularPlans == null || allDegreeCurricularPlans.isEmpty())
            return allDegreeCurricularPlans;

        // build the result of this service
        Iterator iterator = allDegreeCurricularPlans.iterator();
        List result = new ArrayList(allDegreeCurricularPlans.size());

        while (iterator.hasNext()) {
            IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) iterator.next();
            result.add(Cloner.copyIDegreeCurricularPlan2InfoDegreeCurricularPlan(degreeCurricularPlan));
        }
        return result;
    }
}