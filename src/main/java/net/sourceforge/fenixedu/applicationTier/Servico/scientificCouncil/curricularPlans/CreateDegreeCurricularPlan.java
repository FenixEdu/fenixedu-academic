package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.curricularPlans;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class CreateDegreeCurricularPlan {

    @Checked("RolePredicates.SCIENTIFIC_COUNCIL_PREDICATE")
    @Service
    public static void run(Integer degreeId, String name, GradeScale gradeScale) throws FenixServiceException {

        if (degreeId == null || name == null) {
            throw new InvalidArgumentsServiceException();
        }

        final Person creator = AccessControl.getPerson();
        if (creator == null) {
            throw new FenixServiceException("error.degreeCurricularPlan.non.existing.creator");
        }

        final Degree degree = RootDomainObject.getInstance().readDegreeByOID(degreeId);
        if (degree == null) {
            throw new FenixServiceException("error.degreeCurricularPlan.non.existing.degree");
        }

        degree.createBolonhaDegreeCurricularPlan(name, gradeScale, creator);
    }

}