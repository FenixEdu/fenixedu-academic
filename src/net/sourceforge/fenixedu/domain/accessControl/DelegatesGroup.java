package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.student.Student;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class DelegatesGroup extends LeafGroup {

    private static final long serialVersionUID = 1L;

    private DomainReference<Degree> degree;

    private FunctionType functionType;

    public DelegatesGroup(FunctionType functionType) {
	setFunctionType(functionType);
    }

    public DelegatesGroup(Degree degree) {
	setDegree(degree);
    }

    @Override
    public Set<Person> getElements() {
	Set<Person> people = new HashSet<Person>();

	if (!hasDegree()) {
	    for (Function function : Function.readAllActiveFunctionsByType(getFunctionType())) {
		for (PersonFunction personFunction : function.getActivePersonFunctions()) {
		    people.add(personFunction.getPerson());
		}
	    }
	} else {
	    for (Student student : getDegree().getAllActiveDelegates()) {
		people.add(student.getPerson());
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
	if (hasDegree()) {
	    return person != null && person.hasStudent()
		    && person.getStudent().getLastActiveRegistration().getDegree().equals(getDegree())
		    && getDegree().hasAnyActiveDelegateFunctionForStudent(person.getStudent());
	} else {
	    return person != null && person.hasStudent() && person.getStudent().hasActiveDelegateFunction(getFunctionType());
	}
    }

    @Override
    public String getName() {
	final ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.DelegateResources", Language.getLocale());
	if (hasDegree()) {
	    return resourceBundle.getString("label." + getClass().getSimpleName()) + " " + resourceBundle.getString("label.of")
		    + " " + getDegree().getSigla();
	} else {
	    return resourceBundle.getString("label." + getClass().getSimpleName() + "." + getFunctionType().getName());
	}
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

}
