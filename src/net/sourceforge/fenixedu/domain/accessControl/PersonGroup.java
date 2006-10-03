package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.GroupDynamicExpressionException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.WrongNumberOfArgumentsException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.WrongTypeOfArgumentException;

public class PersonGroup extends DomainBackedGroup<Person> {

    private static final long serialVersionUID = 1L;

    public PersonGroup(Person person) {
        super(person);
    }

    @Override
    public Set<Person> getElements() {
        Set<Person> elements = super.buildSet();

        elements.add(getObject());

        return super.freezeSet(elements);
    }

    public Person getPerson() {
        return getObject();
    }

    /**
     * Builder used to create a person group expression. This builder
     * accepts one argument but the builder behaves differently accordingly with
     * the type of that argument.
     * 
     * <p>
     * If the argument is a number then it's considered a Person's oid. If the
     * argument is a string then it's converted to a number and used as a
     * Person's oid. If the argument is already a Person then a group is built
     * from it.
     * 
     * @author cfgi
     */
    public static class Builder implements GroupBuilder {

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
                Integer oid;
                
                if (argument instanceof String) {
                    try {
                        oid = new Integer((String) argument);
                    } catch (NumberFormatException e) {
                        throw new GroupDynamicExpressionException(e, "accessControl.group.builder.person.argument.convert", String.valueOf(argument));
                    }
                }
                else if (argument instanceof Integer) {
                    oid = (Integer) argument;
                }
                else {
                    throw new WrongTypeOfArgumentException(1, Integer.class, argument.getClass());
                }
                
                try {
                    person = (Person) RootDomainObject.readDomainObjectByOID(Person.class, oid);
                } catch (ClassCastException e) {
                    throw new GroupDynamicExpressionException(e, "accessControl.group.builder.person.id.notValid", String.valueOf(argument));
                }
            }
            
            if (person == null) {
                throw new GroupDynamicExpressionException("accessControl.group.builder.person.doesNotExist", String.valueOf(argument));
            }
            
            return new PersonGroup(person);
        }

        public int getMinArguments() {
            return 1;
        }

        public int getMaxArguments() {
            return 1;
        }

    }
}
