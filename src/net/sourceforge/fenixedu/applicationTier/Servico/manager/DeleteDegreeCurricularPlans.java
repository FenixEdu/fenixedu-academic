/*
 * Created on 31/Jul/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author lmac1
 */

public class DeleteDegreeCurricularPlans extends Service {

    public List run(List degreeCurricularPlansIds) throws FenixServiceException {
	Iterator<Integer> iter = degreeCurricularPlansIds.iterator();

	List<String> undeletedDegreeCurricularPlansNames = new ArrayList<String>();

	while (iter.hasNext()) {

	    Integer degreeCurricularPlanId = (Integer) iter.next();
	    DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanId);

	    try {
		degreeCurricularPlan.delete();
	    } catch (DomainException e) {
		undeletedDegreeCurricularPlansNames.add(degreeCurricularPlan.getName());
	    }
	}

	return undeletedDegreeCurricularPlansNames;
    }
}