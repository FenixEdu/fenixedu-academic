package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.VariableNotDefinedException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.IdOperator;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.BundleUtil;

public class AllStudentsByCampus extends Group {

    private final Campus campus;

    public AllStudentsByCampus(Campus campus) {
	this.campus = campus;
    }

    public Campus getCampus() {
	return campus;
    }

    @Override
    public Set<Person> getElements() {
	Set<Person> people = new HashSet<Person>();
	Campus campus = getCampus();
	for (final ExecutionDegree executionDegree : ExecutionYear.readCurrentExecutionYear().getExecutionDegreesSet()) {
	    if (executionDegree.getCampus() == campus) {
		final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
		for (final StudentCurricularPlan studentCurricularPlan : degreeCurricularPlan.getStudentCurricularPlansSet()) {
		    final Registration registration = studentCurricularPlan.getRegistration();
		    if (registration != null && registration.isActive()) {
			people.add(registration.getPerson());
		    }
		}
	    }
	}
	return people;
    }

    @Override
    public String getName() {
	String name = BundleUtil.getStringFromResourceBundle("resources.GroupNameResources",
		"label.name." + getClass().getSimpleName(), new String[] { getCampus().getName() });
	return name != null ? name : super.getName();
    }

    @Override
    protected Argument[] getExpressionArguments() {
	return new Argument[] { new IdOperator(getCampus()) };
    }

    public static class Builder implements GroupBuilder {

	public Group build(Object[] arguments) {
	    final Campus campus = (Campus) arguments[0];
	    if (campus == null) {
		throw new VariableNotDefinedException("campus");
	    }
	    return new AllStudentsByCampus(campus);

	}

	public int getMaxArguments() {
	    return 1;
	}

	public int getMinArguments() {
	    return 1;
	}

    }

}
