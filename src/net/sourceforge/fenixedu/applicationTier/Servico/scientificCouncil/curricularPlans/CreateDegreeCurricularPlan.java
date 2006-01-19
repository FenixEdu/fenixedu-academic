package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.curricularPlans;

import java.util.List;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

public class CreateDegreeCurricularPlan extends Service {

    public void run(Integer degreeId, String name, Double ectsCredits, CurricularStage curricularStage,
            GradeScale gradeScale) throws FenixServiceException, ExcepcaoPersistencia {

        if (degreeId == null || name == null || ectsCredits == null || curricularStage == null) {
            throw new InvalidArgumentsServiceException();
        }

        final Person creator = AccessControl.getUserView().getPerson();

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final Degree degree = (Degree) persistentSupport.getICursoPersistente().readByOID(Degree.class,
                degreeId);

        assertExistingObjectsToAssociate(creator, degree);
        assertUniques(degree, name);

        CurricularPeriod curricularPeriod = DomainFactory.makeCurricularPeriod(degree
                .getBolonhaDegreeType().getCurricularPeriodType());

        DomainFactory.makeDegreeCurricularPlan(degree, name, ectsCredits, curricularStage, gradeScale,
                creator, curricularPeriod);
        addBolonhaRoleToCreator(creator);
    }

    private void assertExistingObjectsToAssociate(Person person, Degree degree)
            throws ExcepcaoPersistencia, FenixServiceException {
        if (person == null) {
            throw new FenixServiceException("error.degreeCurricularPlan.non.existing.creator");
        } else if (degree == null) {
            throw new FenixServiceException("error.degreeCurricularPlan.non.existing.degree");
        }
    }

    private void assertUniques(Degree degree, String name) throws ExcepcaoPersistencia,
            FenixServiceException {
        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final List<DegreeCurricularPlan> dcps = (List<DegreeCurricularPlan>) persistentSupport
                .getIPersistentDegreeCurricularPlan().readFromNewDegreeStructure();

        // assert unique pair name/degree
        for (DegreeCurricularPlan dcp : dcps) {
            if ((dcp.getDegree() == degree) && dcp.getName().equalsIgnoreCase(name)) {
                throw new FenixServiceException("error.degreeCurricularPlan.existing.name.and.degree");
            }
        }
    }

    private void addBolonhaRoleToCreator(Person creator) throws ExcepcaoPersistencia {
        if (!creator.hasRole(RoleType.BOLONHA_MANAGER)) {
            final ISuportePersistente persistentSuport = PersistenceSupportFactory
                    .getDefaultPersistenceSupport();
            final Role bolonhaRole = persistentSuport.getIPersistentRole().readByRoleType(
                    RoleType.BOLONHA_MANAGER);
            creator.addPersonRoles(bolonhaRole);
        }
    }

}
