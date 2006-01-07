package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.curricularPlans;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ICursoPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class CreateDegreeCurricularPlan implements IService {

    public void run(Integer degreeId, String name, Double ectsCredits, CurricularStage curricularStage, GradeScale gradeScale) throws FenixServiceException, ExcepcaoPersistencia {

        if (degreeId == null || name == null || ectsCredits == null || curricularStage == null) {
            throw new InvalidArgumentsServiceException();
        }

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final List<DegreeCurricularPlan> dcps = (List<DegreeCurricularPlan>) persistentSupport
                .getIPersistentDegreeCurricularPlan().readFromNewDegreeStructure();

        // assert unique pair name/degree
        for (DegreeCurricularPlan dcp : dcps) {
            if (dcp.getDegree().getIdInternal().equals(degreeId)
                    && dcp.getName().equalsIgnoreCase(name)) {
                throw new FenixServiceException("error.degreeCurricularPlan.existing.name.and.degree");
            }
        }

        final ICursoPersistente persistentDegree = persistentSupport.getICursoPersistente();
        final Degree degree = (Degree) persistentDegree.readByOID(Degree.class, degreeId);
        if (degree == null) {
            throw new FenixServiceException("error.degreeCurricularPlan.non.existing.degree");
        }

        new DegreeCurricularPlan(degree, name, ectsCredits, curricularStage, gradeScale);
    }

}
