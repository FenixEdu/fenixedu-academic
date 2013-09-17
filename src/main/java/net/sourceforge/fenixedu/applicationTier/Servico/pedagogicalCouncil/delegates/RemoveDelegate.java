package net.sourceforge.fenixedu.applicationTier.Servico.pedagogicalCouncil.delegates;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.elections.DelegateElection;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.DegreeUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PedagogicalCouncilUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Student;

import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class RemoveDelegate {

    @Atomic
    public static void run(Student student) throws FenixServiceException {
        check(RolePredicates.PEDAGOGICAL_COUNCIL_PREDICATE);
        run(student, FunctionType.DELEGATE_OF_YEAR);
    }

    @Atomic
    public static void run(PersonFunction personFunction, LocalDate newEndDate) throws FenixServiceException {
        check(RolePredicates.PEDAGOGICAL_COUNCIL_PREDICATE);
        Student student = personFunction.getPerson().getStudent();

        if (!personFunction.getBeginDate().isBefore(newEndDate)) {
            throw new FenixServiceException("error.personFunction.endDateBeforeBeginDate");
        } else {
            try {
                personFunction.setOccupationInterval(personFunction.getBeginDate(), new YearMonthDay(newEndDate));
            } catch (DomainException e) {
                throw new FenixServiceException(e.getMessage());
            }
            if (student.getAllActiveDelegateFunctions().isEmpty()) {
                student.getPerson().removePersonRoles(Role.getRoleByRoleType(RoleType.DELEGATE));
            }
        }

    }

    @Atomic
    public static void run(PersonFunction personFunction) throws FenixServiceException {
        check(RolePredicates.PEDAGOGICAL_COUNCIL_PREDICATE);
        Student student = personFunction.getPerson().getStudent();
        YearMonthDay yesterday = new YearMonthDay().minusDays(1);
        if (!personFunction.getBeginDate().isBefore(yesterday)) {
            throw new FenixServiceException("error.personFunction.endDateBeforeBeginDate");
        } else {
            personFunction.setOccupationInterval(personFunction.getBeginDate(), yesterday);
            if (student.getAllActiveDelegateFunctions().isEmpty()) {
                student.getPerson().removePersonRoles(Role.getRoleByRoleType(RoleType.DELEGATE));
            }
        }

    }

    @Atomic
    public static void run(Student student, FunctionType delegateFunctionType) throws FenixServiceException {
        check(RolePredicates.PEDAGOGICAL_COUNCIL_PREDICATE);
        final DegreeUnit degreeUnit = student.getLastActiveRegistration().getDegree().getUnit();

        if (delegateFunctionType.equals(FunctionType.DELEGATE_OF_YEAR)) {

            degreeUnit.removeAllActiveDelegatePersonFunctionsFromStudent(student);

            /* Remove delegate role from this student */
            student.getPerson().removePersonRoles(Role.getRoleByRoleType(RoleType.DELEGATE));

            /*
             * Remove this student from the election in wich he was elected (if
             * he has it)
             */
            DelegateElection election = student.getLastElectedDelegateElection();
            if (election != null && election.getExecutionYear().equals(ExecutionYear.readCurrentExecutionYear())) {
                election.setElectedStudent(null);
            }
        } else {
            degreeUnit.removeActiveDelegatePersonFunctionFromStudentByFunctionType(student, delegateFunctionType);
        }
    }

    @Atomic
    public static void run(Person person, Function delegateFunction) throws FenixServiceException {
        check(RolePredicates.PEDAGOGICAL_COUNCIL_PREDICATE);
        PedagogicalCouncilUnit unit = (PedagogicalCouncilUnit) delegateFunction.getUnit();

        unit.removeActiveDelegatePersonFunctionFromPersonByFunction(person, delegateFunction);
    }
}