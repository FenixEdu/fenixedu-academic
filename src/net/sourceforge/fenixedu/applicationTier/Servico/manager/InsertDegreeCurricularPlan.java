package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ICursoPersistente;

public class InsertDegreeCurricularPlan extends Service {

    public void run(InfoDegreeCurricularPlan infoDcp) throws FenixServiceException, ExcepcaoPersistencia {

        if (infoDcp.getInfoDegree().getIdInternal() == null || infoDcp.getName() == null
                || infoDcp.getInitialDate() == null || infoDcp.getDegreeDuration() == null
                || infoDcp.getMinimalYearForOptionalCourses() == null) {
            throw new InvalidArgumentsServiceException();
        }

        // assert unique pair name/degree
        final List<DegreeCurricularPlan> dcps = (List<DegreeCurricularPlan>) persistentSupport
                .getIPersistentDegreeCurricularPlan().readByCurricularStage(CurricularStage.OLD);
        for (DegreeCurricularPlan dcp : dcps) {
            if (dcp.getDegree().getIdInternal().equals(infoDcp.getInfoDegree().getIdInternal())
                    && dcp.getName().equalsIgnoreCase(infoDcp.getName())) {
                throw new FenixServiceException("error.degreeCurricularPlan.existing.name.and.degree");
            }
        }

        final ICursoPersistente persistentDegree = persistentSupport.getICursoPersistente();
        final Degree degree = (Degree) persistentDegree.readByOID(Degree.class, infoDcp
                .getInfoDegree().getIdInternal());
        if (degree == null) {
            throw new FenixServiceException("error.degreeCurricularPlan.non.existing.degree");
        }

        new DegreeCurricularPlan(degree, infoDcp.getName(), infoDcp.getState(),
                infoDcp.getInitialDate(), infoDcp.getEndDate(), infoDcp.getDegreeDuration(), infoDcp
                        .getMinimalYearForOptionalCourses(), infoDcp.getNeededCredits(), infoDcp
                        .getMarkType(), infoDcp.getNumerusClausus(), infoDcp.getAnotation(), infoDcp
                        .getGradeScale());
    }

}
