package net.sourceforge.fenixedu.applicationTier.Servico.pedagogicalCouncil.delegates;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.elections.DelegateElection;
import net.sourceforge.fenixedu.domain.organizationalStructure.DegreeUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PedagogicalCouncilUnit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class RemoveDelegate extends Service {

    public void run(Student student) throws FenixServiceException {
	this.run(student, FunctionType.DELEGATE_OF_YEAR);
    }

    public void run(Student student, FunctionType delegateFunctionType) throws FenixServiceException {
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
		election.removeElectedStudent();
	    }
	} else {
	    degreeUnit.removeActiveDelegatePersonFunctionFromStudentByFunctionType(student, delegateFunctionType);
	}
    }

    public void run(Person person, Function delegateFunction) throws FenixServiceException {
	PedagogicalCouncilUnit unit = (PedagogicalCouncilUnit) delegateFunction.getUnit();

	unit.removeActiveDelegatePersonFunctionFromPersonByFunction(person, delegateFunction);
    }
}
