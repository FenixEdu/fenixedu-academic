/*
 * Created on 31/Jul/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author lmac1
 */

public class DeleteDegreeCurricularPlans extends Service {

	public List run(List degreeCurricularPlansIds) throws FenixServiceException, ExcepcaoPersistencia {
		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = sp
				.getIPersistentDegreeCurricularPlan();

		Iterator<Integer> iter = degreeCurricularPlansIds.iterator();

		List<String> undeletedDegreeCurricularPlansNames = new ArrayList<String>();

		while (iter.hasNext()) {

			Integer degreeCurricularPlanId = (Integer) iter.next();
			DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) persistentDegreeCurricularPlan
					.readByOID(DegreeCurricularPlan.class, degreeCurricularPlanId);

			try {
				degreeCurricularPlan.delete();
			} catch (DomainException e) {
				undeletedDegreeCurricularPlansNames.add(degreeCurricularPlan.getName());
			}
		}

		return undeletedDegreeCurricularPlansNames;
	}
}