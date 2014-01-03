package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.candidacy;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.serviceAgreements.DegreeCurricularPlanServiceAgreement;
import net.sourceforge.fenixedu.domain.candidacy.Candidacy;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.predicates.RolePredicates;

import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;

public class CreateCandidacy {

    @Atomic
    public static Candidacy run(ExecutionDegree executionDegree, DegreeType degreeType, String name,
            String identificationDocumentNumber, IDDocumentType identificationDocumentType, String contributorNumber,
            YearMonthDay startDate) {
        check(RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE);

        Person person = Person.readByDocumentIdNumberAndIdDocumentType(identificationDocumentNumber, identificationDocumentType);
        if (person == null) {
            person = new Person(name, identificationDocumentNumber, identificationDocumentType, Gender.MALE);
        }

        person.setSocialSecurityNumber(contributorNumber);

        if (!person.hasStudent()) {
            new Student(person);
        }

        person.addPersonRoleByRoleType(RoleType.CANDIDATE);
        person.addPersonRoleByRoleType(RoleType.PERSON);

        Candidacy candidacy = CandidacyFactory.newCandidacy(degreeType, person, executionDegree, startDate);

        new DegreeCurricularPlanServiceAgreement(person, executionDegree.getDegreeCurricularPlan().getServiceAgreementTemplate());

        return candidacy;

    }

}