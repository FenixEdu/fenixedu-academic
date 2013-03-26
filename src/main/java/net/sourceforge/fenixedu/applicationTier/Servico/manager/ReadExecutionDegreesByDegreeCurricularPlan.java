package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import pt.ist.fenixWebFramework.services.Service;

public class ReadExecutionDegreesByDegreeCurricularPlan extends FenixService {

    @Service
    public static List<InfoExecutionDegree> run(final DegreeCurricularPlan degreeCurricularPlan) throws FenixServiceException {
        return getExecutionCourses(degreeCurricularPlan);
    }

    public static List<InfoExecutionDegree> getExecutionCourses(final DegreeCurricularPlan degreeCurricularPlan) {
        final List<ExecutionDegree> executionDegrees = degreeCurricularPlan.getExecutionDegrees();

        final List<InfoExecutionDegree> result = new ArrayList<InfoExecutionDegree>(executionDegrees.size());
        for (final ExecutionDegree executionDegree : executionDegrees) {
            final InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree.newInfoFromDomain(executionDegree);
            result.add(infoExecutionDegree);
        }

        return result;
    }

}