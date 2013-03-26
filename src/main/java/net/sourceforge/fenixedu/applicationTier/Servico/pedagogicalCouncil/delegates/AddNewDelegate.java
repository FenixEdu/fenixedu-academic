package net.sourceforge.fenixedu.applicationTier.Servico.pedagogicalCouncil.delegates;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.elections.YearDelegateElection;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.DegreeUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PedagogicalCouncilUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.YearDelegate;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class AddNewDelegate extends FenixService {

    /* Year Delegates */
    @Checked("RolePredicates.PEDAGOGICAL_COUNCIL_PREDICATE")
    @Service
    public static void run(Student student, YearDelegateElection election) throws FenixServiceException {
        election.setElectedStudent(student);

        run(student, election.getCurricularYear(), election.getDegree());
    }

    /* Year Delegates */
    @Checked("RolePredicates.PEDAGOGICAL_COUNCIL_PREDICATE")
    @Service
    public static void run(Student student, CurricularYear curricularYear, Degree degree) throws FenixServiceException {
        final DegreeUnit degreeUnit = degree.getUnit();
        final Person studentPerson = student.getPerson();
        Registration lastActiveRegistration = student.getActiveRegistrationFor(degree);
        if (lastActiveRegistration == null || !lastActiveRegistration.getDegree().equals(degree)) {
            throw new FenixServiceException("error.delegates.studentNotBelongsToDegree");
        }

        try {
            PersonFunction personFunction = degreeUnit.addYearDelegatePersonFunction(student, curricularYear);
            studentPerson.addPersonRoleByRoleType(RoleType.DELEGATE);

            new YearDelegate(lastActiveRegistration, personFunction);

        } catch (DomainException ex) {
            throw new FenixServiceException(ex.getMessage());
        }
    }

    /* All other delegates */
    @Checked("RolePredicates.PEDAGOGICAL_COUNCIL_PREDICATE")
    @Service
    public static void run(Student student, Degree degree, FunctionType delegateFunctionType) throws FenixServiceException {
        final DegreeUnit degreeUnit = degree.getUnit();
        final Person studentPerson = student.getPerson();

        try {
            degreeUnit.addDelegatePersonFunction(student, delegateFunctionType);
            studentPerson.addPersonRoleByRoleType(RoleType.DELEGATE);

        } catch (DomainException ex) {
            throw new FenixServiceException(ex.getMessage());
        }
    }

    /* GGAE Delegates */
    @Checked("RolePredicates.PEDAGOGICAL_COUNCIL_PREDICATE")
    @Service
    public static void run(Person person, Function delegateFunction) throws FenixServiceException {
        final PedagogicalCouncilUnit unit = (PedagogicalCouncilUnit) delegateFunction.getUnit();

        try {
            unit.addDelegatePersonFunction(person, delegateFunction);

            person.addPersonRoleByRoleType(RoleType.DELEGATE);

        } catch (DomainException ex) {
            throw new FenixServiceException(ex.getMessage());
        }
    }
}