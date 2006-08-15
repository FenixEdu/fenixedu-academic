package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadExecutionDegreesByDegreeCurricularPlan extends Service {

    public List<InfoExecutionDegree> run(Integer idDegreeCurricularPlan) throws FenixServiceException, ExcepcaoPersistencia {
        final DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(idDegreeCurricularPlan);

        final List<ExecutionDegree> executionDegrees = degreeCurricularPlan.getExecutionDegrees();

        final List<InfoExecutionDegree> result = new ArrayList<InfoExecutionDegree>(executionDegrees.size());
        for (final ExecutionDegree executionDegree : executionDegrees) {
            final InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree.newInfoFromDomain(executionDegree);
            result.add(infoExecutionDegree);
        }

        return result;
    }

}
