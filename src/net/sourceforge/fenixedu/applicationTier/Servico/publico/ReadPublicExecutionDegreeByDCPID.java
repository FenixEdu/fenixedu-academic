package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadPublicExecutionDegreeByDCPID extends Service {

    public List<InfoExecutionDegree> run(Integer degreeCurricularPlanID) throws FenixServiceException, ExcepcaoPersistencia {
        DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanID);
        
        List<InfoExecutionDegree> result = new ArrayList<InfoExecutionDegree>();
        for (ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegrees()) {
            result.add(copyExecutionDegree2InfoExecutionDegree(executionDegree));
        }

        return result;
    }

    public InfoExecutionDegree run(Integer degreeCurricularPlanID, Integer executionYearID) throws ExcepcaoPersistencia {
        DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanID);
        ExecutionYear executionYear = rootDomainObject.readExecutionYearByOID(executionYearID);
        
        ExecutionDegree executionDegree = ExecutionDegree.getByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear.getYear());
        if (executionDegree == null) {
            return null;
        }

        return copyExecutionDegree2InfoExecutionDegree(executionDegree);
    }

    protected InfoExecutionDegree copyExecutionDegree2InfoExecutionDegree(ExecutionDegree executionDegree) {
        InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree.newInfoFromDomain(executionDegree);
        
        InfoExecutionYear infoExecutionYear = InfoExecutionYear.newInfoFromDomain(executionDegree.getExecutionYear());
        infoExecutionDegree.setInfoExecutionYear(infoExecutionYear);

        DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
        InfoDegreeCurricularPlan infoDegreeCurricularPlan = InfoDegreeCurricularPlan.newInfoFromDomain(degreeCurricularPlan);
        infoExecutionDegree.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);

        return infoExecutionDegree;
    }

}
