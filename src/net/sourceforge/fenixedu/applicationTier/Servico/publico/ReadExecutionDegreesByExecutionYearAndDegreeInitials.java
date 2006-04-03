package net.sourceforge.fenixedu.applicationTier.Servico.publico;

/**
 * Servico LerSalas
 * 
 * @author tfc130
 * @version
 */

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadExecutionDegreesByExecutionYearAndDegreeInitials extends Service {

    public InfoExecutionDegree run(final InfoExecutionYear infoExecutionYear,
            final String degreeInitials, final String nameDegreeCurricularPlan)
            throws ExcepcaoPersistencia {

        DegreeCurricularPlan degreeCurricularPlan = DegreeCurricularPlan.readByNameAndDegreeSigla(nameDegreeCurricularPlan, degreeInitials);
        final ExecutionDegree executionDegree = ExecutionDegree.getByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan,
                        infoExecutionYear.getYear());
        final InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree
                .newInfoFromDomain(executionDegree);
        if (executionDegree != null) {
            final InfoDegreeCurricularPlan infoDegreeCurricularPlan = InfoDegreeCurricularPlan
                    .newInfoFromDomain(executionDegree.getDegreeCurricularPlan());
            infoExecutionDegree.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);
            final InfoDegree infoDegree = InfoDegree.newInfoFromDomain(executionDegree
                    .getDegreeCurricularPlan().getDegree());
            infoDegreeCurricularPlan.setInfoDegree(infoDegree);
        }

        return infoExecutionDegree;
    }

}