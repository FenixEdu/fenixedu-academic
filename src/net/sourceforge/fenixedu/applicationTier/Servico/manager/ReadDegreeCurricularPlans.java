/*
 * Created on 4/Set/2003, 13:55:41
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlanWithDegree;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 4/Set/2003, 13:55:41
 * 
 */
public class ReadDegreeCurricularPlans implements IService {

	/**
	 * Executes the service. Returns the current InfoDegreeCurricularPlan.
	 * 
	 * @throws ExcepcaoPersistencia
	 */
	public List run() throws ExcepcaoPersistencia {
		final ISuportePersistente sp = PersistenceSupportFactory
				.getDefaultPersistenceSupport();

		final List curricularPlans = sp.getIPersistentDegreeCurricularPlan().readAll();
		final List infoCurricularPlans = new ArrayList(curricularPlans.size());

		for (final Iterator iter = curricularPlans.iterator(); iter.hasNext();) {
			final IDegreeCurricularPlan curricularPlan = (IDegreeCurricularPlan) iter.next();
			infoCurricularPlans.add(InfoDegreeCurricularPlanWithDegree
					.newInfoFromDomain(curricularPlan));
		}
		return infoCurricularPlans;
	}
}