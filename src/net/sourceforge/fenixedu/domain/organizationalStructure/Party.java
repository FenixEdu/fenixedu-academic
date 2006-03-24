/*
 * Created on Feb 10, 2006
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;

public abstract class Party extends Party_Base {
    
    public Party() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());        
        setOjbConcreteClass(getClass().getName());
    }
    
    public static List<Unit> readAllUnits() {
        List<Unit> allUnits = new ArrayList<Unit>();
        for (Party party : RootDomainObject.getInstance().getPartys()) {
           if (party instanceof Unit) {               
               allUnits.add((Unit) party);
           }
        }
        return allUnits;
    }

    public static List<Person> readAllPersons() {
        List<Person> allPersons = new ArrayList<Person>();
        for (Party party : RootDomainObject.getInstance().getPartys()) {
           if (party instanceof Person) {              
               allPersons.add((Person) party);
           }
        }
        return allPersons;
    }
}
