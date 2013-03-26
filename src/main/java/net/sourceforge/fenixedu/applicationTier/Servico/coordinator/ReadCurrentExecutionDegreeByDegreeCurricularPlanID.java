package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class ReadCurrentExecutionDegreeByDegreeCurricularPlanID extends FenixService {

    public InfoExecutionDegree run(final Integer degreeCurricularPlanID) {

        final DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanID);

        final List executionDegrees = degreeCurricularPlan.getExecutionDegrees();
        final ExecutionDegree executionDegree = (ExecutionDegree) CollectionUtils.find(executionDegrees, new Predicate() {
            @Override
            public boolean evaluate(Object arg0) {
                final ExecutionDegree executionDegree = (ExecutionDegree) arg0;
                return executionDegree.getExecutionYear().isCurrent();
            }
        });

        return InfoExecutionDegree.newInfoFromDomain(executionDegree);
    }
}