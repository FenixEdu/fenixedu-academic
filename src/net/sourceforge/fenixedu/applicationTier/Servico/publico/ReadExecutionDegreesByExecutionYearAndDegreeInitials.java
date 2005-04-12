package net.sourceforge.fenixedu.applicationTier.Servico.publico;

/**
 * Servico LerSalas
 * 
 * @author tfc130
 * @version
 */

import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadExecutionDegreesByExecutionYearAndDegreeInitials implements
		IService {

	public InfoExecutionDegree run(final InfoExecutionYear infoExecutionYear,
			final String degreeInitials, final String nameDegreeCurricularPlan)
			throws ExcepcaoPersistencia {

		final ISuportePersistente sp = PersistenceSupportFactory
				.getDefaultPersistenceSupport();
		final IPersistentExecutionDegree executionDegreeDAO = sp
				.getIPersistentExecutionDegree();
		final IExecutionYear executionYear = Cloner
				.copyInfoExecutionYear2IExecutionYear(infoExecutionYear);

		final IExecutionDegree executionDegree = executionDegreeDAO
				.readByDegreeInitialsAndNameDegreeCurricularPlanAndExecutionYear(
						degreeInitials, nameDegreeCurricularPlan, executionYear);
		final InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree.newInfoFromDomain(executionDegree);
        if (executionDegree != null) {
            final InfoDegreeCurricularPlan infoDegreeCurricularPlan = InfoDegreeCurricularPlan.newInfoFromDomain(executionDegree.getDegreeCurricularPlan());
            infoExecutionDegree.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);
            final InfoDegree infoDegree = InfoDegree.newInfoFromDomain(executionDegree.getDegreeCurricularPlan().getDegree());
            infoDegreeCurricularPlan.setInfoDegree(infoDegree);
        }

		return infoExecutionDegree;
	}

}