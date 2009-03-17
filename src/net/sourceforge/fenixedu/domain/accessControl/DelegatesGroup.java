package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.StaticArgument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.VariableNotDefinedException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.IdOperator;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.student.Student;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class DelegatesGroup extends LeafGroup {

    private static final long serialVersionUID = 1L;

    private final DomainReference<Degree> degree;

    private final FunctionType functionType;

    public DelegatesGroup(FunctionType functionType) {
	this.functionType = functionType;
	this.degree = null;
    }

    public DelegatesGroup(Degree degree) {
	this.functionType = null;
	this.degree = new DomainReference<Degree>(degree);
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
	final Argument argument = functionType == null ? new IdOperator(getDegree()) : new StaticArgument(functionType.getName());
	return new Argument[] { argument };
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

    public boolean hasDegree() {
	return (getDegree() != null ? true : false);
    }

    public FunctionType getFunctionType() {
	return functionType;
    }

    public static class Builder implements GroupBuilder {

	public Group build(Object[] arguments) {
	    final Object arg0 = arguments[0];
	    if (arg0 instanceof Degree) {
		final Degree degree = (Degree) arg0;
		return new DelegatesGroup(degree);
	    } else {
		final FunctionType functionType = FunctionType.valueOf((String) arg0);
		if (functionType == null) {
		    throw new VariableNotDefinedException("functionType");
		}
		return new DelegatesGroup(functionType);
	    }
	}

	public int getMaxArguments() {
	    return 1;
	}

	public int getMinArguments() {
	    return 1;
	}

    }

}
