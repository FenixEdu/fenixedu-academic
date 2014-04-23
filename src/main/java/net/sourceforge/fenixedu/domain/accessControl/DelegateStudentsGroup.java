package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.StaticArgument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.GroupDynamicExpressionException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.OidOperator;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.student.Student;
import pt.ist.fenixframework.FenixFramework;

public class DelegateStudentsGroup extends LeafGroup {

    private static final long serialVersionUID = 1L;

    private final FunctionType functionType;

    private final String personFunctionId;

    public DelegateStudentsGroup(final PersonFunction delegateFunction, final FunctionType functionType) {
        personFunctionId = delegateFunction.getExternalId();
        this.functionType = functionType;
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
        if (functionType == null) {
            return new Argument[] { new OidOperator(getPersonFunction()) };
        } else {
            return new Argument[] { new OidOperator(getPersonFunction()), new StaticArgument(functionType.getName()) };
        }
    }

    public static class Builder implements GroupBuilder {

        @Override
        public Group build(Object[] arguments) {
            try {
                if (arguments.length > 1 && arguments[1] != null) {
                    final String functionTypeName = (String) arguments[1];
                    final FunctionType functionType = FunctionType.valueOf(functionTypeName);
                    return new DelegateStudentsGroup((PersonFunction) arguments[0], functionType);
                } else {
                    return new DelegateStudentsGroup((PersonFunction) arguments[0]);
                }
            } catch (ClassCastException e) {
                throw new GroupDynamicExpressionException("accessControl.group.builder.executionCourse.notExecutionCourse",
                        arguments[0].toString());
            }
        }

        @Override
        public int getMinArguments() {
            return 1;
        }

        @Override
        public int getMaxArguments() {
            return 2;
        }

    }

    @Override
    public boolean isMember(Person person) {
        if (person != null && person.hasStudent()) {
            if (getElements().contains(person.getStudent())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getPresentationNameBundle() {
        return "resources.DelegateResources";
    }

    @Override
    public String getPresentationNameKey() {
        return "label." + getClass().getSimpleName() + "." + getFunctionType().getName()
                + (getSender().hasStudent() ? "" : ".coordinator");
    }

    public FunctionType getFunctionType() {
        return functionType;
    }

    public PersonFunction getPersonFunction() {
        return personFunctionId != null ? (PersonFunction) FenixFramework.getDomainObject(personFunctionId) : null;
    }

    public Student getStudent() {
        final PersonFunction personFunction = getPersonFunction();
        final Person person = personFunction.getPerson();
        return person.hasStudent() ? person.getStudent() : null;
    }

    public ExecutionYear getExecutionYear() {
        final PersonFunction personFunction = getPersonFunction();
        return ExecutionYear.getExecutionYearByDate(personFunction.getBeginDate());
    }

    public Person getPerson() {
        final PersonFunction personFunction = getPersonFunction();
        return personFunction.getPerson();
    }

    private Person getSender() {
        return getPerson();
    }

    @Override
    public org.fenixedu.bennu.core.domain.groups.Group convert() {
        return PersistentDelegateStudentsGroup.getInstance(getPersonFunction(), getFunctionType());
    }
}
