package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.StaticArgument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.GroupDynamicExpressionException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.WrongNumberOfArgumentsException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.WrongTypeOfArgumentException;

import org.fenixedu.bennu.core.domain.groups.NobodyGroup;
import org.fenixedu.bennu.core.domain.groups.UserGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.FenixFramework;

public class PersonGroup extends DomainBackedGroup<Person> {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(PersonGroup.class);

    public PersonGroup(Person person) {
        super(person);
    }

    @Override
    public String getName() {
        return getPerson().getFirstAndLastName();
    }

    @Override
    public boolean hasPresentationNameDynamic() {
        return true;
    }

    @Override
    public Set<Person> getElements() {
        Set<Person> elements = super.buildSet();

        final Person object = getObject();
        if (object != null) {
            elements.add(object);
        }

        return super.freezeSet(elements);
    }

    public Person getPerson() {
        return getObject();
    }

    @Override
    protected Argument[] getExpressionArguments() {
        return new Argument[] { new StaticArgument(getPerson().getExternalId()) };
    }

    /**
     * Builder used to create a person group expression. This builder accepts
     * one argument but the builder behaves differently accordingly with the
     * type of that argument.
     * 
     * <p>
     * If the argument is a number then it's considered a Person's oid. If the argument is a string then it's converted to a
     * number and used as a Person's oid. If the argument is already a Person then a group is built from it.
     * 
     * @author cfgi
     */
    public static class Builder implements GroupBuilder {

        @Override
        public Group build(Object[] arguments) {
            if (arguments.length != 1) {
                throw new WrongNumberOfArgumentsException(arguments.length, getMinArguments(), getMaxArguments());
            }

            Object argument = arguments[0];

            if (argument == null) {
                throw new GroupDynamicExpressionException("accessControl.group.builder.person.argument.null");
            }

            Person person = null;

            if (argument instanceof Person) {
                person = (Person) argument;
            }

            if (person == null) {

                if (argument instanceof String) {
                    person = FenixFramework.getDomainObject((String) argument);
                } else {
                    throw new WrongTypeOfArgumentException(1, String.class, argument.getClass());
                }

            }

            if (person == null) {
                logger.info("accessControl.group.builder.person.doesNotExist" + String.valueOf(argument));
//		throw new GroupDynamicExpressionException("accessControl.group.builder.person.doesNotExist", String
//			.valueOf(argument));
            }

            return new PersonGroup(person);
        }

        @Override
        public int getMinArguments() {
            return 1;
        }

        @Override
        public int getMaxArguments() {
            return 1;
        }

    }

    @Override
    public org.fenixedu.bennu.core.domain.groups.Group convert() {
        if (getPerson().getUser() == null) {
            return NobodyGroup.getInstance();
        }
        return UserGroup.getInstance(getPerson().getUser());
    }
}
