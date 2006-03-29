package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;

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
}
