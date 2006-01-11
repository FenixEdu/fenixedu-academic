package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.curricularPlans;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class EditDegreeCurricularPlan implements IService {

    public void run(Integer dcpId, String name, Double ectsCredits, CurricularStage curricularStage,
            GradeScale gradeScale) throws FenixServiceException, ExcepcaoPersistencia {

        if (dcpId == null || name == null || ectsCredits == null || curricularStage == null) {
            throw new InvalidArgumentsServiceException();
        }

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();

        final DegreeCurricularPlan dcpToEdit = (DegreeCurricularPlan) persistentSupport
                .getIPersistentDegreeCurricularPlan().readByOID(DegreeCurricularPlan.class, dcpId);
        if (dcpToEdit == null) {
            throw new FenixServiceException("error.degreeCurricularPlan.no.existing.degreeCurricularPlan");
        }

        // assert unique pair name/degree
        final List<DegreeCurricularPlan> dcps = (List<DegreeCurricularPlan>) persistentSupport
                .getIPersistentDegreeCurricularPlan().readFromNewDegreeStructure();
        for (DegreeCurricularPlan dcp : dcps) {
            if (dcp != dcpToEdit) {
                if (dcp.getDegree() == dcpToEdit.getDegree() && dcp.getName().equalsIgnoreCase(name)) {
                    throw new FenixServiceException("error.degreeCurricularPlan.existing.name.and.degree");
                }
            }
        }

        dcpToEdit.edit(name, ectsCredits, curricularStage, gradeScale);
    }

}
