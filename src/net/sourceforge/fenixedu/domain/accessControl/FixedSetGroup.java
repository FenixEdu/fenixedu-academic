package net.sourceforge.fenixedu.domain.accessControl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

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
    
}
