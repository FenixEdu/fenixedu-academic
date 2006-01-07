package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class EditDegreeCurricularPlan implements IService {

    public void run(InfoDegreeCurricularPlan infoDcp) throws FenixServiceException, ExcepcaoPersistencia {

        if (infoDcp.getIdInternal() == null || infoDcp.getName() == null
                || infoDcp.getInitialDate() == null || infoDcp.getDegreeDuration() == null
                || infoDcp.getMinimalYearForOptionalCourses() == null) {
            throw new InvalidArgumentsServiceException();
        }

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();

        final DegreeCurricularPlan dcpToEdit = (DegreeCurricularPlan) persistentSupport
                .getIPersistentDegreeCurricularPlan().readByOID(DegreeCurricularPlan.class,
                        infoDcp.getIdInternal());
        if (dcpToEdit == null) {
            throw new FenixServiceException("message.nonExistingDegreeCurricularPlan");
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

        final Degree degree = dcpToEdit.getDegree();
        if (degree == null) {
            throw new FenixServiceException("message.nonExistingDegree");
        }

        dcpToEdit.edit(infoDcp.getName(), infoDcp.getState(), infoDcp.getInitialDate(), infoDcp
                .getEndDate(), infoDcp.getDegreeDuration(), infoDcp.getMinimalYearForOptionalCourses(),
                infoDcp.getNeededCredits(), infoDcp.getMarkType(), infoDcp.getNumerusClausus(), infoDcp
                        .getAnotation(), infoDcp.getGradeScale());
    }

}
