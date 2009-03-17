package net.sourceforge.fenixedu.domain.accessControl.groups;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.LeafGroup;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.IdOperator;
import net.sourceforge.fenixedu.domain.student.Student;

public class StudentsByDegreeAndCurricularYear extends LeafGroup {

    private final DomainReference<Degree> degree;

    private final DomainReference<CurricularYear> curricularYear;

    private final DomainReference<ExecutionYear> executionYear;

    public StudentsByDegreeAndCurricularYear(final Degree degree, final CurricularYear curricularYear,
	    final ExecutionYear executionYear) {
	this.degree = new DomainReference<Degree>(degree);
	this.executionYear = new DomainReference<ExecutionYear>(executionYear);
	this.curricularYear = new DomainReference<CurricularYear>(curricularYear);
    }

    @Override
    public Set<Person> getElements() {
	final Set<Person> persons = new HashSet<Person>();
	for (Student student : getDegree().getStudentsFromGivenCurricularYear(getCurricularYear().getYear(), getExecutionYear())) {
	    persons.add(student.getPerson());
	}
	return persons;
    }

    @Override
    protected Argument[] getExpressionArguments() {
	return new Argument[] { new IdOperator(getDegree()), new IdOperator(getCurricularYear()),
		new IdOperator(getExecutionYear()) };
    }

    public Degree getDegree() {
	return (degree != null ? degree.getObject() : null);
    }

    public CurricularYear getCurricularYear() {
	return (curricularYear != null ? curricularYear.getObject() : null);
    }

    public ExecutionYear getExecutionYear() {
	return (executionYear != null ? executionYear.getObject() : null);
    }

    public static class Builder implements GroupBuilder {

	public Group build(Object[] arguments) {
	    return new StudentsByDegreeAndCurricularYear((Degree) arguments[0], (CurricularYear) arguments[1],
		    (ExecutionYear) arguments[2]);
	}

	public int getMaxArguments() {
	    return 3;
	}

	public int getMinArguments() {
	    return 3;
	}

    }
}
