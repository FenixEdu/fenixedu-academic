package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.student.Student;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class DelegateStudentsGroup extends LeafGroup {

    private static final long serialVersionUID = 1L;

    private final FunctionType functionType;

    private final DomainReference<Student> student;

    private final DomainReference<Person> person;

    private final DomainReference<ExecutionYear> executionYear;

    public DelegateStudentsGroup(PersonFunction delegateFunction, FunctionType functionType) {
	Person person = delegateFunction.getPerson();
	if (person.hasStudent()) {
	    this.student = new DomainReference<Student>(person.getStudent());
	    this.person = null;
	} else {
	    this.student = null;
	    this.person = new DomainReference<Person>(person);
	}
	this.functionType = functionType;
	this.executionYear = new DomainReference<ExecutionYear>(ExecutionYear.getExecutionYearByDate(delegateFunction
		.getBeginDate()));
    }

    public DelegateStudentsGroup(PersonFunction delegateFunction) {
	this(delegateFunction, delegateFunction.getFunction().getFunctionType());
    }

    @Override
    public Set<Person> getElements() {
	Set<Person> people = new HashSet<Person>();

	if (getSender().hasStudent()) {
	    for (Student student : getStudent().getStudentsResponsibleForGivenFunctionType(getFunctionType(), getExecutionYear())) {
		people.add(student.getPerson());
	    }
	} else {
	    if (getSender().hasAnyCoordinators()) {
		for (Coordinator coordinator : getSender().getCoordinators()) {
		    final Degree degree = coordinator.getExecutionDegree().getDegree();
		    for (Student student : degree.getAllStudents()) {
			people.add(student.getPerson());
		    }
		}
	    }
	}

	return people;
    }

    @Override
    protected Argument[] getExpressionArguments() {
	return new Argument[] {};
    }

    @Override
    public boolean isMember(Person person) {
	if (person.hasStudent()) {
	    if (getElements().contains(person.getStudent())) {
		return true;
	    }
	}
	return false;
    }

    @Override
    public String getName() {
	if (getSender().hasStudent())
	    return RenderUtils.getResourceString("DELEGATES_RESOURCES", "label." + getClass().getSimpleName() + "."
		    + getFunctionType().getName());
	else
	    return RenderUtils.getResourceString("DELEGATES_RESOURCES", "label." + getClass().getSimpleName() + "."
		    + getFunctionType().getName() + ".coordinator");
    }

    public FunctionType getFunctionType() {
	return functionType;
    }

    public Student getStudent() {
	return (student != null ? student.getObject() : null);
    }

    public ExecutionYear getExecutionYear() {
	return (executionYear != null ? executionYear.getObject() : null);
    }

    public Person getPerson() {
	return (person != null ? person.getObject() : null);
    }

    private Person getSender() {
	return (getPerson() != null ? getPerson() : getStudent().getPerson());
    }

}
