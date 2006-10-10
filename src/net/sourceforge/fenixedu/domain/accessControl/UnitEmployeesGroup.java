/*
 * Author : Goncalo Luiz
 * Creation Date: Jun 27, 2006,12:38:18 PM
 */
package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a><br>
 *         <br>
 *         Created on Jun 27, 2006,12:38:18 PM
 * 
 */
public class UnitEmployeesGroup extends DomainBackedGroup<Unit> {

    public UnitEmployeesGroup(Unit unit) {
        super(unit);
    }

    @Override
    public Set<Person> getElements() {
        final Set<Person> elements = super.buildSet();
        for (final Employee employee : this.getObject().getAllCurrentActiveWorkingEmployees()) {
            elements.add(employee.getPerson());
        }
        return elements;
    }
    
    @Override
    public boolean isMember(Person person) {
	return (person.hasEmployee() && person.getEmployee().getCurrentWorkingPlace() == getObject());
    }

}
