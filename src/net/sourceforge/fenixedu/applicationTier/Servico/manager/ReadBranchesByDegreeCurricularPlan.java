/*
 * Created on 22/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoBranch;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author lmac1
 */

public class ReadBranchesByDegreeCurricularPlan extends Service {

	public List run(Integer idDegreeCurricularPlan) throws FenixServiceException, ExcepcaoPersistencia {
		ISuportePersistente sp;
		List allBranches = null;

		sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) sp
				.getIPersistentDegreeCurricularPlan().readByOID(DegreeCurricularPlan.class,
						idDegreeCurricularPlan);
		if (degreeCurricularPlan == null) {
			throw new NonExistingServiceException();
		}
		allBranches = degreeCurricularPlan.getAreas();

		if (allBranches == null || allBranches.isEmpty()) {
			return null;
		}

		// build the result of this service
		Iterator iterator = allBranches.iterator();
		List result = new ArrayList(allBranches.size());

		while (iterator.hasNext()) {
			result.add(InfoBranch.newInfoFromDomain((Branch) iterator.next()));
		}
		return result;
	}
}