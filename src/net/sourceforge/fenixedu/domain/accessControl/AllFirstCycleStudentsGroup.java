package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;

public class AllFirstCycleStudentsGroup extends Group {

	private static final long serialVersionUID = 1L;
	
	public AllFirstCycleStudentsGroup() {
	}

	@Override
	public Set<Person> getElements() {
		Set<Person> elements = new HashSet<Person>();
		List<Person> people = Role.getRoleByRoleType(RoleType.STUDENT).getAssociatedPersons();

		for (Person person : people) {
			if (!person.getStudent().getActiveRegistrations().isEmpty()) {								
				for (Registration registration : person.getStudent().getActiveRegistrations()) {
					StudentCurricularPlan scp = registration.getLastStudentCurricularPlan();
					if(!scp.hasConcludedCycle(CycleType.FIRST_CYCLE)) {
						elements.add(person);
						break;
					}
				}
			}
		}
		return elements;
	}

	@Override
	protected Argument[] getExpressionArguments() {
		return null;
	}

}
