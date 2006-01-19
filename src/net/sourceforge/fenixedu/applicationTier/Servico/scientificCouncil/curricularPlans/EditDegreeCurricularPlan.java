package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.curricularPlans;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

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
        for (final DegreeCurricularPlan degreeCurricularPlan : dcpToEdit.getDegree().getDegreeCurricularPlans()) {
            if (degreeCurricularPlan != dcpToEdit && degreeCurricularPlan.getName().equalsIgnoreCase(name)) {
                throw new FenixServiceException("error.degreeCurricularPlan.existing.name.and.degree");
            }
        }
        
        dcpToEdit.edit(name, ectsCredits, curricularStage, gradeScale);
    }

}
