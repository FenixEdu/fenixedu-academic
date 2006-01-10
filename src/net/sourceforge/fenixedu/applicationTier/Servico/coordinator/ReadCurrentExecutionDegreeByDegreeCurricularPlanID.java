package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadCurrentExecutionDegreeByDegreeCurricularPlanID implements IService {

    public InfoExecutionDegree run(final Integer degreeCurricularPlanID) throws ExcepcaoPersistencia {
        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();

        final DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) persistentSupport
                .getIPersistentDegreeCurricularPlan().readByOID(DegreeCurricularPlan.class,
                        degreeCurricularPlanID);

        final List executionDegrees = degreeCurricularPlan.getExecutionDegrees();
        final ExecutionDegree executionDegree = (ExecutionDegree) CollectionUtils.find(
                executionDegrees, new Predicate() {
                    public boolean evaluate(Object arg0) {
                        final ExecutionDegree executionDegree = (ExecutionDegree) arg0;
                        return PeriodState.CURRENT.equals(executionDegree.getExecutionYear().getState());
                    }
                });

        return InfoExecutionDegree.newInfoFromDomain(executionDegree);
    }
}