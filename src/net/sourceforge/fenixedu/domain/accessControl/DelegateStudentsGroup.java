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

    private DomainReference<Degree> degree;

    private FunctionType functionType;

    private DomainReference<Student> student;

    private DomainReference<Person> person;

    private DomainReference<ExecutionYear> executionYear;

    public DelegateStudentsGroup(PersonFunction delegateFunction, FunctionType functionType) {
	setSender(delegateFunction.getPerson());
	setFunctionType(functionType);
	setExecutionYear(ExecutionYear.getExecutionYearByDate(delegateFunction.getBeginDate()));
    }

    public DelegateStudentsGroup(PersonFunction delegateFunction) {
	setSender(delegateFunction.getPerson());
	setFunctionType(delegateFunction.getFunction().getFunctionType());
	setExecutionYear(ExecutionYear.getExecutionYearByDate(delegateFunction.getBeginDate()));
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

    public Degree getDegree() {
	return (degree != null ? degree.getObject() : null);
    }

    public void setDegree(Degree degree) {
	this.degree = new DomainReference<Degree>(degree);
    }

    public boolean hasDegree() {
	return (getDegree() != null ? true : false);
    }

    public FunctionType getFunctionType() {
	return functionType;
    }

    public void setFunctionType(FunctionType functionType) {
	this.functionType = functionType;
    }

    public Student getStudent() {
	return (student != null ? student.getObject() : null);
    }

    public void setStudent(Student student) {
	this.student = new DomainReference<Student>(student);
    }

    public ExecutionYear getExecutionYear() {
	return (executionYear != null ? executionYear.getObject() : null);
    }

    public void setExecutionYear(ExecutionYear executionYear) {
	this.executionYear = new DomainReference<ExecutionYear>(executionYear);
    }

    public Person getPerson() {
	return (person != null ? person.getObject() : null);
    }

    public void setPerson(Person person) {
	this.person = new DomainReference<Person>(person);
    }

    private void setSender(Person person) {
	if (person.hasStudent()) {
	    setStudent(person.getStudent());
	} else {
	    setPerson(person);
	}
    }

    private Person getSender() {
	return (getPerson() != null ? getPerson() : getStudent().getPerson());
    }

}
