package net.sourceforge.fenixedu.domain.accessControl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;

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
    
    public FixedSetGroup(List<Person> persons) {
        this();
        
        for (Person person : persons) {
            this.persons.add(new DomainReference<Person>(person));
        }
    }
    
    @Override
    public Iterator<Person> getElementsIterator() {
        List<Person> persons = new ArrayList<Person>();
        
        for (DomainReference<Person> reference : this.persons) {
            persons.add(reference.getObject());
        }
        
        return persons.iterator();
    }

    @Override
    public int getElementsCount() {
        return this.persons.size();
    }
}
