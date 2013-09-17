package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.curricularPlans;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class CreateDegreeCurricularPlan {

    @Atomic
    public static void run(String degreeId, String name, GradeScale gradeScale) throws FenixServiceException {
        check(RolePredicates.SCIENTIFIC_COUNCIL_PREDICATE);

        if (degreeId == null || name == null) {
            throw new InvalidArgumentsServiceException();
        }

        final Person creator = AccessControl.getPerson();
        if (creator == null) {
            throw new FenixServiceException("error.degreeCurricularPlan.non.existing.creator");
        }

        final Degree degree = FenixFramework.getDomainObject(degreeId);
        if (degree == null) {
            throw new FenixServiceException("error.degreeCurricularPlan.non.existing.degree");
        }

        degree.createBolonhaDegreeCurricularPlan(name, gradeScale, creator);
    }

}