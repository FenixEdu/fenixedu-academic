package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadPublicExecutionDegreeByDCPID {

    @Service
    public static List<InfoExecutionDegree> run(Integer degreeCurricularPlanID) throws FenixServiceException {
        DegreeCurricularPlan degreeCurricularPlan = RootDomainObject.getInstance().readDegreeCurricularPlanByOID(degreeCurricularPlanID);

        List<InfoExecutionDegree> result = new ArrayList<InfoExecutionDegree>();
        for (ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegrees()) {
            result.add(copyExecutionDegree2InfoExecutionDegree(executionDegree));
        }

        return result;
    }

    @Service
    public static InfoExecutionDegree run(Integer degreeCurricularPlanID, Integer executionYearID) {
        DegreeCurricularPlan degreeCurricularPlan = RootDomainObject.getInstance().readDegreeCurricularPlanByOID(degreeCurricularPlanID);
        ExecutionYear executionYear = RootDomainObject.getInstance().readExecutionYearByOID(executionYearID);

        ExecutionDegree executionDegree =
                ExecutionDegree.getByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear.getYear());
        if (executionDegree == null) {
            return null;
        }

        return copyExecutionDegree2InfoExecutionDegree(executionDegree);
    }

    protected static InfoExecutionDegree copyExecutionDegree2InfoExecutionDegree(ExecutionDegree executionDegree) {
        return InfoExecutionDegree.newInfoFromDomain(executionDegree);
    }

}