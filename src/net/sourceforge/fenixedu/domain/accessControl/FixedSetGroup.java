package net.sourceforge.fenixedu.domain.accessControl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.StaticArgument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.GroupDynamicExpressionException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.WrongTypeOfArgumentException;

public class FixedSetGroup extends LeafGroup {
    
    private static final long serialVersionUID = 1L;
    
    private List<DomainReference<Person>> persons;
    
    protected FixedSetGroup() {
        super();
        
        this.persons = new ArrayList<DomainReference<Person>>();
    }
    
    public FixedSetGroup(Person ... persons) {
        this();
        
        for (Person person : persons) {
            this.persons.add(new DomainReference<Person>(person));
        }
    }
    
    public FixedSetGroup(Collection<Person> persons) {
        this();
        
        for (Person person : persons) {
            this.persons.add(new DomainReference<Person>(person));
        }
    }
    
    @Override
    public Set<Person> getElements() {
        Set<Person> elements = super.buildSet();
        
        for (DomainReference<Person> reference : this.persons) {
            elements.add(reference.getObject());
        }
        
        return super.freezeSet(elements);
    }

    @Override
    public int getElementsCount() {
        return this.persons.size();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        
        if (! (object instanceof FixedSetGroup)) {
            return false;
        }
        
        return this.persons.equals(((FixedSetGroup) object).persons);
    }

    @Override
    public int hashCode() {
        return this.persons.hashCode();
    }

    @Override
    protected Argument[] getExpressionArguments() {
        Collection<Person> persons = getElements();
        Argument[] arguments = new Argument[persons.size()];
        
        int index = 0;
        for (Person person : persons) {
            arguments[index++] = new StaticArgument(person.getIdInternal());
        }
        
        return arguments;
    }
    
    public static class Builder implements GroupBuilder {

        public Group build(Object[] arguments) {
            List<Person> persons = new ArrayList<Person>();
            
            for (int i = 0; i < arguments.length; i++) {
                Object object = arguments[i];
                
                Person person;
                if (object instanceof Integer) {
                    try {
                        person = (Person) RootDomainObject.getInstance().readPartyByOID((Integer) object);
                    }
                    catch (ClassCastException e) {
                        throw new GroupDynamicExpressionException(
                        "accessControl.group.builder.fixed.id.notPerson", object.toString());
                    }
                }
                else if (object instanceof Person) {
                    person = (Person) object;
                }
                else {
                    throw new WrongTypeOfArgumentException(1, Integer.class, object.getClass());
                }
                
                if (person != null) {
                    persons.add(person);
                }
            }
            
            return new FixedSetGroup(persons);
        }

        public int getMinArguments() {
            return 0;
        }

        public int getMaxArguments() {
            return Integer.MAX_VALUE;
        }
        
    }
}
