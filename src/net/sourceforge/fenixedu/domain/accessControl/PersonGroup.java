package net.sourceforge.fenixedu.domain.accessControl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;

public class PersonGroup extends DomainBackedGroup<Person> {

    private static final long serialVersionUID = 1L;

    public PersonGroup(Person person) {
        super(person);
    }

    @Override
    public Iterator<Person> getElementsIterator() {
        List<Person> singlePersonList = new ArrayList<Person>();
        singlePersonList.add(getObject());
        
        return singlePersonList.iterator();
    }

    public Person getPerson() {
        return getObject();
    }
}
