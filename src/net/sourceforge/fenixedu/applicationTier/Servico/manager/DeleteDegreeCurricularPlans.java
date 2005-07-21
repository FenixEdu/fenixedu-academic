/*
 * Created on 31/Jul/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author lmac1
 */

public class DeleteDegreeCurricularPlans implements IService {

    public List run(List degreeCurricularPlansIds) throws FenixServiceException {

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = sp.getIPersistentDegreeCurricularPlan();
            
            Iterator<Integer> iter = degreeCurricularPlansIds.iterator();

            List<String> undeletedDegreeCurricularPlansNames = new ArrayList<String>();

            while (iter.hasNext()) {

				Integer degreeCurricularPlanId = (Integer) iter.next();
				IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) persistentDegreeCurricularPlan.readByOID(
                        DegreeCurricularPlan.class, degreeCurricularPlanId);
 
				try {
					degreeCurricularPlan.delete();
				}
				catch (DomainException e) {
					undeletedDegreeCurricularPlansNames.add(degreeCurricularPlan.getName());
				}	
            }

            return undeletedDegreeCurricularPlansNames;

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}