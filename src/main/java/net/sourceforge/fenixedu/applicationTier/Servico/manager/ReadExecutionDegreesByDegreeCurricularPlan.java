package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import pt.ist.fenixframework.Atomic;

public class ReadExecutionDegreesByDegreeCurricularPlan {

    @Atomic
    public static List<InfoExecutionDegree> run(final DegreeCurricularPlan degreeCurricularPlan) throws FenixServiceException {
        return getExecutionCourses(degreeCurricularPlan);
    }

    public static List<InfoExecutionDegree> getExecutionCourses(final DegreeCurricularPlan degreeCurricularPlan) {
        final Collection<ExecutionDegree> executionDegrees = degreeCurricularPlan.getExecutionDegrees();

        final List<InfoExecutionDegree> result = new ArrayList<InfoExecutionDegree>(executionDegrees.size());
        for (final ExecutionDegree executionDegree : executionDegrees) {
            final InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree.newInfoFromDomain(executionDegree);
            result.add(infoExecutionDegree);
        }

        return result;
    }

}