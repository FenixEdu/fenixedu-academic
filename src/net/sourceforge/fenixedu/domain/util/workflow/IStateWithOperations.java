/**
 * 
 */
package net.sourceforge.fenixedu.domain.util.workflow;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.Person;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public interface IStateWithOperations extends IState {

    public Collection<Operation> getOperationsForPerson(Person person);

    public void onOperationFinished(Operation operation, Person person);

}
