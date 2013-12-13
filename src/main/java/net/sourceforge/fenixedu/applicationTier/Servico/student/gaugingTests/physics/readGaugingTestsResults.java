package net.sourceforge.fenixedu.applicationTier.Servico.student.gaugingTests.physics;


import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.gaugingTests.physics.InfoGaugingTestResult;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.gaugingTests.physics.GaugingTestResult;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class readGaugingTestsResults {

    @Atomic
    public static InfoGaugingTestResult run(User userView) throws FenixServiceException {
        check(RolePredicates.STUDENT_PREDICATE);

        Person person = Person.readPersonByUsername(userView.getUsername());

        Registration registration = person.readStudentByDegreeType(DegreeType.DEGREE);
        if (registration == null) {
            return null;
        }

        GaugingTestResult gaugingTestsResult = registration.getAssociatedGaugingTestResult();
        if (gaugingTestsResult != null) {
            return InfoGaugingTestResult.newInfoFromDomain(gaugingTestsResult);
        }

        return null;
    }
}